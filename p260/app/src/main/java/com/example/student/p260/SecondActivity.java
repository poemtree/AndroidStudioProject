package com.example.student.p260;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    TextView textView2;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        textView2 = findViewById(R.id.textView2);
        Intent intent = getIntent();
        num = intent.getIntExtra("num1",0);
        textView2.setText(num+" ");
    }
    public void clickBt(View v){
        int result = num * 3000;
        Intent intent = new Intent();
        intent.putExtra("result",result);
        setResult(RESULT_OK, intent);
        finish();
    }
}