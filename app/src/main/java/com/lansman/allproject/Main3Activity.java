package com.lansman.allproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ClassLoader classLoader = getClassLoader();
        while (true) {
            if (classLoader != null) {
                System.out.println("----" + classLoader);
                classLoader = classLoader.getParent();
            } else {
                System.out.println("55555");
                break;
            }
        }

    }

    public void click(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, Main2Activity.class);
        if (view.getId() == R.id.b1) {
            bundle.putInt("index", 0);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (view.getId() == R.id.b2) {
            bundle.putInt("index", 1);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
