package com.lansman.allproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Author：zixin on 2017/10/19 14:58
 * E-mail：lanshenming@linghit.com
 * Desc：
 */

public class EdgeTransparentView extends FrameLayout {

    private Paint mPaint;
    private int position;
    private float drawSize;

    private int topMask = 0x01;
    private int bottomMask = topMask << 1;
    private int leftMask = topMask << 2;
    private int rightMask = topMask << 3;

    private int mWidth;
    private int mHeight;

    public EdgeTransparentView(@NonNull Context context) {
        this(context, null);
    }

    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));


//        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EdgeTransparentView);
//        position = typedArray.getInt(R.styleable.EdgeTransparentView_edge_position, 0);
//        drawSize = typedArray.getDimension(R.styleable.EdgeTransparentView_edge_width, Utils.d2p(getContext(), 20));
//        typedArray.recycle();
        drawSize = 20;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initShader();
        mWidth = getWidth();
        mHeight = getHeight();
    }

    /**
     * 渐变颜色
     */
    private int[] mGradientColors = {0xffffffff, 0x00000000};
    /**
     * 渐变位置
     */
    private float[] mGradientPosition = new float[]{0, 1};

    private void initShader() {
        mPaint.setShader(new LinearGradient(0, 0, 0, drawSize, mGradientColors, mGradientPosition, Shader.TileMode.CLAMP));
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int layerSave = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        boolean drawChild = super.drawChild(canvas, child, drawingTime);
//        if (position == 0 || (position & topMask) != 0) {
            canvas.drawRect(0, 0, mWidth, drawSize, mPaint);
//        }
        canvas.restoreToCount(layerSave);
        return drawChild;
    }
}
