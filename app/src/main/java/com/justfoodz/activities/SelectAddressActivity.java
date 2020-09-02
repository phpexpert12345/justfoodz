package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.justfoodz.R;
import com.justfoodz.adapters.AddressAdapter;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.interfaces.AddressEditItemClickListener;
import com.justfoodz.models.AddressModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.justfoodz.activities.SplashScreenActivity.customer_city;
import static com.justfoodz.activities.SplashScreenActivity.customer_country;

public class SelectAddressActivity extends AppCompatActivity implements View.OnClickListener, AddressEditItemClickListener, LocationListener {
    private ImageView ivBack;
    private TextView tvAddNewAddress, tvContinue, txtcurrentaddress;
    private RecyclerView rvAddressList;
    private AddressAdapter addressAdapter;
    private ProgressDialog pDialog, pd1;
    private AuthPreference authPreference;
    public ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
    public AddressModel addressModel;
    public int addressId;
    private String date, time, itemId, id, updateTotalPrice = "", subTot = "", disPrice = "", deliveryChargeValue = "",
            serviceFees = "", packageFees = "", salesTaxAmount = "", vatTax = "", restaurantName, restaurantAddress, orderType = "";
    // private ArrayList<SubMenuModel> subMenuModelArrayListReceive = null;
    // private ArrayList<MenuSizeModel> menuSizeModelArrayList;
    // private ArrayList<ExtrasModel> extrasModelArrayList;
    private EditText edtUserAddressTitle, edtFloorNumber, edtCustomerStreetName, edtCustomerCity,
            edtCustomerState, edtCustomerPostcode, edtCustomerPhoneNumber, edt_address_direction;
    public int updateIndexPos;
    private String userAddress = "", userPhone = "", zipCode = "", cityName = "", userLocality = "", customerState = "", companyStreet = "",
            gustUserLandLineAddress = "", gustUserFloor = "", company_streetNo = "", addressTitle = "", customer_address_lat = "", customer_address_long = "",
            address_direction;
    public static String address_id;
    public static double dist;
    public static double miles;

    public static RadioButton rbcurrent;
    double latitude, longitude;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    LocationManager locationManager;
    Location location;
    String lat = "", lng = "";
    double llt1, lln1, llt2, lln2;
    MyPref myPref;


