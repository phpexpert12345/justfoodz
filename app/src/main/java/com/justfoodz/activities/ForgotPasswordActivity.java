package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private HomeActivity homeActivity;
    private ProgressDialog pDialog;
    private MaterialEditText edtEmail;
    private String email;
    private Button tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        statusBarColor();
        initialization();
        initializedListener();

    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        edtEmail = findViewById(R.id.edt_email);
        tvSubmit = findViewById(R.id.tv_submit);


    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_submit:
                forgotValidation();
                break;
        }

    }

    public void forgotValidation() {
        email = edtEmail.getText().toString().trim();
        if (!isValidEmail(email)) {
            edtEmail.setError("Enter Valid Email Address");
            edtEmail.requestFocus();
        }
        if (Network.isNetworkCheck(this)) {
            forgotPassword();

        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

        }

    }


    private void forgotPassword() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();


        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_forgetpassword> userLoginCall = interfaceRetrofit.forgetPassword(email);
        userLoginCall.enqueue(new Callback<Model_forgetpassword>() {
            @Override
            public void onResponse(Call<Model_forgetpassword> call, retrofit2.Response<Model_forgetpassword> response) {
                if (response.isSuccessful()) {
                    pDialog.dismiss();
                    if (response.body().getError().equalsIgnoreCase("0")) {
//                        String error_msg = jObj.getString("error_msg");
                        showCustomDialog1decline(response.body().getError_msg(), "1");

                    } else if (response.body().getError().equalsIgnoreCase("1")) {
                        //error_msg":"Sorry ! This Email is not already regsitered with us, please provide another email!"
//                        String error_msg = jObj.getString("error_msg");
                        showCustomDialog1decline(response.body().getError_msg(), "2");
                    }
                }

            }

            @Override
            public void onFailure(Call<Model_forgetpassword> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }

    private void showCustomDialog1decline(String s, final String todo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1")) {
                    startActivity(new Intent(ForgotPasswordActivity.this, HomeActivity.class));
                    finish();
                } else {
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog.show();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
