package com.example.student.tcsphone.board;

import com.example.student.tcsphone.fragmentinterface.FragmentContract;

public class BoardPresenterImpl implements FragmentContract.Presenter {

    private final FragmentContract.View mFragmentContractView;

    public BoardPresenterImpl(FragmentContract.View fragmentContractView) {
        mFragmentContractView = fragmentContractView;
        mFragmentContractView.setPresenter(this);
    }

    @Override
    public void buttonClickAction() {
        mFragmentContractView.ShowToast("Board");
    }
}
