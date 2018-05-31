package com.example.student.tcsphone.service.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.student.tcsphone.R;
import com.example.student.tcsphone.fragmentinterface.FragmentContract;

public class BoardFragment extends Fragment implements FragmentContract.View {
    private static final String tag = "Board";
    private FragmentContract.Presenter mPresenter;
    private WebView wb_board;

    // 사용자 정보
    private String id;
    private String pwd;
    private String car_num;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board, container, false);

        Bundle extra = getArguments();

        id = extra.getString("id");
        pwd = extra.getString("pwd");
        car_num = extra.getString("car_num");

        wb_board = (WebView)root.findViewById(R.id.wb_board);
        wb_board.loadUrl("http://70.12.114.140/car/websettingsp.do?car_num="+car_num);

        return root;
    }

    @Override
    public void setPresenter(FragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
