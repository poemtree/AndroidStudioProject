package com.example.student.tcsphone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    // 로그에 표시할 태그명
    private final String tag = "---LoginActivity---";

    // 프로그래스 다이얼로그
    CarProgressDialog carProgressDialog;

    // 회원 정보
    private String id;
    private String pwd;
    private String member_seq;
    private String car_num;
    private boolean save_id_flag;
    private boolean auto_login_flag;

    // 정보를 입력 받을 뷰
    private EditText txt_id;
    private EditText txt_pwd;
    private CheckBox save_id;
    private CheckBox auto_login;

    // 로그인타스크에서 결과를 받을 핸들러 객체
    private LoginHandler loginHandler;

    // 자동로그인을 위한 공유 객체
    private SharedPreferences sharedPreferences;

    // UI 초기화 함수
    public void initUI() {

        txt_id = findViewById(R.id.txt_id);
        txt_pwd = findViewById(R.id.txt_pwd);
        save_id = findViewById(R.id.save_id);
        auto_login = findViewById(R.id.auto_login);

        txt_id.setText(id);
        txt_pwd.setText(pwd);
        save_id.setChecked(save_id_flag);
        auto_login.setChecked(auto_login_flag);

        // ID 입력칸에 입력 될 때 마다 id 변수에 저장
        txt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                id = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // PWD 입력칸에 입력 될 때 마다 pwd 변수에 저장
        txt_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwd = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // ID저장의 선택해제 시 자동 로그인도 선택해제
        save_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    auto_login.setChecked(false);
                }
            }
        });

        // 자동로그인의 선택 시 ID저장도 선택
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    save_id.setChecked(true);
                }
            }
        });
    }

    // 자동로그인 확인 함수
    public boolean checkAutoLogin() {
        sharedPreferences = getSharedPreferences("tcs_auto_login", MODE_PRIVATE);
        id = sharedPreferences.getString("id",null);
        pwd = sharedPreferences.getString("pwd",null);
        car_num = sharedPreferences.getString("car_num", null);
        save_id_flag = sharedPreferences.getBoolean("save_id_flag", false);
        auto_login_flag = sharedPreferences.getBoolean("auto_login_flag", false);

        return auto_login_flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginHandler = new LoginHandler();
        carProgressDialog = new CarProgressDialog(LoginActivity.this);

        if(checkAutoLogin()) {
            // 로그인 타스크 생성 및 실행
            new LoginTask(loginHandler).execute(id,pwd);

            // 프로그래스다이얼로그 출력
            carProgressDialog.show();
        }
        initUI();
    }

    // 찾기버튼의 클릭리스너
    public void onClickFindButton(View v) {
        carProgressDialog.show();
    }

    // 레지스터버튼의 클릭리스너
    public void onClickRegisterButton(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    // 로그인버튼의 클릭리스너
    public void onClickLoginButton(View v) {

        // 로그인 타스크 생성 및 실행
        new LoginTask(loginHandler).execute(id,pwd);

        // 프로그래스다이얼로그 출력
        carProgressDialog.show();
    }

    // 로그인 결과에 대한 후속 처리
    public void checkedLogin(boolean result) {
        Log.e(tag,"Check to login -> " + result);
        if(result) {
            // 성공 -> id, pwd 를 메인엑티비티로 전달
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("pwd",pwd);
            intent.putExtra("member_seq", member_seq);
            setResult(RESULT_OK,intent);
            this.finish();
        } else {
            // 실패 토스트 출력
            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 로그인타스크에서 결과를 받을 핸들러
    public class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // 프로그래스다이얼로그 종료
            carProgressDialog.dismiss();
            Log.e(tag, "Login Result : "+msg.getData().getBoolean("result"));

            //member_seq 결과 저장
            member_seq = msg.getData().getString("member_seq");

            // 로그인 결과 반환
            checkedLogin(msg.getData().getBoolean("result"));
        }
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

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor se = sharedPreferences.edit();

        if(!save_id.isChecked()) {
            id = null;
        }
        if(!auto_login.isChecked()) {
            pwd = null;
        }
        se.putBoolean("save_id_flag", save_id.isChecked());
        se.putBoolean("auto_login_flag", auto_login.isChecked());
        se.putString("id", id);
        se.putString("pwd", pwd);
        se.commit();
        super.onDestroy();
    }
}
