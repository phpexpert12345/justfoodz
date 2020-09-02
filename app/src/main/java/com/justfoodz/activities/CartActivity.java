package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.R;
import com.justfoodz.adapters.CartAdapter;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.Database.Database;


import com.justfoodz.models.Citymodel;
import com.justfoodz.models.Deal_RaviCartModle;
import com.justfoodz.models.RaviCartModle;

import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.MyProgressDialog;
import com.justfoodz.utils.UrlConstants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;


public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private RelativeLayout rlCheckout, dineinrelative;
    private TextView tvRestaurantName, tvRestaurantAddress, tvCheckout, table;
    private String restaurantName, restaurantAddress;
    CheckBox groupcheck;

    public static TextView tvGrandTotalPrice, tvSaveDiscountAmountMsg;
    private RecyclerView rvAddItem, deal_rv_add_item;
    private CartAdapter cartAdapter;
    private String id;
    private String restaurantCategoryId;
    private RelativeLayout rlFoodCost, rlTotalDiscount, rlSubTotal, rlServiceFees, rlPackageFees, rlSalesTaxAmount, rlVatTax, rlDeliveryCharge;
    public TextView tvTotalFoodCost, tvTotalDiscount, tvSubtotal, tvServiceFees, tvPackageFees, tvSalesTaxAmount, tvVatTaxAmount, tvDeliveryCharge;
    private RadioButton rdBtnCollection, rdBtnPreOrderDeliveryTime, rdBtnPreOrderCollectionTime, rdBtnDelivery;
    public Double totalPrice = 0.0, disPrice = 0.0, subTot = 0.0, serviceFees = 0.0, seviceFeesValue = 0.0, packageFees = 0.0, packageFeesValue = 0.0, salesTaxAmount = 0.0, vatTax = 0.0, deliveryChargeValue = 0.0, minOrder = 0.0, grandTotal = 0.0;
    public static String totPrice;
    private TextView edtPreOrderDelivery, edtPreOrderCollectionTime;
    String pound = "";
    public String date, time, itemId, deepsubmenuitemid;
    public String orderType = "", hour, mintue;
    private AuthPreference authPreference;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView btnPreOrderCollectionTime, btnPreOrderDeliveryTime;
    private LinearLayout rl_pre_order_delivery_time, rl_pre_order_collection_time;
    private RadioButton rd_btn_collection, rd_btn_delivery, dinein;
    Database database;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    ArrayList<RaviCartModle> raviCartModles;
    ArrayList<Deal_RaviCartModle> deal_raviCartModles;
    RecyclerView tablerecycler;
    private ArrayList<Citymodel> tablenoList;
    private TableNumberAdapter tableNumberAdapter;
    public static String tableid, tablenumber;
    Citymodel citymodel;
    String Restaurantid;

    double min_price;
    int flag = 0;
    String CITY;
    Dialog tabledialog;
    MyPref myPref;

    public static String preorderTime = "";
    String preorderTime1 = "", preorderTime2 = "";

    public static String delivery_to_show = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        myPref = new MyPref(CartActivity.this);
        statusBarColor();
        initialization();
        initializedListener();
        pound = myPref.getBookid();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        date = sdf.format(c.getTime());
        time = sdf1.format(c.getTime());

        if (!authPreference.getMinprice().isEmpty()) {
            min_price = Double.parseDouble(authPreference.getMinprice());
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
        database.deal_delete();
        updateCart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        updateCart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateCart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCart();
    }



    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
        PaymentActivity.groupvisible = "false";

    }

    private void initialization() {
        database = new Database(CartActivity.this);
        raviCartModles = new ArrayList<>();
        deal_raviCartModles = new ArrayList<>();
        ivBack = findViewById(R.id.iv_back);
        //parent view for amount
        rlFoodCost = findViewById(R.id.rl_food_cost);
        rlTotalDiscount = findViewById(R.id.rl_total_discount);
        rlSubTotal = findViewById(R.id.rl_sub_total);
        rlServiceFees = findViewById(R.id.rl_service_fees);
        rlPackageFees = findViewById(R.id.rl_package_fees);
        rlSalesTaxAmount = findViewById(R.id.rl_sales_tax_amount);
        rlVatTax = findViewById(R.id.rl_vat_tax);
        table = findViewById(R.id.table);
        dineinrelative = findViewById(R.id.dineinrelative);


        edtPreOrderDelivery = findViewById(R.id.edt_pre_order_delivery_time);
        edtPreOrderCollectionTime = findViewById(R.id.edt_pre_order_collection_time);
        btnPreOrderDeliveryTime = findViewById(R.id.btn_pre_order_delivery_time);
        btnPreOrderCollectionTime = findViewById(R.id.btn_pre_order_collection_time);
        rlDeliveryCharge = findViewById(R.id.rl_delivery_charge);
        rd_btn_collection = findViewById(R.id.rd_btn_collection);
        rd_btn_delivery = findViewById(R.id.rd_btn_delivery);
        dinein = findViewById(R.id.dinein);
        rl_pre_order_delivery_time = findViewById(R.id.rl_pre_order_delivery_time);
        rl_pre_order_collection_time = findViewById(R.id.rl_pre_order_collection_time);
        authPreference = new AuthPreference(this);
        CITY = authPreference.getRestraurantCity();
        Restaurantid = authPreference.getRestrarantid();
        Log.e("resss", Restaurantid);
        try {
            restaurantName = getIntent().getStringExtra("restaurantName");
            restaurantAddress = getIntent().getStringExtra("restaurantAddress");
            restaurantCategoryId = getIntent().getStringExtra("restaurantCategoryId");
            id = getIntent().getStringExtra("id");
            //set the Restaurant name and Address
            tvRestaurantName = findViewById(R.id.tv_restaurant_name);
            tvRestaurantAddress = findViewById(R.id.tv_restaurant_address);
            tvRestaurantName.setText(restaurantName);
            tvRestaurantAddress.setText(restaurantAddress);
            String pickupStatus = MainMenuActivity.restaurantModel.getPickupStatus();
            String homeDeliveryStatus = MainMenuActivity.restaurantModel.getHomeDeliveryStatus();
            String preOrderPickupStatus = MainMenuActivity.restaurantModel.getPre_Order_PickupStatus();
            String preHomeDeliveryStatus = MainMenuActivity.restaurantModel.getPre_homeDeliveryStatus();
            String DineInavailable = MainMenuActivity.restaurantModel.getDINEin();
            if (pickupStatus.equalsIgnoreCase("0")) {
                rd_btn_collection.setVisibility(View.VISIBLE);
            }
            if (homeDeliveryStatus.equalsIgnoreCase("0")) {
                rd_btn_delivery.setVisibility(View.VISIBLE);
            }
            if (preOrderPickupStatus.equalsIgnoreCase("0")) {
                rl_pre_order_collection_time.setVisibility(View.VISIBLE);
            }
            if (preHomeDeliveryStatus.equalsIgnoreCase("0")) {
                rl_pre_order_delivery_time.setVisibility(View.VISIBLE);
            }
            if (DineInavailable.equalsIgnoreCase("Yes")) {
                dinein.setVisibility(View.VISIBLE);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //TextView for different prices
        tvTotalFoodCost = findViewById(R.id.tv_total_food_cost);
        tvTotalDiscount = findViewById(R.id.tv_total_discount);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvServiceFees = findViewById(R.id.tv_service_fees);
        tvPackageFees = findViewById(R.id.tv_package_fees);
        tvSalesTaxAmount = findViewById(R.id.tv_sales_tax_amount);
        tvVatTaxAmount = findViewById(R.id.tv_vat_tax_amount);
        tvDeliveryCharge = findViewById(R.id.tv_delivery_charge);
        tvGrandTotalPrice = findViewById(R.id.tv_grand_total_price);
        tvSaveDiscountAmountMsg = findViewById(R.id.tv_save_discount_amount_msg);

        //Radio Buttons
        rdBtnCollection = findViewById(R.id.rd_btn_collection);
        rdBtnPreOrderDeliveryTime = findViewById(R.id.rd_btn_pre_order_delivery_time);
        rdBtnPreOrderCollectionTime = findViewById(R.id.rd_btn_pre_order_collection_time);
        rdBtnDelivery = findViewById(R.id.rd_btn_delivery);


        //recyclerView for cart item

        rvAddItem = findViewById(R.id.rv_add_item);
        deal_rv_add_item = findViewById(R.id.deal_rv_add_item);

        //Logic for add item in cart


        tvCheckout = findViewById(R.id.tv_checkout);
        // tvGrandTotalPrice.setText((grandTotal(subMenuModelArrayList)));
        try {
            //      tvTotalFoodCost.setText(pound.concat(String.format("%.2f", updateTotalPrice())));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        ////////////groupcheckcode///////////////
        groupcheck = findViewById(R.id.groupcheck);
        groupcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (groupcheck.isChecked()) {
                    PaymentActivity.groupvisible = "true";
                } else {
                    PaymentActivity.groupvisible = "false";
                }

            }
        });
        /////////////////////////////////


    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvCheckout.setOnClickListener(this);
        rdBtnCollection.setOnClickListener(this);
        rdBtnPreOrderDeliveryTime.setOnClickListener(this);
        rdBtnPreOrderCollectionTime.setOnClickListener(this);
        rdBtnDelivery.setOnClickListener(this);
        btnPreOrderDeliveryTime.setOnClickListener(this);
        btnPreOrderCollectionTime.setOnClickListener(this);
        dinein.setOnClickListener(this);
    }

    private void updateCart() {
        try {


            //made by me
            totalPrice = 0.0;
            raviCartModles.clear();
            deal_raviCartModles.clear();
            SQLiteDatabase db = database.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM item_table", null);
            Cursor cursor1 = db.rawQuery("SELECT * FROM deal_item_table", null);
            if (cursor.moveToFirst() || cursor1.moveToFirst()) {
                if (cursor.moveToFirst()) {
                    do {
                        String item_id = cursor.getString(0);
                        String item_name = cursor.getString(1);
                        String size_item_id = cursor.getString(2);
                        String size_item_name = cursor.getString(3);
                        String extra_item_id = cursor.getString(4);
                        String extra_item_name = cursor.getString(5);
                        String price = cursor.getString(6);
                        totalPrice = totalPrice + Double.parseDouble(price);
                        totalPrice = (double) Math.round(totalPrice * 100) / 100;

                        String item_quantity = cursor.getString(7);
                        raviCartModles.add(new RaviCartModle(item_id, item_name, size_item_id, size_item_name, extra_item_id, extra_item_name, price, item_quantity));
                    } while (cursor.moveToNext());
                    tvTotalFoodCost.setText("+".concat(pound.concat("" + String.format("%.2f", totalPrice))));
                    //    tvTotalFoodCost.setText("+".concat(pound.concat("" +String.valueOf(totalPrice))));
                    getDiscount();
                    CartAdapterravi cartAdapterravi = new CartAdapterravi(CartActivity.this, raviCartModles);
                    linearLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvAddItem.setLayoutManager(linearLayoutManager);
                    rvAddItem.setAdapter(cartAdapterravi);
                } else {
                    rvAddItem.setVisibility(View.GONE);
                }
                if (cursor1.moveToFirst()) {
                    do {
                        String item_id = cursor1.getString(0);
                        String item_name = cursor1.getString(1);
                        String price = cursor1.getString(2);
                        totalPrice = totalPrice + Double.parseDouble(price);
                        totalPrice = (double) Math.round(totalPrice * 100) / 100;
                        String item_quantity = cursor1.getString(3);

                        deal_raviCartModles.add(new Deal_RaviCartModle(item_id, item_name, price, item_quantity));
                        Log.e("deal", "deal item" + deal_raviCartModles.size());
                    }
                    while (cursor1.moveToNext());
                    tvTotalFoodCost.setText("+".concat(pound.concat("" + String.format("%.2f", totalPrice))));
//                    getDiscount();
                    deal_CartAdapterravi deal_cartAdapterravi = new deal_CartAdapterravi(CartActivity.this, deal_raviCartModles);
                    linearLayoutManager1 = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
                    deal_rv_add_item.setLayoutManager(linearLayoutManager1);
                    deal_rv_add_item.setAdapter(deal_cartAdapterravi);
                } else {
                    deal_rv_add_item.setVisibility(View.GONE);
                }

            } else {
                db.close();
                Ravifinalitem.cart_Item_number = 0;
                Intent iii = new Intent(CartActivity.this, EmptyCartActivity.class);
                iii.putExtra("restaurantAddress", restaurantAddress);
                iii.putExtra("restaurantName", restaurantName);
                iii.putExtra("id", id);
                iii.putExtra("restaurantCategoryId", restaurantCategoryId);
                startActivity(iii);
                finish();
            }


        } catch (Exception e) {

            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intents = new Intent();
                intents.putExtra("editTextValue", "0");
                setResult(RESULT_OK, intents);
                finish();
                break;
            case R.id.tv_checkout:
                if (flag == 1) {
                    showCustomDialog1decline("Please add more item");
                } else if (orderType == null || orderType.equalsIgnoreCase("")) {
                    showCustomDialog1decline("Please select take out options");
                } else if ((orderType.equals("Pre Order Collection") && (preorderTime1.equals(""))) || (orderType.equals("Pre Order Delivery") && (preorderTime2.equals("")))) {
                    showCustomDialog1decline("Please select time");
                } else if (authPreference.getCustomerId().equals("")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("restaurantName", restaurantName);
                    intent.putExtra("restaurantAddress", restaurantAddress);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("itemId", itemId);
                    intent.putExtra("updateTotalPrice", totalPrice + "");
                    intent.putExtra("subTot", "" + subTot);
                    intent.putExtra("disPrice", "" + disPrice);
                    intent.putExtra("deliveryChargeValue", "" + deliveryChargeValue);
                    intent.putExtra("serviceFees", "" + serviceFees);
                    intent.putExtra("packageFees", "" + packageFees);
                    intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                    intent.putExtra("vatTax", "" + vatTax);
                    intent.putExtra("orderType", orderType);
                    intent.putExtra("subMenuItemId", deepsubmenuitemid);
                    startActivity(intent);
                } else {
                    if (orderType.equalsIgnoreCase("Collection") || orderType.equalsIgnoreCase("Pre Order Collection")) {
                        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("restaurantName", restaurantName);
                        intent.putExtra("restaurantAddress", restaurantAddress);
                        intent.putExtra("date", date);
                        intent.putExtra("time", time);
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("updateTotalPrice", totalPrice + "");
                        intent.putExtra("subTot", "" + subTot);
                        intent.putExtra("disPrice", "" + disPrice);
                        intent.putExtra("deliveryChargeValue", "");
                        intent.putExtra("serviceFees", "" + serviceFees);
                        intent.putExtra("packageFees", "" + packageFees);
                        intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                        intent.putExtra("vatTax", "" + vatTax);
                        //      intent.putExtra("subMenuModelArrayLists", subMenuModelArrayLists);
                        intent.putExtra("orderType", orderType);
//                        intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                        intent.putExtra("subMenuCartList",subMenuCartList);
                        intent.putExtra("subMenuItemId", deepsubmenuitemid);
                        startActivity(intent);

                    } else if (orderType.equalsIgnoreCase("Dine In") || orderType.equalsIgnoreCase("Pre Order Dine In")) {
                        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("restaurantName", restaurantName);
                        intent.putExtra("restaurantAddress", restaurantAddress);
                        intent.putExtra("date", date);
                        intent.putExtra("time", time);
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("updateTotalPrice", totalPrice + "");
                        intent.putExtra("subTot", "" + subTot);
                        intent.putExtra("disPrice", "" + disPrice);
                        intent.putExtra("deliveryChargeValue", "");
                        intent.putExtra("serviceFees", "" + serviceFees);
                        intent.putExtra("packageFees", "" + packageFees);
                        intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                        intent.putExtra("vatTax", "" + vatTax);
                        intent.putExtra("orderType", orderType);
//
                        intent.putExtra("subMenuItemId", deepsubmenuitemid);
                        intent.putExtra("tablenumber", tableid);
                        startActivity(intent);
                    } else if (orderType.equalsIgnoreCase("Delivery") || orderType.equalsIgnoreCase("Pre Order Delivery")) {
                        Intent intent = new Intent(this, SelectAddressActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("restaurantName", restaurantName);
                        intent.putExtra("restaurantAddress", restaurantAddress);
                        intent.putExtra("date", date);
                        intent.putExtra("time", time);
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("updateTotalPrice", totalPrice + "");
                        intent.putExtra("subTot", "" + subTot);
                        intent.putExtra("disPrice", "" + disPrice);
                        intent.putExtra("deliveryChargeValue", "" + deliveryChargeValue);
                        intent.putExtra("serviceFees", "" + serviceFees);
                        intent.putExtra("packageFees", "" + packageFees);
                        intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                        intent.putExtra("vatTax", "" + vatTax);
                        intent.putExtra("orderType", orderType);
                        String customerAddressId = authPreference.getCustomerAddressId();
                        intent.putExtra("customerAddressId", customerAddressId);

                        intent.putExtra("subMenuItemId", deepsubmenuitemid);

                        startActivity(intent);
                    }
                }
                break;
            case R.id.rd_btn_collection:
                try {
                    preorderTime = "";
                    preorderTime1 = "";
                    preorderTime2 = "";
                    orderType = "Collection";
                    tableid = "";
                    edtPreOrderDelivery.setText("");
                    edtPreOrderCollectionTime.setText("");
                    rdBtnCollection.setChecked(true);
                    rdBtnPreOrderDeliveryTime.setChecked(false);
                    rdBtnPreOrderCollectionTime.setChecked(false);
                    rlDeliveryCharge.setVisibility(View.GONE);
                    rdBtnDelivery.setChecked(false);
                    //  subTot = updateTotalPrice() - disPrice;
                    grandTotal = subTot + serviceFees + packageFees + salesTaxAmount + vatTax;
                    tvGrandTotalPrice.setText(pound.concat(String.format("%.2f", grandTotal)));
                    cartAdapter.notifyDataSetChanged();
                } catch (
                        NullPointerException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rd_btn_delivery:
                tableid = "";
                preorderTime = "";
                preorderTime1 = "";
                preorderTime2 = "";

//                }
                try {

                    getDeliveryCharge();

                    orderType = "Delivery";
                    rlDeliveryCharge.setVisibility(View.VISIBLE);
                    edtPreOrderDelivery.setText("");
                    edtPreOrderCollectionTime.setText("");
                    rdBtnDelivery.setChecked(true);
                    rdBtnCollection.setChecked(false);
                    dineinrelative.setVisibility(View.GONE);
                    rdBtnPreOrderDeliveryTime.setChecked(false);
                    rdBtnPreOrderCollectionTime.setChecked(false);
                    cartAdapter.notifyDataSetChanged();


                } catch (NullPointerException e) {
                    e.printStackTrace();

                }
                break;
            case R.id.dinein:
                try {
                    dineinrelative.setVisibility(View.VISIBLE);
                    orderType = "Dine In";
                    rdBtnDelivery.setChecked(false);
                    rdBtnCollection.setChecked(false);
                    rdBtnPreOrderDeliveryTime.setChecked(false);
                    rdBtnPreOrderCollectionTime.setChecked(false);
                    rlDeliveryCharge.setVisibility(View.GONE);
                    dinein.setChecked(true);

                    getDinintablenumberpopup();
                    cartAdapter.notifyDataSetChanged();

                } catch (NullPointerException e) {
                    e.printStackTrace();

                }
                break;
            case R.id.rd_btn_pre_order_collection_time:
                try {
                    preorderTime = "";
                    preorderTime1 = "";
                    preorderTime2 = "";
                    tableid = "";
                    showCustomDialog1decline("Please select time");
                    edtPreOrderDelivery.setText("");
                    orderType = "Pre Order Collection";
                    rlDeliveryCharge.setVisibility(View.GONE);
                    dineinrelative.setVisibility(View.GONE);
                    rdBtnPreOrderCollectionTime.setChecked(true);
                    rdBtnCollection.setChecked(false);
                    rdBtnPreOrderDeliveryTime.setChecked(false);
                    rdBtnDelivery.setChecked(false);
                    //    subTot = updateTotalPrice() - disPrice;
                    grandTotal = subTot + serviceFees + packageFees + salesTaxAmount + vatTax;
                    tvGrandTotalPrice.setText(pound.concat(String.format("%.2f", grandTotal)));
                    cartAdapter.notifyDataSetChanged();

                } catch (
                        NullPointerException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rd_btn_pre_order_delivery_time:
                tableid = "";
                try {
                    preorderTime = "";
                    preorderTime1 = "";
                    preorderTime2 = "";
                    getDeliveryCharge();
//                    grandTotal = subTot + serviceFees + packageFees + salesTaxAmount + vatTax + deliveryChargeValue;
//                    tvGrandTotalPrice.setText(pound.concat(String.format("%.2f", grandTotal)));
                    showCustomDialog1decline("Please select time");
                    edtPreOrderCollectionTime.setText("");
                    orderType = "Pre Order Delivery";
                    rdBtnPreOrderDeliveryTime.setChecked(true);
                    rdBtnCollection.setChecked(false);
                    rdBtnPreOrderCollectionTime.setChecked(false);
                    rdBtnDelivery.setChecked(false);
                    cartAdapter.notifyDataSetChanged();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_pre_order_delivery_time:

                tableid = "";
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                mintue = String.valueOf(c.get(Calendar.MINUTE));

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                edtPreOrderDelivery.setText(hourOfDay + ":" + minute);

                                preorderTime2 = hourOfDay + ":" + minute;

                                preorderTime = "" + edtPreOrderDelivery.getText().toString();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.btn_pre_order_collection_time:
                tableid = "";
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR_OF_DAY);
                mMinute = c1.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this,
                        (view, hourOfDay, minute) -> {
                            preorderTime1 = hourOfDay + ":" + minute;
                            edtPreOrderCollectionTime.setText(hourOfDay + ":" + minute);
                            preorderTime = "" + edtPreOrderCollectionTime.getText().toString();

                        }, mHour, mMinute, false);
                timePickerDialog1.show();
                break;

            default:
                break;
        }

    }


    public void getDinintablenumberpopup() {
        tabledialog = new Dialog(this);
        tabledialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tabledialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tabledialog.setContentView(R.layout.tablebookingtablenolist);
        tabledialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        tabledialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        tabledialog.setCanceledOnTouchOutside(true);
        tablerecycler = (RecyclerView) tabledialog.findViewById(R.id.tablerecycler);
        ImageView imgcancle = (ImageView) tabledialog.findViewById(R.id.imgcancle);

        imgcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType = "";
                tableid = "";
                dinein.setChecked(false);
                dineinrelative.setVisibility(View.GONE);
                tabledialog.dismiss();
            }
        });
        HitURLforTableNumberList();
        tabledialog.show();
    }

    public void HitURLforTableNumberList() {
        tablenoList = new ArrayList<Citymodel>();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GetRestaurantTableNumberList, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray citylist = jsonObj.getJSONArray("RestaurantTableList");
                    for (int i = 0; i < citylist.length(); i++) {
                        JSONObject c = citylist.getJSONObject(i);

                        int error = c.getInt("error");
                        if (error == 1) {
                            String error_msg = c.getString("error_msg");
                            showCustomDialog1decline(error_msg);
                            dinein.setChecked(false);
                            orderType = "";
                            tabledialog.dismiss();

                        } else {
                            String tableid1 = c.getString("id");
                            String restaurant_id = c.getString("restaurant_id");
                            String table_caption = c.getString("table_caption");
                            String restaurantCity = c.getString("table_number");
                            String table_no_guest_available = c.getString("table_no_guest_available");
                            String tabledisvplay = c.getString("Table_display_view");

                            Citymodel citymodel = new Citymodel();
                            citymodel.setCity_name(tabledisvplay);
                            citymodel.setId(tableid1);
                            tablenoList.add(citymodel);
                            TableNumberAdapter tableNumberAdapter = new TableNumberAdapter(CartActivity.this, tablenoList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
                            tablerecycler.setLayoutManager(horizontalLayoutManager1);
                            tablerecycler.setAdapter(tableNumberAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline("Please check your network connection");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", id);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public class TableNumberAdapter extends RecyclerView.Adapter<TableNumberAdapter.ViewHolder> {

        Context context;
        private ArrayList<Citymodel> tablenoList;
        private int lastSelectedPosition = -1;

        public TableNumberAdapter(Context c, ArrayList<Citymodel> tablenoList) {
            this.context = c;
            this.tablenoList = tablenoList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.citylistrecycler, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

//            holder.tv_city_name.setText(pp.get(position).getCityname());

            citymodel = tablenoList.get(position);
            holder.tv_city_name.setText(tablenoList.get(position).getCity_name());


            holder.citychecked.setOnClickListener(v -> {
                holder.citychecked.setChecked(true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lastSelectedPosition = position;
                        notifyDataSetChanged();
                        dineinrelative.setVisibility(View.VISIBLE);
                        table.setText(holder.tv_city_name.getText());
                        tableid = tablenoList.get(position).getId();
                        tablenumber = tablenoList.get(position).getCity_name();
                        Log.e("tablenumber", tableid);
                        tabledialog.dismiss();

                    }
                }, 1000);
            });
        }

        @Override
        public int getItemCount() {
            return tablenoList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_city_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;
            RadioGroup citygroup;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
                cuisinesLayout = (LinearLayout) itemView.findViewById(R.id.cuisines_layout);
                citychecked = (RadioButton) itemView.findViewById(R.id.citychecked);
                citygroup = (RadioGroup) itemView.findViewById(R.id.citygroup);
            }
        }
    }

    public void getDiscount() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Discount> userLoginCall = interfaceRetrofit.discountGet(id, String.valueOf(totalPrice));
        userLoginCall.enqueue(new Callback<Model_Discount>() {
            @Override
            public void onResponse(Call<Model_Discount> call, retrofit2.Response<Model_Discount> response) {
                if (response.isSuccessful()) {

                    Model_Discount body = response.body();
                    String discountOfferPrice = body.getDiscountOfferPrice();
                    String discountOfferDiscrition = body.getDiscountOfferDescription();

                    tvTotalDiscount.setText(("-").concat(" ").concat(pound.concat(String.format("%.2f", Double.parseDouble(discountOfferPrice)))));
                    if (disPrice.doubleValue() == 0.0 || disPrice.doubleValue() == 0.00) {
                        rlTotalDiscount.setVisibility(View.GONE);
                    }
                    subTot = totalPrice - Double.parseDouble(discountOfferPrice);
                    minOrderCondition("" + subTot);
                    getServiceCharge("" + subTot);
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_Discount> call, Throwable t) {
                Toast.makeText(CartActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });

    }

    public void getDeliveryCharge() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_DeliveryCharge> userLoginCall = interfaceRetrofit.DeliveryChargeGet(id, subTot + "", CITY);
        userLoginCall.enqueue(new Callback<Model_DeliveryCharge>() {
            @Override
            public void onResponse(Call<Model_DeliveryCharge> call, retrofit2.Response<Model_DeliveryCharge> response) {
                if (response.isSuccessful()) {
                    try {
                        deliveryChargeValue = Double.valueOf(response.body().getDeliveryChargeValue());
                        delivery_to_show = response.body().getDeliveryChargeValue();
                        rlDeliveryCharge.setVisibility(View.VISIBLE);
                        tvDeliveryCharge.setText(("+").concat(" ").concat(pound.concat(delivery_to_show)));
                        grandTotal = subTot + serviceFees + packageFees + salesTaxAmount + vatTax + deliveryChargeValue;
                        tvGrandTotalPrice.setText(pound.concat(String.format("%.2f", grandTotal)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_DeliveryCharge> call, Throwable t) {
                Toast.makeText(CartActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


        /*MyProgressDialog.show(this, R.string.please_wait);
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.DELIVERY_CHARGE_URL, response -> {
            Log.e("qwe", "" + response);
            MyProgressDialog.dismiss();
            try {
                JSONObject obj = new JSONObject(response);
                deliveryChargeValue = obj.optDouble("deliveryChargeValue");
                delivery_to_show = obj.optString("deliveryChargeValue");
                rlDeliveryCharge.setVisibility(View.VISIBLE);
                tvDeliveryCharge.setText(("+").concat(" ").concat(pound.concat(delivery_to_show)));
                grandTotal = subTot + serviceFees + packageFees + salesTaxAmount + vatTax + deliveryChargeValue;
                tvGrandTotalPrice.setText(pound.concat(String.format("%.2f", grandTotal)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            showCustomDialog1decline("Please check your network connection");
            MyProgressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("resid", id);
                params.put("subTotal", subTot + "");
                params.put("restaurant_locality", CITY);
                Log.e("qw", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);*/
    }


    public void getServiceCharge(final String a) {

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_GetServiceCharge> userLoginCall = interfaceRetrofit.ServiceChargetGet(id, a);
        userLoginCall.enqueue(new Callback<Model_GetServiceCharge>() {
            @Override
            public void onResponse(Call<Model_GetServiceCharge> call, retrofit2.Response<Model_GetServiceCharge> response) {
                if (response.isSuccessful()) {

                    try {

                        Model_GetServiceCharge body = response.body();
                        String packageFees = body.getPackageFees();
                        String packageFeesType = body.getPackageFeesType();
                        String packageFeesValue = body.getPackageFeesValue();
                        String salesTaxAmount = body.getSalesTaxAmount();
                        String serviceFees = body.getServiceFees();
                        String serviceFeesType = body.getServiceFeesType();
                        String seviceFeesValue = body.getSeviceFeesValue();
                        String vatTax = body.getVatTax();

                        CartActivity.this.vatTax = Double.valueOf(body.getVatTax());

                        if (serviceFees.equalsIgnoreCase("0") || serviceFees.equalsIgnoreCase("0.0") || serviceFees.equalsIgnoreCase("0.00")
                                || serviceFees.equalsIgnoreCase("00.00")) {
                            rlServiceFees.setVisibility(View.GONE);
                        } else {
                            rlServiceFees.setVisibility(View.VISIBLE);
                            tvServiceFees.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", Double.parseDouble(serviceFees)))));
                        }
                        if (packageFees.equalsIgnoreCase("0") || packageFees.equalsIgnoreCase("0.0") || packageFees.equalsIgnoreCase("0.00") || packageFees.equalsIgnoreCase("00.00")) {
                            rlPackageFees.setVisibility(View.GONE);
                        } else {
                            rlPackageFees.setVisibility(View.VISIBLE);
                            tvPackageFees.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", Double.parseDouble(packageFees)))));
                        }
                        if (salesTaxAmount.equalsIgnoreCase("0") || salesTaxAmount.equalsIgnoreCase("0.0") || salesTaxAmount.equalsIgnoreCase("0.00") ||
                                salesTaxAmount.equalsIgnoreCase("00.00")) {
                            rlSalesTaxAmount.setVisibility(View.GONE);
                        } else {
                            rlSalesTaxAmount.setVisibility(View.VISIBLE);
                            tvSalesTaxAmount.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", Double.parseDouble(salesTaxAmount)))));
                        }
                        if (CartActivity.this.vatTax.doubleValue() == 0 || CartActivity.this.vatTax.doubleValue() == 0.0 || CartActivity.this.vatTax.doubleValue() == 0.00 ||
                                CartActivity.this.vatTax.doubleValue() == 00.00) {
                            rlVatTax.setVisibility(View.GONE);
                        } else {
                            rlVatTax.setVisibility(View.VISIBLE);
                            tvVatTaxAmount.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", CartActivity.this.vatTax))));
                        }

                        grandTotal = Double.parseDouble(a) + Double.parseDouble(serviceFees) + deliveryChargeValue;
                        tvGrandTotalPrice.setText(pound.concat(String.format("%.2f", grandTotal)));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_GetServiceCharge> call, Throwable t) {
                Toast.makeText(CartActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }

    @Override
    public void onBackPressed() {
        Intent intents = new Intent();
        intents.putExtra("editTextValue", "0");
        setResult(RESULT_OK, intents);
        finish();
    }

    public class CartAdapterravi extends RecyclerView.Adapter<CartAdapterravi.ViewHolder> {
        Context context;
        ArrayList<RaviCartModle> rr;

        public CartAdapterravi(Context c, ArrayList<RaviCartModle> r) {
            this.context = c;
            this.rr = r;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vv = layoutInflater.inflate(R.layout.recyclerview_cart_item, parent, false);
            return new ViewHolder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            if (rr.get(position).getSize_item_name().equals("0") && rr.get(position).getExtra_item_name().equals("0")) {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name());
                holder.tv_menu_item_extra.setVisibility(View.GONE);
            } else if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name() + " (" + rr.get(position).getSize_item_name() + ")");
                holder.tv_menu_item_extra.setVisibility(View.GONE);
            } else if (rr.get(position).getSize_item_name().equals("0") && rr.get(position).getExtra_item_name() != "0") {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name());
                holder.tv_menu_item_extra.setVisibility(View.VISIBLE);
                String a = rr.get(position).getExtra_item_name().replace("[", "");
                a = a.replace("]", "");
                a = a.replace(",", "+");
                holder.tv_menu_item_extra.setText(a);
            } else if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name() != "0") {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name() + " (" + rr.get(position).getSize_item_name() + ")");
                holder.tv_menu_item_extra.setVisibility(View.VISIBLE);
                String a = rr.get(position).getExtra_item_name().replace("[", "");
                a = a.replace("]", "");
                a = a.replace(",", "+");
                holder.tv_menu_item_extra.setText(a);
            }
            double pp = Double.parseDouble(rr.get(position).getPrice());
            holder.tvItemPrice.setText(pound.concat(String.format("%.2f", pp)));
            holder.tvQuantity.setText(rr.get(position).getItem_quantity());

            holder.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    String subMenuItemId = rr.get(position).getItem_id();
                    String size_id = rr.get(position).getSize_item_id();
                    SQLiteDatabase db = database.getReadableDatabase();

                    if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'AND size_item_id='" + size_id + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt + 1;
                            price = price * qunt;
                        }
                        database.update_item_size(subMenuItemId, size_id, qunt, price);
                        Log.e("query", "" + subMenuItemId + " " + rr.get(position).getSize_item_id() + " " + qunt + " " + price);
                    } else {
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt + 1;
                            price = price * qunt;
                        }
                        database.update_item(subMenuItemId, qunt, price);
                        Log.e("query", "" + subMenuItemId + " " + rr.get(position).getSize_item_id() + " " + qunt + " " + price);
                    }
                    updateCart();
                }

            });

            holder.ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    String subMenuItemId = rr.get(position).getItem_id();
                    String size_id = rr.get(position).getSize_item_id();
                    if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                        SQLiteDatabase db = database.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'AND size_item_id='" + size_id + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt - 1;
                            price = price * qunt;
                            if (qunt == 0) {
                                database.delete_Item_size(rr.get(position).getItem_id(), rr.get(position).getSize_item_id());
                                Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number - 1;
                            } else {
                                database.update_item_size(subMenuItemId, size_id, qunt, price);
                            }
                        }
                    } else {
                        SQLiteDatabase db = database.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt - 1;
                            price = price * qunt;
                            if (qunt == 0) {
                                database.delete_Item(rr.get(position).getItem_id());
                                Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number - 1;
                            } else {
                                database.update_item(subMenuItemId, qunt, price);
                            }
                        }
                    }
                    updateCart();
                }
            });
            holder.cartDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                        database.delete_Item_size(rr.get(position).getItem_id(), rr.get(position).getSize_item_id());
                        Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number - 1;
                    } else {
                        database.delete_Item(rr.get(position).getItem_id());
                        Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number - 1;
                    }
                    updateCart();
                }
            });
        }

        @Override
        public int getItemCount() {
            return rr.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvMenuItemName, tvItemPrice, tvQuantity, tv_menu_item_extra;
            private ImageView ivPlus, ivMinus, cartDelete;

            public ViewHolder(View itemView) {
                super(itemView);
                tvMenuItemName = itemView.findViewById(R.id.tv_menu_item_name);
                tvItemPrice = itemView.findViewById(R.id.tv_item_price);
                tvQuantity = itemView.findViewById(R.id.tv_quantity);
                tv_menu_item_extra = itemView.findViewById(R.id.tv_menu_item_extra);
                ivPlus = itemView.findViewById(R.id.iv_plus);
                ivMinus = itemView.findViewById(R.id.iv_minus);
                cartDelete = itemView.findViewById(R.id.cart_delete);
            }
        }
    }

    @Override
    protected void onDestroy() {
        MyProgressDialog.dismiss();
        database.deal_delete();
        super.onDestroy();
    }

    public class deal_CartAdapterravi extends RecyclerView.Adapter<deal_CartAdapterravi.ViewHolder> {
        Context context;
        ArrayList<Deal_RaviCartModle> rr;

        public deal_CartAdapterravi(Context c, ArrayList<Deal_RaviCartModle> r) {
            this.context = c;
            this.rr = r;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vv = layoutInflater.inflate(R.layout.recyclerview_cart_item, parent, false);
            return new ViewHolder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


            holder.tvMenuItemName.setText(rr.get(position).getItem_name());
            holder.tv_menu_item_extra.setVisibility(View.GONE);
            double pp = Double.parseDouble(rr.get(position).getPrice());
            holder.tvItemPrice.setText(pound.concat(String.format("%.2f", pp)));
            holder.tvQuantity.setText(rr.get(position).getItem_quantity());

            holder.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    String subMenuItemId = rr.get(position).getItem_id();
                    SQLiteDatabase db = database.getReadableDatabase();

                    Cursor cursor = db.rawQuery("SELECT * FROM deal_item_table where deal_item_id='" + subMenuItemId + "'", null);
                    if (cursor.moveToNext()) {
                        qunt = Integer.parseInt(cursor.getString(3));
                        price = Double.parseDouble(rr.get(position).getPrice());
                        price = price / qunt;
                        price = (double) Math.round(price * 100) / 100;
                        qunt = qunt + 1;
                        price = price * qunt;
                    }
                    database.deal_update_item(subMenuItemId, qunt, price);

                    updateCart();
                }

            });

            holder.ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    String subMenuItemId = rr.get(position).getItem_id();

                    SQLiteDatabase db = database.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM deal_item_table where deal_item_id='" + subMenuItemId + "'", null);
                    if (cursor.moveToNext()) {
                        qunt = Integer.parseInt(cursor.getString(3));
                        price = Double.parseDouble(rr.get(position).getPrice());
                        price = price / qunt;
                        price = (double) Math.round(price * 100) / 100;
                        qunt = qunt - 1;
                        price = price * qunt;
                        if (qunt == 0) {
                            database.deal_delete_Item(rr.get(position).getItem_id());
                            Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number - 1;
                        } else {
                            database.deal_update_item(subMenuItemId, qunt, price);
                        }
                    }
                    updateCart();
                }
            });
            holder.cartDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.deal_delete_Item(rr.get(position).getItem_id());
                    Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number - 1;
                    updateCart();
                }
            });
        }

        @Override
        public int getItemCount() {
            return rr.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvMenuItemName, tvItemPrice, tvQuantity, tv_menu_item_extra;
            private ImageView ivPlus, ivMinus, cartDelete;

            public ViewHolder(View itemView) {
                super(itemView);
                tvMenuItemName = itemView.findViewById(R.id.tv_menu_item_name);
                tvItemPrice = itemView.findViewById(R.id.tv_item_price);
                tvQuantity = itemView.findViewById(R.id.tv_quantity);
                tv_menu_item_extra = itemView.findViewById(R.id.tv_menu_item_extra);
                ivPlus = itemView.findViewById(R.id.iv_plus);
                ivMinus = itemView.findViewById(R.id.iv_minus);
                cartDelete = itemView.findViewById(R.id.cart_delete);
            }
        }
    }

    private synchronized void minOrderCondition(String a) {
        try {

            tvSubtotal.setText("+".concat(pound.concat(String.format("%.2f", subTot))));
            if (!MainMenuActivity.restaurantModel.getRestaurant_minimumorder().isEmpty()) {
                minOrder = Double.parseDouble(((MainMenuActivity.restaurantModel.getRestaurant_minimumorder())));
                double aa = Double.parseDouble(a);
//            double dd = aa - subTot;

                if (totalPrice < minOrder) {
                    flag = 1;
                    double difference = ((minOrder) - (totalPrice));
                    tvSaveDiscountAmountMsg.setText("Add".concat(" ").concat(pound.concat(String.format("%.2f", (difference)).concat(" ").concat(" to reach").concat(" ").concat(pound).concat(String.format("%.2f", minOrder)).concat(" ").concat("Minimum order"))));
//                subTot = updateTotalPrice();
//                grandTotal = updateTotalPrice();
//                tvGrandTotalPrice.setText(pound.concat(String.format("%.2f", grandTotal)));
                } else {
                    flag = 2;
                    tvSaveDiscountAmountMsg.setText("You have saved".concat(" ").concat(pound).concat(String.format("%.2f", disPrice).concat(" ").concat("using Justfoodz Today")));
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}