    @Override
    protected void onResume() {
        super.onResume();
        fn_getlocation();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        address_id = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        initialization();
        initializedListener();
        statusBarColor();

        myPref = new MyPref(SelectAddressActivity.this);
        try {
            date = getIntent().getStringExtra("date");
            id = getIntent().getStringExtra("id");
            time = getIntent().getStringExtra("time");
            itemId = getIntent().getStringExtra("itemId");
            addressId = getIntent().getIntExtra("addressId", 1);
            updateTotalPrice = getIntent().getStringExtra("updateTotalPrice");
            subTot = getIntent().getStringExtra("subTot");
            disPrice = getIntent().getStringExtra("disPrice");
            deliveryChargeValue = getIntent().getStringExtra("deliveryChargeValue");
            serviceFees = getIntent().getStringExtra("serviceFees");
            packageFees = getIntent().getStringExtra("packageFees");
            salesTaxAmount = getIntent().getStringExtra("salesTaxAmount");
            vatTax = getIntent().getStringExtra("vatTax");
            restaurantName = getIntent().getStringExtra("restaurantName");
            restaurantAddress = getIntent().getStringExtra("restaurantAddress");
            orderType = getIntent().getStringExtra("orderType");
//            extrasModelArrayList = (ArrayList<ExtrasModel>) getIntent().getSerializableExtra("extrasModelArrayList");
//            menuSizeModelArrayList = (ArrayList<MenuSizeModel>) getIntent().getSerializableExtra("menuSizeModelArrayList");
//            subMenuModelArrayListReceive = (ArrayList<SubMenuModel>) getIntent().getSerializableExtra("subMenuModelArrayLists");
            // itemId = getIntent().getStringExtra("itemId");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        tvAddNewAddress = findViewById(R.id.tv_add_new_address);
        tvContinue = findViewById(R.id.tv_continue);
        rvAddressList = findViewById(R.id.rv_address_list);
        txtcurrentaddress = findViewById(R.id.txtcurrentaddress);
        rbcurrent = findViewById(R.id.rbcurrent);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAddressList.setLayoutManager(mLayoutManager);
        rvAddressList.setItemAnimator(new DefaultItemAnimator());
        authPreference = new AuthPreference(this);
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvAddNewAddress.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
        rbcurrent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_new_address:
                Intent ii = new Intent(this, CompleteProfileActivity.class);
                ii.putExtra("id", id);
                ii.putExtra("restaurantName", restaurantName);
                ii.putExtra("restaurantAddress", restaurantAddress);
                ii.putExtra("date", date);
                ii.putExtra("time", time);
                String menuItemId = authPreference.getItemId();
                ii.putExtra("itemId", menuItemId);
                ii.putExtra("updateTotalPrice", updateTotalPrice);
                ii.putExtra("subTot", subTot);
                ii.putExtra("disPrice", disPrice);
                ii.putExtra("deliveryChargeValue", deliveryChargeValue);
                ii.putExtra("serviceFees", serviceFees);
                ii.putExtra("packageFees", packageFees);
                ii.putExtra("salesTaxAmount", salesTaxAmount);
                ii.putExtra("vatTax", vatTax);
                ii.putExtra("orderType", orderType);
                //  intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                ii.putExtra("addressId", addressId);
//               intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//              intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                startActivity(ii);

                break;
            case R.id.tv_continue:
                double mm = Double.parseDouble(HomeFragment.restaurant_distance_check);
                if (address_id.equals("")) {
                    showCustomDialog1decline("Please select address", "2");
                } else if (miles > mm) {
                    showCustomDialog1decline("We are sorry our Riders only cover up to " + mm + " mile Radius from the outlet, therefore we unable delivery to your area.", "2");
                } else {
                    try {
                        Intent intent = new Intent(this, PaymentActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("restaurantName", restaurantName);
                        intent.putExtra("restaurantAddress", restaurantAddress);
                        intent.putExtra("date", date);
                        intent.putExtra("time", time);
                        String menuItemId1 = authPreference.getItemId();
                        intent.putExtra("itemId", menuItemId1);
                        intent.putExtra("updateTotalPrice", updateTotalPrice);
                        intent.putExtra("subTot", subTot);
                        intent.putExtra("disPrice", disPrice);
                        intent.putExtra("deliveryChargeValue", deliveryChargeValue);
                        intent.putExtra("serviceFees", serviceFees);
                        intent.putExtra("packageFees", packageFees);
                        intent.putExtra("salesTaxAmount", salesTaxAmount);
                        intent.putExtra("vatTax", vatTax);
                        intent.putExtra("orderType", orderType);
                        //  intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                        intent.putExtra("addressId", addressId);
//                        intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                        intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                        startActivity(intent);
                        finish();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.rbcurrent:
                userAddressList();
                address_id = "0";
//                addressAdapter.refreshEvents(addressModelArrayList);
                llt1 = Double.parseDouble(RestaurantsListActivity.res_lat);
                lln1 = Double.parseDouble(RestaurantsListActivity.res_lng);
                llt2 = Double.parseDouble(lat);
                lln2 = Double.parseDouble(lng);
                distance(llt1, lln1, llt2, lln2);
                SelectAddressActivity.miles = 0.621371 * SelectAddressActivity.dist;
                SelectAddressActivity.miles = Double.parseDouble(String.format("%.2f", SelectAddressActivity.miles));
                break;
            default:
        }
    }


    public void userAddressList() {
        addressModelArrayList.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlConstants.USER_ADDRESS_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("response", "" + response);
                    String successMsg = "";
                    String success = obj.optString("success");
                    if (success.equals("0")) {
                        JSONObject user = obj.getJSONObject("user");
                        JSONArray jsonArray = user.getJSONArray("deliveryaddress");
                        JSONArray current_location = user.getJSONArray("current_location");
                        for (int ii = 0; ii < current_location.length(); ii++) {
                            JSONObject jjj = current_location.getJSONObject(ii);
                            customer_city = jjj.getString("customer_city");
                            myPref.setCity(jjj.getString("customer_city"));
                            SplashScreenActivity.customer_locality = jjj.getString("customer_locality");
                            customer_country = jjj.getString("customer_country");
                            myPref.setState(jjj.getString("customer_country"));
                            //     SplashScreenActivity.customer_country_code = jjj.getString("customer_country_code");
                            SplashScreenActivity.customer_postcode = jjj.getString("customer_postcode");
                            SplashScreenActivity.customer_full_address = jjj.getString("customer_full_address");
                            myPref.setFirebaseTokenId( jjj.getString("customer_postcode"));
                            myPref.setLatitude(jjj.getString("customer_lat"));
                            SplashScreenActivity.customer_lat = jjj.getString("customer_lat");
                            txtcurrentaddress.setText("" + myPref.getPickupAdd());
                            myPref.setReferId(SplashScreenActivity.customer_search_display);
                            myPref.setEmergency(SplashScreenActivity.customer_search);
                            myPref.setCity(customer_city);
                            myPref.setDropAdd(SplashScreenActivity.customer_locality);
                            myPref.setState(customer_country);
                            myPref.setUserName(SplashScreenActivity.customer_country_code);
                            myPref.setFirebaseTokenId(SplashScreenActivity.customer_postcode);
                            myPref.setPickupAdd(SplashScreenActivity.customer_full_address);
                            myPref.setLatitude(SplashScreenActivity.customer_lat);
                        }
                        if (jsonArray.length() == 0) {
//                            Intent intent = new Intent(SelectAddressActivity.this, CompleteProfileActivity.class);
//                            intent.putExtra("addressId", addressId);
//                            intent.putExtra("userEmail", authPreference.getUserEmail());
//                            intent.putExtra("userPhone", authPreference.getUserPhone());
//                            startActivity(intent);

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                addressId = jsonObject.optInt("id");
                                authPreference.setCustomerAddressId(addressId + "");
                                userAddress = jsonObject.optString("address");  //user address
                                userPhone = jsonObject.optString("user_phone");
                                zipCode = jsonObject.optString("zipcode");
                                cityName = jsonObject.optString("city_name");
                                userLocality = jsonObject.optString("user_locality");
                                customerState = jsonObject.optString("customerState");
                                String customerCountry = jsonObject.optString("customerCountry");
                                companyStreet = jsonObject.optString("company_street");
                                gustUserLandLineAddress = jsonObject.optString("GustUserLandlineAdress");
                                gustUserFloor = jsonObject.optString("GustUserFloor");
                                company_streetNo = jsonObject.optString("company_streetNo");
                                addressTitle = jsonObject.optString("address_title");
                                address_direction = jsonObject.optString("address_direction");
                                customer_address_lat = jsonObject.optString("customer_address_lat");
                                customer_address_long = jsonObject.optString("customer_address_long");
                                addressModel = new AddressModel();
                                addressModel.setId(addressId);
                                addressModel.setAddress(userAddress);
                                addressModel.setAddress_title(addressTitle);
                                addressModel.setUser_phone(userPhone);
                                addressModel.setZipcode(zipCode);
                                addressModel.setCity_name(cityName);
                                addressModel.setUser_locality(userLocality);
                                addressModel.setCustomerState(customerState);
                                addressModel.setCompany_street(companyStreet);
                                addressModel.setGustUserLandlineAdress(gustUserLandLineAddress);
                                addressModel.setGustUserFloor(gustUserFloor);
                                addressModel.setCompany_streetNo(company_streetNo);
                                addressModel.setcustomer_address_lat(customer_address_lat);
                                addressModel.setcustomer_address_long(customer_address_long);
                                addressModel.setaddress_direction(address_direction);
                                addressModelArrayList.add(addressModel);
                            }
                            addressAdapter = new AddressAdapter(SelectAddressActivity.this, addressModelArrayList, SelectAddressActivity.this);
                            rvAddressList.setAdapter(addressAdapter);
                        }
                    } else {
                        successMsg = obj.optString("success_msg");
                        showCustomDialog1decline(successMsg, "2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "" + error);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String customerId = authPreference.getCustomerId();
                params.put("CustomerId", customerId);
                params.put("customer_lat", lat);
                params.put("customer_long", lng);
                Log.e("params", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }


    @Override
    public void singleAddressItemClick(boolean isTrue, int position) {
        try {
            if (addressModelArrayList.size() > 0) {
                addressModelArrayList.get(position).setTrue(isTrue);
            } else {
                addressModelArrayList.get(position).setTrue(isTrue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addressEditItemClick(final int position) {
        String userPhone = addressModelArrayList.get(position).getUser_phone();
        String addressTitle = addressModelArrayList.get(position).getAddress_title();
        String userFloor = addressModelArrayList.get(position).getGustUserFloor();
        String userStreetName = addressModelArrayList.get(position).getCompany_street();
        String city = addressModelArrayList.get(position).getCity_name();
        String state = addressModelArrayList.get(position).getCustomerState();
        String userZipCode = addressModelArrayList.get(position).getZipcode();
        String address_direction = addressModelArrayList.get(position).getaddress_direction();
        String customerCountry = addressModelArrayList.get(position).getCustomerCountry();

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_address_popup);

        ivBack = findViewById(R.id.iv_back);
//        edtEmail = (MaterialEditText) dialog.findViewById(R.id.edt_email);
        edtUserAddressTitle = (MaterialEditText) dialog.findViewById(R.id.edt_address_name);
        edt_address_direction = (MaterialEditText) dialog.findViewById(R.id.edt_address_direction);
        edtFloorNumber = (MaterialEditText) dialog.findViewById(R.id.edt_floor_number);
        edtCustomerStreetName = (MaterialEditText) dialog.findViewById(R.id.edt_street_name);
        edtCustomerCity = (MaterialEditText) dialog.findViewById(R.id.edt_town_city);
        edtCustomerState = (MaterialEditText) dialog.findViewById(R.id.edt_country_region);
        edtCustomerPostcode = (MaterialEditText) dialog.findViewById(R.id.edt_postcode);
//        edtCustomerCountry = (MaterialEditText) dialog.findViewById(R.id.edt_country);
        edtCustomerPhoneNumber = (MaterialEditText) dialog.findViewById(R.id.edt_phone_number);
        Button tvSubmit = (Button) dialog.findViewById(R.id.tv_submit_txt);
//        edtEmail.setText(authPreference.getUserEmail());
//        edtEmail.setEnabled(false);
        edtUserAddressTitle.setText(addressTitle);
        edtFloorNumber.setText(userFloor);
        edtCustomerStreetName.setText(userStreetName);
        edtCustomerCity.setText(city);
        edtCustomerState.setText(state);
        edtCustomerPostcode.setText(userZipCode);
        edt_address_direction.setText(address_direction);
//        edtCustomerCountry.setText(customerCountry);
        edtCustomerPhoneNumber.setText(userPhone);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIndexPos = position;
                addressModel = new AddressModel();
                addressModel = addressModelArrayList.get(updateIndexPos);
                addressModel.setAddress_title(edtUserAddressTitle.getText().toString() + "");
                addressModel.setGustUserFloor(edtFloorNumber.getText().toString() + "");
                addressModel.setCompany_street(edtCustomerStreetName.getText().toString() + "");
                addressModel.setCity_name(edtCustomerCity.getText().toString() + "");
                addressModel.setCustomerState(edtCustomerState.getText().toString() + "");
                addressModel.setZipcode(edtCustomerPostcode.getText().toString() + "");
//                addressModel.setCustomerCountry(edtCustomerCountry.getText().toString() + "");
                addressModel.setUser_phone(edtCustomerPhoneNumber.getText().toString() + "");
                addressModel.setaddress_direction(edt_address_direction.getText().toString() + "");
                addressModelArrayList.set(updateIndexPos, addressModel);

                if (com.justfoodz.utils.Network.isNetworkCheck(SelectAddressActivity.this)) {
                    editUserAddress(position);

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void editUserAddress(final int position) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.UPDATE_USER_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("qw", "" + response);
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.optInt("success");
                    if (success == 1) {
                        userAddressList();
                        String successMsg = obj.optString("success_msg");
                        address_id = "";
                        showCustomDialog1decline(successMsg, "2");
                        addressAdapter.notifyDataSetChanged();
                    } else if (success == 0) {
                        String errorMsg = obj.optString("error_msg");
                        showCustomDialog1decline(errorMsg, "2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "" + error);
                Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_phone", addressModelArrayList.get(position).getUser_phone());
                params.put("customerAddressLabel", addressModelArrayList.get(position).getAddress_title());
                params.put("customerAppFloor", addressModelArrayList.get(position).getGustUserFloor());
                params.put("customerStreet", addressModelArrayList.get(position).getCompany_street());
                params.put("customerCity", addressModelArrayList.get(position).getCity_name());
                params.put("customerState", addressModelArrayList.get(position).getCustomerState());
                params.put("customerZipcode", addressModelArrayList.get(position).getZipcode());
                params.put("customerCountry", addressModelArrayList.get(position).getCustomerState());
                int customerAddressId = addressModelArrayList.get(position).getId();
                params.put("CustomerAddressId", customerAddressId + "");
                params.put("address_direction", addressModelArrayList.get(position).getaddress_direction() + "");

                params.put("customer_country", myPref.getState());
                params.put("customer_city", myPref.getCity());
                params.put("customer_lat", myPref.getLatitude());
                params.put("customer_long", myPref.getLongitude());
                Log.e("paramsparams", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }


    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showCustomDialog1decline(String s, final String todo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(SelectAddressActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1")) {
                    // alertDialog.dismiss();
                } else {
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog.show();
    }

    private void fn_getlocation() {
        pd1 = new ProgressDialog(this);
        pd1.setCancelable(false);
        pd1.setMessage("Please Wait...");
        pd1.setCancelable(true);
        pd1.show();
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (LocationListener) this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        lat = String.valueOf(latitude);
                        lng = String.valueOf(longitude);
//                         Toast.makeText(this, "networl"+lat+lng, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (isGPSEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        lat = (String.format("%.6f", latitude));
                        lng = (String.format("%.6f", longitude));
//                          Toast.makeText(this, "gps"+lat+lng, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        if (lat.equals("") || lat.equals(null)) {
            pd1.dismiss();
            fn_getlocation();
        } else {
            pd1.dismiss();
            if (com.justfoodz.utils.Network.isNetworkCheck(SelectAddressActivity.this)) {
                userAddressList();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(SelectAddressActivity.dist);
        dist = rad2deg(SelectAddressActivity.dist);
        dist = SelectAddressActivity.dist * 60 * 1.1515;
        return (SelectAddressActivity.dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onLocationChanged(Location location) {

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
