package com.example.student.tcsphone.memberfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.student.tcsphone.R;
import com.example.student.tcsphone.fragmentinterface.FragmentContract;

public class LoginFragment extends Fragment implements FragmentContract.View  {

    private static final String tag = "Login";
    private FragmentContract.Presenter mPresenter;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        return root;
    }

    @Override
    public void setPresenter(FragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
