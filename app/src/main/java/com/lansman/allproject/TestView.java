package com.lansman.allproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2017/12/27 18:15<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class TestView extends LinearLayout {
    Paint paint = new Paint();
    Path path = new Path();
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        path.moveTo(0,20);
        path.lineTo(20, 0);
        path.lineTo(getWidth() - 20, 0);
        path.lineTo(getWidth(), 20);
        path.lineTo(getWidth(), getHeight() - 20);
        path.lineTo(getWidth() - 20, getHeight());
        path.lineTo(20, getHeight());
        path.lineTo(0, getHeight() - 20);
        path.close();
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }
}
