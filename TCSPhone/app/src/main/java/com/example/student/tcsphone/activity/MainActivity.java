package com.example.student.tcsphone.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.student.tcsphone.R;
import com.example.student.tcsphone.service.TabPagerAdapter;

public class MainActivity extends AppCompatActivityFrame {

    // 엑티비티 코드
    private static final int REQUEST_CODE_LOGIN = 100;
    private static final int REQUEST_CODE_RELOGIN = 200;
    private static final int REQUEST_CODE_CARLIST = 300;
    private static final int REQUEST_CODE_SETTING = 400;
    private static final int REQUEST_CODE_REMOTE = 500;

    // 로그에 표시할 태그명
    private final String TAG = "-----MainActivity-----";

    // 프래그먼트 이용을 위한 탭과 뷰페이저
    private TabLayout mTab;
    private ViewPager mViewPager;

    // 사용자 정보 저장
    private String id;
    private String pwd;
    private String member_seq;
    private String car_num;
    private int car_img;

    // 엑티비티를 호출하는 함수
    public void startActivity(int code) {
        Intent intent = null;
        switch (code) {
            case REQUEST_CODE_LOGIN :
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("Relogin",false);
                break;
            case REQUEST_CODE_RELOGIN :
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("Relogin",true);
                break;
            case REQUEST_CODE_CARLIST :
                intent = new Intent(getApplicationContext(), CarListActivity.class);
                intent.putExtra("member_seq", member_seq);
                break;
            case REQUEST_CODE_SETTING :
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("pwd", pwd);
                break;
            case REQUEST_CODE_REMOTE :
                intent = new Intent(getApplicationContext(), RemoteActivity.class);
                break;
        }
        if(intent != null) {
            startActivityForResult(intent, code);
        }
    }

    public void onClickSettingButton(View v) {
        startActivity(REQUEST_CODE_SETTING);
    }

    public void onClickRemoteButton(View v) {
        startActivity(REQUEST_CODE_REMOTE);
    }

    public void initUI() {
        // 탭 생성 및 적용
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        tabPagerAdapter.setInfo(id, pwd, car_num);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(tabPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTab = (TabLayout) findViewById(R.id.tabs);
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프로그래스 다이얼로그 생성
        makeCarProgressDialog(MainActivity.this);

        // 로그인 액티비티 실행
        startActivity(REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"Result Check");
        if(resultCode==RESULT_OK && !data.getExtras().getBoolean("Relogin")) {
            Log.e(TAG,"Result OK");
            switch (requestCode) {
                case REQUEST_CODE_RELOGIN :
                    Log.e(TAG,"REQUEST_CODE_RELOGIN");
                case REQUEST_CODE_LOGIN :
                    Log.e(TAG,"REQUEST_CODE_LOGIN");
                    id = data.getExtras().getString("id");
                    pwd = data.getExtras().getString("pwd");
                    member_seq = data.getExtras().getString("member_seq");
                    startActivity(REQUEST_CODE_CARLIST);
                    break;
                case REQUEST_CODE_CARLIST :
                    Log.e(TAG,"REQUEST_CODE_CARLIST");
                    car_num = data.getExtras().getString("car_num");
                    car_img = data.getExtras().getInt("car_img");
                    initUI();
                    break;
                case REQUEST_CODE_SETTING :
                    Log.e(TAG,"REQUEST_CODE_SETTING");
                    break;
            }

        } else if(resultCode==RESULT_CANCELED) {
            Log.e(TAG,"Result not OK");
            finish();
        } else {
            if(requestCode == REQUEST_CODE_SETTING) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        showCarProgressDialog();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        dismissCarProgressDialog();
                        startActivity(REQUEST_CODE_RELOGIN);
                    }
                }.execute();
            } else {
                startActivity(REQUEST_CODE_RELOGIN);
            }
        }
    }

}
