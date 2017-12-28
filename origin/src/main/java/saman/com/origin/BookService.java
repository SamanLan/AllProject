package saman.com.origin;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import saman.com.base.Book;

public class BookService extends Service {
    private BookBinder bookBinder;

    private class BookBinder extends IBookAidlInterface.Stub{
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public Book getBook() throws RemoteException {
            return new Book("蓝二狗的自传");
        }

        @Override
        public void say() throws RemoteException {
            System.out.println("-----say" + Thread.currentThread().getName());
        }
    }

    public BookService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bookBinder = new BookBinder();
        System.out.println("------onCreate");
    }

    Messenger messenger = new Messenger(new ServiceHandler());

    @Override
    public IBinder onBind(Intent intent) {
        // 使用binder
        return bookBinder;
        // 使用messager
//        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("------destroy");
    }

    private static class ServiceHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                System.out.println("****服务器收到" + msg.getData().get("data"));
                Messenger clientMessager = msg.replyTo;
                Message message = Message.obtain(null, 200);
                Bundle bundle = new Bundle();
                bundle.putString("data", "5555");
                message.setData(bundle);
                try {
                    clientMessager.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
