package com.example.student.tcsphone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.student.tcsphone.AppCompatActivityFrame;
import com.example.student.tcsphone.R;
import com.example.student.tcsphone.listview.ListVO.MyCar;
import com.example.student.tcsphone.listview.ListViewAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class CarListActivity extends AppCompatActivityFrame {

    // 로그에 표시할 태그명
    private final String TAG = "---CarListActivity---";

    // 사용자 정보
    private String member_seq;
    private String car_num;
    private int car_img;

    // 리스트뷰와 어뎁터
    private ListView listView;
    private ListViewAdapter adapter;

    // 리스트에 보여줄 자동차 이미지 맵
    private HashMap<String, Integer> logos;

    // 리스트 초기화 함수
    public void initListView() {
        // 리스트 변수 초기화
        adapter = new ListViewAdapter();
        listView = findViewById(R.id.car_list);

        // 리스트 아이템 클릭 리스트너 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showCarProgressDialog();

                // 클릭된 아이템을 MyCar로 변환
                MyCar item = (MyCar) adapterView.getItemAtPosition(position);
                // 차의 고유번호 획득..
                Intent intent = new Intent();
                intent.putExtra("car_num", item.getCar_num());
                intent.putExtra("Relogin",false);
                intent.putExtra("car_type", item.getCar_memo());
                setResult(RESULT_OK, intent);
                dismissCarProgressDialog();
                finish();
            }
        });
    }

    public void onClickLogoutButton(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Close");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.putExtra("Relogin",true);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        builder.setNeutralButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlist);

        makeCarProgressDialog(CarListActivity.this);

        // 회원 번호 저장
        Intent intent = getIntent();
        member_seq = intent.getExtras().getString("member_seq");

        Log.e(TAG, "member_seq : " + member_seq);

        // 리스트뷰 초기화
        initListView();

        // 이미지 맵 생성
        logos = new HashMap<>();
        logos.put("audiA6", R.drawable.audi_a6);
        logos.put("BMWi8", R.drawable.bmw_i8);
        logos.put("sm5", R.drawable.sm5);
        logos.put("sedan", R.drawable.sm5);

        new MyCarsTask().execute(member_seq);
    }

    class MyCarsTask extends AsyncTask<String, Void, Void> {

        private final String tag = "------MyCarsTask------";

        @Override
        protected void onPreExecute() {
            showCarProgressDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection con = null;
            BufferedReader br = null;

            try {
                Log.e(tag, "MyCarsTask run -> " + strings[0]);
                url = new URL("http://70.12.114.140/car/allCarinfo.do?member_seq="+strings[0]);
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
                        Log.e(tag,"car_num : " + result[i] + ", car_memo : " + result[i+1]);
                        adapter.addVO(getDrawable(logos.get(result[i+1])), result[i], result[i+1]);
                    }
                }
                Log.e(tag, "MyCarsTask done");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(NullPointerException ne) {
                ne.printStackTrace();
            } finally {
                con.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.setAdapter(adapter);
            dismissCarProgressDialog();
        }
    }

}
