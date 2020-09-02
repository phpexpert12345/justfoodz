package com.justfoodz.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.Gson;
import com.justfoodz.Database.Database;


import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.Deal_RaviCartModle;
import com.justfoodz.models.RaviCartModle;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.ConnectionDetector;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.justfoodz.activities.MainMenuActivity.restaurantModel;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PAYMENT = 1;
    private ImageView ivBack, ivRestaurantLogo;
    private TextView tvRestaurantName, tvRestaurantAddress, tvCardOption, tvCashPayment, googlepay, paypal,
            tvPayConfirm, tvTotalPay, tvHeadingOne, tvContentDescriptionOne, tvHeadingTwo, tvContentDescriptionTwo, tvAllergies;
    private ProgressDialog pDialog;
    private String restaurantName, restaurantAddress, TableNumber, date, time, itemId, id, disPrice = "", deliveryChargeValue = "",
            serviceFees = "", packageFees = "", salesTaxAmount = "", vatTax = "", preOrderTime, updateTotalPrice;
    private AuthPreference authPreference;
    private RelativeLayout rlFoodCost, rlTotalDiscount, rlSubTotal, rlServiceFees, rlPackageFees, rlSalesTaxAmount, rlVatTax, rlDeliveryCost, rlCouponPrice;
    private TextView tvTotalFoodCost, tvTotalDiscount, tvSubtotal, tvServiceFees, tvPackageFees,
            tvSalesTaxAmount, tvVatTaxAmount, tvDeliveryCost, tvCouponPrice, txtError,
            tvCouponCode, tv_gift_discount, tv_wallet_discount;
    private RecyclerView rvItemList, deal_rv_item_list;
    LinearLayout tvEnterCouponCode, redeempoints, redeemvouchers, walletpay;
    //    private PaymentCartAdapter paymentCartAdapter;
    String pound = SplashScreenActivity.currency_symbol;
    public double subTotal = 0.0, totalAmountPay = 0.0, totalAmount = 0.0,
            total_gift_applied = 0.0, tota_lwallet_applied = 0.0;
    public String paymentType = "", orderType = "";

    public int addressId;
    public Dialog dialogCoupon, dialogAllergies, dialogloyalitypoints, dialogredeemvouchers, dialogwallet, groupdialog;
    public LinearLayout layoutAllergies;
    public RelativeLayout layoutCoupon;
    public EditText edtCoupon;
    private String coupon, deepsubmenuitemid;
    public Double offerPrice = 0.00;
    private String itemPrice;

    private NestedScrollView paymentLayout;
    ArrayList<String> itemIdList;
    ArrayList<String> orderPriceList;
    ArrayList<String> itemQuantityList;
    ArrayList<String> orderSizeItemId;

    String orderExtraItemId = "";
    String extraItemIDName1 = "";

    ArrayList<String> deal_itemIdList;
    ArrayList<String> deal_orderPriceList;
    ArrayList<String> deal_itemQuantityList;

    Database database;
    ArrayList<RaviCartModle> raviCartModles;
    ArrayList<Deal_RaviCartModle> dealraviCartModles;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;

    EditText point, amount, walletamount, giftamount;
    String customerId;
    TextView tablenumber, groupnamep;
    LinearLayout grouplayout;
    Citymodel citymodel;
    public static String groupvisible;
    String GROUPid, group_member_id_to_send = "";
    public String payment_transaction_paypal = "";
    private ArrayList<Citymodel> groupList;
    private GroupAdapter groupAdapter;

    /////////////loyality points/////////
    private RecyclerView loyalitypointsrecycler;
    ArrayList<String> loyalitypointsList;
    private LoyalitypointAdapter loyalitypointAdapter;

    TextView totalloyalityamount, giftbalance, totalgiftgardamount, walletbalance, totalwalletamount;
    RelativeLayout rademmloyality, rademmvouc, rademmwallet, table,
            rl_gift_discount, rl_wallet_discount;
    String Selectloyalitypoints = "No Points";
    String total_loyalty_amount = "", total_wallet_Amount = "", total_giftcard_amount = "";
    RecyclerView grouprecycler;
    LinearLayout groupshow;

    private String paymentAmount, total_gift_card_money_available, total_wallet_money_available;
    public static final int PAYPAL_REQUEST_CODE = 123;

    private static com.paypal.android.sdk.payments.PayPalConfiguration config = new com.paypal.android.sdk.payments.PayPalConfiguration()

            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    int flag_to_check_for_wallet, flag_to_check_for_gift;

    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        myPref = new MyPref(PaymentActivity.this);
        AuthPreference authPreference = new AuthPreference(this);
        customerId = authPreference.getCustomerId();
        flag_to_check_for_wallet = 0;
        flag_to_check_for_gift = 0;
        initialization();
        initializedListener();
        statusBarColor();
    }

    private void initialization() {
        raviCartModles = new ArrayList<>();
        dealraviCartModles = new ArrayList<>();
        itemIdList = new ArrayList<String>();
        orderPriceList = new ArrayList<String>();
        itemQuantityList = new ArrayList<String>();
        orderSizeItemId = new ArrayList<String>();
//        orderExtraItemId = new ArrayList<String>();

        deal_itemIdList = new ArrayList<String>();
        deal_orderPriceList = new ArrayList<String>();
        deal_itemQuantityList = new ArrayList<String>();

        database = new Database(PaymentActivity.this);
        ivBack = findViewById(R.id.iv_back);
        totalloyalityamount = findViewById(R.id.totalloyalityamount);
        totalwalletamount = findViewById(R.id.totalwalletamount);
        totalgiftgardamount = findViewById(R.id.totalgiftgardamount);
        rademmvouc = findViewById(R.id.rademmvoucher);
        rademmwallet = findViewById(R.id.rademmwallet);
        rademmloyality = findViewById(R.id.rademmloyality);
        tablenumber = findViewById(R.id.tablenumber);
        table = findViewById(R.id.table);


        //parent view for amount
        //------------------------------------------------------------------------------------------------//

        rlFoodCost = findViewById(R.id.rl_food_cost);
        rlTotalDiscount = findViewById(R.id.rl_total_discount);
        rlSubTotal = findViewById(R.id.rl_sub_total);
        rlServiceFees = findViewById(R.id.rl_service_fees);
        rlPackageFees = findViewById(R.id.rl_package_fees);
        rlSalesTaxAmount = findViewById(R.id.rl_sales_tax_amount);
        rlVatTax = findViewById(R.id.rl_vat_tax);
        rlDeliveryCost = findViewById(R.id.rl_delivery_cost);
        rlCouponPrice = findViewById(R.id.rl_coupon_price);
        rl_gift_discount = findViewById(R.id.rl_gift_discount);
        rl_wallet_discount = findViewById(R.id.rl_wallet_discount);
        tv_gift_discount = findViewById(R.id.tv_gift_discount);
        tv_wallet_discount = findViewById(R.id.tv_wallet_discount);

        //TextView for different prices
        //------------------------------------------------------------------------------------------------//
        tvTotalFoodCost = findViewById(R.id.tv_total_food_cost);
        tvTotalDiscount = findViewById(R.id.tv_total_discount);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvServiceFees = findViewById(R.id.tv_service_fees);
        tvPackageFees = findViewById(R.id.tv_package_fees);
        tvSalesTaxAmount = findViewById(R.id.tv_sales_tax_amount);
        tvVatTaxAmount = findViewById(R.id.tv_vat_tax_amount);
        tvDeliveryCost = findViewById(R.id.tv_delivery_cost);
        tvTotalPay = findViewById(R.id.tv_total_pay);
        tvCouponPrice = findViewById(R.id.tv_coupon_price);
        tvAllergies = findViewById(R.id.tv_any_allergies);
        paymentLayout = findViewById(R.id.payment_layout);

        //-------------------------------------------------------------------------------------------------------//

        rvItemList = findViewById(R.id.rv_item_list);
        deal_rv_item_list = findViewById(R.id.deal_rv_item_list);
        tvCouponCode = findViewById(R.id.tv_coupon_code);
        tvEnterCouponCode = findViewById(R.id.tv_enter_coupon_code);
        redeempoints = findViewById(R.id.redeempoints);
        redeemvouchers = findViewById(R.id.redeemvouchers);
        walletpay = findViewById(R.id.walletpay);
        tvRestaurantName = findViewById(R.id.tv_restaurant_name);
        tvRestaurantAddress = findViewById(R.id.tv_restaurant_address);
        ivRestaurantLogo = findViewById(R.id.iv_restaurant_logo);
        tvCardOption = findViewById(R.id.tv_card_option);
        googlepay = findViewById(R.id.googlepay);
        paypal = findViewById(R.id.paypal);
        tvCashPayment = findViewById(R.id.tv_cash_payment);
        tvPayConfirm = findViewById(R.id.tv_pay_confirm);
        groupnamep = findViewById(R.id.groupnamep);
        grouplayout = findViewById(R.id.grouplayout);
        groupshow = findViewById(R.id.groupshow);

        if (groupvisible.equals("true")) {
            groupshow.setVisibility(View.VISIBLE);
        } else {
            groupshow.setVisibility(View.GONE);
        }

        if (RestaurantsListActivity.cash_avaible.equals("Yes")) {
            tvCashPayment.setVisibility(View.VISIBLE);
        }
        if (RestaurantsListActivity.card_avaible.equals("Yes")) {
            tvCardOption.setVisibility(View.VISIBLE);
        }
        if (RestaurantsListActivity.card_avaible.equals("Yes")) {
            googlepay.setVisibility(View.VISIBLE);
        }
        if (RestaurantsListActivity.card_avaible.equals("Yes")) {
            paypal.setVisibility(View.VISIBLE);
        }
        //-------------------------------------------------------------------------------------------------------//

        String xx1 = "";
        String xx2 = "";


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
                    String item_quantity = cursor.getString(7);
                    itemIdList.add(item_id);
                    orderPriceList.add(price);
                    itemQuantityList.add(item_quantity);
                    orderSizeItemId.add(size_item_id);
//                    orderExtraItemId.add(extra_item_id);
                    orderExtraItemId = orderExtraItemId + xx1 + "\"" + extra_item_id + "\"";
                    extraItemIDName1 = extraItemIDName1 + xx2 + "\"" + extra_item_name + "\"";

                    xx1 = "-";
                    xx2 = "=";


                    raviCartModles.add(new RaviCartModle(item_id, item_name, size_item_id, size_item_name, extra_item_id, extra_item_name, price, item_quantity));
                } while (cursor.moveToNext());
                PaymentRecycler paymentRecycler = new PaymentRecycler(PaymentActivity.this, raviCartModles);

                Log.e("deal", "itemTable" + raviCartModles.size());
                linearLayoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
                rvItemList.setLayoutManager(linearLayoutManager);
                rvItemList.setAdapter(paymentRecycler);
            } else {
                rvItemList.setVisibility(View.GONE);
            }
            if (cursor1.moveToFirst()) {
                do {
                    String item_id = cursor1.getString(0);
                    String item_name = cursor1.getString(1);
                    String price = cursor1.getString(2);
                    Log.d("TAG", "Cor.........");
                    String item_quantity = cursor1.getString(3);
                    dealraviCartModles.add(new Deal_RaviCartModle(item_id, item_name, price, item_quantity));
                    deal_itemIdList.add(item_id);
                    deal_orderPriceList.add(price);
                    deal_itemQuantityList.add(item_quantity);

                }
                while (cursor1.moveToNext());

                deal_PaymentRecycler deal_paymentRecycler = new deal_PaymentRecycler(PaymentActivity.this, dealraviCartModles);
                linearLayoutManager1 = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
                deal_rv_item_list.setLayoutManager(linearLayoutManager1);
                deal_rv_item_list.setAdapter(deal_paymentRecycler);
            } else {
                deal_rv_item_list.setVisibility(View.GONE);
            }
        } else {
            db.close();
        }

        restaurantName = getIntent().getStringExtra("restaurantName");
        restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        tvRestaurantName.setText(restaurantName);
        tvRestaurantAddress.setText(restaurantAddress);
        Picasso.get().load(restaurantModel.getRestaurant_Logo()).into(ivRestaurantLogo);
        authPreference = new AuthPreference(this);

        try {
            deepsubmenuitemid = getIntent().getStringExtra("subMenuItemId");
            TableNumber = getIntent().getStringExtra("tablenumber");
            date = getIntent().getStringExtra("date");
            id = getIntent().getStringExtra("id");
            time = getIntent().getStringExtra("time");
            disPrice = getIntent().getStringExtra("disPrice");
            deliveryChargeValue = getIntent().getStringExtra("deliveryChargeValue");
            serviceFees = getIntent().getStringExtra("serviceFees");
            packageFees = getIntent().getStringExtra("packageFees");
            salesTaxAmount = getIntent().getStringExtra("salesTaxAmount");
            vatTax = getIntent().getStringExtra("vatTax");
            orderType = getIntent().getStringExtra("orderType");
            itemId = getIntent().getStringExtra("itemId");
            updateTotalPrice = getIntent().getStringExtra("updateTotalPrice");
            addressId = getIntent().getIntExtra("addressId", 1);

            double we = Double.parseDouble(updateTotalPrice);
            double dp = Double.parseDouble(disPrice);
            tvTotalFoodCost.setText(pound.concat(String.format("%.2f", we)));
            tvTotalDiscount.setText(("-").concat(" ").concat(pound).concat(String.format("%.2f", dp)));
            if (serviceFees.equalsIgnoreCase("0") || serviceFees.equalsIgnoreCase("0.0") || serviceFees.equalsIgnoreCase("0.00") || serviceFees.equalsIgnoreCase("00.00")) {
                rlServiceFees.setVisibility(View.GONE);
            } else {
                rlServiceFees.setVisibility(View.VISIBLE);
                double sf = Double.parseDouble(serviceFees);
                tvServiceFees.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", sf))));

            }
            if (packageFees.equalsIgnoreCase("0") || packageFees.equalsIgnoreCase("0.0") || packageFees.equalsIgnoreCase("0.00") || packageFees.equalsIgnoreCase("00.00")) {
                rlPackageFees.setVisibility(View.GONE);
            } else {
                rlPackageFees.setVisibility(View.VISIBLE);
                double pf = Double.parseDouble(packageFees);
                tvPackageFees.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", pf))));
            }
            if (salesTaxAmount.equalsIgnoreCase("0") || salesTaxAmount.equalsIgnoreCase("0.0") || salesTaxAmount.equalsIgnoreCase("0.00") || salesTaxAmount.equalsIgnoreCase("00.00")) {
                rlSalesTaxAmount.setVisibility(View.GONE);
            } else {
                rlSalesTaxAmount.setVisibility(View.VISIBLE);
                double sta = Double.parseDouble(salesTaxAmount);
                tvSalesTaxAmount.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", sta))));
            }
            if (vatTax.equalsIgnoreCase("0") || vatTax.equalsIgnoreCase("0.0") || vatTax.equalsIgnoreCase("0.00") || vatTax.equalsIgnoreCase("00.00")) {
                rlVatTax.setVisibility(View.GONE);
            } else {
                rlVatTax.setVisibility(View.VISIBLE);
                double vt = Double.parseDouble(vatTax);
                tvVatTaxAmount.setText(("+").concat(" ").concat(pound.concat(String.format("%.2f", vt))));
            }

            try {
                if (orderType.equalsIgnoreCase("Collection") || orderType.equalsIgnoreCase("Pre Order Collection")) {
                    subTotal = Double.parseDouble(updateTotalPrice) - Double.parseDouble(disPrice);
                    tvSubtotal.setText(("+").concat(" ").concat(pound).concat(String.format("%.2f", subTotal)));

                    totalAmountPay = subTotal + Double.parseDouble(serviceFees) + Double.parseDouble(packageFees) + Double.parseDouble(salesTaxAmount) + Double.parseDouble(vatTax);
                    DecimalFormat precision = new DecimalFormat("0.00");
                    totalAmountPay = Double.parseDouble((precision.format(totalAmountPay)));
                    tvTotalPay.setText(pound.concat(String.format("%.2f", totalAmountPay)));


                } else if (orderType.equalsIgnoreCase("Delivery") || orderType.equalsIgnoreCase("Pre Order Delivery")) {
                    rlDeliveryCost.setVisibility(View.VISIBLE);
                    tvCashPayment.setVisibility(View.GONE);
//                    tvDeliveryCost.setText(pound.concat(deliveryChargeValue));
                    subTotal = Double.parseDouble(updateTotalPrice) - Double.parseDouble(disPrice);
                    tvSubtotal.setText(("+").concat(" ").concat(pound).concat(String.format("%.2f", subTotal)));
                    tvDeliveryCost.setText(("+").concat(pound.concat(CartActivity.delivery_to_show)));
                    totalAmountPay = subTotal + Double.parseDouble(serviceFees) + Double.parseDouble(packageFees) + Double.parseDouble(salesTaxAmount) + Double.parseDouble(vatTax) + Double.parseDouble(deliveryChargeValue);
                    tvTotalPay.setText(pound.concat(String.format("%.2f", totalAmountPay)));
                } else if (orderType.equalsIgnoreCase("Dine In")) {
                    subTotal = Double.parseDouble(updateTotalPrice) - Double.parseDouble(disPrice);
                    tvSubtotal.setText(("+").concat(" ").concat(pound).concat(String.format("%.2f", subTotal)));
                    totalAmountPay = subTotal + Double.parseDouble(serviceFees) + Double.parseDouble(packageFees) + Double.parseDouble(salesTaxAmount) + Double.parseDouble(vatTax);
                    tvTotalPay.setText(pound.concat(String.format("%.2f", totalAmountPay)));
                    table.setVisibility(View.VISIBLE);
                    tablenumber.setText(CartActivity.tablenumber);
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvCardOption.setOnClickListener(this);
        googlepay.setOnClickListener(this);
        paypal.setOnClickListener(this);
        tvCashPayment.setOnClickListener(this);
        tvPayConfirm.setOnClickListener(this);
        tvEnterCouponCode.setOnClickListener(this);
        redeempoints.setOnClickListener(this);
        redeemvouchers.setOnClickListener(this);
        walletpay.setOnClickListener(this);
        grouplayout.setOnClickListener(this);
        tvAllergies.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (orderType.equalsIgnoreCase("Collection") || orderType.equalsIgnoreCase("Pre Order Collection")) {
                    Intent intent = new Intent(this, CartActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("itemId", itemId);
                    intent.putExtra("restaurantName", restaurantName);
                    intent.putExtra("restaurantAddress", restaurantAddress);
                    startActivity(intent);
                    finish();
                } else if (orderType.equalsIgnoreCase("Delivery") || orderType.equalsIgnoreCase("Pre Order Delivery")) {
                    Intent intent = new Intent(this, SelectAddressActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("addressId", addressId);
                    intent.putExtra("itemId", itemId);
                    intent.putExtra("restaurantName", restaurantName);
                    intent.putExtra("restaurantAddress", restaurantAddress);

                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("disPrice", disPrice);
                    intent.putExtra("deliveryChargeValue", deliveryChargeValue);
                    intent.putExtra("serviceFees", serviceFees);
                    intent.putExtra("packageFees", packageFees);
                    intent.putExtra("salesTaxAmount", salesTaxAmount);
                    intent.putExtra("vatTax", vatTax);
                    intent.putExtra("orderType", orderType);
                    intent.putExtra("updateTotalPrice", updateTotalPrice);
                    intent.putExtra("addressId", addressId);

//                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                    startActivity(intent);
                    finish();
                }
                finish();
                //  HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_card_option:
                tvCardOption.setTextColor(getResources().getColor(R.color.white));
                tvCardOption.setBackgroundResource(R.drawable.rectanglered);
                googlepay.setTextColor(getResources().getColor(R.color.green));
                googlepay.setBackgroundResource(R.drawable.green_white);
                paypal.setTextColor(getResources().getColor(R.color.green));
                paypal.setBackgroundResource(R.drawable.green_white);
                tvCashPayment.setTextColor(getResources().getColor(R.color.green));
                tvCashPayment.setBackgroundResource(R.drawable.green_white);
                paymentType = "Credit-card";


                break;
            case R.id.tv_cash_payment:
                tvCashPayment.setTextColor(getResources().getColor(R.color.white));
                tvCashPayment.setBackgroundResource(R.drawable.rectanglered);
                googlepay.setTextColor(getResources().getColor(R.color.green));
                googlepay.setBackgroundResource(R.drawable.green_white);
                paypal.setTextColor(getResources().getColor(R.color.green));
                paypal.setBackgroundResource(R.drawable.green_white);
                tvCardOption.setTextColor(getResources().getColor(R.color.green));
                tvCardOption.setBackgroundResource(R.drawable.green_white);
                paymentType = "Cash";
                payment_transaction_paypal = "";
                break;
            case R.id.googlepay:
                googlepay.setTextColor(getResources().getColor(R.color.white));
                googlepay.setBackgroundResource(R.drawable.rectanglered);
                tvCashPayment.setTextColor(getResources().getColor(R.color.green));
                tvCashPayment.setBackgroundResource(R.drawable.green_white);
                paypal.setTextColor(getResources().getColor(R.color.green));
                paypal.setBackgroundResource(R.drawable.green_white);
                tvCardOption.setTextColor(getResources().getColor(R.color.green));
                tvCardOption.setBackgroundResource(R.drawable.green_white);
                paymentType = "Google Pay";
                payment_transaction_paypal = "";
                break;
            case R.id.paypal:
                paypal.setTextColor(getResources().getColor(R.color.white));
                paypal.setBackgroundResource(R.drawable.rectanglered);
                googlepay.setTextColor(getResources().getColor(R.color.green));
                googlepay.setBackgroundResource(R.drawable.green_white);
                tvCashPayment.setTextColor(getResources().getColor(R.color.green));
                tvCashPayment.setBackgroundResource(R.drawable.green_white);
                tvCardOption.setTextColor(getResources().getColor(R.color.green));
                tvCardOption.setBackgroundResource(R.drawable.green_white);
                paymentType = "Paypal";
                break;

            case R.id.tv_pay_confirm:

                //todo
                //   paymentAmount = String.valueOf(totalAmountPay);
                if (groupvisible.equals("true") && group_member_id_to_send.equals("")) {
                    showCustomDialog1decline("Please select group");
                } else {
                    if ((totalAmountPay == 0) || (totalAmountPay == 0.0) || (totalAmountPay == 0.00)) {
                        if (Network.isNetworkCheck(this)) {
                            paymentType = "Cash";
                            paymentSubmit();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (paymentType.equalsIgnoreCase("")) {
                            showCustomDialog1decline("Please select the payment mode");
                        } else if (paymentType.equals("Google Pay")) {

                        } else if (paymentType.equals("Credit-card")) {
                            if (Network.isNetworkCheck(this)) {
                                paymentAmount = String.valueOf(totalAmountPay);
                                Intent intent = new Intent(this, StripePaymentActivity.class);
                                intent.putExtra("amount", paymentAmount);
                                intent.putExtra("paymentwallet", "2");
//
                                Log.e("payment", "jddj" + intent.putExtra("amount", paymentAmount));
                                startActivityForResult(intent, PAYMENT);


                            } else {
                                Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }

                        } else if (paymentType.equals("Paypal")) {
                            if (Network.isNetworkCheck(this)) {
                                getPayment();
                            } else {
                                Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (Network.isNetworkCheck(this)) {
                                paymentSubmit();
                            } else {
                                Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
                break;
            case R.id.tv_enter_coupon_code:
                showCoupon();
                break;
            case R.id.redeempoints:
                if (Selectloyalitypoints.equals("No Points")) {
                    showRedeempoints();
                } else {
                    showCustomDialog1decline("Already have Redeem points");
                }
                break;
            case R.id.tv_any_allergies:
                showAllergiesPop();
                break;
            case R.id.redeemvouchers:
                showRedeemVouchers();
                break;
            case R.id.walletpay:
                showWalletPay();
                break;
            case R.id.grouplayout:
                showgroupnameselect();
            default:
                break;
        }
    }


    private void getPayment() {

        paymentAmount = String.valueOf(totalAmountPay);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Justfoodz Food Payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Log.e("payment", "getpayment" + paymentAmount);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    public void showgroupnameselect() {
        groupdialog = new Dialog(this);
        groupdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        groupdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        groupdialog.setContentView(R.layout.groupnamelist);
        groupdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        groupdialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        groupdialog.setCanceledOnTouchOutside(true);
        grouprecycler = (RecyclerView) groupdialog.findViewById(R.id.grouprecycler);

        ImageView imgcancle = groupdialog.findViewById(R.id.imgcancle);


        imgcancle.setOnClickListener(v -> {
//                orderType="";
//                tablenumber="";
//                dinein.setChecked(false);
//                dineinrelative.setVisibility(View.GONE);
            groupdialog.dismiss();
        });

        if (!Network.isNetworkCheck(this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
        } else {
            HitURLforGroupList();
        }

        groupdialog.show();
    }

    private void showAllergiesPop() {
        dialogAllergies = new Dialog(this);
        dialogAllergies.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAllergies.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogAllergies.setContentView(R.layout.allergies_pop);
        dialogAllergies.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogAllergies.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        dialogAllergies.setCanceledOnTouchOutside(true);
        tvHeadingOne = (TextView) dialogAllergies.findViewById(R.id.tv_heading_one);
        tvContentDescriptionOne = (TextView) dialogAllergies.findViewById(R.id.tv_content_description_one);
        tvHeadingTwo = (TextView) dialogAllergies.findViewById(R.id.tv_heading_two);
        tvContentDescriptionTwo = (TextView) dialogAllergies.findViewById(R.id.tv_content_description_two);
        Button btnCancel = (Button) dialogAllergies.findViewById(R.id.btn_cancel);
        layoutAllergies = dialogAllergies.findViewById(R.id.layoutAllergies);

        if (Network.isNetworkCheck(PaymentActivity.this)) {
            getAllergies();

        } else {
            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAllergies.dismiss();

            }
        });
    }

    public void getAllergies() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.ALLERGY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                dialogAllergies.show();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("ContentData");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String headingOne = jsonObject.optString("heading_one");
                        String contentDescriptionOne = jsonObject.optString("content_description_one");
                        String headingTwo = jsonObject.optString("heading_two");
                        String contentDescriptionTwo = jsonObject.optString("content_description_two");
                        tvHeadingOne.setText(headingOne);
                        tvContentDescriptionOne.setText(contentDescriptionOne);
                        tvHeadingTwo.setText(headingTwo);
                        tvContentDescriptionTwo.setText(contentDescriptionTwo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void showWalletPay() {
        dialogwallet = new Dialog(this);
        dialogwallet.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogwallet.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogwallet.setContentView(R.layout.custom_walletpay);
        dialogwallet.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogwallet.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        dialogwallet.setCanceledOnTouchOutside(true);
        Button btnCancel = dialogwallet.findViewById(R.id.btn_cancel);
        Button pay = dialogwallet.findViewById(R.id.pay);
        walletamount = dialogwallet.findViewById(R.id.amount);
        walletbalance = dialogwallet.findViewById(R.id.walletbalance);
        TextView txtwallettotalpay = (TextView) dialogwallet.findViewById(R.id.txtwallettotalpay);


        txtwallettotalpay.setText("Total Pay : " + pound.concat(String.format("%.2f", totalAmountPay)));

        if (flag_to_check_for_wallet == 0) {
            HitUrlforWalletAmount();
            flag_to_check_for_wallet = 1;
        } else {
            walletbalance.setText(SplashScreenActivity.currency_symbol + total_wallet_money_available);
        }

        btnCancel.setOnClickListener(v -> dialogwallet.dismiss());
        pay.setOnClickListener(v -> {


            if (walletamount.getText().toString().equals("")) {
                walletamount.setError("Enter amount");
                walletamount.requestFocus();
            } else {
                double aa = Double.parseDouble(walletamount.getText().toString());
                double bb = Double.parseDouble(total_wallet_money_available);
                if (aa > bb) {
                    walletamount.setError("Sorry you don't have that much amount");
                    walletamount.requestFocus();
                } else if (aa > totalAmountPay) {
                    walletamount.setError("Your grand total is less than you entered amount");
                    walletamount.requestFocus();
                } else {

                    tota_lwallet_applied = tota_lwallet_applied + aa;
                    rl_wallet_discount.setVisibility(View.VISIBLE);
                    tv_wallet_discount.setText("- " + pound.concat("" + tota_lwallet_applied));
                    totalAmountPay = Double.parseDouble(String.format("%.2f", (totalAmountPay - aa)));
                    subTotal = Double.parseDouble(String.format("%.2f", (subTotal - aa)));
                    total_wallet_money_available = String.valueOf(bb - aa);
                    walletamount.setText(SplashScreenActivity.currency_symbol + total_wallet_money_available);
                    tvTotalPay.setText(pound.concat(String.format("%.2f", totalAmountPay)));
                    tvSubtotal.setText(pound.concat(String.format("%.2f", subTotal)));
                    dialogwallet.dismiss();
                }
            }
        });
        dialogwallet.show();
    }

    private void HitUrlforWalletAmount() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.WalletMoneyAmount, response -> {
            Log.e("ressponsee", "" + response);
            pDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                int success = jObj.getInt("success");
                if (success == 0) {
                    total_wallet_money_available = jObj.optString("Total_wallet_money_available");
                    walletbalance.setText(SplashScreenActivity.currency_symbol + total_wallet_money_available);


                    String success_msg = jObj.getString("success_msg");
                    //

                } else {
                    String success_msg = jObj.getString("success_msg");
                    //                        showCustomDialog1decline (success_msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show()) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customerId);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    public void showRedeemVouchers() {
        dialogredeemvouchers = new Dialog(this);
        dialogredeemvouchers.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogredeemvouchers.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogredeemvouchers.setContentView(R.layout.custom_redeemvouchers);
        dialogredeemvouchers.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogredeemvouchers.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        dialogredeemvouchers.setCanceledOnTouchOutside(true);
        Button btnCancel = dialogredeemvouchers.findViewById(R.id.btn_cancel);
        Button pay = dialogredeemvouchers.findViewById(R.id.pay);
        giftamount = dialogredeemvouchers.findViewById(R.id.amount);
        giftbalance = dialogredeemvouchers.findViewById(R.id.giftbalance);
        TextView txttpay = dialogredeemvouchers.findViewById(R.id.txttpay);
        txttpay.setText("Total Pay : " + pound.concat(String.format("%.2f", totalAmountPay)));
        authPreference = new AuthPreference(this);

        if (flag_to_check_for_gift == 0) {
            HitUrlForGiftMoneyAvailable();
            flag_to_check_for_gift = 1;
        } else {
            giftbalance.setText(SplashScreenActivity.currency_symbol + total_gift_card_money_available);
        }


        btnCancel.setOnClickListener(v -> dialogredeemvouchers.dismiss());
        pay.setOnClickListener(v -> {

            if (giftamount.getText().toString().equals("")) {
                giftamount.setError("Enter amount");
                giftamount.requestFocus();
            } else {
                double aa = Double.parseDouble(giftamount.getText().toString());
                double bb = Double.parseDouble(total_gift_card_money_available);
                if (aa > bb) {
                    giftamount.setError("Sorry you don't have that much amount");
                    giftamount.requestFocus();
                } else if (aa > totalAmountPay) {
                    giftamount.setError("Your grand total is less than you entered amount");
                    giftamount.requestFocus();
                } else {
                    total_gift_applied = total_gift_applied + aa;
                    rl_gift_discount.setVisibility(View.VISIBLE);
                    tv_gift_discount.setText("- " + pound.concat("" + total_gift_applied));
                    totalAmountPay = Double.parseDouble(String.format("%.2f", (totalAmountPay - aa)));
                    subTotal = Double.parseDouble(String.format("%.2f", (subTotal - aa)));
                    total_wallet_money_available = String.valueOf(bb - aa);
                    giftbalance.setText(SplashScreenActivity.currency_symbol + total_gift_card_money_available);
                    tvTotalPay.setText(pound.concat(String.format("%.2f", totalAmountPay)));
                    tvSubtotal.setText(pound.concat(String.format("%.2f", subTotal)));
                    dialogredeemvouchers.dismiss();


                }
            }
        });
        dialogredeemvouchers.show();

    }

    public void HitUrlForGiftMoneyAvailable() {
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GiftCardMoneyAvailable, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("ressponse", response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success = jsonObj.getInt("success");
                    if (success == 0) {
                        total_gift_card_money_available = jsonObj.optString("Total_gift_card_money_available");
                        giftbalance.setText(SplashScreenActivity.currency_symbol + total_gift_card_money_available);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> showCustomDialog1decline(error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customerId);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void showCoupon() {
        dialogCoupon = new Dialog(this);
        dialogCoupon.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCoupon.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogCoupon.setContentView(R.layout.custom_coupon_pop_up);
        dialogCoupon.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogCoupon.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        dialogCoupon.setCanceledOnTouchOutside(true);
        Button btnCancel = dialogCoupon.findViewById(R.id.btn_cancel);
        Button btnApply = dialogCoupon.findViewById(R.id.btn_apply);
        edtCoupon = dialogCoupon.findViewById(R.id.etCoupan);
        txtError = dialogCoupon.findViewById(R.id.txtError);
        layoutCoupon = dialogCoupon.findViewById(R.id.layoutCoupon);

        btnCancel.setOnClickListener(v -> dialogCoupon.dismiss());
        btnApply.setOnClickListener(v -> {
            if (!ConnectionDetector.networkStatus(getApplicationContext())) {
                dialogCoupon.show();
            } else {
                checkCouponCode();
            }
        });
        dialogCoupon.show();
    }

    public void showRedeempoints() {
        dialogloyalitypoints = new Dialog(this);
        dialogloyalitypoints.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogloyalitypoints.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogloyalitypoints.setContentView(R.layout.custom_redeempoints_pop_up);
        dialogloyalitypoints.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogloyalitypoints.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        dialogloyalitypoints.setCanceledOnTouchOutside(true);
        Button Redeem = (Button) dialogloyalitypoints.findViewById(R.id.Redeem);
        Button cancel = (Button) dialogloyalitypoints.findViewById(R.id.cancel);

        /////////loyality points recycler///////////////////
        loyalitypointsList = new ArrayList<>();
        loyalitypointsList.add("100 points");
        loyalitypointsList.add("200 points");
        loyalitypointsList.add("300 points");
        loyalitypointsList.add("400 points");
        loyalitypointsList.add("500 points");

        loyalitypointsrecycler = dialogloyalitypoints.findViewById(R.id.loyalitypointsrecycler);
        loyalitypointsrecycler.setLayoutManager(new LinearLayoutManager(this));
        loyalitypointAdapter = new LoyalitypointAdapter(this, loyalitypointsList);
        loyalitypointsrecycler.setAdapter(loyalitypointAdapter);

        cancel.setOnClickListener(v -> dialogloyalitypoints.dismiss());
        Redeem.setOnClickListener(v -> {
            if (!ConnectionDetector.networkStatus(getApplicationContext())) {
                dialogloyalitypoints.show();
            } else {
                HitUrlforRadeemLoyalityPoints();

            }
        });
        dialogloyalitypoints.show();
    }

    public void checkCouponCode() {
        coupon = edtCoupon.getText().toString();
        if (coupon.length() == 0) {
            Animation shake = AnimationUtils.loadAnimation(PaymentActivity.this, R.anim.shake_animation);
            layoutCoupon.startAnimation(shake);
            txtError.setVisibility(View.GONE);
            showCustomDialog1decline("Please enter coupon code");
        } else {
            getCouponCode();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    public void HitURLforGroupList() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(true);
        groupList = new ArrayList<Citymodel>();
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GroupList, response -> {
            Log.d("ressponse", response);
            pDialog.dismiss();
            try {
                JSONObject jsonObj = new JSONObject(response);
                JSONArray groulist = jsonObj.getJSONArray("GroupListList");
                for (int i = 0; i < groulist.length(); i++) {
                    JSONObject c = groulist.getJSONObject(i);

                    String id = c.getString("id");
                    GROUPid = c.getString("group_member_id");
                    String customer_group_name = c.getString("customer_group_name");
                    String error = c.getString("error");

                    Citymodel citymodel = new Citymodel();
                    citymodel.setCity_name(customer_group_name);
                    citymodel.setId(GROUPid);


                    groupList.add(citymodel);
                    Log.e("groupid", GROUPid);
                    GroupAdapter groupAdapter = new GroupAdapter(PaymentActivity.this, groupList);
                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
                    grouprecycler.setLayoutManager(horizontalLayoutManager1);
                    grouprecycler.setAdapter(groupAdapter);
                    pDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> showCustomDialog1decline(error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customerId);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

        Context context;
        private ArrayList<Citymodel> groupList;
        private int lastSelectedPosition = -1;

        public GroupAdapter(Context c, ArrayList<Citymodel> groupList) {
            this.context = c;
            this.groupList = groupList;
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

            citymodel = groupList.get(position);
            holder.tv_city_name.setText(groupList.get(position).getCity_name());
            holder.citychecked.setOnClickListener(v -> {
                holder.citychecked.setChecked(true);

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    group_member_id_to_send = groupList.get(position).getId();
                    lastSelectedPosition = position;
                    notifyDataSetChanged();
                    groupnamep.setText(holder.tv_city_name.getText());
                    groupdialog.dismiss();
                    pDialog.dismiss();
                }, 1000);
            });
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_city_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;
            RadioGroup citygroup;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_city_name = itemView.findViewById(R.id.tv_city_name);
                cuisinesLayout = itemView.findViewById(R.id.cuisines_layout);
                citychecked = (RadioButton) itemView.findViewById(R.id.citychecked);
                citygroup = (RadioGroup) itemView.findViewById(R.id.citygroup);


            }
        }
    }

    public void HitUrlforRadeemLoyalityPoints() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.RadeemLoyalityPoints, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success = jsonObj.getInt("success");
                    if (success == 0) {
                        total_loyalty_amount = jsonObj.optString("Total_Loyalty_amount");
                        rademmloyality.setVisibility(View.VISIBLE);
                        totalloyalityamount.setText("- " + SplashScreenActivity.currency_symbol + " " + total_loyalty_amount);
                        subTotal = subTotal - Double.parseDouble(total_loyalty_amount);
                        tvSubtotal.setText(("+").concat(" ").concat(pound).concat(String.format("%.2f", subTotal)));

                        totalAmountPay = totalAmountPay - Double.parseDouble(total_loyalty_amount);
                        tvTotalPay.setText(pound.concat(String.format("%.2f", totalAmountPay)));
                        dialogloyalitypoints.dismiss();

                    } else if (success == 1) {
                        String success_msg = jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg);
                        dialogloyalitypoints.dismiss();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialogloyalitypoints.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage());
                dialogloyalitypoints.dismiss();
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customerId);
                params.put("loyltPnts", Selectloyalitypoints);
                params.put("TotalFoodCostAmount", updateTotalPrice);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void getCouponCode() {
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        // pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        // showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.COUPON_CODE_URL, response -> {
            pDialog.dismiss();
            Animation shake = AnimationUtils.loadAnimation(PaymentActivity.this, R.anim.shake_animation);
            layoutCoupon.startAnimation(shake);
            txtError.setVisibility(View.VISIBLE);
            txtError.setText("Sorry ! Coupon Code is invalid try again");
            edtCoupon.getText().clear();
            try {
                JSONObject jsonObj = new JSONObject(response);
                int error = jsonObj.getInt("error");
                if (error == 0) {
                    offerPrice = jsonObj.optDouble("CouponCodePrice");
                    String couponCode = jsonObj.optString("CouponCode");
                    String couponCodeType = jsonObj.optString("couponCodeType");
                    Double finalTot = subTotal - offerPrice;
                    tvTotalPay.setText(String.format("%.2f", finalTot));
                    authPreference.getCustomerAddressId();
                    authPreference.setCouponCode(couponCode);
                    authPreference.setCouponCodeType(couponCodeType);
                    authPreference.setCouponCodePrice(String.valueOf(offerPrice));
                    tvEnterCouponCode.setVisibility(View.GONE);
                    rlCouponPrice.setVisibility(View.VISIBLE);
                    tvCouponPrice.setText("-".concat(" ").concat(String.format("%.2f", offerPrice)));
                    tvCouponCode.setText(couponCode);

                    dialogCoupon.dismiss();

                } else if (error == 2) {
                    String error_msg = jsonObj.getString("error_msg");
                    showCustomDialog1decline(error_msg);

                } else if (error == 3) {
                    String error_msg = jsonObj.getString("error_msg");
                    showCustomDialog1decline(error_msg);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("resid", id);
                params.put("subTotal", String.format("%.2f", subTotal));
                params.put("CouponCode", coupon);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void paymentSubmit() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_PaymentModel> userLoginCall = interfaceRetrofit.phpexpert_payment_android_submit(itemIdList + "", itemIdList + "", itemQuantityList + "", deal_itemIdList + "", deal_orderPriceList + "",               deal_itemQuantityList + "", orderPriceList + "", orderSizeItemId + "", orderExtraItemId + "", extraItemIDName1 + "", id, customerId, SelectAddressActivity.address_id,                paymentType, String.format("%.2f", totalAmountPay), String.format("%.2f", subTotal) + "", date, time, "", deliveryChargeValue, group_member_id_to_send, authPreference.getCouponCode(), authPreference.getCouponCodePrice(),                authPreference.getCouponCodeType(), salesTaxAmount, orderType, "", "", "", "", disPrice, "", serviceFees, "", "", "",                "", packageFees, "", "", "", "" + CartActivity.preorderTime, vatTax, updateTotalPrice + "", "" + total_gift_applied                , "" + tota_lwallet_applied, payment_transaction_paypal, myPref.getState(), myPref.getPickupAdd(), CartActivity.tableid                , "" + myPref.getLatitude(), "" + myPref.getLongitude(), Selectloyalitypoints);
        userLoginCall.enqueue(new Callback<Model_PaymentModel>() {
            @Override
            public void onResponse(Call<Model_PaymentModel> call, retrofit2.Response<Model_PaymentModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        authPreference.setOrderNo(response.body().getOrder_identifyno());
                        database.delete();
                        database.deal_delete();
                        Intent intent = new Intent(PaymentActivity.this, ThankYouActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showCustomDialog1decline(response.body().getError_msg());
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_PaymentModel> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });
    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (orderType.equalsIgnoreCase("Collection") || orderType.equalsIgnoreCase("Pre Order Collection")) {
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("itemId", itemId);
            intent.putExtra("restaurantName", restaurantName);
            intent.putExtra("restaurantAddress", restaurantAddress);
//                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
            startActivity(intent);
            finish();
        } else if (orderType.equalsIgnoreCase("Delivery") || orderType.equalsIgnoreCase("Pre Order Delivery")) {
            Intent intent = new Intent(this, SelectAddressActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("addressId", addressId);
            intent.putExtra("itemId", itemId);
            intent.putExtra("restaurantName", restaurantName);
            intent.putExtra("restaurantAddress", restaurantAddress);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            intent.putExtra("disPrice", disPrice);
            intent.putExtra("deliveryChargeValue", deliveryChargeValue);
            intent.putExtra("serviceFees", serviceFees);
            intent.putExtra("packageFees", packageFees);
            intent.putExtra("salesTaxAmount", salesTaxAmount);
            intent.putExtra("vatTax", vatTax);
            intent.putExtra("orderType", orderType);
            intent.putExtra("updateTotalPrice", updateTotalPrice);
            intent.putExtra("addressId", addressId);

//                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
            startActivity(intent);
            finish();
        }
        finish();
    }

    public class PaymentRecycler extends RecyclerView.Adapter<PaymentRecycler.VIewHolder> {
        Context context;
        ArrayList<RaviCartModle> rr;

        public PaymentRecycler(Context c, ArrayList<RaviCartModle> r) {
            this.context = c;
            this.rr = r;
        }

        @NonNull
        @Override
        public VIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vv = layoutInflater.inflate(R.layout.single_payment_cart_item, parent, false);
            return new VIewHolder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull VIewHolder holder, int position) {

            if (rr.get(position).getSize_item_name().equals("0") && rr.get(position).getExtra_item_name().equals("0")) {
                holder.tvItemName.setText(rr.get(position).getItem_name());
                holder.tv_item_name_extra.setVisibility(View.GONE);
            } else if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                holder.tvItemName.setText(rr.get(position).getItem_name() + " (" + rr.get(position).getSize_item_name() + ")");
                holder.tv_item_name_extra.setVisibility(View.GONE);
            } else if (rr.get(position).getSize_item_name().equals("0") && rr.get(position).getExtra_item_name() != "0") {
                holder.tvItemName.setText(rr.get(position).getItem_name());
                holder.tv_item_name_extra.setVisibility(View.VISIBLE);
                String a = rr.get(position).getExtra_item_name().replace("[", "");
                a = a.replace("]", "");
                a = a.replace(",", "+");
                holder.tv_item_name_extra.setText(a);
            } else if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name() != "0") {
                holder.tvItemName.setText(rr.get(position).getItem_name() + " (" + rr.get(position).getSize_item_name() + ")");
                holder.tv_item_name_extra.setVisibility(View.VISIBLE);
                String a = rr.get(position).getExtra_item_name().replace("[", "");
                a = a.replace("]", "");
                a = a.replace(",", "+");
                holder.tv_item_name_extra.setText(a);
            }
            double pp = Double.parseDouble(rr.get(position).getPrice());
            holder.tvItemPrice.setText(pound.concat(String.format("%.2f", pp)));
            holder.tvItemQuantity.setText("" + (rr.get(position).getItem_quantity()) + " X");
        }

        @Override
        public int getItemCount() {
            return rr.size();
        }

        public class VIewHolder extends RecyclerView.ViewHolder {
            private TextView tvItemQuantity, tvItemName, tvItemPrice, tv_item_name_extra;

            public VIewHolder(View itemView) {
                super(itemView);
                tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
                tvItemName = itemView.findViewById(R.id.tv_item_name);
                tvItemPrice = itemView.findViewById(R.id.tv_item_price);
                tv_item_name_extra = itemView.findViewById(R.id.tv_item_name_extra);
            }
        }
    }

    public class deal_PaymentRecycler extends RecyclerView.Adapter<deal_PaymentRecycler.VIewHolder> {
        Context context;
        ArrayList<Deal_RaviCartModle> rr;

        public deal_PaymentRecycler(Context c, ArrayList<Deal_RaviCartModle> r) {
            this.context = c;
            this.rr = r;
        }

        @NonNull
        @Override
        public VIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vv = layoutInflater.inflate(R.layout.single_payment_cart_item, parent, false);
            return new VIewHolder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull VIewHolder holder, int position) {


            holder.tvItemName.setText(rr.get(position).getItem_name());
            holder.tv_item_name_extra.setVisibility(View.GONE);

            double pp = Double.parseDouble(rr.get(position).getPrice());
            holder.tvItemPrice.setText(pound.concat(String.format("%.2f", pp)));
            holder.tvItemQuantity.setText("" + (rr.get(position).getItem_quantity()) + " X");
        }

        @Override
        public int getItemCount() {
            return rr.size();
        }

        public class VIewHolder extends RecyclerView.ViewHolder {
            private TextView tvItemQuantity, tvItemName, tvItemPrice, tv_item_name_extra;

            public VIewHolder(View itemView) {
                super(itemView);
                tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
                tvItemName = itemView.findViewById(R.id.tv_item_name);
                tvItemPrice = itemView.findViewById(R.id.tv_item_price);
                tv_item_name_extra = itemView.findViewById(R.id.tv_item_name_extra);
            }
        }
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this).create();
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

    public class LoyalitypointAdapter extends RecyclerView.Adapter<LoyalitypointAdapter.ViewHolder> {

        private List<String> loyalitypointsList;
        private LayoutInflater mInflater;
        private int lastSelectedPosition = -1;


        // data is passed into the constructor
        LoyalitypointAdapter(Context context, List<String> loyalitypointsList) {
            this.mInflater = LayoutInflater.from(context);
            this.loyalitypointsList = loyalitypointsList;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.loyalitypointslist, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            String cc = loyalitypointsList.get(position);
            holder.loyaltiyypointsname.setText(cc);
            holder.loyalitychecked.setChecked(lastSelectedPosition == position);


            holder.loyalitychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastSelectedPosition = position;
                    notifyDataSetChanged();


                    Selectloyalitypoints = holder.loyaltiyypointsname.getText().toString();
//                    Toast.makeText(getApplicationContext(), holder.loyaltiyypointsname.getText(), Toast.LENGTH_LONG).show();
                }
            });
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return loyalitypointsList.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView loyaltiyypointsname;
            RadioButton loyalitychecked;

            ViewHolder(View itemView) {
                super(itemView);
                loyaltiyypointsname = itemView.findViewById(R.id.loyaltiyypointsname);
                loyalitychecked = itemView.findViewById(R.id.loyalitychecked);

            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);


                        try {

                            JSONObject jsonDetails = new JSONObject(paymentDetails);
                            JSONObject jj = jsonDetails.getJSONObject("response");
                            payment_transaction_paypal = jj.getString("id");

                            paymentSubmit();
                            //showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
                        } catch (JSONException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

        if (requestCode == PAYMENT) {
            if (resultCode == Activity.RESULT_OK)
                paymentSubmit();
        }
    }
}