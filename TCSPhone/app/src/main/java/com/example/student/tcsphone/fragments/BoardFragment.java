package com.example.student.tcsphone.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.student.tcsphone.R;

public class BoardFragment extends Fragment {
    private static final String TAG = "-----BoardFragment-----";
    private WebView wb_board;

    // 사용자 정보
    private String car_num;
    private String car_type;


    //공유된 정보를 받아올 공유객채
    SharedPreferences fragmentSharedPreferences;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board, container, false);

        fragmentSharedPreferences = getContext().getSharedPreferences("tcs_fragment", Context.MODE_PRIVATE);

        car_num = fragmentSharedPreferences.getString("car_num", null);
        car_type = fragmentSharedPreferences.getString("car_type", "sedan");

        Log.e(TAG, "car_num : " + car_num);
        Log.e(TAG, "car_type : " + car_type);


        wb_board = (WebView)root.findViewById(R.id.wb_board);
        WebSettings webSettings = wb_board.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wb_board.loadUrl("http://70.12.114.140/car/appdiagnosis.do?car_num="+car_num+"&car_type="+car_type);

        return root;
    }

}
