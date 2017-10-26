package com.lansman.allproject;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    /**
     * 可滑动的内容view
     */
    private View contentView;

    /**
     * 原始位置
     */
    private Point autoSlidePoint = new Point();

    public CanDragLayout(Context context) {
        super(context);
    }

    public CanDragLayout(Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        draggerHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 只可以滑这个view
                if (child == contentView) {
                    return true;
                }
                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 左右滑动的距离
                int leftSide = getPaddingLeft();
                int bottomSide = getWidth() - getPaddingRight() - contentView.getWidth();
                return Math.min(Math.max(left, leftSide), bottomSide);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 上下可滑动的距离
                int topSide = getPaddingTop();
                int bottomSide = getHeight() - getPaddingBottom() - contentView.getHeight();
                return Math.min(Math.max(top, topSide), bottomSide);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //contentView手指释放时可以自动回去
                if (releasedChild == contentView) {
                    if (releasedChild.getLeft() + releasedChild.getWidth() / 2 > getWidth() / 2) {
                        autoSlidePoint.x = getWidth() - getPaddingRight() - releasedChild.getWidth();
                    }  else {
                        autoSlidePoint.x = getPaddingLeft();
                    }
                    autoSlidePoint.y = releasedChild.getTop();
                    draggerHelper.settleCapturedViewAt(autoSlidePoint.x, autoSlidePoint.y);
                    invalidate();
                }
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = findViewById(R.id.can_drag_content);
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
}
