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
import android.widget.TextView;

import java.lang.reflect.Field;


/**
 * Author：zixin on 2017/10/19 14:14
 * E-mail：lanshenming@linghit.com
 */

/**
 * 可拖动，可回弹边缘的viewgroup
 */
public class CanDragLayout extends LinearLayout {

    /**
     * helper类
     */
    private ViewDragHelper draggerHelper;

    private boolean refreshFlag = true;

    private Point lastPoint = new Point();

    public CanDragLayout(Context context) {
        this(context, null);
    }

    public CanDragLayout(Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        draggerHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 只可以滑这个view
                if (child instanceof WishAnimatorView) {
                    return true;
                }
                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 左右滑动的距离
                int leftSide = getPaddingLeft();
                int bottomSide = getWidth() - getPaddingRight() - child.getWidth();
                return left;
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
                System.out.println(changedView.getTop()+"---"+changedView.getY()+"----"+changedView.getTranslationY());
                if (refreshFlag) {
                    refreshFlag = false;
                    lastPoint.y = top;
                    lastPoint.x = left;
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
                refreshFlag = true;
                invalidate();
            }
        });
        try {
            replaceInterpolator(context,draggerHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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

    private static void replaceInterpolator(Context context, ViewDragHelper helper) throws NoSuchFieldException, IllegalAccessException {
        Class cla = helper.getClass();
        Field scroller = cla.getDeclaredField("mScroller");
        scroller.setAccessible(true);
        scroller.set(helper, ScrollerCompat.create(context, new JellyInterpolator()));
    }
}
