package com.lansman.allproject;

import android.accessibilityservice.AccessibilityService;
import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;


/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/1/6 10:15<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class JumpYiJumpService extends AccessibilityService {

    public static final String TAG = "JumpYiJumpService";
    private CalculateHelper helper;

    /**
     * 系统成功绑定该服务时被触发,也就是当你在设置中开启相应的服务,系统成功的绑定了该服务时会触发,通常我们可以在这里做一些初始化操作
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "跳一跳辅助启动");
        helper = new CalculateHelper();
    }

    /**
     * 如果配置能够获取窗口内容,则会返回当前活动窗口的根结点
     */
    @Override
    public AccessibilityNodeInfo getRootInActiveWindow() {
        return super.getRootInActiveWindow();
    }

    /**
     * 有关AccessibilityEvent事件的回调函数.系统通过sendAccessibiliyEvent()不断的发送AccessibilityEvent到此处
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent + " + event.getEventType());
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d(TAG, "单击");
                Toast.makeText(JumpYiJumpService.this, "景天个扑街", Toast.LENGTH_SHORT).show();
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                Log.d(TAG, "长按");break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
//                Log.d(TAG, "滑动");dealScroll();
                break;
            default:
        }
    }

    /**
     * 如果允许服务监听按键操作,该方法是按键事件的回调,需要注意,这个过程发生了系统处理按键事件之前
     */
    @Override
    public void onInterrupt() {

    }

    /**
     * 查找拥有特定焦点类型的控件
     */
    @Override
    public AccessibilityNodeInfo findFocus(int focus) {
        return super.findFocus(focus);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    private Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_SCROLL:
                    long t = (long) msg.obj;
                    if (t != lastScrollTime) {
                        return;
                    }
                    calculateDistance();
                    break;
                    default:
            }
        }
    };

    private void simulateLongClick(long pressTime) {
        Log.d(TAG, "模拟长按");
//        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
    }

    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    private void getNode(AccessibilityNodeInfo nodeInfo,int index) {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
//            if (node.getClassName().equals("android.widget.MultiAutoCompleteTextView")) {
//                Bundle arguments = new Bundle();
//                arguments.putCharSequence(AccessibilityNodeInfo
//                        .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "android");
//                node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
//            }
            for (int i1 = 0; i1 < index; i1++) {
                System.out.print("  ");
            }
            System.out.println(node.getClassName());
            getNode(node, index + 1);
        }
    }

    private void simulateUpLongClick() {
        Log.d(TAG, "模拟释放长按");
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
    }

    private void dealScroll() {
        Log.d(TAG, "处理滑动");
        lastScrollTime = System.currentTimeMillis();
        // 这里过了1700之后才去更新数据，但是更新数据
        Message message = Message.obtain(timeHandler, MESSAGE_SCROLL, lastScrollTime);
        timeHandler.sendMessageDelayed(message, INTERVAL_SCROLL);
    }

    private void calculateDistance() {
        Log.d(TAG, "计算距离");
//        getRootInActiveWindow().
//        int distance = helper.calculateJumpDistance();
        long pressTime = 0L;
        simulateLongClick(pressTime);
    }

    private static final int INTERVAL_SCROLL = 0x1000;
    private static final int MESSAGE_SCROLL = 0x1;
    private long lastScrollTime = 0L;
}
