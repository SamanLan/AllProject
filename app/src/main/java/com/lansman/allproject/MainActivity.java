package com.lansman.allproject;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.lansman.allproject.StatueCallBack.LoadingCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadSir sir = new LoadSir.Builder()
                .addCallback(new LoadingCallback())
                .setDefaultCallback(LoadingCallback.class)
                .build();
        final LoadService loadService = sir.register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                // 重新加载逻辑
            }
        });
        loadService.showCallback(LoadingCallback.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadService.showSuccess();
            }
        }, 5000);
    }
}
