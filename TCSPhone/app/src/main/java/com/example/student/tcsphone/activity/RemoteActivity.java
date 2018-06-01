package com.example.student.tcsphone.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.student.tcsphone.AppCompatActivityFrame;
import com.example.student.tcsphone.R;
import com.example.student.tcsphone.TabPagerAdapter;
import com.example.student.tcsphone.fragments.RemoteConditionFragment;
import com.example.student.tcsphone.fragments.RemoteCoreFragment;

public class RemoteActivity extends AppCompatActivityFrame {

    private ViewPager mViewPager;

    private RemoteCoreFragment remoteCoreFragment;
    private RemoteConditionFragment remoteConditionFragment;

    // 프래그먼트에 정보 전달을 위한 공유객체
    private SharedPreferences remoteSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        remoteCoreFragment = RemoteCoreFragment.newInstance();
        remoteConditionFragment = RemoteConditionFragment.newInstance();

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), remoteCoreFragment, remoteConditionFragment);
        tabPagerAdapter.setTitles("Core", "Condition");
        mViewPager = (ViewPager) findViewById(R.id.retmote_view_pager);
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

    }

    public void onClockBackButton(View v) {
        Intent intent = new Intent();
        intent.putExtra("Relogin",false);
        setResult(RESULT_OK,intent);
        finish();
    }
}
