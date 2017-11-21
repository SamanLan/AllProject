package com.lansman.allproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.left;
import static android.R.attr.width;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2017/11/9 13:01<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> 扫一扫前景动画view<br>
 */

public class RichScanAnimationView extends View {
    public RichScanAnimationView(Context context) {
        this(context, null);
    }

    public RichScanAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private int mainColor = 0xffB84046;
    private int minorColor = 0xffFFFFFF;
    private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint minorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint moveLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float density, scale = 0.5f,width, height, animaHeight;
    private Path path = new Path();
    private boolean isStart = true;
    private Bitmap bitmap;
    private Rect rect;

    private void init(Context context) {
        setBackgroundColor(Color.TRANSPARENT);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.scan_light);
        density = context.getResources().getDisplayMetrics().density;
        mainPaint.setColor(mainColor);
        minorPaint.setColor(minorColor);
        moveLinePaint.setColor(mainColor);
        mainPaint.setStrokeWidth(density * 3);
        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setStrokeJoin(Paint.Join.MITER);
        minorPaint.setStrokeWidth(density * 1);
        minorPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        float realWidth = width * scale;
        float left = (width - realWidth) / 2;
        float top = (height - realWidth) / 2;
        float bottom = top + realWidth;
        float right = left + realWidth;
        if (animaHeight == 0) {
            animaHeight = top + 5 * density;
        }
        rect = new Rect((int)(left + density * 5), (int)(animaHeight), (int)(right - density * 5), (int)(animaHeight + 5 * density));
        path.moveTo(left + density * 15, top);
        path.lineTo(left, top);
        path.lineTo(left, top + density * 15);
        canvas.drawColor(0xdd000000);
        minorPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, minorPaint);
        minorPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, right, bottom, minorPaint);
        for (int i = 0; i < 4; i++) {
            canvas.drawPath(path,mainPaint);
            canvas.rotate(90 * (i + 1), width / 2, height / 2);
        }
        if (isStart && bitmap != null) {
            canvas.drawBitmap(bitmap, null, rect, moveLinePaint);
//            canvas.drawLine(left + density * 5, animaHeight, right - density * 5, animaHeight, moveLinePaint);
//            animaHeight += density * 10;
//            if (animaHeight >= bottom - 5 * density) {
//                animaHeight = top + 5 * density;
//            }
//            postInvalidateDelayed(50);
            setStart();
        }
    }

    private boolean ok = false;
    public void setStart() {
        if (ok) {
            return;
        }
        ok = true;
        ValueAnimator animator = ValueAnimator.ofFloat(5*density, width * scale-5*density);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animaHeight = (getMeasuredHeight() - width * scale) / 2 + ((float) animation.getAnimatedValue());
                invalidate();
            }
        });
        animator.setRepeatCount(-1);
        animator.start();
    }
}
