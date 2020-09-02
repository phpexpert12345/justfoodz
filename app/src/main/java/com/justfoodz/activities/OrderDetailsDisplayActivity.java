package com.justfoodz.activities;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.justfoodz.adapters.ViewOrderDetailCartAdapter;

import com.justfoodz.models.OrderFoodItemModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvOrderDate, tvOrderTime, tvOrderNo, tvRestaurantName, tvRestaurantAddress,
            tvOrderType, tvPaymentMethod, tvTotalFoodCost, tvTotalDiscount, tvSubtotal,
            tvServiceFees, tvPackageFees, tvSalesTaxAmount, tvVatTaxAmount, tvDeliveryCost,
            tvGrandTotal, tvCouponPrice, tvOrderDisplayMessage,tv_wallet,tv_gift;
    private RecyclerView rvOrderDetailList;
    private String orderIdentifyNo, date, time, orderPrice, restaurantAddress, restaurantName, orderType, paymentMode, orderDisplayMessage;
    private String pound = SplashScreenActivity.currency_symbol;
    private AuthPreference authPreference;
    private ViewOrderDetailCartAdapter viewOrderDetailCartAdapter;
    private ScrollView scrollView;
    private ProgressDialog pDialog;
    private ArrayList<OrderFoodItemModel> orderFoodItemModelArrayList = new ArrayList<OrderFoodItemModel>();
    private OrderFoodItemModel orderFoodItemModel;
    private RelativeLayout rlFoodCost, rlTotalDiscount, rlSubTotal, rlServiceFees, rlPackageFees, rlSalesTaxAmount, rlVatTax, rlDeliveryCost, rlCouponPrice;
    private LinearLayout orderDetailLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        statusBarColor();
        initialization();
        initializedListener();

        if (Network.isNetworkCheck(this)) {
            getOrderDetailsDisplay();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void initialization() {

        ivBack = findViewById(R.id.iv_back);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvOrderTime = findViewById(R.id.tv_order_time);
        tvOrderNo = findViewById(R.id.tv_order_no);
        tvRestaurantName = findViewById(R.id.tv_restaurant_name);
        tvRestaurantAddress = findViewById(R.id.tv_restaurant_address);
        tvOrderType = findViewById(R.id.tv_order_type);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvOrderDisplayMessage = findViewById(R.id.tv_orderDisplayMessage);
        scrollView = findViewById(R.id.scrollView);
        rvOrderDetailList = findViewById(R.id.rv_order_detail_list);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_gift = findViewById(R.id.tv_gift);



//Parent view of all costs
//-----------------------------------------------------------------------//
        rlFoodCost = findViewById(R.id.rl_food_cost);
        rlTotalDiscount = findViewById(R.id.rl_total_discount);
        rlSubTotal = findViewById(R.id.rl_sub_total);
        rlServiceFees = findViewById(R.id.rl_service_fees);
        rlPackageFees = findViewById(R.id.rl_package_fees);
        rlSalesTaxAmount = findViewById(R.id.rl_sales_tax_amount);
        rlVatTax = findViewById(R.id.rl_vat_tax);
        rlDeliveryCost = findViewById(R.id.rl_delivery_cost);
        rlCouponPrice = findViewById(R.id.rl_coupon_price);
        orderDetailLayout = findViewById(R.id.order_detail_layout);
        //-----------------------------------------------------------------------//

        //Different Cost
        tvTotalFoodCost = findViewById(R.id.tv_total_food_cost);
        tvTotalDiscount = findViewById(R.id.tv_total_discount);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvServiceFees = findViewById(R.id.tv_service_fees);
        tvPackageFees = findViewById(R.id.tv_package_fees);
        tvSalesTaxAmount = findViewById(R.id.tv_sales_tax_amount);
        tvVatTaxAmount = findViewById(R.id.tv_vat_tax_amount);
        tvDeliveryCost = findViewById(R.id.tv_delivery_cost);
        tvCouponPrice = findViewById(R.id.tv_coupon_price);
        tvGrandTotal = findViewById(R.id.tv_grand_total);


        //-------------------------------------------------------------------------//

        authPreference = new AuthPreference(this);
        try {

            orderIdentifyNo = getIntent().getStringExtra("orderIdentifyNo");
            //RecyclerView for Cart List

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rvOrderDetailList.setLayoutManager(mLayoutManager);
            rvOrderDetailList.setItemAnimator(new DefaultItemAnimator());
//             viewOrderDetailCartAdapter = new ViewOrderDetailCartAdapter(this, subMenuModelArrayList);
            rvOrderDetailList.setAdapter(viewOrderDetailCartAdapter);
            viewOrderDetailCartAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
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


    private void getOrderDetailsDisplay() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        orderFoodItemModelArrayList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ORDER_DETAILS_DISPLAY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();

                Log.e("tv_view_details",""+response);
                try {
                    JSONObject obj = new JSONObject(response);
                    orderDetailLayout.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = obj.getJSONArray("OrderDetailItem");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int success = jsonObject.optInt("success");
                        JSONObject jsonOrders = jsonObject.getJSONObject("orders");
                        JSONArray jsonArrayOrderView = jsonOrders.getJSONArray("OrderViewResult");
                        for (int j = 0; j < jsonArrayOrderView.length(); j++) {
                            JSONObject OrderViewResult = jsonArrayOrderView.getJSONObject(i);
                            String orderIdentifyNo = OrderViewResult.optString("order_identifyno");
                            String orderTime = OrderViewResult.optString("order_time");
                            String restaurantId = OrderViewResult.optString("restaurant_id");
                            String ordPrice = OrderViewResult.optString("ordPrice");
                            String foodCosts = OrderViewResult.optString("FoodCosts");
                            String orderType = OrderViewResult.optString("order_type");
                            String paymentMode = OrderViewResult.optString("payment_mode");
                            String restaurantName = OrderViewResult.optString("restaurant_name");
                            String restaurantAddress = OrderViewResult.optString("restaurant_address");
                            String orderDisplayMessage = OrderViewResult.optString("order_display_message");
                            String orderDate = OrderViewResult.optString("order_date");


                            tvOrderNo.setText(orderIdentifyNo);
                            tvRestaurantAddress.setText(restaurantAddress);
                            tvRestaurantName.setText(restaurantName);
                            tvOrderDate.setText(orderDate);
                            tvOrderTime.setText(orderTime);
                            tvOrderType.setText(orderType);
                            tvPaymentMethod.setText(paymentMode);
                            tvOrderDisplayMessage.setText(orderDisplayMessage);
                            tvTotalFoodCost.setText(pound.concat(foodCosts));
                            tvGrandTotal.setText(pound.concat(ordPrice));


                        }

                        String orderIdentifyNo = jsonObject.optString("order_identifyno");
                        String orderNumber = jsonObject.optString("OrderNumber");
                        String orderType = jsonObject.optString("OrderType");
                        String status = jsonObject.optString("status");
                        String orderStatusNo = jsonObject.optString("orderStatusNo");
                        String orderPickupStatus = jsonObject.optString("orderPickupStatus");
                        String orderClose = jsonObject.optString("order_close");
                        String orderCloseMessage = jsonObject.optString("order_closeMessage");
                        String prevOrdersFromCustomer = jsonObject.optString("PrevOrdersFromCustomer");
                        String payOptionStatus = jsonObject.optString("PayOptionStatus");
                        String rewardPoint = jsonObject.optString("rewardpoint");
                        String requestAtDate = jsonObject.optString("RequestAtDate");
                        String requestAtTime = jsonObject.optString("RequestAtTime");
                        String requestForDate = jsonObject.optString("RequestforDate");
                        String requestForTime = jsonObject.optString("RequestforTime");
                        String OrderAcceptedDate = jsonObject.optString("OrderAcceptedDate");
                        String orderAcceptedTime = jsonObject.optString("OrderAcceptedTime");
                        String orderId = jsonObject.optString("orderid");
                        String paymentMethod = jsonObject.optString("PaymentMethod");
                        String nameCustomer = jsonObject.optString("name_customer");
                        String customerAddress = jsonObject.optString("customer_address");
                        String customerNewAddress = jsonObject.optString("customer_NewAddress");
                        String customerCity = jsonObject.optString("customer_city");
                        String customerZipCode = jsonObject.optString("customer_zipcode");
                        String customerPhone = jsonObject.optString("customer_phone");
                        String customerEmail = jsonObject.optString("customer_email");
                        String customerInstruction = jsonObject.optString("customer_instruction");
                        String resId = jsonObject.optString("resid");
                        String restaurantName = jsonObject.optString("restaurant_name");
                        String restaurantAddress = jsonObject.optString("restaurant_address");
                        String companyName = jsonObject.optString("CompanyName");
                        String companyAddress = jsonObject.optString("CompanyAddress");
                        String companyCity = jsonObject.optString("CompanyCity");
                        String companyPostcode = jsonObject.optString("CompanyPostcode");
                        String companyMobile = jsonObject.optString("CompanyMobile");
                        String subTotal = jsonObject.optString("subTotal");
                        tvSubtotal.setText(pound.concat(subTotal));
                        String deliveryCharge = jsonObject.optString("DeliveryCharge");//I need this price if user select delivery charge
                        String packageFees = jsonObject.optString("PackageFees");//I need this price
                        String packageFeesType = jsonObject.optString("PackageFeesType");
                        String serviceFees = jsonObject.optString("ServiceFees");//I need this price
                        String serviceFeesType = jsonObject.optString("ServiceFeesType");
                        String couponPrice = jsonObject.optString("CouponPrice");//I need this price if user select coupon code
                        String couponType = jsonObject.optString("CouponType");
                        String discountPrice = jsonObject.optString("DiscountPrice");
                        tvTotalDiscount.setText(("-").concat(pound).concat(discountPrice));
                        String discountDiscription = jsonObject.optString("DiscountDiscription");
                        String DiscountType = jsonObject.optString("DiscountType");
                        String payByLoyality = jsonObject.optString("PayByLoyality");
                        String vatTax = jsonObject.optString("VatTax");//I need this price
                        String extraTipAddAmount = jsonObject.optString("extraTipAddAmount");
                        String salesTaxAmount = jsonObject.optString("SalesTaxAmount"); //I need this price
                        String orderPrice = jsonObject.optString("OrderPrice");
                        String currency = jsonObject.optString("currency");
                        String WalletPay = jsonObject.optString("WalletPay");
                        String GiftCardPay = jsonObject.optString("GiftCardPay");

                        tv_wallet.setText("- "+pound.concat(WalletPay));
                        tv_gift.setText("- "+pound.concat(GiftCardPay));

                        JSONArray orderFoodItemArray = obj.getJSONArray("OrderFoodItem");
                        if (orderFoodItemArray.length()>0) {
                            for (int k = 0; k < orderFoodItemArray.length(); k++) {
                                orderFoodItemModel = new OrderFoodItemModel();
                                JSONObject orderFoodItemObject = orderFoodItemArray.getJSONObject(k);

                                if (orderFoodItemObject.getString("item_size").equals("")) {
                                    orderFoodItemModel.setItemsName(orderFoodItemObject.optString("ItemsName"));
                                } else {
                                    orderFoodItemModel.setItemsName(orderFoodItemObject.optString("ItemsName") + "(" + orderFoodItemObject.optString("item_size") + ")");
                                }
                                int quantity = orderFoodItemObject.optInt("quantity");
                                String menuPrice = orderFoodItemObject.optString("menuprice");
                                String ExtraTopping = orderFoodItemObject.optString("ExtraTopping");

                                orderFoodItemModel.setQuantity(quantity);
                                orderFoodItemModel.setMenuprice(menuPrice);
                                orderFoodItemModel.setExtraTopping(ExtraTopping);
                                orderFoodItemModelArrayList.add(orderFoodItemModel);
                            }
                        }
                        /////meal///////////////
                        JSONArray orderMealItemArray = obj.getJSONArray("OrderMealItem");
                        if (orderMealItemArray.length()>0) {
                            for (int l = 0; l < orderMealItemArray.length(); l++) {
                                JSONObject orderMealItemObject = orderMealItemArray.getJSONObject(l);
                                orderFoodItemModel = new OrderFoodItemModel();
                                if (orderMealItemObject.optString("item_size").equals("")) {
                                    orderFoodItemModel.setItemsName(orderMealItemObject.optString("ItemsName"));
                                } else {
                                    orderFoodItemModel.setItemsName(orderMealItemObject.optString("ItemsName") + "(" + orderMealItemObject.optString("item_size") + ")");
                                }
                                int qquantity = orderMealItemObject.optInt("quantity");
                                String mmenuPrice = orderMealItemObject.optString("menuprice");
                                String eExtraTopping = orderMealItemObject.optString("ExtraTopping");
                                orderFoodItemModel.setQuantity(qquantity);
                                orderFoodItemModel.setMenuprice(mmenuPrice);
                                orderFoodItemModel.setExtraTopping(eExtraTopping);
                                orderFoodItemModelArrayList.add(orderFoodItemModel);
                            }
                        }
                         /////////////////////////////
                        viewOrderDetailCartAdapter = new ViewOrderDetailCartAdapter(OrderDetailsDisplayActivity.this, orderFoodItemModelArrayList);
                        rvOrderDetailList.setAdapter(viewOrderDetailCartAdapter);

                        if (serviceFees.equalsIgnoreCase("0") || serviceFees.equalsIgnoreCase("0.0") || serviceFees.equalsIgnoreCase("0.00") || serviceFees.equalsIgnoreCase("00.00")) {
                            rlServiceFees.setVisibility(View.GONE);
                        } else {
                            rlServiceFees.setVisibility(View.VISIBLE);
                            tvServiceFees.setText(("+").concat(" ").concat(pound.concat(serviceFees)));
                        }
                        if (packageFees.equalsIgnoreCase("0") || packageFees.equalsIgnoreCase("0.0") || packageFees.equalsIgnoreCase("0.00") || packageFees.equalsIgnoreCase("00.00")) {
                            rlPackageFees.setVisibility(View.GONE);
                        } else {
                            rlPackageFees.setVisibility(View.VISIBLE);
                            tvPackageFees.setText(("+").concat(" ").concat(pound.concat(packageFees)));
                        }
                        if (salesTaxAmount.equalsIgnoreCase("0") || salesTaxAmount.equalsIgnoreCase("0.0") || salesTaxAmount.equalsIgnoreCase("0.00") || salesTaxAmount.equalsIgnoreCase("00.00")) {
                            rlSalesTaxAmount.setVisibility(View.GONE);
                        } else {
                            rlSalesTaxAmount.setVisibility(View.VISIBLE);
                            tvSalesTaxAmount.setText(("+").concat(" ").concat(pound.concat(salesTaxAmount)));
                        }
                        if (vatTax.equalsIgnoreCase("0") || vatTax.equalsIgnoreCase("0.0") || vatTax.equalsIgnoreCase("0.00") || vatTax.equalsIgnoreCase("00.00")) {
                            rlVatTax.setVisibility(View.GONE);
                        } else {
                            rlVatTax.setVisibility(View.VISIBLE);
                            tvVatTaxAmount.setText(("+").concat(" ").concat(pound.concat(vatTax)));
                        }

                        if (couponPrice.equalsIgnoreCase("0") || couponPrice.equalsIgnoreCase("0.0") || couponPrice.equalsIgnoreCase("0.00") || couponPrice.equalsIgnoreCase("00.00")) {
                            rlCouponPrice.setVisibility(View.GONE);
                        } else {
                            rlCouponPrice.setVisibility(View.VISIBLE);
                            tvCouponPrice.setText(("-").concat(" ").concat(pound.concat(couponPrice)));
                        }

                        if (deliveryCharge.equalsIgnoreCase("0") || deliveryCharge.equalsIgnoreCase("0.0") || deliveryCharge.equalsIgnoreCase("0.00") || deliveryCharge.equalsIgnoreCase("00.00") || deliveryCharge == null) {
                            rlDeliveryCost.setVisibility(View.GONE);
                        } else {
                            rlDeliveryCost.setVisibility(View.VISIBLE);
                            tvDeliveryCost.setText(("+").concat(" ").concat(pound.concat(deliveryCharge)));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exx",""+e);
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                params.put("order_identifyno", orderIdentifyNo);
                Log.e("orderparams",""+params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }


    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
