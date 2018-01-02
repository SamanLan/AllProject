package com.lansman.allproject;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;


/**
 * Author：zixin on 2018/1/2 10:14
 * E-mail：lanshenming@linghit.com
 */
public class WishDragLayout extends LinearLayout {

    /**
     * helper类
     */
    private ViewDragHelper draggerHelper;

    /**
     * 可以滑
     */
    private boolean refreshFlag = true;

    /**
     * down的时候，对应的view的left和top位置信息
     */
    private Point lastPoint = new Point();

    private float den;

    /**
     * 位置改变，回调那根绳子进行动画
     */
    private WTAnimatiorCallBack callBack;

    public WishDragLayout(Context context) {
        this(context, null);
    }

    public WishDragLayout(Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        den = context.getResources().getDisplayMetrics().density;
        draggerHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 只可以滑这个view
                if (refreshFlag) {
                    refreshFlag = false;
                } else {
                    return false;
                }
                if (child instanceof WishAnimatorView) {
                    lastPoint.y = child.getTop();
                    lastPoint.x = child.getLeft();
                    System.out.println("记录前---" + lastPoint.x + "**" + lastPoint.y);
                    return true;
                }
                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 左右滑动的距离
                int leftSide = getPaddingLeft();
                int bottomSide = getWidth() - getPaddingRight() - child.getWidth();
                // 可以上下左右滑
//                return Math.min(Math.max(left, leftSide), bottomSide);
                // 只上下滑
                return lastPoint.x;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 上下可滑动的距离
                int topSide = getPaddingTop();
                int bottomSide = getHeight() - getPaddingBottom() - child.getHeight();
                return Math.min(Math.max(top, topSide), bottomSide);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (callBack != null) {
                    callBack.onViewPositionChanged(lastPoint.x + changedView.getWidth() / 2, lastPoint.y,
                            left + changedView.getWidth() / 2, top);
                }
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 0;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //手指释放时可以自动回去
                draggerHelper.settleCapturedViewAt(lastPoint.x, lastPoint.y);

                System.out.println("释放后---" + lastPoint.x + "**" + lastPoint.y);
                invalidate();
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                if (state == ViewDragHelper.STATE_IDLE) {
                    if (!refreshFlag) {
                        refreshFlag = true;
                    }
                }
            }
        });
        // 反射改变helper里面的mScroller，为了改变它里面的插值器，实现重力弹射动画
        try {
            replaceInterpolator(context,draggerHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return draggerHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        draggerHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (draggerHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void setCallBack(WTAnimatiorCallBack callBack) {
        this.callBack = callBack;
    }

    private static void replaceInterpolator(Context context, ViewDragHelper helper) throws NoSuchFieldException, IllegalAccessException {
        Class cla = helper.getClass();
        Field scroller = cla.getDeclaredField("mScroller");
        scroller.setAccessible(true);
        scroller.set(helper, ScrollerCompat.create(context, new JellyInterpolator()));
    }

    public interface WTAnimatiorCallBack{
        void onViewPositionChanged(int left, int top, int dx, int dy);
    }
}
