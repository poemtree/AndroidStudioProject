package com.example.student.p585;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText,editText2;
    LoginTask loginTask;
    boolean flag;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edtTxt1);
        editText2 = findViewById(R.id.edtTxt1);
        builder = new AlertDialog.Builder(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this);

    }

    //network to server (JAVA : android)
    public void clickBt(View v){
        String id = editText.getText().toString();
        String pwd = editText2.getText().toString();
        //more safe to code
        if(id == null || pwd == null || id.equals("") || pwd.equals("")){
            return;
        }

        loginTask = new LoginTask("http://70.12.114.134/android/login.jsp");
        loginTask.execute(id.trim(), pwd.trim());

        new Thread(new MyRunnable()).start();
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new LocationTask("http://70.12.114.134/android/location.jsp").execute(37.12, 127.123);
            }
        }
    }

    //Thread to server (input, update, return)
    class LoginTask extends AsyncTask<String, String, String>{

        String url;
        //constructor
        public LoginTask(){

        }
        public LoginTask(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Login..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String pwd = strings[1];
            //To JSP
            url += "?id="+id+"&pwd="+pwd;

            //HTTP REQUEST
            StringBuilder sb = new StringBuilder();
            URL url;
            HttpURLConnection con = null;
            BufferedReader br = null;
            try{
                url = new URL(this.url);
                //Connection
                con = (HttpURLConnection)url.openConnection();
                if(con != null){
                    con.setConnectTimeout(5000);
                    //con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode()!=HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while ((line=br.readLine())!=null) {
                        sb.append(line);
                    }
                }
            }catch (Exception e){
                progressDialog.dismiss();
                return e.getMessage(); //postExecute
                //
            }finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.disconnect();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            String text="";
            if(s.equals("1")) {
                text = "Login OK";
            } else if(s.equals("0")) {
                text = "Login Fail";
            } else {
                text = "Error";
            }

            if(progressDialog!=null) {
                progressDialog.dismiss();
            }
            Toast.makeText(MainActivity.this, text+s, Toast.LENGTH_SHORT).show();
        }
    }

    class LocationTask extends AsyncTask<Double, Void, Void> {
        String url;
        public LocationTask(String url) {
            this.url = url;
        }
        @Override
        protected Void doInBackground(Double... doubles) {
            Double lati = doubles[0];
            Double longi = doubles[1];

            url += "?lati="+lati+"&longi="+longi;

            //HTTP REQUEST
            StringBuilder sb = new StringBuilder();
            URL url;
            HttpURLConnection con = null;
            try{
                url = new URL(this.url);
                //Connection
                con = (HttpURLConnection)url.openConnection();
                if(con != null){
                    con.setConnectTimeout(5000);
                    //con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode()!=HttpURLConnection.HTTP_OK){
                        return null;
                    }
                }
            }catch (Exception e){

            }finally {
                con.disconnect();
            }

            return null;
        }

    }

    @Override
    public void onBackPressed() {
        builder.setTitle("Alert");
        builder.setMessage("Do you want to finish this app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flag = false;
                // 쓰래드의 무한루프가 flag 변경을 인식할 수 있도록 센스 있게 슬립을 걸어주자
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}