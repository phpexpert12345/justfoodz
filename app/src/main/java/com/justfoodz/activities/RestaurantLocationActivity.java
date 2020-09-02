package com.justfoodz.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.justfoodz.R;
import com.justfoodz.fragments.HomeFragment;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RestaurantLocationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private ImageView ivBack;
    private GoogleMap googleMaps;
    private MapFragment mapFragment;
    private Double lat = 0.0;
    private Double longs = 0.0;
    String latitude = "", longitude = "";
    private String restaurant_address = "";
    Double latLng = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_location);
       String address =  getIntent().getStringExtra("resturtantaddress");
        System.out.println("===== lat address: " + address);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            Address addresss = addresses.get(0);
            longs = addresss.getLongitude();
            lat = addresss.getLatitude();
        } catch (IOException e) {
            e.printStackTrace();
        }


        initialization();
        initializedListener();
        statusBarColor();


    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);

        try {
            if (googleMaps == null) {
                mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMaps = googleMap;
        Geocoder geocoder;
        List<Address> addresses = null;
        googleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // RestaurantModel restaurantModel = new RestaurantModel();
        try {
           /* lat = Double.parseDouble(HomeFragment.restaurantModel.getRestaurant_LatitudePoint());
            longs = Double.parseDouble(HomeFragment.restaurantModel.getRestaurant_LongitudePoint());*/

          /*  lat = Double.parseDouble(latitude);
            longs = Double.parseDouble(longitude);*/

        } catch (NullPointerException e) {
            e.printStackTrace();

        }

        System.out.println("===== lat is :" + lat + " " + "long :" + longs);
        LatLng latLong = new LatLng(lat, longs);
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, longs, 1);
//            String address = addresses.get(0).getAddressLine(0);
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
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
            googleMap.setMyLocationEnabled(true);
            googleMap.setTrafficEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.0f));
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMaps.addMarker(new MarkerOptions().position(latLong).title((""+getIntent().getStringExtra("resturtantaddress"))));

         //   googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 17));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




}