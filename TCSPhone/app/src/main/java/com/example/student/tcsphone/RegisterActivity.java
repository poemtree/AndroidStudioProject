package com.example.student.tcsphone;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imgV = findViewById(R.id.imageView4);
        AnimationDrawable animationDrawable = (AnimationDrawable)imgV.getBackground();
        animationDrawable.start();
    }
}
