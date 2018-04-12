package com.example.student.p300;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import static android.app.AlertDialog.*;

public class MainActivity extends AppCompatActivity {

    EditText edit_txt;
    Intent intent;
    TextView txtv;
    ProgressBar prgrsbr;
    ImageView imgv;
    int[] imagesId = {R.drawable.bg5, R.drawable.bg1, R.drawable.bg3, R.drawable.bg6};

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_txt = findViewById(R.id.edit_txt);
        txtv = findViewById(R.id.txtv);
        prgrsbr = findViewById(R.id.prgrsbr);
        imgv = findViewById(R.id.imgv);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);

        prgrsbr.setMax(100);
        intent = new Intent(this, MyService.class);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        if(intent != null) {
            String command = intent.getStringExtra("command");
            int cnt = intent.getIntExtra("cnt", 0);
            txtv.setText(command + " "+ String.valueOf(cnt));
            prgrsbr.setProgress(cnt*10);
            imgv.setImageResource(imagesId[cnt%3]);

        }
    }

    public void onClickBtn(View v) {
        String name = edit_txt.getText().toString();
        intent.putExtra("command","start");
        intent.putExtra("name", name);
        startService(intent);
    }

    public void onClickBtn2(View v) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.create();
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intent != null) {
            stopService(intent);
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고!");
        builder.setMessage("끝내시겠습니까?");
        builder.setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.create().show();
    }
}
