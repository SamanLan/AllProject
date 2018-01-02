package com.lansman.allproject;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/1/2 10:10<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class WishBgAinmatorView extends View implements WishDragLayout.WTAnimatiorCallBack{

    private Paint paint;
    private float startX, startY, endX, endY;

    public WishBgAinmatorView(Context context) {
        this(context, null);
    }

    public WishBgAinmatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 不能使用BitmapShader了，tm是从画布左上角开始画的
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wish);
//        if (bitmap != null) {
//            paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//            paint.setStrokeWidth(bitmap.getWidth());
//        }
        paint.setStrokeWidth(2 * getResources().getDisplayMetrics().density);
        paint.setColor(0xffBB3B28);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    @Override
    public void onViewPositionChanged(int left, int top, int dx, int dy) {
        startX = left;
        startY = top;
        endX = dx;
        endY = dy;
        invalidate();
    }
}