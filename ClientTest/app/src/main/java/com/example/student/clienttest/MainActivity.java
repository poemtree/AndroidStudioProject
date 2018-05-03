package com.example.student.clienttest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Client client;
    private EditText edtTxt;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTxt = findViewById(R.id.edtTxt);
        client = new Client();
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
}
