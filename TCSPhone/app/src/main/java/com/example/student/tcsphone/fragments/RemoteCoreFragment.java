package com.example.student.tcsphone.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student.tcsphone.R;

public class RemoteCoreFragment extends Fragment {

    private static final String TAG = "---RemoteFragment---";

    //공유된 정보를 받아올 공유객채
    SharedPreferences fragmentSharedPreferences;

    public static RemoteCoreFragment newInstance() {
        return new RemoteCoreFragment();
    }

    public void onClickEngineButton(View v){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remote_core, container, false);

        fragmentSharedPreferences = getContext().getSharedPreferences("tcs_remote", Context.MODE_PRIVATE);

        return root;
    }

}
