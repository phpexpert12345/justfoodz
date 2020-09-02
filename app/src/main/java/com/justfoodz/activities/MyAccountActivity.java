package com.justfoodz.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.justfoodz.R;
import com.justfoodz.utils.AuthPreference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private LinearLayout  group,rlProfileSetting,managetablebooking, rlTrackOrder, rlReviewRating, rlChangePassword, rlOrderIssue, rlMyAddress,logout,applycoupon,
            helpsupport,faq,termscondition,refer,radeem,mywallet,aboutus,digitalwallet,payviawalletid,scanqrcode;
    private TextView tvUserName, tvUserAddress, tvUserPhone;
    private AuthPreference authPreference;
    private String firstName, lastName, userEmail, userPhone,userphoto, userAddress, userFloor, userStreetName, userZipCode, city, state;
    private LinearLayout rlOrder;
    CircleImageView userimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        statusBarColor();
        initialization();
        initializedListener();
    }

    private void initialization() {
        authPreference = new AuthPreference(this);
        ivBack = findViewById(R.id.iv_back);
        rlOrder = findViewById(R.id.rl_order);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserAddress = findViewById(R.id.tv_user_address);
        tvUserPhone = findViewById(R.id.tv_user_phone);
        rlProfileSetting = findViewById(R.id.rl_profile_setting);
        group=findViewById(R.id.group);
        rlTrackOrder = findViewById(R.id.rl_track_order);
        rlReviewRating = findViewById(R.id.rl_review_rating);
        rlChangePassword = findViewById(R.id.rl_change_password);
        rlOrderIssue = findViewById(R.id.rl_order_issue);
        rlMyAddress = findViewById(R.id.rl_my_address);
        logout=findViewById(R.id.logout);
        aboutus=findViewById(R.id.aboutus);
        managetablebooking=findViewById(R.id.managetablebooking);
//        applycoupon=findViewById(R.id.applycoupon);
        helpsupport=findViewById(R.id.helpsupport);
        faq=findViewById(R.id.faq);
        userimage=findViewById(R.id.userimage);
        termscondition=findViewById(R.id.termscondition);
        refer=findViewById(R.id.refer);
        radeem=findViewById(R.id.radeem);
        mywallet=findViewById(R.id.mywallet);

        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        userEmail = getIntent().getStringExtra("userEmail");
        userPhone = getIntent().getStringExtra("userPhone");
        userFloor = getIntent().getStringExtra("userFloor");//floor
        userStreetName = getIntent().getStringExtra("userStreetName");
        userZipCode = getIntent().getStringExtra("userZipCode");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        userAddress = getIntent().getStringExtra("userAddress");


        try {
            firstName = authPreference.getFirstName();
            lastName = authPreference.getLastName();
            userEmail = authPreference.getUserEmail();
            userPhone = authPreference.getUserPhone();
            userFloor = authPreference.getCustomerFloor();
            userStreetName = authPreference.getCompanyStreet();
            userZipCode = authPreference.getUserPostcode();
            city = authPreference.getCustomerCity();
            state = authPreference.getCustomerCountry();
            tvUserName.setText(firstName.concat(" ").concat(lastName));
            tvUserAddress.setText(userStreetName);
            tvUserPhone.setText(userPhone);
            if (authPreference.getUserPhoto().equals("")||(authPreference.getUserPhoto().equals(null)))
            {

            }else {
                Picasso.get().load(authPreference.getUserPhoto()).into(userimage);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        rlOrder.setOnClickListener(this);
        rlProfileSetting.setOnClickListener(this);
        group.setOnClickListener(this);
        rlTrackOrder.setOnClickListener(this);
        rlReviewRating.setOnClickListener(this);
        rlChangePassword.setOnClickListener(this);
        rlOrderIssue.setOnClickListener(this);
        rlMyAddress.setOnClickListener(this);
        logout.setOnClickListener(this);
//        applycoupon.setOnClickListener(this);
        helpsupport.setOnClickListener(this);
        faq.setOnClickListener(this);
        termscondition.setOnClickListener(this);
        refer.setOnClickListener(this);
        radeem.setOnClickListener(this);
        mywallet.setOnClickListener(this);
        managetablebooking.setOnClickListener(this);
        aboutus.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.rl_order:
                startActivity(new Intent(this, MyOrderActivity.class));
                break;
            case R.id.rl_profile_setting:
                Intent intent = new Intent(this, UpdateProfileActivity.class);
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
                intent.putExtra("userphoto",authPreference.getUserPhoto());
                startActivity(intent);
                finish();
                break;
            case R.id.group:
                startActivity(new Intent(this, GroupActivity.class));
                finish();
                break;
             case R.id.aboutus:
                startActivity(new Intent(this, AnoutUsActivity.class));
                break;
                case R.id.managetablebooking:
                startActivity(new Intent(this, ManageTableBooking.class));
                break;
            case R.id.rl_track_order:
                startActivity(new Intent(this, TrackOrderActivity.class));
                break;
            case R.id.rl_review_rating:
                startActivity(new Intent(this, WriteReviewActivity.class));
                break;
            case R.id.rl_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.rl_order_issue:
                startActivity(new Intent(this, OrderIssueActivity.class));
                break;
            case R.id.rl_my_address:
                Intent myAddress = new Intent(this, UserAddressActivity.class);
                myAddress.putExtra("userAddress", authPreference.getUserAddress());
                startActivity(myAddress);
                break;
            case R.id.logout:
              logoutDialog();
                break;
//            case R.id.applycoupon:
//                Intent c = new Intent(this, ApplyCouponActivity.class);
//                startActivity(c);
//                break;
            case R.id.helpsupport:
                Intent h = new Intent(this, HelpSupportActivity.class);
                startActivity(h);
                break;
            case R.id.faq:
                Intent f = new Intent(this, FaqActivity.class);
                startActivity(f);
                break;
            case R.id.termscondition:
                Intent t = new Intent(this, TermsAndConditionActivity.class);
                startActivity(t);
                break;
            case R.id.refer:
                Intent r = new Intent(this,ReferEarnActivity .class);
                startActivity(r);
                break;
            case R.id.radeem:
                Intent gr = new Intent(this,GiftRadeemActivity .class);
                startActivity(gr);
                break;
            case R.id.mywallet:
                Intent mw = new Intent(this,MainWalletactivity .class);
                startActivity(mw);
                break;
            default:
        }
    }
    private void logoutDialog() {
        new AlertDialog.Builder(MyAccountActivity.this)
                .setTitle("Justfoodz")
                .setMessage("Are you sure you want to logout now ?")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                new AuthPreference(MyAccountActivity.this).setCustomerId("");
                                authPreference.clear();//clear the local data
                                startActivity(new Intent(MyAccountActivity.this, HomeActivity.class));
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
    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
