package com.example.student.tcsphone.status;

import com.example.student.tcsphone.fragmentinterface.FragmentContract;

public class StatusPresenterImpl implements FragmentContract.Presenter{

    private final FragmentContract.View mFragmentContractView;

    public StatusPresenterImpl(FragmentContract.View fragmentContractView){
        mFragmentContractView = fragmentContractView;
        mFragmentContractView.setPresenter(this);
    }

    @Override
    public void buttonClickAction() {
        mFragmentContractView.ShowToast("Status");
    }
}
