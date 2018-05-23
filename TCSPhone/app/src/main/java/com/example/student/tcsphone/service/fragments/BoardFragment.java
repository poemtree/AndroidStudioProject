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

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board, container, false);
        wb_board = (WebView)root.findViewById(R.id.wb_board);
        wb_board.loadUrl("https://m.naver.com");

        return root;
    }

    @Override
    public void setPresenter(FragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
