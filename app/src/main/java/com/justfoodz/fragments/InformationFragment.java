package com.justfoodz.fragments;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.justfoodz.R;
import com.justfoodz.activities.RestaurantLocationActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment implements View.OnClickListener {
    private TextView tvrecusions,tvPhoneNumber, tvMile, tvMinimumDeliveryCharge, tvRestaurantContactEmail,
            tvMondayTime, tvTuesdayTime, tvWednesdayTime, tvThursdayTime,
            tvFridayTime, tvSaturdayTime, tvSundayTime, tvPaymentModeType, tvAboutDescription, tvShowMap;

    double longitude, latitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        try {
            initialization(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializedListener(view);
        return view;
    }

    private void initialization(View view) throws IOException {
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        tvRestaurantContactEmail = view.findViewById(R.id.tv_restaurant_contact_email);
        tvMile = view.findViewById(R.id.tv_mile);
        tvrecusions = view.findViewById(R.id.recusions);
        tvMinimumDeliveryCharge = view.findViewById(R.id.tv_minimum_delivery_charge);
        tvMondayTime = view.findViewById(R.id.tv_monday_time);
        tvTuesdayTime = view.findViewById(R.id.tv_tuesday_time);
        tvWednesdayTime = view.findViewById(R.id.tv_wednesday_time);
        tvThursdayTime = view.findViewById(R.id.tv_thursday_time);
        tvFridayTime = view.findViewById(R.id.tv_friday_time);
        tvSaturdayTime = view.findViewById(R.id.tv_saturday_time);
        tvSundayTime = view.findViewById(R.id.tv_sunday_time);
        tvPaymentModeType = view.findViewById(R.id.tv_payment_mode_type);
        tvAboutDescription = view.findViewById(R.id.tv_about_description);
        tvShowMap = view.findViewById(R.id.tv_show_map_txt);


        String contactEmail = getArguments().getString("contactEmail");
        String restaurantContactMobile = getArguments().getString("restaurantContactMobile");
        String contactPhone = getArguments().getString("contactPhone");
        String deliveryDistance = getArguments().getString("deliveryDistance");
        String restaurantDeliverycharge = getArguments().getString("restaurantDeliverycharge");
        String restaurantAbout = getArguments().getString("restaurantAbout");
        String monday = getArguments().getString("monday");
        String tuesday = getArguments().getString("tuesday");
        String wednesday = getArguments().getString("wednesday");
        String thursday = getArguments().getString("thursday");
        String friday = getArguments().getString("friday");
        String saturday = getArguments().getString("saturday");
        String sunday = getArguments().getString("sunday");
        String recusions = getArguments().getString("recusions");
        String ratingValue = getArguments().getString("ratingValue");
        String restaurantnotes = getArguments().getString("restaurantnotes");
        //for Cash
        String restaurantOnlyCashOn = getArguments().getString("restaurantOnlyCashOn");
        //for card
        String restaurantCardAccept = getArguments().getString("restaurantCardAccept");

        if ((restaurantOnlyCashOn.equals("0") && (restaurantCardAccept.equals("0")))) {
            tvPaymentModeType.setText("Card/Cash");
        } else if ((restaurantOnlyCashOn.equals("1") && (restaurantCardAccept.equals("0")))) {
            tvPaymentModeType.setText("Card Only");

        } else if ((restaurantOnlyCashOn.equals("0") && (restaurantCardAccept.equals("1")))) {
            tvPaymentModeType.setText("Cash Only");
        }

       /* tvPhoneNumber.setText(restaurantContactMobile);
        tvRestaurantContactEmail.setText(contactEmail);
        tvMile.setText(deliveryDistance);
        tvMinimumDeliveryCharge.setText(restaurantDeliverycharge);*/
        tvAboutDescription.setText(restaurantAbout);
        tvMondayTime.setText(monday);
        tvTuesdayTime.setText(tuesday);
        tvWednesdayTime.setText(wednesday);
        tvThursdayTime.setText(thursday);
        tvFridayTime.setText(friday);
        tvSaturdayTime.setText(saturday);
        tvSundayTime.setText(sunday);
        tvrecusions.setText(recusions);
        String addresss =  getArguments().getString("resturtantaddress");
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(addresss, 1);
        Address address = addresses.get(0);
         longitude = address.getLongitude();
         latitude = address.getLatitude();

        System.out.println("====== lat : " + latitude + " " + "long : " + longitude);
    }

    private void initializedListener(View view) {
        tvShowMap.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_map_txt:
                // startActivity(new Intent(getActivity(), RestaurantLocationActivity.class));
                String addresss =  getArguments().getString("resturtantaddress");
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(addresss, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addresses.get(0);
                longitude = address.getLongitude();
                latitude = address.getLatitude();

                System.out.println("====== lat 1: " + latitude + " " + "long 1: " + longitude);
                Intent i = new Intent(getActivity(), RestaurantLocationActivity.class);
                i.putExtra("resturtantaddress", getArguments().getString("resturtantaddress"));
                i.putExtra("longitudee", longitude);
                i.putExtra("latitudee", latitude);
                startActivity(i);
                break;
            default:
        }

    }
}
