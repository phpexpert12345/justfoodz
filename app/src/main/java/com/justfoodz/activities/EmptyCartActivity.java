package com.justfoodz.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.justfoodz.R;

import java.io.Serializable;


public class EmptyCartActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAdd;
    private ImageView ivBack;
    private TextView tvRestaurantName, tvRestaurantAddress;
    private String restaurantName, restaurantAddress;
    MainMenuActivity activity;
    private String id, restaurantCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_cart);
        initialization();
        initializedListener();
        statusBarColor();
       /* Intent i = getIntent();
        SubMenuModel subMenuModel = (SubMenuModel) i.getSerializableExtra("subMenuObject");*/


    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        tvRestaurantName = findViewById(R.id.tv_restaurant_name);
        tvRestaurantAddress = findViewById(R.id.tv_restaurant_address);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        restaurantName = getIntent().getStringExtra("restaurantName");
        restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        id = getIntent().getStringExtra("id");
        restaurantCategoryId = getIntent().getStringExtra("restaurantCategoryId");
        tvRestaurantName.setText(restaurantName);
        tvRestaurantAddress.setText(restaurantAddress);
        SubMenuActivity.subMenuCartList.clear();


    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent i = new Intent(this, MainMenuActivity.class);
                i.putExtra("restaurantModel", (Serializable) MainMenuActivity.restaurantModel);
                startActivity(i);
                finish();
                break;
            case R.id.btnAdd:
                Intent intent = new Intent(this, MainMenuActivity.class);
                intent.putExtra("restaurantModel", (Serializable) MainMenuActivity.restaurantModel);
                startActivity(intent);
                finish();
            default:
                break;
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
        Intent i = new Intent(this, MainMenuActivity.class);
        i.putExtra("restaurantModel", (Serializable) MainMenuActivity.restaurantModel);
        startActivity(i);
        finish();
    }
}
