package com.lansman.allproject;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.animation.OvershootInterpolator;

import java.lang.reflect.Field;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2017/12/28 17:57<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class WishAnimatorEngine {

    private ObjectAnimator animator;
    private final int ANIMATOR_DURATION_TOTAL = 0x500;

    public WishAnimatorEngine() {
        animator = new ObjectAnimator();
        animator.setDuration(ANIMATOR_DURATION_TOTAL);
        animator.setInterpolator(new JellyInterpolator());
    }

    public void startAnimator(Object target, float startValue, float endValue) {
        if (animator != null) {
            if (!animator.isRunning()) {
                int duration = (int) ((startValue - endValue) / startValue * ANIMATOR_DURATION_TOTAL);
                animator.setDuration(duration);
                animator.setPropertyName("Y");
                animator.setTarget(target);
                animator.setFloatValues(startValue, endValue);
                animator.start();
            }
        }
    }


}
