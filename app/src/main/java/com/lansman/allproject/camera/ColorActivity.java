package com.lansman.allproject.camera;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.lansman.allproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.iv_content)
    ImageView ivContent;
    @BindView(R.id.sb_R)
    SeekBar sbR;
    @BindView(R.id.sb_G)
    SeekBar sbG;
    @BindView(R.id.sb_B)
    SeekBar sbB;
    @BindView(R.id.sb_light)
    SeekBar sbLight;
    @BindView(R.id.sb_full)
    SeekBar sbFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        ButterKnife.bind(this);
        sbR.setOnSeekBarChangeListener(this);
        sbG.setOnSeekBarChangeListener(this);
        sbB.setOnSeekBarChangeListener(this);
        sbLight.setOnSeekBarChangeListener(this);
        sbFull.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorMatrix light = new ColorMatrix();
        ColorMatrix full = new ColorMatrix();
        ColorMatrix rgb = new ColorMatrix();
        rgb.setRotate(0, sbR.getProgress() * 1f / 256 * 360 - 180);
        rgb.setRotate(1, sbG.getProgress() * 1f / 256 * 360 - 180);
        rgb.setRotate(2, sbB.getProgress() * 1f / 256 * 360 - 180);
        light.setScale(sbLight.getProgress() * 1f / 256 * 2, sbLight.getProgress() * 1f / 256 * 2, sbLight.getProgress() * 1f / 256 * 2, 1f);
        full.setSaturation(sbFull.getProgress() * 1f / 256 * 2);

        colorMatrix.postConcat(rgb);
        colorMatrix.postConcat(light);
        colorMatrix.postConcat(full);

        ivContent.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
