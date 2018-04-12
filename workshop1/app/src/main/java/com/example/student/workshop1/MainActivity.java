package com.example.student.workshop1;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Intent service1;
    Intent service2;
    Intent service3;
    MainFragment mainFragment;
    FragmentManager fragmentManager;
    int[] images = {R.drawable.agumon, R.drawable.gabumon, R.drawable.gomamon, R.drawable.guilmon, R.drawable.parumon, R.drawable.patamon, R.drawable.piyomon, R.drawable.tailmon, R.drawable.tentomon, R.drawable.terriermon};
    int imgNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        service1 = new Intent(this, Service1.class);
        service2 = new Intent(this, Service2.class);
        service3 = new Intent(this, Service3.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
    }

    public void processIntent(Intent intent) {
        if(intent != null) {
            imgNum = intent.getIntExtra("imgNum",0);
        }
        drawFragment();
    }
    public void drawFragment(){
        mainFragment.setImgId(images[imgNum]);
        fragmentManager.beginTransaction().replace(R.id.main_cntn, mainFragment).commit();
    }
    public void onClickBtnStart(View v) {
        switch (v.getId()) {
            case R.id.main_btn_start :
                imgNum = 3;
                startService(service1);
                break;
            case R.id.main_frg_btn_start :
                startService(service2);
                service3.putExtra("imgsSize", images.length);
                service3.putExtra("imgNum", imgNum);
                startService(service3);
                break;
        }
        drawFragment();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(service1 != null) {
            stopService(service1);
        }
        if(service2 != null) {
            stopService(service3);
        }
        if(service3 != null) {
            stopService(service3);
        }

    }

    public static class MainFragment extends Fragment {
        private ImageView imgv;
        private int imgId;

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.main_fragment, container, false);
            imgv = v.findViewById(R.id.main_frg_imgv);
            imgv.setImageResource(imgId);
            return v;
        }

    }

}
