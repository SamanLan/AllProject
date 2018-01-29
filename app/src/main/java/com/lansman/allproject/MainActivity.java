package com.lansman.allproject;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import saman.com.lib.myClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("test", "onCreate");
//        set();
    }

//    @AspectHelper.Permission(value = {Manifest.permission.SEND_SMS})
//    private void set() {
//        System.out.println("有权限后才会执行");
//    }
}
