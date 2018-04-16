package com.example.student.p536;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView txtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtv = findViewById(R.id.txtv);
    }

    public void onClickBtn(View v) {
        t.start();
    }

    class MyThread1 implements Runnable {
        @Override
        public void run() {

        }
    }

    class MyThread2 extends Thread {

        @Override
        public void run() {
            super.run();
        }
    }

    Thread t = new Thread(new Runnable() {
        int i = 0;
        @Override
        public void run() {
            while (i < 10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtv.setText(String.valueOf(i));
                    }
                });

            }
        }
    });
}
