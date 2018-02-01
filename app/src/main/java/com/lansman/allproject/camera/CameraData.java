package com.lansman.allproject.camera;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/2/1 10:59<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class CameraData {
    public static final int FACING_FRONT = 1;
    public static final int FACING_BACK = 2;

    public int cameraID;            //camera的id
    public int cameraFacing;        //区分前后摄像头
    public int cameraWidth;         //camera的采集宽度
    public int cameraHeight;        //camera的采集高度
    public boolean hasLight;        //camera是否有闪光灯
    public int orientation;         //camera旋转角度
    public boolean supportTouchFocus;   //camera是否支持手动对焦
    public boolean touchFocusMode;      //camera是否处在自动对焦模式

    public CameraData(int id, int facing, int width, int height){
        cameraID = id;
        cameraFacing = facing;
        cameraWidth = width;
        cameraHeight = height;
    }

    public CameraData(int id, int facing) {
        cameraID = id;
        cameraFacing = facing;
    }
}
