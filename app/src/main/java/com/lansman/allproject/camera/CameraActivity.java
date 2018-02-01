package com.lansman.allproject.camera;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.lansman.allproject.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends Activity {

    @BindView(R.id.btn_open)
    Button btnOpen;
    @BindView(R.id.btn_change)
    Button btnChange;
    CameraHelper cameraHelper;
    @BindView(R.id.sv_camera)
    SurfaceView svCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        cameraHelper = new CameraHelper(this);
    }

    @OnClick({R.id.btn_open, R.id.btn_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                break;
            case R.id.btn_change:
                break;
            default:
        }
    }
}
