package com.lansman.allproject.camera;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.lansman.allproject.R;

import java.lang.reflect.Field;

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
    @BindView(R.id.sb_sexiang)
    SeekBar sbSexiang;
    @BindView(R.id.spinner)
    Spinner spinner;

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
        sbSexiang.setOnSeekBarChangeListener(this);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item,new String[]{
                "黑白","怀旧","哥特","淡雅","蓝调","光晕",
                "梦幻","酒红","胶片","湖光掠影","褐片","复古",
                "泛黄","传统","胶片2","锐色","清宁","浪漫", "夜色", "浮雕"
        }));
        try {
            Field field = getDeclaredField(spinner, "mOnItemClickListener");
            field.setAccessible(true);
            field.set(spinner,new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MatrixUtil.setColormatrix(ivContent, MatrixUtil.matrixList[position]);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorMatrix light = new ColorMatrix();
        ColorMatrix full = new ColorMatrix();
        ColorMatrix seXiang = new ColorMatrix();
        ColorMatrix color = new ColorMatrix();
        seXiang.setRotate(0, sbSexiang.getProgress() * 1f / 256 * 360 - 180);
        seXiang.setRotate(1, sbSexiang.getProgress() * 1f / 256 * 360 - 180);
        seXiang.setRotate(2, sbSexiang.getProgress() * 1f / 256 * 360 - 180);
        light.setScale(sbLight.getProgress() * 1f / 256 * 2, sbLight.getProgress() * 1f / 256 * 2, sbLight.getProgress() * 1f / 256 * 2, 1f);
        color.setScale(sbR.getProgress() * 1f / 256 * 2, sbG.getProgress() * 1f / 256 * 2, sbB.getProgress() * 1f / 256 * 2, 1f);
        full.setSaturation(sbFull.getProgress() * 1f / 256 * 2);

        colorMatrix.postConcat(seXiang);
        colorMatrix.postConcat(light);
        colorMatrix.postConcat(full);
        colorMatrix.postConcat(color);

        ivContent.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public static Field getDeclaredField(Object object, String fieldName){
        Field field = null ;

        Class<?> clazz = object.getClass() ;

        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName) ;
                return field ;
            } catch (Exception e) {
                //这里甚么都不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会进入
            }
        }
        return null;
    }
}
