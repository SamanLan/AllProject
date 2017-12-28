package com.lansman.allproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("------"+isMainActivityAlive(MainActivity.this,"com.lansman.allproject.MainActivity"));
//                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(intent);
//            }
//        }, 5000);
    }

    private boolean isMainActivityAlive(Context context, String activityName){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
            if (info.topActivity.toString().equals(activityName) || info.baseActivity.toString().equals(activityName)) {
                Log.i("",info.baseActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
                return true;
            }
        }
        return false;
    }
}
