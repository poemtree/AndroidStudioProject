package com.example.student.tcsphone.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.student.tcsphone.AppCompatActivityFrame;
import com.example.student.tcsphone.R;
import com.example.student.tcsphone.TabPagerAdapter;
import com.example.student.tcsphone.fragments.BoardFragment;
import com.example.student.tcsphone.fragments.MapFragment;

public class MainActivity extends AppCompatActivityFrame {

    // 엑티비티 코드
    private static final int REQUEST_CODE_LOGIN = 100;
    private static final int REQUEST_CODE_RELOGIN = 200;
    private static final int REQUEST_CODE_CARLIST = 300;
    private static final int REQUEST_CODE_SETTING = 400;
    private static final int REQUEST_CODE_REMOTE = 500;

    // 프래그먼트 이용을 위한 탭과 뷰페이저
    private TabLayout mTab;
    private ViewPager mViewPager;

    // 프래그먼트
    private MapFragment mapFragment;
    private BoardFragment boardFragment;

    // 프래그먼트에 정보 전달을 위한 공유객체
    private SharedPreferences fragmentSharedPreferences;

    // 사용자 정보
    private String id;
    private String pwd;
    private String member_seq;
    private String car_num;
    private String car_type;

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
                intent.putExtra("car_num", car_num);
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

    // 프래그먼트 및 탭 초기화 함수
    public void initUI() {

        // 프래그먼트 생성
        mapFragment = new MapFragment();
        boardFragment = new BoardFragment();


        // 탭 어뎁터 및 뷰 패이저 생성
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mapFragment, boardFragment);
        tabPagerAdapter.setTitles("Map", "Board");
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

    public void setFragmentSharedPreferences() {
        SharedPreferences.Editor se = fragmentSharedPreferences.edit();
        se.putString("id", id);
        se.putString("pwd", pwd);
        se.putString("car_num", car_num);
        se.putString("car_type", car_type);
        se.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그에 표시할 태그명
        setTAG("-----MainActivity-----");

        // 프로그래스 다이얼로그 생성
        makeCarProgressDialog(MainActivity.this);

        // 공유객체 생성
        fragmentSharedPreferences = getSharedPreferences("tcs_fragment", MODE_PRIVATE);

        // 로그인 액티비티 실행
        startActivity(REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(getTAG(),"Result Check");
        if(resultCode==RESULT_OK && !data.getExtras().getBoolean("Relogin")) {
            Log.e(getTAG(),"Result OK");
            switch (requestCode) {
                case REQUEST_CODE_RELOGIN :
                    Log.e(getTAG(),"REQUEST_CODE_RELOGIN");
                case REQUEST_CODE_LOGIN :
                    Log.e(getTAG(),"REQUEST_CODE_LOGIN");
                    id = data.getExtras().getString("id");
                    pwd = data.getExtras().getString("pwd");
                    member_seq = data.getExtras().getString("member_seq");
                    startActivity(REQUEST_CODE_CARLIST);
                    break;
                case REQUEST_CODE_CARLIST :
                    Log.e(getTAG(),"REQUEST_CODE_CARLIST");
                    car_num = data.getExtras().getString("car_num");
                    car_type = data.getExtras().getString("car_type");
                    // 프래그먼트 생성
                    setFragmentSharedPreferences();
                    // 딜레이
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            showCarProgressDialog();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            dismissCarProgressDialog();
                            initUI();
                        }
                    }.execute();
                    break;
                case REQUEST_CODE_SETTING :
                    Log.e(getTAG(),"REQUEST_CODE_SETTING");
                    break;
            }

        } else if(resultCode==RESULT_CANCELED) {
            Log.e(getTAG(),"Result not OK");
            finish();
        } else {
            if(requestCode == REQUEST_CODE_SETTING) {
                // 딜레이
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        showCarProgressDialog();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        mapFragment.setMapFlag(false);
                        dismissCarProgressDialog();
                        startActivity(REQUEST_CODE_RELOGIN);
                    }
                }.execute();
            } else {
                startActivity(REQUEST_CODE_RELOGIN);
            }
        }
    }

    @Override
    protected void onDestroy() {
        fragmentSharedPreferences.edit().clear().commit();
        super.onDestroy();
    }

}
