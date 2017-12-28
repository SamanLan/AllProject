package com.lansman.allproject;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2017/12/25 10:26<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class LauncherHelper {

    /**
     * 何为主线程？
     * APK的静态入口在ActivityThread的main方法，ActivityThread即为主线程
     * 何为UI线程的消息队列
     * UI线程有一个Handler（Class H），主线程的所有处理都是在主线程的Message处理的
     * 应用一启动有几个线程？
     * group为main的线程有：Main（ActivityThread主线程），Binder线程池：一启动时会有Binder_1（ApplicationThread线程，接受AMS的消息），
     * Binder_2（ViewRootImpl里面W线程，接受WindowManagerService的消息）
     */

    /**
     * main
     *      ↓
     * new ActivityThread（）
     *      ↓
     * attach（）
     *      ↓
     * 获取AMS，传递applicationThread（向AMS注册Client的binder对象）
     *      ↓
     * 这个applicationThread是一个IBinder对象，就是进程的Token。AMS会保存这个Token，当有消息就向这个Token的IBinder线程
     * 发送消息，Token接收到再发给对应的子系统。
     */


    /**
     * 每个Activity在启动之后都会初始化一个Instrumentation对象,用于桥接处理与系统交互的一些操作,比如说要启动其他的activity
     *      ↓
     * ApplicationThread主要负责APP进程与系统进程之间的Binder通信,通俗的说将APP进程的ApplicationThread对象通过Binder传递给系统进程用于系统进程调用APP进程的某些方法让APP进程做出一些操作
     *      ↓
     * Instrumentation.execStartActivity()
     *      ↓
     * ActivityManagerNative.getDefault().startActivity()   getDefault()实际上是得到了一个ActivityManagerNative的内部类ActivityManagerProxy代理对象,
     * 接下来通过Binder进程间通讯机制进入ActivityManagerService进程执行启动activity操作
     *      ↓
     * ActivityManagerService.startActivity()
     */

    public static class TestManager{
        public TestManager() {
        }

        public Object proxy;

        public void deal() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
            Class cla = Class.forName("android.app.ActivityThread");
            Method m = cla.getDeclaredMethod("currentActivityThread");
            proxy = m.invoke(null);
            Field field = cla.getDeclaredField("mInstrumentation");
            field.setAccessible(true);
            field.set(proxy,new ProxyInstrumentation());
        }
    }

    private static class ProxyInstrumentation extends Instrumentation {
        @Override
        public void callActivityOnResume(Activity activity) {
            super.callActivityOnResume(activity);
            System.out.println("---------tm我劫持了activity");
        }
    }

    LocationManager locationManager;

    public void getLM(Context context) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final Field lms = locationManager.getClass().getDeclaredField("mService");
        lms.setAccessible(true);
        final Object o = lms.get(locationManager);
        final Method method = o.getClass().getDeclaredMethod("handleLocationChanged", Location.class, boolean.class);
        Handler proxyHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        try {
                            method.invoke(o, true);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }
}
