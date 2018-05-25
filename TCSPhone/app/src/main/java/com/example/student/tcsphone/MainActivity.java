package com.example.student.tcsphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOGIN = 101;

    private final String tag = "-----MainActivity-----";

    private String id;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent serviceintent = new Intent(getApplicationContext(), ServiceActivity.class);
        serviceintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(serviceintent);*/

        Intent memberIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(memberIntent, REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    id = data.getExtras().getString("id");
                    pwd = data.getExtras().getString("pwd");
                    break;
                default:
                    break;
            }

        }
    }

}
