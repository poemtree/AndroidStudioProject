package com.example.student.tcsphone.listview.ListVO;

import android.graphics.drawable.Drawable;

public class MyCar {
    private Drawable car_img;
    private String car_num;
    private String car_memo;

    public Drawable getCar_img() {
        return car_img;
    }

    public void setCar_img(Drawable car_img) {
        this.car_img = car_img;
    }

    public String getCar_num() {
        return car_num;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }

    public String getCar_memo() {
        return car_memo;
    }

    public void setCar_memo(String car_memo) {
        this.car_memo = car_memo;
    }
}
