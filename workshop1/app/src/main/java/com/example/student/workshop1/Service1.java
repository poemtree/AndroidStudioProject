package com.example.student.workshop1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;

public class Service1 extends Service {

    public Service1() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
