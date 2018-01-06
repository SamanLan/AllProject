package com.lansman.allproject;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.lansman.allproject.JumpYiJumpService.TAG;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/1/6 11:55<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class CalculateHelper {
    /**
     * 阴影颜色
     */
    private final int SHADOW_COLOR = 0xff898C91;
    /**
     * 背景颜色会变
     */
    private int bgColor = 0xffCFD3DC;

    /**
     * 计算得到顶点point
     */
    private Point calculateTopPoint(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = getPicturePixel(bitmap);
        if (pixels != null) {
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] != bgColor) {
                    return new Point(i % width - 1, i / width);
                }
            }
        }
        Log.d(TAG, "无法获取到顶点位置");
        return null;
    }

    private Point calculateLeftPoint(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Point tmpPoint = new Point(0, 0);
        int[] pixels = getPicturePixel(bitmap);
        if (pixels != null) {
            for (int i = 0; i < pixels.length; i++) {
                int pix = pixels[i];
                if (pix != bgColor && pix != SHADOW_COLOR) {
                    int x = i % width - 1;
                    int y = i / width;
                    if (tmpPoint.x == 0) {
                        tmpPoint.x = x;
                        tmpPoint.y = y;
                    } else {
                        if (x <= tmpPoint.x && y > tmpPoint.y) {
                            return tmpPoint;
                        } else {
                            tmpPoint.x = x;
                            tmpPoint.y = y;
                        }
                    }
                }
            }
        }
        Log.d(TAG, "无法获取到最新箱子的最左端位置");
        return null;
    }

    private Point findPeopleBottonPoint() {
        return null;
    }

    /**
     * 计算要跳的距离
     */
    public int calculateJumpDistance(@NonNull Bitmap bitmap) {
        Point next = calculateMidPoint(calculateTopPoint(bitmap),calculateLeftPoint(bitmap));
        Point now = findPeopleBottonPoint();
        return (int) Math.sqrt(Math.pow(next.x - now.x, 2) + Math.pow(next.y - now.y, 2));
    }

    private Point calculateMidPoint(Point topPoint, Point leftPoint) {
        return new Point(topPoint.x, leftPoint.y);
    }

    /**
     * 获取像素信息
     */
    private int[] getPicturePixel(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 保存所有的像素的数组，图片宽×高
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//        for (int i = 0; i < pixels.length; i++) {
//            int clr = pixels[i];
//            int red = (clr & 0x00ff0000) >> 16; // 取高两位
//            int green = (clr & 0x0000ff00) >> 8; // 取中两位
//            int blue = clr & 0x000000ff; // 取低两位
//            Log.d("tag", "r=" + red + ",g=" + green + ",b=" + blue);
//        }
        return pixels;
    }
}
