package com.example.student.p553;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView imgv1, imgv2, imgv3;
    MyHandler myHandler;
    int[] imgs = {R.drawable.digimon_icon, R.drawable.digivice_tri, R.drawable.img2, R.drawable.chicken_icon, R.drawable.tux};
    int img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgv1 = findViewById(R.id.imgv1);
        imgv2 = findViewById(R.id.imgv2);
        imgv3 = findViewById(R.id.imgv3);

        myHandler = new MyHandler();

    }

    public void onClickBtn(View v) {
        Intent intent = new Intent(this, MyService.class);
        switch (v.getId()) {
            case R.id.btn1:
                new Thread(new MyThread1()).start();
                new Thread(new MyThread2()).start();
                intent.putExtra("flag",true);
                break;
            case R.id.btn2 :
                intent.putExtra("flag",false);
                break;
        }
        startService(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);

    }

    private void processIntent(Intent intent) {
        imgv3.setImageResource(imgs[intent.getIntExtra("img",0)]);
    }

    class MyThread1 implements Runnable {
        @Override
        public void run() {
            for(int i=0;i<5; i++){
                try{
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Image Switch...
                img = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgv1.setImageResource(imgs[img]);
                    }
                });
            }
        }
    }

    class MyThread2 implements Runnable {

        @Override
        public void run() {
            for(int i=0;i<5; i++){
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Image Switch...
                Message msg = new Message();
                Bundle bundle = msg.getData();
                bundle.putInt("img",i);
                myHandler.sendMessage(msg);
            }
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            imgv2.setImageResource(imgs[msg.getData().getInt("img")]);
        }
    }

}
