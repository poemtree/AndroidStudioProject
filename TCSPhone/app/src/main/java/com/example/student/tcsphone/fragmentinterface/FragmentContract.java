package com.example.student.tcsphone.fragmentinterface;

public interface FragmentContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void buttonClickAction();
    }

}
