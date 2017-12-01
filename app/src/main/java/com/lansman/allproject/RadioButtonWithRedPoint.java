package com.lansman.allproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioButton;

import static android.R.attr.width;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2017/11/23 11:46<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class RadioButtonWithRedPoint extends RadioButton {

    private Paint paint;
    private boolean isShowRedPoint = false;

    public RadioButtonWithRedPoint(Context context) {
        this(context, null);
    }

    public RadioButtonWithRedPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFE61E23);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowRedPoint) {
            int width = getWidth();
            int height = getHeight();
            int diameter = width > height ? height / 10 : width / 10;
            canvas.drawCircle(width - diameter * 2, diameter * 2, diameter, paint);
        }
    }

    public boolean isShowRedPoint() {
        return isShowRedPoint;
    }

    public void setShowRedPoint(boolean showRedPoint) {
        isShowRedPoint = showRedPoint;
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean re = super.dispatchTouchEvent(event);
        System.out.println("dispatchTouchEvent------" + event.getAction() + re);
        return re;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean re = super.onTouchEvent(event);
        System.out.println("onTouchEvent------" + event.getAction() + re);
        return re;
    }
}
