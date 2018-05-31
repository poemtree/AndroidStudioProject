package com.example.student.tcsphone.service.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.student.tcsphone.R;
import com.example.student.tcsphone.fragmentinterface.FragmentContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MapFragment extends Fragment implements FragmentContract.View, OnMapReadyCallback {

    private static final String TAG = "---MapFragment---";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private FragmentContract.Presenter mPresenter;

    // 구글맵에 필요한 변수
    private GoogleMap gmap;
    private MapView map;
    private Marker marker;

    // 마커 이미지
    private Bitmap bitmap;

    // 서버 통신 플레그
    private boolean mapFlag;

    // 실시간 차량 위치 정보를 받아올 쓰래드
    private Thread locationSetting;

    // 차량 고유식별번호
    private String car_num;

    private CheckBox chck_Bx_target;

    public void setCar_num(String car_num) {
        Log.e(TAG, "setCar_num - > "+car_num);
        this.car_num = car_num;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        Bundle extra = getArguments();
        car_num = extra.getString("car_num");
        Log.e(TAG,"car_num : " + car_num);

        map = (MapView)root.findViewById(R.id.map);
        map.onCreate(mapViewBundle);
        map.getMapAsync(this);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.carmarker1);
        setBitmapSize(0.2f);

        chck_Bx_target = root.findViewById(R.id.chck_Bx_target);
        chck_Bx_target.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mapFlag = b;
                if(b) {
                    Log.e(TAG, "Target on");
                    startLocationTask(car_num);
                } else {
                    Log.e(TAG, "Target off");
                }
            }
        });
        chck_Bx_target.setChecked(true);
        return root;
    }

    @Override
    public void setPresenter(FragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
        mapFlag = chck_Bx_target.isChecked();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onPause() {
        mapFlag = false;
        map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public void startLocationTask(String car_num) {
        locationSetting = new Thread(new LocationSetting(car_num));
        locationSetting.start();
    }

    public void setBitmapSize(float scall) {
        //Marker Image resize
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.carmarker1), Math.round(bitmap.getWidth() * scall),Math.round(bitmap.getHeight() * scall),false );
    }

    class LocationTask extends AsyncTask<String, Void, List<Double>> {

        @Override
        protected List<Double> doInBackground(String... strings) {
            Log.e(TAG, "Run LocationTask");
            String address = "http://70.12.114.140/car/readCarloc.do";
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader br = null;
            List<Double> list = new ArrayList<Double>();

            try {
                url = new URL(address+"?"+strings[0]+"="+strings[1]);

                //connect
                con=(HttpURLConnection)url.openConnection();

                if(con != null) {
                    con.setConnectTimeout(2000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringTokenizer result = new StringTokenizer(br.readLine());

                    list.add(Double.parseDouble(result.nextToken("/")));
                    list.add(Double.parseDouble(result.nextToken("/")));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            Log.e(TAG, "Stop LocationTask");
            return list;
        }

        @Override
        protected void onPostExecute(List<Double> list) {
            if(marker != null)
                marker.remove();

            LatLng CAR = new LatLng(list.get(0), list.get(1));

            Log.e(TAG, "Lat : " + list.get(0) + "Lng : " + list.get(1));

            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(CAR);

            markerOptions.title("MyCarLocation");

            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.carmarker));

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

            marker = gmap.addMarker(markerOptions);

            gmap.moveCamera(CameraUpdateFactory.newLatLng(CAR));

        }

    }

    class LocationSetting implements Runnable {

        private String car_num;

        public LocationSetting(String car_num) {
            this.car_num = car_num;
        }

        @Override
        public void run() {
            Log.e(TAG, "while roof started");
            while(mapFlag) {
                try {
                    LocationTask locationTask = new LocationTask();
                    locationTask.execute("car_num", car_num);
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "while roof ended");
        }
    }


}
