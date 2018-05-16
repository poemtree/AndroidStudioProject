package com.example.student.tcsphone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.student.tcsphone.board.BoardFragment;
import com.example.student.tcsphone.board.BoardPresenterImpl;
import com.example.student.tcsphone.map.MapFragment;
import com.example.student.tcsphone.map.MapPresenterImpl;
import com.example.student.tcsphone.status.StatusFragment;
import com.example.student.tcsphone.status.StatusPresenterImpl;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_NUMBER = 3;

    private MapPresenterImpl controlPresenter;

    private StatusPresenterImpl statusPresenter;

    private BoardPresenterImpl boardPresenter;

    public TabPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch ( position ) {
            case 0: {
                MapFragment mapFragment = MapFragment.newInstance();
                controlPresenter = new MapPresenterImpl(mapFragment);

                return mapFragment;
            }

           case 1: {
                StatusFragment statusFragment = StatusFragment.newInstance();
                statusPresenter = new StatusPresenterImpl(statusFragment);

                return statusFragment;
            }

            case 2: {
                BoardFragment boardFragment = BoardFragment.newInstance();
                boardPresenter = new BoardPresenterImpl(boardFragment);
                return boardFragment;
            }
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
                return "Status";
            case 2:
                return "Board";
            default:
                return "Default";
        }
    }
}

