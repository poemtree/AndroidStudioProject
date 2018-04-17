package com.example.student.p553;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.ImageView;

public class MyService extends Service {

    private boolean flag;

    public MyService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null) {
            return Service.START_STICKY;
        } else {
            flag = intent.getBooleanExtra("flag",false);
            if(flag) {
                new Thread(new myRunnable()).start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    class myRunnable implements Runnable {

        @Override
        public void run() {
            for(int i=0;i<5 & flag; i++){
                try{
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Image Switch...
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("img", i);
                startActivity(intent);
            }
        }
    }
}
