package com.example.student.tcsphone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.student.tcsphone.fragments.BoardFragment;
import com.example.student.tcsphone.fragments.MapFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    // 프래그먼트 수
    private static final int PAGE_NUMBER = 2;

    // 프래그먼트 객체
    private MapFragment mapFragment;
    private BoardFragment boardFragment;
    private Fragment[] fragments;
    private String[] titles;

    // 프래그먼트 전달 받는 생성자
    public TabPagerAdapter(FragmentManager fragmentManager, Fragment... fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }
    public void setTitles(String... strings) {
        titles = strings;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

