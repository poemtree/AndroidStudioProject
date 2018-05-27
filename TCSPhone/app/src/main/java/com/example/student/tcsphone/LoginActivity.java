package com.example.student.tcsphone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    // 로그에 표시할 태그명
    private final String tag = "---LoginActivity---";

    // 프로그래스 다이얼로그
    CarProgressDialog carProgressDialog;

    // 로그인에 필요한 정보
    private String id;
    private String pwd;

    // 정보를 입력 받을 에디트텍스트
    private EditText txt_id;
    private EditText txt_pwd;

    // 로그인타스크에서 결과를 받을 핸들러 객체
    private LoginHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_id = findViewById(R.id.txt_id);
        txt_pwd = findViewById(R.id.txt_pwd);

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

        loginHandler = new LoginHandler();
        carProgressDialog = new CarProgressDialog(LoginActivity.this);
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
        LoginTask loginTask = new LoginTask(loginHandler);
        Log.e(tag,"id : "+id);
        Log.e(tag,"pwd : "+pwd);
        loginTask.execute(id,pwd);

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

}
