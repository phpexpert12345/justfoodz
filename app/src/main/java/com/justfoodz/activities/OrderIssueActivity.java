package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
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

public class OrderIssueActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private ProgressDialog pDialog;
    private TextView tvSubmitIssue;
    private MaterialEditText edtOrderIssue, edt_email, edtOrderNumber, edtIssueDescription;
    private String orderIssue, user_email, orderNumber, issueDescription;
    private AuthPreference authPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_issue);
        statusBarColor();
        initialization();
        initializedListener();
    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        edtOrderIssue = findViewById(R.id.edt_order_issue);
        edt_email = findViewById(R.id.edt_email);
        edtOrderNumber = findViewById(R.id.edt_order_number);
        edtIssueDescription = findViewById(R.id.edt_issue_description);
        tvSubmitIssue = findViewById(R.id.tv_submit_issue);
        authPreference = new AuthPreference(this);
        edt_email.setText(authPreference.getUserEmail());
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvSubmitIssue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_submit_issue:
                issueOrderSupportValidation();
                break;
            default:
        }
    }

    public void issueOrderSupportValidation() {
        orderIssue = edtOrderIssue.getText().toString().trim();
        orderNumber = edtOrderNumber.getText().toString().trim();
        issueDescription = edtIssueDescription.getText().toString().trim();
        if (orderIssue.isEmpty()) {
            edtOrderIssue.setError("Enter Order Issue");
            edtOrderIssue.requestFocus();
        } else if (orderNumber.isEmpty()) {
            edtOrderNumber.setError("Enter Order Number");
            edtOrderNumber.requestFocus();
        } else if (issueDescription.isEmpty()) {
            edtIssueDescription.setError("Write Issue Description");
            edtIssueDescription.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(OrderIssueActivity.this)) {
            orderIssueSupport();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void orderIssueSupport() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlConstants.ORDER_ISSUE_SUPPORT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("issueresponse", "" + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("error");
                    String error_msg = jObj.getString("error_msg");
                    if (error == 1) {
                        showCustomDialog1decline(error_msg, "1");
                    } else {
                        showCustomDialog1decline(error_msg, "2");
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
                String customerId = authPreference.getCustomerId();

                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customerId);
                params.put("orderIssue", orderIssue);
                params.put("orderIDNumber", orderNumber);
                params.put("orderIssueEmail", authPreference.getUserEmail());
                params.put("orderIssueMessage", issueDescription);
                Log.e("issueparams", "" + params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    private void showCustomDialog1decline(String s, final String todo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(OrderIssueActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1")) {
                    startActivity(new Intent(OrderIssueActivity.this, HomeActivity.class));
                    finish();
                } else {
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
