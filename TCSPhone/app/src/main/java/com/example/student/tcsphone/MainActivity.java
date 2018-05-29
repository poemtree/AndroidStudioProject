package com.example.student.tcsphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.student.tcsphone.service.TabPagerAdapter;

public class MainActivity extends AppCompatActivity {

    // 엑티비티 코드
    public static final int REQUEST_CODE_LOGIN = 100;
    public static final int REQUEST_CODE_CARLIST = 200;
    public static final int REQUEST_CODE_SETTING = 300;

    // 로그에 표시할 태그명
    private final String tag = "-----MainActivity-----";

    // 프래그먼트 이용을 위한 탭과 뷰페이저
    private TabLayout mTab;
    private ViewPager mViewPager;

    // 사용자 정보 저장
    private String id;
    private String pwd;
    private String member_seq;
    private String car_num;

    // 엑티비티를 호출하는 함수
    public void startActivity(int code) {
        Intent intent = null;
        switch (code) {
            case REQUEST_CODE_LOGIN :
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                break;
            case REQUEST_CODE_CARLIST :
                intent = new Intent(getApplicationContext(), CarListActivity.class);
                intent.putExtra("member_seq", member_seq);
                break;
            case REQUEST_CODE_SETTING :
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                break;
        }
        if(intent != null) {
            startActivityForResult(intent, code);
        }
    }

    public void onClickSettingButton(View v) {
        startActivity(REQUEST_CODE_SETTING);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 탭 생성 및 적용
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
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

        // 로그인 액티비티 실행
        startActivity(REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(tag,"Result Check");
        if(resultCode==RESULT_OK) {
            Log.e(tag,"Result OK");
            switch (requestCode) {
                case REQUEST_CODE_LOGIN :
                    Log.e(tag,"REQUEST_CODE_LOGIN");
                    id = data.getExtras().getString("id");
                    pwd = data.getExtras().getString("pwd");
                    member_seq = data.getExtras().getString("member_seq");
                    startActivity(REQUEST_CODE_CARLIST);
                    break;
                case REQUEST_CODE_CARLIST :
                    Log.e(tag,"REQUEST_CODE_CARLIST");
                    car_num = data.getExtras().getString("car_num");
                    break;
                case REQUEST_CODE_SETTING :
                    Log.e(tag,"REQUEST_CODE_SETTING");
                    break;
            }

        } else {
            Log.e(tag,"Result not OK");
            finish();
        }
    }

}
