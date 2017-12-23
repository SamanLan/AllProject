package com.lansman.allproject;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Field;

import saman.com.base.Book;
import saman.com.origin.IBookAidlInterface;

import static android.R.attr.data;
import static android.animation.ValueAnimator.ofFloat;

public class MainActivity extends AppCompatActivity {

    /**
     * Linux 原有的 IPC 机制为何不用，却偏偏使用 Binder 呢？
     * 1. 传输性能：Binder 只需拷贝一次数据；虽然共享内存只需一次，但是使用起来很复杂
     * 当Client向Server发送数据时，Client会先从自己的进程空间把通信数据拷贝到内核空间，因为Server和内核共享数据，所以不再需要重新拷贝数据，
     * 而是直接通过内存地址的偏移量直接获取到数据地址。总体来说只拷贝了一次数据。
     * 2. 传统的IPC方式没有严格的安全措施；比方说Socket，IP地址可以由客户端手动填入，调用者的身份很容易进行伪造
     * Android会为每个已安装的应用程序添加属于自己的UID，Binder 基于此会根据请求端的UID来鉴别调用者的身份，保障了安全性
     *
     **************************************
     *
     * Binder 服务在调用期间抛出了RuntimeException异常，服务端会Crash么？
     * 服务端并不会Crash，RuntimeException被Binder服务端线程捕捉，随后将异常信息写入到reply中，发回Binder客户端进程，
     * 最后会客户端binder线程抛出这个异常，如果没有捕捉这个RuntimeException，那么Binder客户端进程会Crash
     *
     * ************************************
     *
     * 客户端调用 Binder 接口后抛出的DeadObjectException是什么意思？
     * 1. 最常见的原因就是Binder服务端进程已经死亡。客户端进行binder调用时，Binder驱动发现服务端进程不能回应请求，那么就会抛出异常给客户端；
     * 2. 服务端此时的缓存内存空间(1016K)已经被占满了，Binder驱动就认为服务端此时并不能处理这个调用，那么就会在C++层抛出DeadObjectException到Java层
     *
     * ************************************
     *
     * 每个进程最多存在多少个 Binder 线程，这些线程都被占满后会导致什么问题？
     * Binder线程池中的线程数量是在Binder驱动初始化时被定义的; 进程池中的线程个数上限为15个，加上主Binder线程，一共最大能存在16个binder线程；
     * 当Binder线程都在执行工作时，也就是当出现线程饥饿的时候，从别的进程调用的binder请求如果是同步的话，会在todo队列中阻塞等待，直到线程池中有空闲的binder进程来处理请求
     *
     **************************************
     *
     * 使用 Binder 传输数据的最大限制是多少，被占满后会导致什么问题？
     * 1. 在调用mmap时会指定Binder内存缓冲区的大小为1016K；当服务端的内存缓冲区被Binder进程占用满后，Binder驱动不会再处理binder调用并在c++层抛出DeadObjectException到binder客户端
     * 2. 同步空间是1016K，异步空间只有它的一半，也就是508K
     *
     * ************************************
     *
     * 为什么使用广播传输 2MB的Bitmap会抛异常，而使用AIDL生成的Binder接口传输Bitmap就不会抛异常呢？
     * 1. 通过Binder直接传输Bitmap，如果Bitmap的大于128K，那么传输Bitmap内容的方式就会使用ashmem，Binder只需要负责传输ashmem的fd到服务端即可。
     * 2. 使用广播来传输跨进程传输数据的话则不一样，Bitmap是被填入到Bundle中，随后以Parcelable的序列化方式传输到AMS的，如果Bundle的数据量大于800K，就会抛出TransactionTooLargeException的异常
     *
     * ************************************
     *
     * 调用bindService之后，Service.onBind方法会返回Binder服务Stub对象，然后APP会在AMS进行登记，随后经过层层调用才会在Binder客户端调用onConnected方法，
     * 如果Binder客户端和其服务端在同一个进程中，客户端拿到的应该还是Stub对象，如果不再同一进程中，客户端拿到的应该是Binder服务的Proxy对象。
     */

    /**
     *
               返回数据，唤醒client                              +---------+
            +---------------------------+                       | Data    |      Transcat
            |                           |                +------>         +--------------+
            |                           |  写入参数      |      +---------+              |
            |                           |               |                               |
     +-----v-----+    远程请求   +------+-----+         |                        +------v------+
     |           |              |            |         |                        |             |
     |  Client   +------+------->  Binder    +---------+                        |  Service    |
     |           |      |       |            |                                  |             |
     +-----^-----+      |       +------^-----+                                  +------+------+
           |            |              |                                               |
           |            |              |                                               |
           |            |              |                                               | OnTransact
           +------------+              |                                               |
                 挂起                  |          +--------------+             +------v------+
                                       |          |              |   写入结果   |             |
                                       +----------+   reply      <-------------+  线程池      |
                                                  |              |             |             |
                                                  +--------------+             +-------------+

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        call();
//        useMessage();
    }

    /**
     * ***********************************************
     * *     AIDL                                    *
     * ***********************************************
     */

    /**
     * AIDL接口类
     */
    IBookAidlInterface stub;
    /**
     * 链接断开（死亡）回调
     */
    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (stub != null) {
                stub.asBinder().unlinkToDeath(deathRecipient, 0);
                stub = null;
                // TODO: 2017/12/14 重新绑定远程service
                System.out.println("-------------死亡回调" + Thread.currentThread().getName());
            }
        }
    };

    private void call() {

        ServiceConnection conn = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                stub = IBookAidlInterface.Stub.asInterface(service);
                try {
                    service.linkToDeath(deathRecipient, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.out.println("-------------"+stub);
                deal();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                stub = null;
            }
        };

        Intent intent = new Intent();
        intent.setAction("book");
        intent.setPackage("saman.com.origin");
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void deal() {
        if (stub != null) {
            try {
                // 调用远程方法会阻塞当前线程（挂起）
                stub.say();
                Book book = stub.getBook();
                Class bc = book.getClass();
                Field name = bc.getDeclaredField("name");
                name.setAccessible(true);
                System.out.println("-------------名字是" + name.get(book));
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * ***********************************************
     * *     Messager                                *
     * ***********************************************
     */

    /**
     * 连通服务端和本地的messager，客户端可以通过这个来传递消息给服务端
     */
    Messenger messenger;
    /**
     * 处理服务端发回来的消息
     */
    Handler clientHandler = new ClientHandler();
    /**
     * 传递给服务端的messager，服务端可以通过这个来传递消息给客户端
     */
    Messenger clientMessager = new Messenger(clientHandler);

    private static class ClientHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                System.out.println("****客户端收到" + msg.getData().get("data"));
            }
        }
    }

    /**
     * 通过messager进行跨进程通信
     */
    private void useMessage() {
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                messenger = new Messenger(service);
                Message message = Message.obtain(null, 100);
                Bundle bundle = new Bundle();
                bundle.putString("data", "hello");
                message.setData(bundle);
                // 使用replyTo传递clientMessager给服务端
                message.replyTo = clientMessager;
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent();
        intent.setAction("book");
        intent.setPackage("saman.com.origin");
        bindService(intent, connection, BIND_AUTO_CREATE);
    }
}
