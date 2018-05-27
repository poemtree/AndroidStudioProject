package com.example.student.tcsphone;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class CarProgressDialog extends ProgressDialog{
    private Context context;
    private ImageView loading_img;

    public CarProgressDialog(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_progress_dialog);
        loading_img = findViewById(R.id.loading_img);
        AnimationDrawable animationDrawable = (AnimationDrawable)loading_img.getDrawable();
        animationDrawable.start();
    }

    public void show() {
        super.show();
    }

    public void dismiss() {
        super.dismiss();
    }
}
