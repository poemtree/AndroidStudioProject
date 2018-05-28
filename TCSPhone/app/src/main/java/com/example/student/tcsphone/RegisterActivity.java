package com.example.student.tcsphone;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    // 로그에 표시한 태그명
    private final String tag = "---RegisterActivity---";

    // 프로그래스 다이얼로그
    private CarProgressDialog carProgressDialog;

    // 회원가입에 필요한 정보
    private String id;
    private String pwd;
    private String name;
    private String tel;

    // 정보를 입력 받을 에디트텍스트
    private EditText rgst_txt_id;
    private EditText rgst_txt_pwd;
    private EditText rgst_txt_name;
    private EditText rgst_txt_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rgst_txt_id = findViewById(R.id.rgst_txt_id);
        rgst_txt_pwd = findViewById(R.id.rgst_txt_pwd);
        rgst_txt_name = findViewById(R.id.rgst_txt_name);
        rgst_txt_tel = findViewById(R.id.rgst_txt_tel);

        // ID 입력칸에 입력 될 때 마다 id 변수에 저장
        rgst_txt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                id  = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // PWD 입력칸에 입력 될 때 마다 pwd 변수에 저장
        rgst_txt_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwd = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // NAME 입력칸에 입력 될 때마다 name 변수에 저장
        rgst_txt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // PHONE 입력칸에 입력 될 때마다 phone 변수에 저장
        rgst_txt_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tel = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        carProgressDialog = new CarProgressDialog(RegisterActivity.this);

    }

    public void onClickRegisterButton(View v) {
        if( id!=null && pwd!=null && name!=null && tel!=null && !id.trim().equals("") && !pwd.trim().equals("") && !name.trim().equals("") && !tel.trim().equals("")) {
            new RegisterTask(RegisterActivity.this).execute(id, pwd, name, tel);
        } else {
            Toast.makeText(this, "입력 정보를 확인해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    class RegisterTask extends AsyncTask<String, Void, Boolean> {

        private final String tag = "-----RegisterTask-----";
        private Activity activity;

        public RegisterTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            carProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean result=false;

            String id = strings[0];
            String pwd = strings[1];
            String name = strings[2];
            String tel = strings[3];

            URL url = null;
            HttpURLConnection con = null;
            BufferedReader br = null;
            try {
                Thread.sleep(2000);
                Log.e(tag, "RegisterTask run");
                url = new URL("http://70.12.114.140/car/registerMember.do?id="+id+"&pwd="+pwd+"&name="+name+"&tel="+tel);
                con = (HttpURLConnection) url.openConnection();
                Log.e(tag, "Ready connection");
                if(con!=null) {
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return result;
                    }
                    Log.e(tag, "Succeed connection");
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    if(!br.readLine().toString().equals("0")) {
                        result = true;
                    } else {
                        Log.e(tag, "Fail to register");
                    }
                }
                Log.e(tag, "RegisterTask done");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            carProgressDialog.dismiss();
            if(aBoolean) {
                Toast.makeText(activity, "회원가입 완료", Toast.LENGTH_SHORT).show();
                activity.finish();
            } else {
                Toast.makeText(activity, "회원가입 실패", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
