package com.justfoodz.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.adapters.MenuSubItemAdapter;
import com.justfoodz.Database.Database;

import com.justfoodz.models.ExtrasModel;
import com.justfoodz.models.MenuSizeModel;
import com.justfoodz.models.SubMenuModel;
import com.justfoodz.models.SubMenumodelravi;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class SubMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvSubmenuList;
    private MenuSubItemAdapter menuSubItemAdapter;
    private ProgressDialog pDialog;
    private ImageView ivBack;
    private RelativeLayout rlCart;
    public ArrayList<SubMenuModel> subMenuArrayList;
    public static SubMenuModel subMenuModel;
    private AuthPreference authPreference;
    private int restaurantCategoryId;
    public static String id, categoryName, restaurantAddress, restaurantName, categoryImage;
    public static TextView tvMenuItemName, tvCartItemCount;
    public static List<SubMenuModel> subMenuCartList = new ArrayList<>();
    int subMenuItemId;
    public String extraAvailable = "", sizeAvailable = "", menuId;
    private ArrayList<MenuSizeModel> menuSizeModelArrayList;
    private ArrayList<ExtrasModel> extrasModelArrayList;
    private ArrayList<SubMenumodelravi> subMenumodelravis;
    Database database;
    String currentDateTimeString;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SubMenuActivity.tvCartItemCount.setText("" + Ravifinalitem.cart_Item_number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);
        statusBarColor();
        initialization();
        initializedListener();
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        if (Network.isNetworkCheck(this)) {
            subMenuList();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        tvMenuItemName = findViewById(R.id.tv_menu_item_name);
        tvCartItemCount = findViewById(R.id.tv_cart_item_count);

        rlCart = findViewById(R.id.rl_cart);
        rvSubmenuList = findViewById(R.id.rv_submenu_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvSubmenuList.setLayoutManager(mLayoutManager);
        rvSubmenuList.setItemAnimator(new DefaultItemAnimator());
        authPreference = new AuthPreference(this);
        id = getIntent().getStringExtra("id");//resId
        restaurantCategoryId = getIntent().getIntExtra("restaurantCategoryId", 1);//MainMenu item ID
        restaurantName = getIntent().getStringExtra("restaurantName");
        restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        categoryName = getIntent().getStringExtra("categoryName");
        subMenuItemId = getIntent().getIntExtra("subMenuItemId", 1);
        extraAvailable = getIntent().getStringExtra("extraAvailable");
        sizeAvailable = getIntent().getStringExtra("sizeAvailable");
        extrasModelArrayList = (ArrayList<ExtrasModel>) getIntent().getSerializableExtra("extrasModelArrayList");
        menuSizeModelArrayList = (ArrayList<MenuSizeModel>) getIntent().getSerializableExtra("menuSizeModelArrayList");
        tvMenuItemName.setText(categoryName);
        subMenumodelravis = new ArrayList<>();
        database = new Database(SubMenuActivity.this);


    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        rlCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intents = new Intent();
                intents.putExtra("listSize", "0");
                setResult(Activity.RESULT_OK, intents);
                finish();
                break;
            case R.id.rl_cart:

                SQLiteDatabase db = database.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM item_table", null);
                Cursor cursor1 = db.rawQuery("SELECT * FROM deal_item_table", null);
                if (cursor.moveToNext() || cursor1.moveToNext()) {
                    Intent intent = new Intent(SubMenuActivity.this, CartActivity.class);
                    intent.putExtra("restaurantAddress", SubMenuActivity.restaurantAddress);
                    intent.putExtra("restaurantName", SubMenuActivity.restaurantName);
                    intent.putExtra("id", SubMenuActivity.id);
                    intent.putExtra("restaurantCategoryId", authPreference.getRestaurantCategoryID());
                    startActivity(intent);
                } else {
                    showCustomDialog1decline("Cart is empty,please add item in cart.");
                }
                break;
            default:
                break;
        }
    }


    public void subMenuList() {

        subMenuArrayList = new ArrayList<SubMenuModel>();
        subMenumodelravis.clear();
        subMenumodelravis=new ArrayList<>();
        MyProgressDialog.show(SubMenuActivity.this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_RestaurantMenuItem> userLoginCall = interfaceRetrofit.phpexpert_restaurantMenuItem(id, String.valueOf(restaurantCategoryId), currentDateTimeString);
        userLoginCall.enqueue(new Callback<Model_RestaurantMenuItem>() {
            @Override
            public void onResponse(Call<Model_RestaurantMenuItem> call, retrofit2.Response<Model_RestaurantMenuItem> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getRestaurantMenItems().size(); i++) {

                        SubMenumodelravi subMenumodelravi = response.body().getRestaurantMenItems().get(i);

                        authPreference.setItemId(subMenumodelravi.getId());
                        subMenumodelravis.add(subMenumodelravi);
                    }

                    if (subMenumodelravis.size()>0) {
                        menuSubItemAdapter = new MenuSubItemAdapter(SubMenuActivity.this, subMenumodelravis);
                        rvSubmenuList.setAdapter(menuSubItemAdapter);
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_RestaurantMenuItem> call, Throwable t) {
                Toast.makeText(SubMenuActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });

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

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(SubMenuActivity.this).create();
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