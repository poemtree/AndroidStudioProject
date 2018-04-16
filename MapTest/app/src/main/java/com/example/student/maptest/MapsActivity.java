package com.example.student.maptest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LinearLayout lnr_btn;
    SupportMapFragment mapFragment;
    private WebView wbv;
    private ImageView imgv;
    private int[] imgs = {R.drawable.agumon, R.drawable.gabumon, R.drawable.gomamon, R.drawable.guilmon, R.drawable. parumon, R.drawable.patamon, R.drawable.piyomon, R.drawable.tailmon, R.drawable.tentomon, R.drawable.terriermon};
    private int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lnr_btn = findViewById(R.id.lnr_btn);
        wbv = findViewById(R.id.wbv);
        imgv = findViewById(R.id.imgv);

        imgv.setVisibility(View.INVISIBLE);
        wbv.setVisibility(View.INVISIBLE);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Toast.makeText(this, "onMapReady", Toast.LENGTH_SHORT).show();
        requestMyLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void onClickBtn(View v) {
        switch (v.getId()) {
            case R.id.btn_map:
                mapFragment.getView().setVisibility(View.VISIBLE);
                lnr_btn.setVisibility(View.VISIBLE);
                imgv.setVisibility(View.INVISIBLE);
                wbv.setVisibility(View.INVISIBLE);
                requestMyLocation();
                break;
            case R.id.btn_img :
                imgv.setImageResource(imgs[++img%imgs.length]);
                mapFragment.getView().setVisibility(View.INVISIBLE);
                lnr_btn.setVisibility(View.INVISIBLE);
                imgv.setVisibility(View.VISIBLE);
                wbv.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_chart :
                mapFragment.getView().setVisibility(View.INVISIBLE);
                lnr_btn.setVisibility(View.INVISIBLE);
                imgv.setVisibility(View.INVISIBLE);
                wbv.setVisibility(View.VISIBLE);

                wbv.loadUrl("http://70.12.114.134/mv/main.do");
                break;
        }

    }

    public void onClickMapBtn(View v) {
        LatLng m1 = null;
        ArrayList<LatLng> markers = makeMarkers();
        switch (v.getId()) {
            case R.id.btn_a :
                mMap.clear();
                /*m1 = new LatLng(37.5012319,127.0396087);
                mMap.addMarker(new MarkerOptions().position(m1).title("Wild Digimon"));*/
                for(int i = 0; i < markers.size(); i++) {
                    mMap.addMarker(new MarkerOptions().position(markers.get(i)).title("Wild Digimon" + String.valueOf(i)));
                }
                break;
            case R.id.btn_b :
                mMap.clear();
                /*m1 = new LatLng(37.500555,127.034908);
                mMap.addMarker(new MarkerOptions().position(m1).title("Digimon Tamer").icon(BitmapDescriptorFactory.fromResource(R.mipmap.digivice_tri)));*/
                for(LatLng m : markers)
                    mMap.addMarker(new MarkerOptions().position(m).title("Digimon Tamer").icon(BitmapDescriptorFactory.fromResource(R.mipmap.digivice_tri)));
                break;
            case R.id.btn_c :
                mMap.clear();
                /*m1 = new LatLng(37.501155,127.031258);
                mMap.addMarker(new MarkerOptions().position(m1).title("Srore"));*/
                for(LatLng m : markers)
                    mMap.addMarker(new MarkerOptions().position(m).title("Srore"));
                break;
        }

    }


    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    public ArrayList<LatLng> makeMarkers(){
        ArrayList<LatLng> markers = new ArrayList<>();
        for(int i = 0 ; i <10; i++) {
            markers.add(new LatLng( 37.501306+ i/1000,127.0396478+i/1000));
        }
        return markers;
    }

    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }
}
