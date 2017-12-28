package com.lansman.allproject;


import android.view.animation.Interpolator;

/**
 * 类名 :${Method}</br>
 * 创建日期 :2017/12/28 0028</br>
 * 描述：com.example.administrator.myselfstudy.view</br>
 *
 * @author Xiaoqi
 *         <p/>
 *         更新时间 2017/12/28 0028
 *         最后更新者 Xiaoqi</br>
 *         <p/>
 */

public class JellyInterpolator implements Interpolator {

    // 因子数值越小振动频率越高
    private float factor;

    public JellyInterpolator() {
        this.factor = 0.18f;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
