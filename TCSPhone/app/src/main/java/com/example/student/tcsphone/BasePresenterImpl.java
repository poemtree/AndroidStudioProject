package com.example.student.tcsphone;

import com.example.student.tcsphone.fragmentinterface.FragmentContract;

public class BasePresenterImpl implements FragmentContract.Presenter {

    private final FragmentContract.View mFragmentContractView;

    public BasePresenterImpl(FragmentContract.View fragmentContractView) {
        mFragmentContractView = fragmentContractView;
        mFragmentContractView.setPresenter(this);
    }

    public FragmentContract.View getFragmentContract() {
        return mFragmentContractView;
    }

}
