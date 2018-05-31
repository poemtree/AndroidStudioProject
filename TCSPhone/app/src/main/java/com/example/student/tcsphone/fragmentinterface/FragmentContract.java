package com.example.student.tcsphone.fragmentinterface;

public interface FragmentContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter<S> extends BasePresenter {
        public FragmentContract.View getFragmentContract();
    }

}
