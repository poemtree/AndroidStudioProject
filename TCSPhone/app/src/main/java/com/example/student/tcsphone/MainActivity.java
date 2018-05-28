package com.example.student.tcsphone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.student.tcsphone.listview.ListViewAdapter;
import com.example.student.tcsphone.service.ServiceActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOGIN = 100;
    private static final int REQUEST_CODE_SERVICE = 200;


    private final String tag = "-----MainActivity-----";

    private String id;
    private String pwd;
    private String member_seq;
    private String car_num;
    private ListView listView;
    private ListViewAdapter adapter;

    // 초기화 함수
    public void initUI() {
        // 리스트 변수 초기화
        adapter = new ListViewAdapter();
        listView = findViewById(R.id.car_list);
        // 리스트에 어뎁터 할당
        listView.setAdapter(adapter);
    }

    public void startActivity(int code) {
        Intent intent = null;
        switch (code) {
            case REQUEST_CODE_LOGIN :
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("pwd",pwd);
                break;
            case REQUEST_CODE_SERVICE :
                intent = new Intent(getApplicationContext(), ServiceActivity.class);
                break;
        }
        if(intent != null) {
            intent.addFlags(/*Intent.FLAG_ACTIVITY_CLEAR_TOP | */ Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, code);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        startActivity(REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    id = data.getExtras().getString("id");
                    pwd = data.getExtras().getString("pwd");
                    member_seq = data.getExtras().getString("member_seq");
                    new MyCarsTask().execute(member_seq);
                    break;
            }

        } else {

        }
    }

    class MyCarsTask extends AsyncTask<String, Void, Void> {

        private final String tag = "------MyCarsTask------";

        @Override
        protected Void doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection con = null;
            BufferedReader br = null;

            try {
                Thread.sleep(2000);

                Log.e(tag, "LoginTask run");
                url = new URL("http://70.12.114.140/car/allCarinfo.do?member_swq="+strings[0]);
                con = (HttpURLConnection) url.openConnection();
                Log.e(tag, "Ready connection");
                if(con != null) {
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    Log.e(tag, "Succeed connection");
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String[] result = br.readLine().toString().split("/");
                    for(int i = 0; i < result.length; i+=2) {
                        adapter.addVO(getDrawable(R.drawable.mycar), result[i], result[i+1]);
                    }
                }
                Log.e(tag, "LoginTask done");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            return null;
        }
    }

}
