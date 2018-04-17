package com.example.student.p554;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private String tag = "---Main Activity---";
    TextView txtv;
    ProgressBar prgrsb;
    ProgressDialog progressDialog;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtv = findViewById(R.id.txtv);
        btn = findViewById(R.id.btn);
        prgrsb = findViewById(R.id.prgrsb);
        progressDialog = new ProgressDialog(MainActivity.this);
    }

    public void onClickBnt(View v) {
        new MyAsyncTask("192.168.111.100").execute();
        Log.d(tag, "onClickBtn");
    }

    class MyAsyncTask extends AsyncTask<String, Integer, Integer> {
        String msg;
        public MyAsyncTask(String msg) {
            super();
        }

        @Override
        protected void onPreExecute() {
            prgrsb.setMax(55);
            txtv.setText("Start AsyncTask...");
            //btn.setVisibility(View.INVISIBLE);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("What the...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... Strings) {
            int result = 0;
            Log.d(tag, "Start -- ");
            for(int i = 1; i <=10; i++){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result+=i;
                publishProgress(result);
            }
            Log.d(tag, "End -- ");
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            prgrsb.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressDialog.dismiss();
            //btn.setVisibility(View.VISIBLE);
            txtv.setText("End AsyncTask..." + result);
        }
    }
}
