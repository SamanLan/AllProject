package com.lansman.allproject.camera;

import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/2/3 12:00<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class MatrixUtil {

    public static void setColormatrix(ImageView imageView, float[] m) {
        imageView.setColorFilter(new ColorMatrixColorFilter(m));
    }

    // 黑白
    public static float COLORMATRIX_HEIBAI[] = {0.8f, 1.6f, 0.2f, 0,
            -163.9f, 0.8f, 1.6f, 0.2f, 0, -163.9f, 0.8f, 1.6f, 0.2f, 0,
            -163.9f, 0, 0, 0, 1.0f, 0};
    // 怀旧
    public static final float COLORMATRIX_HUAJIU[] = {0.2f, 0.5f, 0.1f, 0,
            40.8f, 0.2f, 0.5f, 0.1f, 0, 40.8f, 0.2f, 0.5f, 0.1f, 0, 40.8f, 0,
            0, 0, 1, 0};
    // 哥特
    public static final float COLORMATRIX_GETE[] = {1.9f, -0.3f, -0.2f, 0,
            -87.0f, -0.2f, 1.7f, -0.1f, 0, -87.0f, -0.1f, -0.6f, 2.0f, 0,
            -87.0f, 0, 0, 0, 1.0f, 0};
    // 淡雅
    public static final float COLORMATRIX_DANYA[] = {0.6f, 0.3f, 0.1f, 0,
            73.3f, 0.2f, 0.7f, 0.1f, 0, 73.3f, 0.2f, 0.3f, 0.4f, 0, 73.3f, 0,
            0, 0, 1.0f, 0};
    // 蓝调
    public static final float COLORMATRIX_LANDIAO[] = {2.1f, -1.4f, 0.6f,
            0.0f, -71.0f, -0.3f, 2.0f, -0.3f, 0.0f, -71.0f, -1.1f, -0.2f, 2.6f,
            0.0f, -71.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 光晕
    public static final float COLORMATRIX_GUANGYUN[] = {0.9f, 0, 0, 0, 64.9f,
            0, 0.9f, 0, 0, 64.9f, 0, 0, 0.9f, 0, 64.9f, 0, 0, 0, 1.0f, 0};

    // 梦幻
    public static final float COLORMATRIX_MENGHUAN[] = {0.8f, 0.3f, 0.1f,
            0.0f, 46.5f, 0.1f, 0.9f, 0.0f, 0.0f, 46.5f, 0.1f, 0.3f, 0.7f, 0.0f,
            46.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 酒红
    public static final float COLORMATRIX_JIUHONG[] = {1.2f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.9f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 胶片
    public static final float COLORMATRIX_FANSE[] = {-1.0f, 0.0f, 0.0f, 0.0f,
            255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f,
            255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 湖光掠影
    public static final float COLORMATRIX_HUGUANG[] = {0.8f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 褐片
    public static final float COLORMATRIX_HEPIAN[] = {1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 复古
    public static final float COLORMATRIX_FUGU[] = {0.9f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 泛黄
    public static final float COLORMATRIX_HUAN_HUANG[] = {1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f,
            0.0f, 0, 0, 0, 1.0f, 0};
    // 传统
    public static final float COLORMATRIX_CHUAN_TONG[] = {1.0f, 0.0f, 0.0f, 0,
            -10f, 0.0f, 1.0f, 0.0f, 0, -10f, 0.0f, 0.0f, 1.0f, 0, -10f, 0, 0,
            0, 1, 0};
    // 胶片2
    public static final float COLORMATRIX_JIAO_PIAN[] = {0.71f, 0.2f, 0.0f,
            0.0f, 60.0f, 0.0f, 0.94f, 0.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.62f,
            0.0f, 60.0f, 0, 0, 0, 1.0f, 0};

    // 锐色
    public static final float COLORMATRIX_RUISE[] = {4.8f, -1.0f, -0.1f, 0,
            -388.4f, -0.5f, 4.4f, -0.1f, 0, -388.4f, -0.5f, -1.0f, 5.2f, 0,
            -388.4f, 0, 0, 0, 1.0f, 0};
    // 清宁
    public static final float COLORMATRIX_QINGNING[] = {0.9f, 0, 0, 0, 0, 0,
            1.1f, 0, 0, 0, 0, 0, 0.9f, 0, 0, 0, 0, 0, 1.0f, 0};
    // 浪漫
    public static final float COLORMATRIX_LANGMAN[] = {0.9f, 0, 0, 0, 63.0f,
            0, 0.9f, 0, 0, 63.0f, 0, 0, 0.9f, 0, 63.0f, 0, 0, 0, 1.0f, 0};
    // 夜色
    public static final float COLORMATRIX_YESE[] = {1.0f, 0.0f, 0.0f, 0.0f,
            -66.6f, 0.0f, 1.1f, 0.0f, 0.0f, -66.6f, 0.0f, 0.0f, 1.0f, 0.0f,
            -66.6f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};

    public static final float[] FUDIAO = {0.39f, 0.769f, 0.189f, 0, 0
            , 0.349f, 0.686f, 0.169f, 0, 0
            , 0.272f, 0.534f, 0.131f, 0, 0
            , 0f, 0f, 0f, 1, 0};

    public static float[][] matrixList = new float[][]{
            COLORMATRIX_HEIBAI, COLORMATRIX_HUAJIU, COLORMATRIX_GETE, COLORMATRIX_DANYA,
            COLORMATRIX_LANDIAO, COLORMATRIX_GUANGYUN, COLORMATRIX_MENGHUAN, COLORMATRIX_JIUHONG,
            COLORMATRIX_FANSE, COLORMATRIX_HUGUANG,
            COLORMATRIX_HEPIAN, COLORMATRIX_FUGU, COLORMATRIX_HUAN_HUANG, COLORMATRIX_CHUAN_TONG,
            COLORMATRIX_JIAO_PIAN, COLORMATRIX_RUISE, COLORMATRIX_QINGNING, COLORMATRIX_LANGMAN, COLORMATRIX_YESE,FUDIAO
    };


}
