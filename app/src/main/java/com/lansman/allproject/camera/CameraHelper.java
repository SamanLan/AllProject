package com.lansman.allproject.camera;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/2/1 10:28<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> 相机帮助类，使用的是Camera1 <br>
 */

public class CameraHelper {

    /**
     * 检查摄像头服务是否可用
     *
     * @param context 上下文
     * @return 是否可用
     */
    public static boolean checkCameraService(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return devicePolicyManager != null && !devicePolicyManager.getCameraDisabled(null);
    }

    /**
     * 获取所有摄像头信息
     *
     * @param isBackFirst 是否优先后置摄像头
     * @return 所有摄像头信息
     */
    public static List<CameraData> getAllCameraData(Context context, boolean isBackFirst) {
        List<CameraData> list = new ArrayList<>();
        if (checkCameraService(context)) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            int number = Camera.getNumberOfCameras();
            for (int i = 0; i < number; i++) {
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    CameraData cameraData = new CameraData(i, CameraData.FACING_BACK);
                    if (isBackFirst) {
                        list.add(0, cameraData);
                    } else {
                        list.add(cameraData);
                    }
                } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    CameraData cameraData = new CameraData(i, CameraData.FACING_FRONT);
                    if (isBackFirst) {
                        list.add(cameraData);
                    } else {
                        list.add(0, cameraData);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 检查指定相机是否有闪光灯
     */
    public static boolean supportFlash(Camera camera){
        Camera.Parameters params = camera.getParameters();
        List<String> flashModes = params.getSupportedFlashModes();
        if(flashModes == null) {
            return false;
        }
        for(String flashMode : flashModes) {
            if(Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
                return true;
            }
        }
        return false;
    }

    private CameraData currentCameraData;
    private List<CameraData> cameraDataList;
    private Camera camera;
    private final Object lock = new Object();
    public boolean isBack;

    public CameraHelper(Context context) {
        cameraDataList = getAllCameraData(context, false);
    }

    public Camera open(Activity activity, boolean isBack) throws RuntimeException {
        if (isBack) {
            if (cameraDataList.size() >= 2) {
                return openCamera(activity, cameraDataList.get(1));
            } else {
                throw new RuntimeException("没有后置相机");
            }
        } else {
            if (cameraDataList.size() >= 1) {
                return openCamera(activity, cameraDataList.get(0));
            } else {
                throw new RuntimeException("没有前置相机");
            }
        }
    }

    public Camera openCamera(Activity activity, CameraData cameraData) throws RuntimeException {
        if (cameraData == currentCameraData) {
            return camera;
        }
        synchronized (lock) {
            if (camera != null) {
                releaseCamera();
            }
            try {
                camera = Camera.open(cameraData.cameraID);
            } catch (Exception e) {
                throw new RuntimeException("相机打开失败" + e.getMessage());
            }
            if (camera == null) {
                throw new RuntimeException("相机打开失败");
            }
            currentCameraData = cameraData;
            isBack = cameraData.cameraID == CameraData.FACING_BACK;
            setPreviewFormat(camera.getParameters());
            setPreviewCallback();
            setCameraDisplayOrientation(activity);
            setCameraFps(15);
            return camera;
        }
    }

    /**
     * 设置预览回调的图片格式。
     */
    public void setPreviewFormat(Camera.Parameters parameters) {
        if (camera == null) {
            return;
        }
        //设置预览回调的图片格式
        try {
            // 编码格式，可以改变
            parameters.setPreviewFormat(ImageFormat.NV21);
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算预览图像大小
     * 在摄像头相关处理中，一个比较重要的是屏幕显示大小和摄像头预览大小比例不一致的处理。在Android中，摄像头有一系列的Preview Size，我们需要从中选出适合的Preview Size。
     * @param width 宽
     * @param height 高
     * @return 预览图像大小
     */
    public Camera.Size getOptimalPreviewSize(int width, int height) {
        if (camera == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minHeightDiff = Double.MAX_VALUE;
        double minWidthDiff = Double.MAX_VALUE;
        List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
        if (sizes == null) {
            return null;
        }
        //找到宽度差距最小的
        for(Camera.Size size:sizes){
            if (Math.abs(size.width - width) < minWidthDiff) {
                minWidthDiff = Math.abs(size.width - width);
            }
        }
        //在宽度差距最小的里面，找到高度差距最小的
        for(Camera.Size size:sizes){
            if(Math.abs(size.width - width) == minWidthDiff) {
                if(Math.abs(size.height - height) < minHeightDiff) {
                    optimalSize = size;
                    minHeightDiff = Math.abs(size.height - height);
                }
            }
        }
        if (optimalSize != null) {
            currentCameraData.cameraHeight = optimalSize.height;
            currentCameraData.cameraWidth = optimalSize.width;
        }
        return optimalSize;
    }

    public int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
            default:
        }
        return 0;
    }

    /**
     * 在Android中摄像头出来的图像需要进行一定的旋转，然后才能交给屏幕显示，而且如果应用支持屏幕旋转的话，也需要根据旋转的状况实时调整摄像头的角度。
     * @param activity a
     */
    public void setCameraDisplayOrientation(Activity activity) {
        if (camera == null) {
            return;
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(currentCameraData.cameraID, info);
        int degrees = getDisplayRotation(activity);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 通过Camera.Parameters中getSupportedPreviewFpsRange()可以获得摄像头支持的帧率变化范围，从中选取合适的设置给摄像头即可。
     * @param fps 期望帧率
     */
    public void setCameraFps(int fps) {
        if (camera == null) {
            return;
        }
        Camera.Parameters params = camera.getParameters();
        int[] range = adaptPreviewFps(fps, params.getSupportedPreviewFpsRange());
        params.setPreviewFpsRange(range[0], range[1]);
        camera.setParameters(params);
    }

    private int[] adaptPreviewFps(int expectedFps, List<int[]> fpsRanges) {
        expectedFps *= 1000;
        int[] closestRange = fpsRanges.get(0);
        int measure = Math.abs(closestRange[0] - expectedFps) + Math.abs(closestRange[1] - expectedFps);
        for (int[] range : fpsRanges) {
            if (range[0] <= expectedFps && range[1] >= expectedFps) {
                int curMeasure = Math.abs(range[0] - expectedFps) + Math.abs(range[1] - expectedFps);
                if (curMeasure < measure) {
                    closestRange = range;
                    measure = curMeasure;
                }
            }
        }
        return closestRange;
    }

    /**
     * 设置对焦模式
     */
    public void setAutoFocusMode() {
        if (camera == null) {
            return;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.size() > 0 && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(parameters);
            } else if (focusModes.size() > 0) {
                parameters.setFocusMode(focusModes.get(0));
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置对焦模式
     */
    public void setTouchFocusMode() {
        if (camera == null) {
            return;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.size() > 0 && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                camera.setParameters(parameters);
            } else if (focusModes.size() > 0) {
                parameters.setFocusMode(focusModes.get(0));
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换闪光灯
     */
    public void switchLight() {
        Camera.Parameters cameraParameters = camera.getParameters();
        if (cameraParameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
            cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        } else {
            cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        try {
            camera.setParameters(cameraParameters);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPreviewCallback() {
        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                System.out.println("相机数据" + data.toString());
            }
        });
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
