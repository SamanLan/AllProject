package com.lansman.allproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class MainActivity extends AppCompatActivity implements QRCodeView.Delegate {

    ZXingView zXingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        zXingView = (ZXingView) findViewById(R.id.zxing);
        zXingView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        zXingView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        zXingView.showScanRect();
        zXingView.startSpot();
    }

    public void click(View v) {
        zXingView.startSpot();
    }

    @Override
    protected void onStop() {
        zXingView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zXingView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        zXingView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        System.out.println("相机error");
    }
}
