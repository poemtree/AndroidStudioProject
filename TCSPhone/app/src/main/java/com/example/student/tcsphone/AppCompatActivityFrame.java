package com.example.student.tcsphone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

public class AppCompatActivityFrame extends AppCompatActivity {
    private String TAG;

    private CarProgressDialog carProgressDialog;

    public void makeCarProgressDialog(Context context) {
        carProgressDialog = new CarProgressDialog(context);
        carProgressDialog.setCancelable(false);
    }

    public void showCarProgressDialog() {
        carProgressDialog.show();
    }

    public void dismissCarProgressDialog() {
        carProgressDialog.dismiss();
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getTAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Close");
        builder.setMessage("종료하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        builder.setNeutralButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}
