package com.example.student.tcpclient;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Client client;
    private EditText edtTxt;
    private String message;
    private TextView txtV;
    SenderHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTxt = findViewById(R.id.edtTxt);
        txtV = findViewById(R.id.txtV);
        handler = new SenderHandler();
        client = new Client(handler);
        client.start();
    }

    public void onClickBtn(View v) {
        if(edtTxt.getText() != null && !edtTxt.getText().toString().equals("")) {
            message = edtTxt.getText().toString();
            client.sendMessage(message);
            edtTxt.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        if(client != null && client.isAlive()) {
            client.closeConnection();
        }
        super.onDestroy();
    }

    public class SenderHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            txtV.setText(msg.getData().getString("receivedMessage"));
        }
    }

}
