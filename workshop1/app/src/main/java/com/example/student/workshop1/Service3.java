package com.example.student.workshop1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Service3 extends Service {
    private static String TAG = "--- Service3 ---";

    public Service3() {

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Service3 onCreate...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service3 onStartCommand...");

        if(intent == null){
            return Service.START_STICKY;
        }else {
            processCommand(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void processCommand(Intent intent){
        int imgSize = intent.getIntExtra("imgsSize", 0);
        int imgNum = intent.getIntExtra("imgNum",0);
        final Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        imgNum = (imgNum+1)%imgSize;
        showIntent.putExtra("imgNum",imgNum);
        Log.d(TAG,"Return : " + imgNum);
        startActivity(showIntent);
        //this.stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Service3 onDestroy...");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
