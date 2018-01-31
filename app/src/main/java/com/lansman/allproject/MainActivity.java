package com.lansman.allproject;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import saman.com.lib.MethodTime;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");
        set();
    }

    @AspectHelper.Permission(Manifest.permission.SEND_SMS)
    private void set() {
        Log.e(TAG, "有权限后才能发信息给小妹妹");
    }

    @MethodTime
    private void deal() {
        System.out.println("执行方法");
    }
}
