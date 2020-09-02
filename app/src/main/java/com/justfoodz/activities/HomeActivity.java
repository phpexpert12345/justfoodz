package com.justfoodz.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.justfoodz.MarketplaceActivity;
import com.justfoodz.R;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.Database.Database;

import com.justfoodz.adapters.DrawerItemCustomAdapter;

import com.justfoodz.models.DataModel;
import com.justfoodz.utils.AuthPreference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private String[] mNavigationDrawerItemTitles;
    public static DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    private AuthPreference authPreference;
    private DataModel[] drawerItem;
    public static LinearLayout rl_main_left_drawer;
    private String customerId, firstName, lastName, userPhone, userEmail, userFloor, userStreetName, userZipCode, city, state, userAddress;
    private String restaurantName = "", restaurantAddress = "", orderDate = "", orderTime = "", ordPrice = "", orderIdentifyno = "";
    Database database;
    LinearLayout editprofile, logonotshow;
    TextView username, usermobile, useremail;
    RelativeLayout myaccLayout, myorderLayout, menuLayout, logoutLayout, logoutLayout1;
    CircleImageView profileimage;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String CustomerId;
    public static String where_para = "", customerlocality_para = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        editprofile = findViewById(R.id.editprofile);
        username = (TextView) findViewById(R.id.username);
        usermobile = (TextView) findViewById(R.id.usermobile);
        useremail = (TextView) findViewById(R.id.useremail);
        logonotshow = (LinearLayout) findViewById(R.id.logonotshow);
        profileimage = (CircleImageView) findViewById(R.id.profileimage);
        authPreference = new AuthPreference(this);
        username.setText(authPreference.getFirstName() + " " + authPreference.getLastName());
        usermobile.setText(authPreference.getUserPhone());
        useremail.setText(authPreference.getUserEmail());
        CustomerId = authPreference.getCustomerId();
        if (authPreference.getUserPhoto().equals("") || (authPreference.getUserPhoto().equals(null))) {

        } else {
            Picasso.get().load(authPreference.getUserPhoto()).into(profileimage);
        }

        if (checkAndRequestPermissions()) {
        }
        statusBarColor();
        Ravifinalitem.cart_Item_number = 0;
        database = new Database(HomeActivity.this);
        database.delete();
        database.deal_delete();
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        rl_main_left_drawer = (LinearLayout) findViewById(R.id.rl_main_left_drawer);

        if (getIntent().getExtras() != null) {
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            userEmail = getIntent().getStringExtra("email");
            userPhone = getIntent().getStringExtra("phoneNumber");

            city = getIntent().getStringExtra("userCity");
            state = getIntent().getStringExtra("userState");
            userAddress = getIntent().getStringExtra("userAddress");
            restaurantName = getIntent().getStringExtra("restaurantAddress");
            restaurantAddress = getIntent().getStringExtra("restaurantName");
            orderDate = getIntent().getStringExtra("date");
            orderTime = getIntent().getStringExtra("time");
            ordPrice = getIntent().getStringExtra("orderPrice");
            orderIdentifyno = getIntent().getStringExtra("orderIdentifyno");
            username.setText(firstName + " " + lastName);
            usermobile.setText(userPhone);
            useremail.setText(userEmail);

        }

        authPreference = new AuthPreference(this);


        setupToolbar();

        replaceHomeFragment();
        customerId = authPreference.getCustomerId();
        if (!customerId.isEmpty()) {
            logonotshow.setVisibility(View.GONE);
            editprofile.setVisibility(View.VISIBLE);
            drawerItem = new DataModel[23];
            drawerItem[0] = new DataModel("Home", R.drawable.home);
            drawerItem[1] = new DataModel("Marketplace", R.drawable.market);
            drawerItem[2] = new DataModel("Event", R.drawable.help_support);
            drawerItem[3] = new DataModel("My Wallet", R.drawable.my_wallet);
            drawerItem[4] = new DataModel("My Account", R.drawable.myaccount);
            drawerItem[5] = new DataModel("Buy Gift", R.drawable.ghift);
            drawerItem[6] = new DataModel("My Order", R.drawable.myorder);
            drawerItem[7] = new DataModel("My Address", R.drawable.my_address);
            drawerItem[8] = new DataModel("Book a Table", R.drawable.table);
            drawerItem[9] = new DataModel("Manage Table Booking", R.drawable.group);
            drawerItem[10] = new DataModel("Redeem Gift Card", R.drawable.radeem_gift_card);
            drawerItem[11] = new DataModel("Change Password", R.drawable.changepassword);
            drawerItem[12] = new DataModel("Refer & Earn", R.drawable.refer_earn);
            drawerItem[13] = new DataModel("Track Order", R.drawable.trackorder);
            drawerItem[14] = new DataModel("Order Issue", R.drawable.orderissu);
            drawerItem[15] = new DataModel("Review & Rating", R.drawable.reviewgrey);
            drawerItem[16] = new DataModel("My Favorites", R.drawable.fav);
            drawerItem[17] = new DataModel("About Us", R.drawable.about_us);
            drawerItem[18] = new DataModel("Terms of Conditions", R.drawable.termsandconditions);
            drawerItem[19] = new DataModel("FAQ's", R.drawable.faqq);
            drawerItem[20] = new DataModel("Help & Support", R.drawable.help_support);
            drawerItem[21] = new DataModel("Privacy & Policy", R.drawable.policy);
////            drawerItem[15] = new DataModel("Apply Coupon",R.drawable.apply_coupon);
            drawerItem[22] = new DataModel("Logout", R.drawable.logout);

        } else {
            editprofile.setVisibility(View.GONE);
            logonotshow.setVisibility(View.VISIBLE);
            drawerItem = new DataModel[8];
            drawerItem[0] = new DataModel("Home", R.drawable.home);
            drawerItem[1] = new DataModel("Marketplace", R.drawable.market);
            drawerItem[2] = new DataModel("Event", R.drawable.event);
            drawerItem[3] = new DataModel("Login", R.drawable.login);
            drawerItem[4] = new DataModel("Register", R.drawable.register);
            drawerItem[5] = new DataModel("Terms of Conditions", R.drawable.termsandconditions);
            drawerItem[6] = new DataModel("Privacy & Policy", R.drawable.help_support);
            drawerItem[7] = new DataModel("FAQ's", R.drawable.faqq);

        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UpdateProfileActivity.class);
                intent.putExtra("firstName", authPreference.getFirstName());
                intent.putExtra("lastName", authPreference.getLastName());
                intent.putExtra("email", authPreference.getUserEmail());
                intent.putExtra("phoneNumber", authPreference.getUserPhone());
                intent.putExtra("streetNumber", authPreference.getCompanyStreetNo());//floor
                intent.putExtra("streetName", authPreference.getCompanyStreet());
                intent.putExtra("zipCode", authPreference.getUserPostcode());
                intent.putExtra("userCity", authPreference.getUserCity());
                intent.putExtra("userState", authPreference.getUserState());
                intent.putExtra("userAddress", authPreference.getUserAddress());
                startActivity(intent);
                // finish();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
            }
        });
    }

    public void replaceHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (customerId.isEmpty()) {
                selectItem(position);
            } else {
                selectItem1(position);
            }
        }
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 1:
                startActivity(new Intent(this, MarketplaceActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 2:
                startActivity(new Intent(this, EventActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 3:
                Intent qw = new Intent(this, LoginActivity.class);
                qw.putExtra("id", "");
                qw.putExtra("restaurantName", "");
                qw.putExtra("restaurantAddress", "");
                qw.putExtra("date", "");
                qw.putExtra("time", "");
                qw.putExtra("itemId", "");
                qw.putExtra("updateTotalPrice", "");
                qw.putExtra("subTot", "");
                qw.putExtra("disPrice", "");
                qw.putExtra("deliveryChargeValue", "");
                qw.putExtra("serviceFees", "");
                qw.putExtra("packageFees", "");
                qw.putExtra("salesTaxAmount", "");
                qw.putExtra("vatTax", "");

                qw.putExtra("orderType", "");
//
                qw.putExtra("subMenuItemId", "");
                startActivity(qw);
                finish();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 4:
                Intent intent = new Intent(new Intent(this, RegisterActivity.class));
                startActivity(intent);
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 5:
                startActivity(new Intent(this, TermsAndConditionActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 6:
                startActivity(new Intent(this, PrivacyPolicy.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 7:
                startActivity(new Intent(this, FaqActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(rl_main_left_drawer);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void selectItem1(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 1:
                startActivity(new Intent(this, MarketplaceActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 2:
                startActivity(new Intent(this, EventActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 3:
                Intent intentO = new Intent(this, MainWalletactivity.class);
                startActivity(intentO);
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 4:
                Intent i = new Intent(this, MyAccountActivity.class);
                i.putExtra("firstName", authPreference.getFirstName());
                i.putExtra("lastName", authPreference.getLastName());
                i.putExtra("email", authPreference.getUserEmail());
                i.putExtra("phoneNumber", authPreference.getUserPhone());
                i.putExtra("streetNumber", authPreference.getCompanyStreetNo());//floor
                i.putExtra("streetName", authPreference.getCompanyStreet());
                i.putExtra("zipCode", authPreference.getUserPostcode());
                i.putExtra("userCity", authPreference.getUserCity());
                i.putExtra("userState", authPreference.getUserState());
                i.putExtra("userAddress", authPreference.getUserAddress());
                startActivity(i);
                //finish();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 5:
                Intent intentOrder = new Intent(this, BuyGiftActivity.class);
                startActivity(intentOrder);
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 6:
                Intent intent = new Intent(this, MyOrderActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 7:
                startActivity(new Intent(this, UserAddressActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 8:
                startActivity(new Intent(this, TableBookingActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 9:
                startActivity(new Intent(this, ManageTableBooking.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 10:
                startActivity(new Intent(this, GiftRadeemActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 11:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 12:
                startActivity(new Intent(this, ReferEarnActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 13:
                Intent trackOrder = new Intent(this, TrackOrderActivity.class);
                trackOrder.putExtra("restaurantAddress", restaurantAddress);
                trackOrder.putExtra("restaurantName", restaurantName);
                trackOrder.putExtra("date", orderDate);
                trackOrder.putExtra("time", orderTime);
                trackOrder.putExtra("orderPrice", ordPrice);
                trackOrder.putExtra("orderIdentifyno", orderIdentifyno);
                startActivity(trackOrder);
                // finish();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 14:
                startActivity(new Intent(this, OrderIssueActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 15:
                startActivity(new Intent(this, WriteReviewActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 16:
                startActivity(new Intent(this, Favourite_res.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 17:
                startActivity(new Intent(this, AnoutUsActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 18:
                startActivity(new Intent(this, TermsAndConditionActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 19:
                startActivity(new Intent(this, FaqActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 20:
                startActivity(new Intent(this, HelpSupportActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 21:
                startActivity(new Intent(this, PrivacyPolicy.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 22:
                logoutDialog();
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(rl_main_left_drawer);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void logoutDialog() {
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Justfoodz")
                .setMessage("Are you sure you want to logout now ?")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                try {
                                    File dir = getCacheDir();
                                    deleteDir(dir);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                new AuthPreference(HomeActivity.this).setCustomerId("");
                                authPreference.clear();//clear the local data
                                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                                finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mDrawerToggle.syncState();
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
        finishAffinity();
    }

    public void tabdisplay() {

        myaccLayout = (RelativeLayout) findViewById(R.id.myaccLayout);
        myorderLayout = (RelativeLayout) findViewById(R.id.myorderLayout);
        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        logoutLayout = (RelativeLayout) findViewById(R.id.logoutLayout);
        logoutLayout1 = (RelativeLayout) findViewById(R.id.logoutLayout1);

        myaccLayout.setBackgroundColor(getResources().getColor(R.color.toolbar));
        myorderLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
        menuLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
        logoutLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
        logoutLayout1.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));

        myaccLayout.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                myaccLayout.setBackgroundColor(getResources().getColor(R.color.toolbar));
                myorderLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                menuLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout1.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
//                Intent myaccIntent = new Intent(DriversActivity.this, DashboardActivity.class);
//                startActivity(myaccIntent);
//                finish();
            }
        });
        myorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myaccLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                myorderLayout.setBackgroundColor(getResources().getColor(R.color.toolbar));
                menuLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout1.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
//                Intent myorderIntent = new Intent(DriversActivity.this, NewOrdersActivity.class);
//                startActivity(myorderIntent);
//                finish();
            }
        });
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myaccLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                myorderLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                menuLayout.setBackgroundColor(getResources().getColor(R.color.toolbar));
                logoutLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout1.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
//                Intent menuIntent = new Intent(DriversActivity.this, MenuMgmtActivity.class);
//                startActivity(menuIntent);
//                finish();
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myaccLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                myorderLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                menuLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout.setBackgroundColor(getResources().getColor(R.color.toolbar));
                logoutLayout1.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
//                Intent menuIntent = new Intent(DriversActivity.this, LogoutActivity.class);
//                startActivity(menuIntent);
            }
        });
        logoutLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myaccLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                myorderLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                menuLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout.setBackgroundColor(getResources().getColor(R.color.colorbottomitem));
                logoutLayout1.setBackgroundColor(getResources().getColor(R.color.toolbar));
//                Intent menuIntent = new Intent(DriversActivity.this, LogoutActivity.class);
//                startActivity(menuIntent);
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}