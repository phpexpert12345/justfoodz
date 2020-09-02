package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
//    private EditText edtOldPwd, edtNewPwd, edtConfirmPwd;
    MaterialEditText edtOldPwd, edtNewPwd, edtConfirmPwd;
    private TextView tvPwdConfirm;
    private String oldPwd, newPwd, confirmPwd;
    private ProgressDialog pDialog;
    HomeActivity homeActivity;
    private AuthPreference authPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        statusBarColor();
        initialization();
        initializedListener();
    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        edtOldPwd = findViewById(R.id.edt_old_pwd);
        edtNewPwd = findViewById(R.id.edt_new_pwd);
        edtConfirmPwd = findViewById(R.id.edt_confirm_pwd);
        tvPwdConfirm = findViewById(R.id.tv_pwd_confirm);
        authPreference = new AuthPreference(this);
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvPwdConfirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_pwd_confirm:
                passwordValidation();
                break;
            default:
        }
    }


    public void passwordValidation() {
//        oldPwd = edtOldPwd.getText().toString().trim();
//        newPwd = edtNewPwd.getText().toString().trim();
//        confirmPwd = edtConfirmPwd.getText().toString().trim();
        if (edtOldPwd.getText().toString().equals("")) {
            edtOldPwd.setError("Enter old password");
            edtOldPwd.requestFocus();

        } else if(edtNewPwd.getText().toString().equals("")) {
            edtNewPwd.setError("Enter new password");
            edtNewPwd.requestFocus();

        } else if(edtConfirmPwd.getText().toString().equals("")) {
            edtConfirmPwd.setError("Enter your new confirm password");
            edtConfirmPwd.requestFocus();


        }else if (edtNewPwd.getText().toString().equals(edtConfirmPwd.getText().toString())){
            if (com.justfoodz.utils.Network.isNetworkCheck(ChangePasswordActivity.this)) {
                changePassword();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
            }
        }
            else {
            edtConfirmPwd.setError("New password and confirm password doesn't match");
            edtConfirmPwd.requestFocus();
        }
    }

    private void changePassword() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlConstants.CHANGE_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        String success_msg = jObj.getString("success_msg");
                        showCustomDialog1decline (success_msg,"1");
                     //   finish();
                    } else {
                        String error_msg = jObj.getString("error_msg");
                        showCustomDialog1decline (error_msg,"2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                String customerId = authPreference.getCustomerId();
                params.put("OldCustomerPassword", ""+edtOldPwd.getText().toString().trim());
                params.put("NewCustomerPassword", ""+edtNewPwd.getText().toString().trim());
                params.put("CustomerId", customerId);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void showCustomDialog1decline (String s, final String todo)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1"))
                {
                    Intent i = new Intent(ChangePasswordActivity.this, MyAccountActivity.class);
                    startActivity(i);
                    finish();
//                    alertDialog.dismiss();
                }
                else {
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog.show();
    }


    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }

    @Override
    public void onBackPressed() {
        finish();
        // super.onBackPressed();
    }
}
