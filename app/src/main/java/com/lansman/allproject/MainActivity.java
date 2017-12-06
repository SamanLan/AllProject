package com.lansman.allproject;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lansman.allproject.helper.ClassHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void say(View view) {

        new testclass().say();
    }

    public void fix(View view) {

        new ClassHelper().structure(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aTest/class2.dex"));
    }
}
