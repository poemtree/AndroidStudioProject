package com.example.student.tcsphone.fragmentinterface;

public interface BaseView<T> {
    void setPresenter(T presenter);
    void ShowToast(String text);

}