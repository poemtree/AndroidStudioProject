package com.example.student.tcsphone.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.tcsphone.AppCompatActivityFrame;
import com.example.student.tcsphone.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteActivity extends AppCompatActivityFrame {

    // 로그에 표시할 태그
    private final String TAG="---RemoteActivity---";

    // Max Temp
    private final int MAX_TEMP = 35;

    // Min Temp
    private final int MIN_TEMP = 18;

    // 사용자 정보
    private String car_num;
    private String[] info;

    // 뷰 객체들
    private TextView txt_temp;
    private SeekBar sb_ac;
    private SeekBar sb_heat;

    // 차 상태 정보
    private String  engine;
    private String elight;
    private String door;

    // Task 번호
    private final int TEMP_TASK = 0;
    private final int ENGINE_TASK = 1;
    private final int ELIGHT_TASK = 2;
    private final int DOOR_TASK = 3;

    //car_num/start_onoff/Door_onoff/Air_lv/Heat_lv/Elight_onoff/Temp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        car_num = getIntent().getExtras().getString("car_num");

        makeCarProgressDialog(RemoteActivity.this);

        initUI();

        new ReloadTask().execute(car_num);

    }

    public void initUI() {
        txt_temp = findViewById(R.id.txt_temp);
        sb_ac = findViewById(R.id.sb_ac);
        sb_heat = findViewById(R.id.sb_heat);

        sb_ac.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i>0) {
                    sb_heat.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_heat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i>0) {
                    sb_ac.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onClockBackButton(View v) {
        Intent intent = new Intent();
        intent.putExtra("Relogin",false);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void onClickReloadButton(View v) {
        new ReloadTask().execute(car_num);
    }

    public void onClockRemoteButton(View v) {
        int task_num = 0;
        switch (v.getId()) {
            case R.id.btn_engine_on :
                engine = "1";
                task_num = 1;
                break;
            case R.id.btn_engine_off :
                engine = "0";
                task_num = 1;
                break;
            case R.id.btn_elight_on :
                elight = "1";
                task_num = 2;
                break;
            case R.id.btn_elight_off :
                elight = "0";
                task_num = 2;
                break;
            case R.id.btn_door_on :
                door = "0";
                task_num = 3;
                break;
            case R.id.btn_door_off :
                door = "1";
                task_num = 3;
                break;
        }
        new ReloadTask(false,  task_num). execute(car_num);
    }

    public void onClickTempButton(View v) {
        int temp = Integer.parseInt(txt_temp.getText().toString());
        switch (v.getId()) {
            case R.id.btn_temp_up :
                if(temp<MAX_TEMP) {
                    temp++;
                } else {
                    Toast.makeText(this, MAX_TEMP +"이상으로 설정 불가합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_temp_down :
                if(temp>MIN_TEMP) {
                    temp--;
                } else {
                    Toast.makeText(this, MIN_TEMP +"이하로 설정 불가합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        txt_temp.setText(String.valueOf(temp));
    }

    public void onClickSetupButton(View v) {
       new ReloadTask(false, 0).execute(car_num);
    }

    public class ReloadTask extends AsyncTask<String, Void, Void> {

        private final String TAG = "ReloadTask";
        private boolean displayFlag;
        private int tasknum;

        public ReloadTask() {
            displayFlag = true;
        }

        public ReloadTask(boolean displayFlag, int tasknum) {
            this.displayFlag = displayFlag;
            this.tasknum = tasknum;
        }

        @Override
        protected void onPreExecute() {
            showCarProgressDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.e(TAG, "Do Task");

            String address = "http://70.12.114.140/car/readCarCtrl.do?car_num=" + strings[0];
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader br = null;

            try {
                url = new URL(address);

                //connect
                con=(HttpURLConnection)url.openConnection();

                if(con != null) {
                    con.setConnectTimeout(1000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    info = br.readLine().toString().split("/");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            Log.e(TAG, "Stop Task");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(displayFlag) {
                txt_temp.setText(info[6]);
                sb_ac.setProgress(Integer.parseInt(info[3]));
                sb_heat.setProgress(Integer.parseInt(info[4]));
                dismissCarProgressDialog();
            } else {
               doSetupTask(tasknum);
            }
        }
    }

    public void doSetupTask(int taskNum) {
        switch (taskNum) {
         case TEMP_TASK :    // Air
            info[3] = String.valueOf(sb_ac.getProgress());
            //Heat
            info[4] = String.valueOf(sb_heat.getProgress());
            // Temp
            info[6] = txt_temp.getText().toString();
            break;
        case ENGINE_TASK :
            info[1] = engine;
            break;
        case ELIGHT_TASK :
            info[5] = elight;
            break;
        case DOOR_TASK :
            info[2] = door;
            break;

        }
        new SetupTask().execute(info);
    }

    public class SetupTask extends AsyncTask<String, Void, Void> {

        private final String TAG = "SetupTask";

        @Override
        protected Void doInBackground(String... strings) {
            String address = "http://70.12.114.140/car/updateCarCtrl.do?car_num=" + strings[0] + "&start_onoff="+ strings[1]+"&Door_onoff="+ strings[2]+"&Air_lv="+ strings[3]+"&Heat_lv="+ strings[4]+"&Elight_onoff="+ strings[5]+"&Temp="+ strings[6];
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader br = null;

            try {
                url = new URL(address);

                //connect
                con=(HttpURLConnection)url.openConnection();

                if(con != null) {
                    con.setConnectTimeout(1000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            Log.e(TAG, "Stop Task");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dismissCarProgressDialog();
        }
    }

}
