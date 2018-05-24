package com.example.student.tcsphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent serviceintent = new Intent(getApplicationContext(), ServiceActivity.class);
        serviceintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(serviceintent);*/

        Intent memberIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(memberIntent);

    }

}
