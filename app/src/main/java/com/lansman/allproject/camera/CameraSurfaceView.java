package com.lansman.allproject.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/2/1 15:52<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    CameraHelper cameraHelper;

    public CameraSurfaceView(Context context) {
        super(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        cameraHelper = new CameraHelper(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Camera camera = cameraHelper.open((Activity) getContext(), true);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
