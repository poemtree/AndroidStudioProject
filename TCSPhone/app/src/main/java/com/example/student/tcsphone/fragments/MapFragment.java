package com.example.student.tcsphone.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.student.tcsphone.CarProgressDialog;
import com.example.student.tcsphone.R;
import com.google.android.gms.maps.CameraUpdate;
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

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private static final String TAG = "---MapFragment---";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    // 구글맵에 필요한 객체
    private GoogleMap gmap;
    private MapView map;
    private Marker marker;

    // 장소 검색 객체
    private Geocoder geocoder;

    // 마커 이미지
    private Bitmap bitmap;

    // 서버 통신 플레그
    private boolean mapFlag;

    // 실시간 차량 위치 정보를 받아올 쓰래드
    private Thread locationSetting;

    // 차량 고유식별번호
    private String car_num;

    // 차량 추적 체크박스
    private CheckBox chck_Bx_target;

    // 검색에 필요한 뷰
    private EditText txt_search;
    private ImageButton btn_search;

    //공유된 정보를 받아올 공유객채
    SharedPreferences fragmentSharedPreferences;

    // 프로그래스 다이얼로그
    private CarProgressDialog carProgressDialog;

    public void setMapFlag(Boolean flag) {
        mapFlag = flag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        fragmentSharedPreferences = getContext().getSharedPreferences("tcs_fragment", Context.MODE_PRIVATE);

        car_num = fragmentSharedPreferences.getString("car_num", null);
        Log.e(TAG, "car_num : " + car_num);

        carProgressDialog = new CarProgressDialog(getContext());

        txt_search = root.findViewById(R.id.txt_search);
        btn_search = root.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_search.getText().toString().equals("") && txt_search.getText() != null) {
                    chck_Bx_target.setChecked(false);
                    new SearchPlaceTask().execute();
                } else {
                    Toast.makeText(getContext(), "장소를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        map = (MapView)root.findViewById(R.id.map);
        map.onCreate(mapViewBundle);
        map.getMapAsync(this);

        geocoder = new Geocoder(getContext());

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
        mapFlag = chck_Bx_target.isChecked();
        startLocationTask(car_num);

    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
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
        gmap.setMinZoomPreference(10);
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public void startLocationTask(String car_num) {
        locationSetting = new Thread(new LocationSetting(car_num));
        locationSetting.start();
    }

    public class SearchPlaceTask extends AsyncTask<Void, Void, List<Address>> {

        private final String TAG="SearchPlaceTask";

        @Override
        protected void onPreExecute() {
            carProgressDialog.show();
        }

        @Override
        protected List<Address> doInBackground(Void... voids) {

            String str = txt_search.getText().toString();
            List<Address> addressList = null;
            List<LatLng> locations = new ArrayList<>();

            try {
                Thread.sleep(1000);
                // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                addressList = geocoder.getFromLocationName(
                        str, // 주소
                        10); // 최대 검색 결과 개수
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return addressList;
        }

        @Override
        protected void onPostExecute(List<Address> addressList) {
            for(int i = 0; i < addressList.size(); i++) {
                Log.e(TAG, addressList.get(i).toString());

                double latitude = addressList.get(i).getLatitude();
                double longitude = addressList.get(i).getLongitude();
                Log.e(TAG, "Lat : " + latitude);
                Log.e(TAG, "Lon : " + longitude);

                MarkerOptions mOptions = new MarkerOptions();
                mOptions.title("search result");
                mOptions.position(new LatLng(latitude, longitude));
                // 마커 추가
                gmap.addMarker(mOptions);
                // 해당 좌표로 화면 줌
            }
            carProgressDialog.dismiss();

            try {
                Log.e(TAG,"Add marker");
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude()), 15));
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG,"Fail to add marker");
                Toast.makeText(getContext(), "장소를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setBitmapSize(float scall) {
        //Marker Image resize
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.carmarker1), Math.round(bitmap.getWidth() * scall),Math.round(bitmap.getHeight() * scall),false );
    }

    class LocationTask extends AsyncTask<String, Void, List<Double>> {

        @Override
        protected List<Double> doInBackground(String... strings) {
            Log.e(TAG, "Do LocationTask");
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
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            gmap.animateCamera(zoom);   // moveCamera 는 바로 변경하지만,
            // animateCamera() 는 근거리에선 부드럽게 변경합



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
