package com.example.student.tcsphone.service;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.student.tcsphone.BasePresenterImpl;
import com.example.student.tcsphone.service.fragments.BoardFragment;
import com.example.student.tcsphone.service.fragments.MapFragment;
import com.example.student.tcsphone.service.fragments.StatusFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    // 프래그먼트 수
    private static final int PAGE_NUMBER = 3;

    // 프래그먼트 객체
    private BasePresenterImpl mapPresenter;
    private BasePresenterImpl boardPresenter;
    private BasePresenterImpl statusPresenter;

    public TabPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch ( position ) {
            case 0:
                MapFragment mapFragment = MapFragment.newInstance();
                mapPresenter = new BasePresenterImpl(mapFragment);
                return mapFragment;
            case 1:
                BoardFragment boardFragment = BoardFragment.newInstance();
                boardPresenter = new BasePresenterImpl(boardFragment);
                return boardFragment;
            case 2:
                StatusFragment statusFragment = StatusFragment.newInstance();
                statusPresenter = new BasePresenterImpl(statusFragment);
                return statusFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch ( position ) {
            case 0:
                return "Map";
            case 1:
                return "Board";
            case 2:
                return "Status";
            default:
                return "Default";
        }
    }
}

