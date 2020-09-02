package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.justfoodz.models.GiftRadeemModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class GiftRadeemActivity extends AppCompatActivity {

    TextView giftmoney, successmsg;
    AuthPreference authPreference;
    String Customerid, VOUCHERCODE, PIN;
    EditText vouchercode, pin;
    Button redeem;
    ImageView iv_back;
    ProgressDialog pDialog;
    private RecyclerView giftrecycler;
    private ArrayList<GiftRadeemModel> GiftRadeemList;
    private GiftRadeemAdapter giftRadeemAdapter;
    GiftRadeemModel giftRadeemModel;
    ImageView imageblank;
    TextView emptytxt;
    LinearLayout layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_radeem);
        giftrecycler = findViewById(R.id.giftrecycler);
        giftmoney = findViewById(R.id.giftmoney);
        vouchercode = findViewById(R.id.vouchercode);
        pin = findViewById(R.id.pin);
        redeem = findViewById(R.id.redeem);
        emptytxt = findViewById(R.id.emptytxt);
        imageblank = findViewById(R.id.imageblank);
        layout2 = findViewById(R.id.layout);
        iv_back = findViewById(R.id.iv_back);
        authPreference = new AuthPreference(this);
        Customerid = authPreference.getCustomerId();
        GiftRadeemList = new ArrayList<>();

        HitURLforGiftRadeemList();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiftRadeemActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VOUCHERCODE = vouchercode.getText().toString();
                PIN = pin.getText().toString();
                if (VOUCHERCODE.equals("")) {
                    vouchercode.setError("Please Enter Voucher Code");
                    vouchercode.requestFocus();
                } else if (PIN.equals("")) {
                    pin.setError("Please Enter Pin");
                    pin.requestFocus();
                } else if (Network.isNetworkCheck(GiftRadeemActivity.this)) {
                    HitUrlForGiftRadeemPutData();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void HitURLforGiftRadeemList() {

        GiftRadeemList = new ArrayList<GiftRadeemModel>();

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_GiftRedeamList> userLoginCall = interfaceRetrofit.phpexpert_gift_card_redeem_list(Customerid);
        userLoginCall.enqueue(new Callback<Model_GiftRedeamList>() {
            @Override
            public void onResponse(Call<Model_GiftRedeamList> call, retrofit2.Response<Model_GiftRedeamList> response) {
                if (response.isSuccessful()) {

                    if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        double total_money = 0.0;
                        giftrecycler.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        imageblank.setVisibility(View.GONE);
                        emptytxt.setVisibility(View.GONE);

                        Model_giftcards giftcards = response.body().getGiftcards();
                        for (int i = 0; i < giftcards.getListrecord().size(); i++) {
                            GiftRadeemModel giftRadeemModel = giftcards.getListrecord().get(i);
                            double d = Double.parseDouble(giftRadeemModel.getGiftVoucherAmount());
                            total_money = total_money + d;
                            GiftRadeemList.add(giftRadeemModel);
                        }

                        vouchercode.setText("");
                        pin.setText("");

                        if (GiftRadeemList.size() > 0) {
                            GiftRadeemAdapter giftRadeemAdapter = new GiftRadeemAdapter(GiftRadeemActivity.this, GiftRadeemList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(GiftRadeemActivity.this, LinearLayoutManager.VERTICAL, false);
                            giftrecycler.setLayoutManager(horizontalLayoutManager1);
                            giftrecycler.setAdapter(giftRadeemAdapter);
                        }
                    } else {
                        giftrecycler.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        imageblank.setVisibility(View.VISIBLE);
                        emptytxt.setVisibility(View.VISIBLE);
                        emptytxt.setText("" + response.body().getSuccess_msg());
                    }
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_GiftRedeamList> call, Throwable t) {
                Toast.makeText(GiftRadeemActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


       /* pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GiftRadeemList, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                double total_money = 0.0;
                Log.e("giftressponse", response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    GiftRadeemList.clear();
                    int success = jsonObj.getInt("success");
                    String total_available_amount = jsonObj.getString("total_available_amount");
                    giftmoney.setText(SplashScreenActivity.currency_symbol + total_available_amount);
                    if (success == 0) {
                        giftrecycler.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        imageblank.setVisibility(View.GONE);
                        emptytxt.setVisibility(View.GONE);
                        JSONObject wallet = jsonObj.getJSONObject("giftcards");
                        JSONArray listrecord = wallet.getJSONArray("listrecord");
                        for (int i = 0; i < listrecord.length(); i++) {
                            JSONObject c = listrecord.getJSONObject(i);
                            String voucherCodes = c.getString("VoucherCode");
                            String voucherPin = c.getString("VoucherPin");
                            String giftVoucherAmount = c.getString("GiftVoucherAmount");
                            double d = Double.parseDouble(giftVoucherAmount);
                            total_money = total_money + d;
                            GiftRadeemList.add(new GiftRadeemModel(voucherCodes, voucherPin, giftVoucherAmount));
                            vouchercode.setText("");
                            pin.setText("");
                            GiftRadeemAdapter giftRadeemAdapter = new GiftRadeemAdapter(GiftRadeemActivity.this, GiftRadeemList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(GiftRadeemActivity.this, LinearLayoutManager.VERTICAL, false);
                            giftrecycler.setLayoutManager(horizontalLayoutManager1);
                            giftrecycler.setAdapter(giftRadeemAdapter);
                        }
//                        double dd = Double.parseDouble(total_available_amount);
//                        total_money = total_money - dd;
//                        giftmoney.setText(" "+String.format("%.2f", total_money));

                    } else if (success == 1) {
                        String success_msg = jsonObj.getString("success_msg");
                        giftrecycler.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        imageblank.setVisibility(View.VISIBLE);
                        emptytxt.setVisibility(View.VISIBLE);
                        emptytxt.setText("" + success_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline("" + error, "2");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", Customerid);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);*/
    }

    public void HitUrlForGiftRadeemPutData() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_GiftRedeam> userLoginCall = interfaceRetrofit.GiftRadeemUpdateData(Customerid, VOUCHERCODE, PIN);
        userLoginCall.enqueue(new Callback<Model_GiftRedeam>() {
            @Override
            public void onResponse(Call<Model_GiftRedeam> call, retrofit2.Response<Model_GiftRedeam> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        showCustomDialog1decline(response.body().getSuccess_msg(), "1");
                    } else {
                        showCustomDialog1decline(response.body().getSuccess_msg(), "2");
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_GiftRedeam> call, Throwable t) {
                Toast.makeText(GiftRadeemActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });

/*
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GiftRadeemUpdateData, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success = jsonObj.getInt("success");
                    if (success == 0) {
                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline(success_msg, "1");
                    } else if (success == 1) {
                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline(success_msg, "2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline("" + error, "2");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", Customerid);
                params.put("GiftVoucherCode", VOUCHERCODE);
                params.put("GiftVoucherPin", PIN);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);*/
    }

    private void showCustomDialog1decline(String s, final String b) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (b.equals("1")) {
                    alertDialog.dismiss();
                    HitURLforGiftRadeemList();
                } else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    public class GiftRadeemAdapter extends RecyclerView.Adapter<GiftRadeemAdapter.ViewHolder> {

        Context context;
        ArrayList<GiftRadeemModel> pp;

        public GiftRadeemAdapter(Context c, ArrayList<GiftRadeemModel> p) {
            this.context = c;
            this.pp = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.giftradeemlist, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.voucher.setText(pp.get(position).getVoucherCode());
            holder.pin.setText(pp.get(position).getVoucherPin());
            holder.amount.setText(SplashScreenActivity.currency_symbol + pp.get(position).getGiftVoucherAmount());
        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView voucher, pin, amount;

            public ViewHolder(View itemView) {
                super(itemView);
                voucher = (TextView) itemView.findViewById(R.id.voucher);
                pin = (TextView) itemView.findViewById(R.id.pin);
                amount = (TextView) itemView.findViewById(R.id.amount);
            }
        }
    }
}