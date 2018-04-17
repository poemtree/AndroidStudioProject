package com.example.student.p555;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText edtTxt1, edtTxt2;
    TextView txtv;
    Button btn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTxt1 = findViewById(R.id.edtTxt1);
        edtTxt2 = findViewById(R.id.edtTxt2);
        txtv = findViewById(R.id.txtv);
        btn = findViewById(R.id.btn);
        progressDialog = new ProgressDialog(MainActivity.this);
    }

    public void onCliclBtn(View v) {
        String id = edtTxt1.getText().toString();
        String pwd = edtTxt2.getText().toString();
        new LoginTask().execute(id, pwd);
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="0";
            String id = strings[0];
            String pwd = strings[1];
            for(int i = 0; i<5; i++){
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(id.equals("qq") && pwd.equals("11")) {
                result = "1";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            if(s.equals("1")) {

                builder.setMessage("Login")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                edtTxt1.setText("");
                                edtTxt2.setText("");
                                return;
                            }
                        });

                txtv.setText("Login OK!");
            } else {

                builder.setMessage("Login")
                        .setPositiveButton("Fail", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                edtTxt1.setText("");
                                edtTxt2.setText("");
                                return;
                            }
                        });

                txtv.setText("Login Fail!");
            }
            progressDialog.dismiss();

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

}
