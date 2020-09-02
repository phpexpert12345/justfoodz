package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.R;
import com.justfoodz.fragments.InformationFragment;
import com.justfoodz.fragments.MenuFragment;
import com.justfoodz.Database.Database;

import com.justfoodz.fragments.Model_feature_restaurant;
import com.justfoodz.fragments.ReviewFragment;
import com.justfoodz.fragments.TableFragment;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvMenu, tvInformation, tvReviews, tv_menu_txtw, tv_information_txtw, tvReviewsw, bookingtext, bookingtextwh, totalreviewww;
    private ImageView ivBack, iv_review1, iv_review2, iv_review3, iv_review4, iv_review5;
    private RelativeLayout rlContainer, rl_menu;
    private LinearLayout layoutBackground;
    private String ratingValue, totalreview, bookTable;
    private Bundle bundle;

    public static RestaurantModel restaurantModel;
    public ImageView ivRestaurantLogo, ivOrderStatus;

    public static TextView tvRestaurantName, tvRestaurantAddress, tvCartItemCount, tvItemDiscountCost;
    private RelativeLayout rlCart;
    private AuthPreference authPreference;
    Database database;
    CardView info, reviews, menu, tablebooking;
    ImageView menuwh, menured, infored, infowh, reviewsred, reviewswh, bookingred, bookingwh;
    String CUSTOMERid;
    public static ImageView img_fav_red, img_fav_grey;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initialization();
        initializedListener();

        replaceMainMenuFragment();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        tvCartItemCount.setText("" + Ravifinalitem.cart_Item_number);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialization() {
        database = new Database(MainMenuActivity.this);
        requestQueue = Volley.newRequestQueue(this);
        ivBack = findViewById(R.id.iv_back);
        tvMenu = findViewById(R.id.tv_menu_txt);
        tvInformation = findViewById(R.id.tv_information_txt);
        tvReviews = findViewById(R.id.tv_reviews_txt);
        rlContainer = findViewById(R.id.rl_container);
        layoutBackground = findViewById(R.id.ll_menu_choose);
        ivRestaurantLogo = findViewById(R.id.iv_restaurant_logo);
        ivOrderStatus = findViewById(R.id.iv_order_status);
        tvRestaurantName = findViewById(R.id.tv_restaurant_name);
        tvRestaurantAddress = findViewById(R.id.tv_restaurant_address);
        tvCartItemCount = findViewById(R.id.tv_cart_count);
        rl_menu = findViewById(R.id.rl_menu);
        menu = findViewById(R.id.menu);
        info = findViewById(R.id.info);
        reviews = findViewById(R.id.reviews);
        tablebooking = findViewById(R.id.tablebooking);
        menuwh = findViewById(R.id.menuwh);
        menured = findViewById(R.id.menured);
        tv_menu_txtw = findViewById(R.id.tv_menu_txtw);
        infowh = findViewById(R.id.infowh);
        infored = findViewById(R.id.infored);
        reviewswh = findViewById(R.id.reviewswh);
        reviewsred = findViewById(R.id.reviewsred);
        tv_information_txtw = findViewById(R.id.tv_information_txtw);
        tvReviewsw = findViewById(R.id.tvReviewsw);
        bookingtext = findViewById(R.id.bookingtext);
        bookingtextwh = findViewById(R.id.bookingtextwh);
        bookingred = findViewById(R.id.bookingred);
        bookingwh = findViewById(R.id.bookingwh);
        totalreviewww = findViewById(R.id.totalreviewww);


        authPreference = new AuthPreference(this);
        CUSTOMERid = authPreference.getCustomerId();

        iv_review1 = findViewById(R.id.iv_review1);
        iv_review2 = findViewById(R.id.iv_review2);
        iv_review3 = findViewById(R.id.iv_review3);
        iv_review4 = findViewById(R.id.iv_review4);
        iv_review5 = findViewById(R.id.iv_review5);
        img_fav_red = findViewById(R.id.img_fav_red);
        img_fav_grey = findViewById(R.id.img_fav_grey);
        tvItemDiscountCost = findViewById(R.id.tv_item_discount_cost);
        rlCart = findViewById(R.id.rl_cart);


        try {
            Intent i = getIntent();
            restaurantModel = (RestaurantModel) i.getSerializableExtra("restaurantModel");
            if (!restaurantModel.getId().isEmpty()) {
                tvRestaurantName.setText(restaurantModel.getRestaurant_name());
                tvRestaurantAddress.setText(restaurantModel.getRestaurant_address());
                tvItemDiscountCost.setText(restaurantModel.getRestaurantOfferPrice());

                Picasso.get().load(restaurantModel.getRestaurant_Logo()).into(ivRestaurantLogo);

                if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Open")) {
                    ivOrderStatus.setImageDrawable(getResources().getDrawable(R.drawable.open));
                }
                if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Preorder")) {
                    ivOrderStatus.setImageDrawable(getResources().getDrawable(R.drawable.pre_order));

                }
                if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Closed")) {
                    ivOrderStatus.setImageDrawable(getResources().getDrawable(R.drawable.closed));

                }
                totalreview = getIntent().getStringExtra("TotalRestaurantReview");
                bookTable = getIntent().getStringExtra("Booktable");
                totalreviewww.setText("( " + totalreview + " Reviews )");

                ratingValue = getIntent().getStringExtra("ratingValue");
                //    String ratingCount = restaurantModel.getRatingValue();
                if (ratingValue.equalsIgnoreCase("0")) {
                    iv_review1.setBackgroundResource(R.drawable.review_star);
                    iv_review2.setBackgroundResource(R.drawable.review_star);
                    iv_review3.setBackgroundResource(R.drawable.review_star);
                    iv_review4.setBackgroundResource(R.drawable.review_star);
                    iv_review5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingValue.equalsIgnoreCase("1")) {
                    iv_review1.setBackgroundResource(R.drawable.review_yellow);
                    iv_review2.setBackgroundResource(R.drawable.review_star);
                    iv_review3.setBackgroundResource(R.drawable.review_star);
                    iv_review4.setBackgroundResource(R.drawable.review_star);
                    iv_review5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingValue.equalsIgnoreCase("2")) {
                    iv_review1.setBackgroundResource(R.drawable.review_yellow);
                    iv_review2.setBackgroundResource(R.drawable.review_yellow);
                    iv_review3.setBackgroundResource(R.drawable.review_star);
                    iv_review4.setBackgroundResource(R.drawable.review_star);
                    iv_review5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingValue.equalsIgnoreCase("3")) {
                    iv_review1.setBackgroundResource(R.drawable.review_yellow);
                    iv_review2.setBackgroundResource(R.drawable.review_yellow);
                    iv_review3.setBackgroundResource(R.drawable.review_yellow);
                    iv_review4.setBackgroundResource(R.drawable.review_star);
                    iv_review5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingValue.equalsIgnoreCase("4")) {
                    iv_review1.setBackgroundResource(R.drawable.review_yellow);
                    iv_review2.setBackgroundResource(R.drawable.review_yellow);
                    iv_review3.setBackgroundResource(R.drawable.review_yellow);
                    iv_review4.setBackgroundResource(R.drawable.review_yellow);
                    iv_review5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingValue.equalsIgnoreCase("5")) {
                    iv_review1.setBackground(ContextCompat.getDrawable(this, R.drawable.review_yellow));
                    iv_review2.setBackground(ContextCompat.getDrawable(this, R.drawable.review_yellow));
                    iv_review3.setBackground(ContextCompat.getDrawable(this, R.drawable.review_yellow));
                    iv_review4.setBackground(ContextCompat.getDrawable(this, R.drawable.review_yellow));
                    iv_review5.setBackground(ContextCompat.getDrawable(this, R.drawable.review_yellow));
                }
            }

            if (restaurantModel.getmealDealAvailable().equals("Yes")) {
                rl_menu.setVisibility(View.VISIBLE);
            } else {
                rl_menu.setVisibility(View.GONE);
            }

            if (bookTable.equals("Yes")) {
                tablebooking.setVisibility(View.VISIBLE);
            } else tablebooking.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        rlCart.setOnClickListener(this);
        menu.setOnClickListener(this);
        info.setOnClickListener(this);
        reviews.setOnClickListener(this);
        tablebooking.setOnClickListener(this);
        totalreviewww.setOnClickListener(this);
        rl_menu.setOnClickListener(this);
        img_fav_red.setOnClickListener(this);
        img_fav_grey.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()) {
            case R.id.iv_back:
                try {
                    emptyCart();
                    HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                } catch (Exception e) {

                }

                break;
            case R.id.rl_menu:
                Intent iiii = new Intent(MainMenuActivity.this, AllMealofDayActivity.class);
                startActivity(iiii);
                finish();
                break;
            case R.id.menu:
                replaceMainMenuFragment();
                break;
            case R.id.info:
                replaceInformationFragment();
                break;
            case R.id.reviews:
                replaceReviewFragment();
                break;
            case R.id.totalreviewww:
                Intent intentr = new Intent(MainMenuActivity.this, ReviewFragment.class);
                intentr.putExtra("id", restaurantModel.getId());
                startActivity(intentr);
                break;
            case R.id.tablebooking:
                if (CUSTOMERid.equals("")) {
                    showCustomDialog1decline("Please login your account first.");
                } else {
                    replaceTableFragment();
                }
                break;
            case R.id.rl_cart:
                SQLiteDatabase db = database.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM item_table", null);
                Cursor cursor1 = db.rawQuery("SELECT * FROM deal_item_table", null);
                if (cursor.moveToNext() || cursor1.moveToNext()) {
                    Intent intent = new Intent(MainMenuActivity.this, CartActivity.class);
                    intent.putExtra("restaurantAddress", SubMenuActivity.restaurantAddress);
                    intent.putExtra("restaurantName", SubMenuActivity.restaurantName);
                    intent.putExtra("id", SubMenuActivity.id);
                    intent.putExtra("restaurantCategoryId", authPreference.getRestaurantCategoryID());
                    startActivity(intent);
                } else {
                    showCustomDialog1decline("Cart is empty,please add item in cart.");
                }
                break;////,
            case R.id.img_fav_red:
                callDialog("Are you sure you want to remove this restaurant from favourite ?", "1");
                break;
            case R.id.img_fav_grey:
                callDialog("Are you sure you want to make it favourite ?", "2");

                break;
            default:
                break;
        }
    }

    private void emptyCart() {

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM item_table", null);
        Cursor cursor1 = db.rawQuery("SELECT * FROM deal_item_table", null);
        if (cursor.moveToNext() || cursor1.moveToNext()) {
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Justfoodz")
                    .setMessage("Are you sure you want to empty the shopping cart ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Ravifinalitem.cart_Item_number = 0;
                            database.delete();
                            database.deal_delete();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss()).show();
        } else {
            finish();
        }
    }


    public void replaceMainMenuFragment() {
        menu.setBackgroundResource(R.drawable.light_green_background);
        menured.setVisibility(View.GONE);
        tvMenu.setVisibility(View.GONE);
        tv_menu_txtw.setVisibility(View.VISIBLE);
        menuwh.setVisibility(View.VISIBLE);
        reviews.setBackgroundResource(R.drawable.white);
        tvReviewsw.setVisibility(View.GONE);
        reviewswh.setVisibility(View.GONE);
        reviewsred.setVisibility(View.VISIBLE);
        tvReviews.setVisibility(View.VISIBLE);
        info.setBackgroundResource(R.drawable.white);
        infowh.setVisibility(View.GONE);
        infored.setVisibility(View.VISIBLE);
        tvInformation.setVisibility(View.VISIBLE);
        tv_information_txtw.setVisibility(View.GONE);
        tablebooking.setBackgroundResource(R.drawable.white);
        bookingwh.setVisibility(View.GONE);
        bookingred.setVisibility(View.VISIBLE);
        bookingtext.setVisibility(View.VISIBLE);
        bookingtextwh.setVisibility(View.GONE);
        MenuFragment.menutypevalue = "false";

        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            Log.e("qwerty", "" + restaurantModel.getId());
            bundle = new Bundle();
            bundle.putString("id", restaurantModel.getId());
            bundle.putString("restaurantName", restaurantModel.getRestaurant_name());
            bundle.putString("restaurantAddress", restaurantModel.getRestaurant_address());

            MenuFragment menuFragment = new MenuFragment();
            menuFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.rl_container, menuFragment).commit();

        } catch (NullPointerException e) {
            e.printStackTrace();

        }


    }

    public void replaceInformationFragment() {
        info.setBackgroundResource(R.drawable.light_green_background);
        infored.setVisibility(View.GONE);
        infowh.setVisibility(View.VISIBLE);
        tvInformation.setVisibility(View.GONE);
        tv_information_txtw.setVisibility(View.VISIBLE);
        tvMenu.setVisibility(View.VISIBLE);
        menured.setVisibility(View.VISIBLE);
        menuwh.setVisibility(View.GONE);
        tv_menu_txtw.setVisibility(View.GONE);
        menu.setBackgroundResource(R.drawable.white);
        tvReviews.setVisibility(View.VISIBLE);
        reviewsred.setVisibility(View.VISIBLE);
        reviewswh.setVisibility(View.GONE);
        tvReviewsw.setVisibility(View.GONE);
        reviews.setBackgroundResource(R.drawable.white);
        tablebooking.setBackgroundResource(R.drawable.white);
        bookingwh.setVisibility(View.GONE);
        bookingred.setVisibility(View.VISIBLE);
        bookingtext.setVisibility(View.VISIBLE);
        bookingtextwh.setVisibility(View.GONE);


        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            bundle = new Bundle();
            bundle.putString("id", restaurantModel.getId());
            bundle.putString("contactEmail", restaurantModel.getRestaurant_contact_email());
            bundle.putString("contactPhone", restaurantModel.getRestaurant_contact_phone());
            bundle.putString("restaurantContactMobile", restaurantModel.getRestaurant_contact_phone());
            bundle.putString("deliveryDistance", restaurantModel.getRestaurant_deliveryDistance());
            bundle.putString("restaurantDeliverycharge", restaurantModel.getRestaurant_deliverycharge());
            bundle.putString("restaurantAbout", restaurantModel.getRestaurant_about());
            bundle.putString("monday", restaurantModel.getMonday());
            bundle.putString("tuesday", restaurantModel.getTuesday());
            bundle.putString("wednesday", restaurantModel.getWednesday());
            bundle.putString("thursday", restaurantModel.getThursday());
            bundle.putString("friday", restaurantModel.getFriday());
            bundle.putString("saturday", restaurantModel.getSaturday());
            bundle.putString("sunday", restaurantModel.getSunday());
            bundle.putString("recusions", restaurantModel.getRestaurant_cuisine());

            bundle.putString("ratingValue", restaurantModel.getRatingValue());
            bundle.putString("restaurantnotes", restaurantModel.getrestaurantnotes());
            bundle.putString("resturtantaddress", restaurantModel.getRestaurant_address());
            //for Cash
            bundle.putString("restaurantOnlyCashOn", restaurantModel.getRestaurant_onlycashon()); //0
            //for Card
            bundle.putString("restaurantCardAccept", restaurantModel.getRestaurant_cardaccept());


            InformationFragment informationFragment = new InformationFragment();
            informationFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.rl_container, informationFragment).commit();


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void replaceReviewFragment() {
        reviews.setBackgroundResource(R.drawable.light_green_background);
        tvReviews.setVisibility(View.GONE);
        tvReviewsw.setVisibility(View.VISIBLE);
        reviewsred.setVisibility(View.GONE);
        reviewswh.setVisibility(View.VISIBLE);
        menu.setBackgroundResource(R.drawable.white);
        tv_menu_txtw.setVisibility(View.GONE);
        menuwh.setVisibility(View.GONE);
        tvMenu.setVisibility(View.VISIBLE);
        menured.setVisibility(View.VISIBLE);
        info.setBackgroundResource(R.drawable.white);
        infowh.setVisibility(View.GONE);
        tv_information_txtw.setVisibility(View.GONE);
        infored.setVisibility(View.VISIBLE);
        tvInformation.setVisibility(View.VISIBLE);
        tablebooking.setBackgroundResource(R.drawable.white);
        bookingwh.setVisibility(View.GONE);
        bookingred.setVisibility(View.VISIBLE);
        bookingtext.setVisibility(View.VISIBLE);
        bookingtextwh.setVisibility(View.GONE);
        MenuFragment.menutypevalue = "true";

        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            bundle = new Bundle();
            bundle.putString("id", restaurantModel.getId());
            bundle.putString("restaurantName", restaurantModel.getRestaurant_name());
            bundle.putString("restaurantAddress", restaurantModel.getRestaurant_address());
            MenuFragment menuFragment = new MenuFragment();
            menuFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.rl_container, menuFragment).commit();

        } catch (NullPointerException e) {
            e.printStackTrace();

        }

    }

    public void replaceTableFragment() {
        tablebooking.setBackgroundResource(R.drawable.light_green_background);
        bookingtext.setVisibility(View.GONE);
        bookingtextwh.setVisibility(View.VISIBLE);
        bookingred.setVisibility(View.GONE);
        bookingwh.setVisibility(View.VISIBLE);
        menu.setBackgroundResource(R.drawable.white);
        tv_menu_txtw.setVisibility(View.GONE);
        menuwh.setVisibility(View.GONE);
        tvMenu.setVisibility(View.VISIBLE);
        menured.setVisibility(View.VISIBLE);
        info.setBackgroundResource(R.drawable.white);
        infowh.setVisibility(View.GONE);
        tv_information_txtw.setVisibility(View.GONE);
        infored.setVisibility(View.VISIBLE);
        tvInformation.setVisibility(View.VISIBLE);
        tvReviews.setVisibility(View.VISIBLE);
        reviewsred.setVisibility(View.VISIBLE);
        reviewswh.setVisibility(View.GONE);
        tvReviewsw.setVisibility(View.GONE);
        reviews.setBackgroundResource(R.drawable.white);

        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            bundle = new Bundle();
            bundle.putString("id", restaurantModel.getId());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        TableFragment tableFragment = new TableFragment();
        tableFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.rl_container, tableFragment).commit();
    }


    private void statusBarColor() {
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // startActivity(new Intent(MainMenuActivity.this, RestaurantsListActivity.class));
        emptyCart();
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainMenuActivity.this).create();
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

    private void callDialog(String msg, final String todo) {

        if (authPreference.getCustomerId().equals("")) {
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Justfoodz")
                    .setMessage("First login/register to add restaurant in favourite")
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
        } else {
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Justfoodz")
                    .setMessage("" + msg)
                    .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialog, int which) {
                                    if (todo.equals("1")) {
                                        getFav("1", "delete");
                                    } else {
                                        getFav("2", "add");
                                    }
                                }
                            })
                    .setNegativeButton(android.R.string.no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            }).show();
        }

    }

    private void getFav(final String a, final String b) {
        progressDialog = progressDialog.show(MainMenuActivity.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.add_remove_fav, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rere", "" + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error = jsonObject.getInt("error");
                    if (error == 1) {
                        if (a.equals("2")) {
                            img_fav_grey.setVisibility(View.GONE);
                            img_fav_red.setVisibility(View.VISIBLE);
                        } else {
                            img_fav_grey.setVisibility(View.VISIBLE);
                            img_fav_red.setVisibility(View.GONE);
                        }
                    } else {
                        String error_msg = jsonObject.getString("error_msg");
                        Toast.makeText(MainMenuActivity.this, "" + error_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("CustomerId", "" + authPreference.getCustomerId());
                params.put("restaurant_id", "" + restaurantModel.getId());
                params.put("favorite_action_type", "" + b);
                Log.e("qw", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}