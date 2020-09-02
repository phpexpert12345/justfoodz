package com.justfoodz.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.justfoodz.R;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThankYouActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGoHome;
    private WebView wbthan;
    private AuthPreference authPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        initialization();
        initializedListener();
        statusBarColor();
        if (Network.isNetworkCheck(this)) {
//            thankYouOrder();
            wbthan.loadUrl("https://www.justfoodz.com/OrderTracking_WebView.php?order_identifyno="+authPreference.getOrderNo());
            WebSettings webSettings = wbthan.getSettings();
            webSettings.setJavaScriptEnabled(true);

        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void initialization() {
        btnGoHome = findViewById(R.id.btn_go_home);
        wbthan = findViewById(R.id.wbthan);

        authPreference = new AuthPreference(this);



    }

    private void initializedListener() {
        btnGoHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_home:
                Intent intent = new Intent(this, HomeActivity.class);

                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

    }


//    private void thankYouOrder() {
//        pDialog = new ProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(true);
//        pDialog.show();
//        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.THANK_YOU_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                pDialog.dismiss();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    layout.setVisibility(View.VISIBLE);
//                    int success = jObj.getInt("success");
//                    if (success == 1) {
//
//                        String thankyouHeading = jObj.optString("ThankyouHeading");
//                        String yourOrderNumberIS = jObj.optString("YourOrderNumberIS");
//                        String preorderTime = jObj.optString("preorderTime");
//                        String orderTimeMsg = jObj.optString("OrderTime");
//                        String ThankyouImage = jObj.optString("ThankyouImage");
//                        String thankyouOrderMessage = jObj.optString("ThankyouOrderMessage");
//                        String DiscountMessageDisplay = jObj.optString("DiscountMessageDisplay");
//                        String thankyouHeadingFirstTitle = jObj.optString("");
//                        String thankyouHeadingSecondTitle = jObj.optString("");
//                        String thankyouHeadingSecondDescription = jObj.optString("");
//                        String thankyouHeadingSecond = jObj.optString("");
//                        String orderAcceptedAt = jObj.optString("");
//                        String orderPlacedAt = jObj.optString("");
//                        orderIdentifyno = jObj.optString("order_identifyno");
//                        orderDate = jObj.optString("order_date");
//                        orderTime = jObj.optString("order_time");
//                        String restaurantId = jObj.optString("restaurant_id");
//                        restaurantName = jObj.optString("restaurant_name");
//                        restaurantAddress = jObj.optString("restaurant_address");
//                        ordPrice = jObj.optString("ordPrice");
//                        String paymentMode = jObj.optString("payment_mode");
//                        String orderDisplayMessage = jObj.optString("order_display_message");
//                        String error = jObj.optString("error");
//                        tvTkyMsg.setText(thankyouHeading);
//                        tvOrderNumber.setText(yourOrderNumberIS);
//                        tvOrderPlacedTime.setText(orderTimeMsg);
//                        Picasso.get().load(ThankyouImage).into(ivThankYouLogo);
//                        tvOrderDescription.setText(thankyouOrderMessage);
//
//
//                    } else {
//                        String errorMsg = jObj.optString("error_msg");
//                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
//
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting params to customer login
//
//                Map<String, String> params = new HashMap<String, String>();
//                String orderNo = authPreference.getOrderNo();
//                params.put("order_identifyno", orderNo);
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(strReq);
//    }


    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.greenColor));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        SubMenuActivity.subMenuCartList.clear();
        startActivity(intent);
        finish();

    }

}
