package com.example.student.tcsphone.service.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.student.tcsphone.R;
import com.example.student.tcsphone.fragmentinterface.FragmentContract;

public class StatusFragment  extends Fragment implements FragmentContract.View{

    private static final String TAG = "Status";

    private FragmentContract.Presenter mPresenter;

    private TextView mTextView;

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_status, container, false);

        mTextView = (TextView)root.findViewById(R.id.txt_sts);

        return root;
    }

    @Override
    public void setPresenter(FragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

}


