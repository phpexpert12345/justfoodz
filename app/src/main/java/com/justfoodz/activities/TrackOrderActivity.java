package com.justfoodz.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.justfoodz.R;
import com.justfoodz.utils.AuthPreference;
import com.rengwuxian.materialedittext.MaterialEditText;

public class TrackOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private MaterialEditText edtOrderId;
    private String orderId;
    private ProgressDialog pDialog;
    private TextView tvTrackSubmit;
    private AuthPreference authPreference;
    private String restaurantName = "", restaurantAddress = "", orderDate = "", orderTime = "", ordPrice = "", orderIdentifyno = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        statusBarColor();
        initialization();
        initializedListener();
    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        tvTrackSubmit = findViewById(R.id.tv_track_submit);
        edtOrderId = findViewById(R.id.edt_order_id);
        restaurantName = getIntent().getStringExtra("restaurantAddress");
        restaurantAddress = getIntent().getStringExtra("restaurantName");
        orderDate = getIntent().getStringExtra("date");
        orderTime = getIntent().getStringExtra("time");
        ordPrice = getIntent().getStringExtra("orderPrice");
        orderIdentifyno = getIntent().getStringExtra("orderIdentifyno");
        authPreference = new AuthPreference(this);
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvTrackSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_track_submit:
                trackOrderValidation();
                break;
            default:
        }
    }

    private void trackOrderValidation() {
        orderId = edtOrderId.getText().toString().trim();
        if (orderId.isEmpty()) {
            edtOrderId.setError("Enter Order Number");
            edtOrderId.requestFocus();
        } else {
            Intent intent = new Intent(TrackOrderActivity.this, OrderTrackDetailActivity.class);
            intent.putExtra("orderIdentifyNo", orderId);
            intent.putExtra("restaurantAddress", restaurantAddress);
            intent.putExtra("restaurantName", restaurantName);
            intent.putExtra("date", orderDate);
            intent.putExtra("time", orderTime);
            intent.putExtra("orderPrice", ordPrice);
            startActivity(intent);
            finish();

        }
       /* } else if (Network.isNetworkCheck(this)) {
            trackOrderStatus();

        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

        }*/

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
}

