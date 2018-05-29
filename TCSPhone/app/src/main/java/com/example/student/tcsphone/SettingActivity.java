package com.example.student.tcsphone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity {

    // 로그에 표시할 태그명
    private final String tag = "---SettingActivity---";

    // 사용자 정보
    private String id;
    private String pwd;

    // 설정 제어 뷰
    private CheckBox save_id_setting;
    private CheckBox auto_login_setting;

    // 자동로그인을 위한 공유 객체
    private SharedPreferences sharedPreferences;

    public void initUI() {
        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences("tcs_auto_login", MODE_PRIVATE);

        id = sharedPreferences.getString("id",null);
        pwd = sharedPreferences.getString("pwd", null);

        save_id_setting = findViewById(R.id.save_id_setting);
        auto_login_setting = findViewById(R.id.auto_login_setting);

        save_id_setting.setChecked(sharedPreferences.getBoolean("save_id_flag", false));
        auto_login_setting.setChecked(sharedPreferences.getBoolean("auto_login_flag", false));

        save_id_setting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    auto_login_setting.setChecked(b);
                }
            }
        });

        auto_login_setting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    save_id_setting.setChecked(b);
                }
            }
        });

    }

    public void onClickLogoutButton(View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUI();

    }

    public void onClockBackButton(View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
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

    // 아이디저장, 자동로그인 설정 반영
    @Override
    protected void onDestroy() {
        SharedPreferences.Editor se = sharedPreferences.edit();

        if(!save_id_setting.isChecked()) {
            id = null;
        }
        if(!auto_login_setting.isChecked()) {
            pwd = null;
        }
        se.putBoolean("save_id_flag", save_id_setting.isChecked());
        se.putBoolean("auto_login_flag", auto_login_setting.isChecked());
        se.putString("id", id);
        se.putString("pwd", pwd);
        se.commit();
        super.onDestroy();
    }
}
