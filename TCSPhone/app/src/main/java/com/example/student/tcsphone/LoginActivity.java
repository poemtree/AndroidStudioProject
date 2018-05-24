package com.example.student.tcsphone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private String id;
    private String pwd;

    private EditText txt_id;
    private EditText txt_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_id = findViewById(R.id.txt_id);
        txt_pwd = findViewById(R.id.txt_pwd);

    }



}
