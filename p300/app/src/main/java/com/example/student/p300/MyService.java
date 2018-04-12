package com.example.student.p300;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {

    private static String TAG = "--- MyService ---";

    public MyService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service onStartCommand...");

        if(intent == null){
            return Service.START_STICKY;
        }else {
            processCommand(intent);
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Service onCreate...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Service onDestroy...");
    }

    private void processCommand(Intent intent){

        String command = intent.getStringExtra("command");
        final String name = intent.getStringExtra("name");

        Log.d(TAG, "Command : "+ command + ", name : "+name );

        final Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Runnable run = new Runnable() {

            @Override
            public void run() {
                int cnt = 0;
                for(int i = 1;i <= 10;i++){
                    Log.d(TAG,"Process : "+ i);
                    try{
                        cnt += i;
                        showIntent.putExtra("command",name);
                        showIntent.putExtra("cnt",i);
                        startActivity(showIntent);
                        Thread.sleep(1000);
                    }catch (Exception e){
                        Log.d(TAG, "Waiting "+ i + " seconds.");
                    }
                }

            }

        };

        new Thread(run).start();


        //this.stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
