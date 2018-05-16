package com.example.student.tcsphone.map;

import com.example.student.tcsphone.fragmentinterface.FragmentContract;

public class MapPresenterImpl implements FragmentContract.Presenter{

    private final FragmentContract.View mFragmentContractView;

    public MapPresenterImpl(FragmentContract.View fragmentContractView){
        mFragmentContractView = fragmentContractView;
        mFragmentContractView.setPresenter(this);
    }

    @Override
    public void buttonClickAction() {
        mFragmentContractView.ShowToast("Map");
    }
}
