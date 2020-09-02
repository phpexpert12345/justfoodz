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
import com.justfoodz.models.TransactionDataModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class DigitalWallet extends AppCompatActivity {
    LinearLayout frontcard, backcard;
    ImageView iv_back, qrimage;
    TextView frstNumber, viewall, secondnumber, thirdnumber, fourthnumber, membersince, username, pinnumber, avamoney, walletmessageinfo;
    AuthPreference authPreference;
    String CardNumber, Membersince;
    private ProgressDialog pDialog;

    RecyclerView transactionrecycler;
    private ArrayList<TransactionDataModel> transactionList;
    private TransactionAdapter transactionAdapter;
    String CustomerId, InfoMessageDigital;

    ImageView imageblank;
    TextView emptytxt;
    LinearLayout layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_wallet);

        frontcard = (LinearLayout) findViewById(R.id.frontcard);
        backcard = (LinearLayout) findViewById(R.id.backcard);
        qrimage = (ImageView) findViewById(R.id.qrimage);
        frstNumber = (TextView) findViewById(R.id.frstNumber);
        secondnumber = (TextView) findViewById(R.id.secondnumber);
        thirdnumber = (TextView) findViewById(R.id.thirdnumber);
        fourthnumber = (TextView) findViewById(R.id.fourthnumber);
        membersince = (TextView) findViewById(R.id.membersince);
        username = (TextView) findViewById(R.id.username);
        pinnumber = (TextView) findViewById(R.id.pinnumber);
        walletmessageinfo = (TextView) findViewById(R.id.walletmessageinfo);
        iv_back = findViewById(R.id.iv_back);
        avamoney = findViewById(R.id.avamoney);
        transactionrecycler = findViewById(R.id.transactionrecycler);
        viewall = findViewById(R.id.viewall);
        emptytxt = findViewById(R.id.emptytxt);
        imageblank = findViewById(R.id.imageblank);
        layout2 = findViewById(R.id.layout);

        transactionList = new ArrayList<>();

        authPreference = new AuthPreference(this);
        Membersince = authPreference.getDigitalWallet_MemerSince();
        CardNumber = authPreference.getDigitalWallet_CardNumber();
        CustomerId = authPreference.getCustomerId();
        membersince.setText(" Member Since " + Membersince);
        username.setText(authPreference.getFirstName() + " " + authPreference.getLastName());
//        pinnumber.setText(authPreference.getDigitalWallet_PINNumber());
        authPreference = new AuthPreference(this);

        InfoMessageDigital = authPreference.getDigitalWalletMessage();
        walletmessageinfo.setText(InfoMessageDigital);

        HitUrlforWalletAmount();
        HitURLforDigitalWaletData();

        if (CardNumber.isEmpty() || CardNumber.length() != 16) {
            showCustomDialog1decline("Sorry ! Digital Wallet Card is not available for you");
        } else {
            String[] strArr = new String[4];

            for (int i = 0; i < 4; i++) {
                int j = i;
                j = j + 1;
                strArr[i] = CardNumber.substring(i * 4, 4 * j);

            }
            frstNumber.setText(strArr[0]);
            secondnumber.setText(strArr[1]);
            thirdnumber.setText(strArr[2]);
            fourthnumber.setText(strArr[3]);
        }

        if (authPreference.getDigitalWallet_QrCode().equals("") || (authPreference.getDigitalWallet_QrCode().equals(null))) {

        } else {
            Picasso.get().load(authPreference.getDigitalWallet_QrCode()).into(qrimage);
        }

        frontcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frontcard.setVisibility(View.GONE);
                backcard.setVisibility(View.VISIBLE);

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalWallet.this, MainWalletactivity.class);
                startActivity(intent);
            }
        });
        backcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backcard.setVisibility(View.GONE);
                frontcard.setVisibility(View.VISIBLE);

            }
        });
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalWallet.this, ViewAllTransactionDigital.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void HitURLforDigitalWaletData() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.DigitalwalletFive, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    transactionList.clear();
                    int success = jsonObj.getInt("success");
                    if (success == 1) {
                        String success_msg = jsonObj.getString("success_msg");
                        transactionrecycler.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        imageblank.setVisibility(View.VISIBLE);
                        emptytxt.setVisibility(View.VISIBLE);
                        emptytxt.setText(success_msg);
                    } else {
                        transactionrecycler.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        imageblank.setVisibility(View.GONE);
                        emptytxt.setVisibility(View.GONE);
                        JSONObject wallet = jsonObj.getJSONObject("wallets");
                        JSONArray listrecord = wallet.getJSONArray("listrecord");
                        for (int i = 0; i < listrecord.length(); i++) {
                            JSONObject c = listrecord.getJSONObject(i);
                            String transaction_id = c.getString("transaction_id");
                            String transactionheading = c.getString("transaction_heading");
                            String Billed_to_name = c.getString("Billed_to_name");
                            String Billed_to_wallet_number = c.getString("Billed_to_wallet_number");
                            String Billed_to_email_address = c.getString("Billed_to_email_address");
                            String Billed_to_mobile_number = c.getString("Billed_to_mobile_number");
                            String Site_Company_Name = c.getString("Site_Company_Name");
                            String Site_Company_Address = c.getString("Site_Company_Address");
                            String wallet_payment_date = c.getString("wallet_payment_date");
                            String wallet_payment_time = c.getString("wallet_payment_time");
                            String wallet_payment_status = c.getString("wallet_payment_status");
                            String wallet_recieve_amount = c.getString("wallet_recieve_amount");
                            String wallet_money_display = c.getString("wallet_money_display");
                            String wallet_payment_status_icon = c.getString("wallet_payment_status_icon");
                            String Billed_Heading = c.getString("Billed_Heading");

                            transactionList.add(new TransactionDataModel(transaction_id, transactionheading, Billed_to_name, Billed_to_wallet_number,
                                    Billed_to_email_address, Billed_to_mobile_number, Site_Company_Name, Site_Company_Address, wallet_payment_date, wallet_payment_time, wallet_payment_status, wallet_recieve_amount, wallet_money_display,
                                    wallet_payment_status_icon, Billed_Heading));

                            TransactionAdapter transactionAdapter = new TransactionAdapter(DigitalWallet.this, transactionList);
                            LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(DigitalWallet.this, LinearLayoutManager.VERTICAL, false);
                            transactionrecycler.setLayoutManager(horizontalLayoutManager2);
                            transactionrecycler.setAdapter(transactionAdapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", CustomerId);
                params.put("digital_wallet_card_number", CardNumber);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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


    public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

        Context context;

        ArrayList<TransactionDataModel> pp;

        public TransactionAdapter(Context c, ArrayList<TransactionDataModel> p) {
            this.context = c;
            this.pp = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.getdigitalwalletdetails, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.transactionheading.setText(pp.get(position).getTransactionheading());
            holder.wallettime.setText(pp.get(position).getWallet_payment_time() + ",");
            holder.walletdate.setText(pp.get(position).getWallet_payment_date());
            Picasso.get().load(pp.get(position).getWallet_payment_status_icon()).into(holder.walletimage);

            String walletStatus = pp.get(position).getWallet_payment_status();
            if (walletStatus.equalsIgnoreCase("debited")) {
                holder.transactionprice.setTextColor(getResources().getColor(R.color.redColor));
                holder.transactionprice.setText("- " + SplashScreenActivity.currency_symbol + pp.get(position).getWallet_money_display());
            } else {
                holder.transactionprice.setTextColor(getResources().getColor(R.color.green));
                holder.transactionprice.setText("+ " + SplashScreenActivity.currency_symbol + pp.get(position).getWallet_money_display());
            }

        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView walletdate, wallettime, transactionprice, transactionheading;
            ImageView walletimage;

            public ViewHolder(View itemView) {
                super(itemView);
                walletdate = (TextView) itemView.findViewById(R.id.walletdate);
                wallettime = (TextView) itemView.findViewById(R.id.wallettime);
                transactionprice = (TextView) itemView.findViewById(R.id.transactionprice);
                transactionheading = (TextView) itemView.findViewById(R.id.transactionheading);
                walletimage = (ImageView) itemView.findViewById(R.id.walletimage);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(DigitalWallet.this, ViewTransactionInfo.class);
                        intent.putExtra("transactionid", pp.get(position).getTransaction_id());
                        intent.putExtra("date", pp.get(position).getWallet_payment_date());
                        intent.putExtra("time", pp.get(position).getWallet_payment_time());
                        intent.putExtra("walletmoney", pp.get(position).getWallet_money_display());
                        intent.putExtra("name", pp.get(position).getBilled_to_name());
                        intent.putExtra("mobileno", pp.get(position).getBilled_to_mobile_number());
                        intent.putExtra("emailid", pp.get(position).getBilled_to_email_address());
                        intent.putExtra("companyaddress", pp.get(position).getSite_Company_Address());
                        intent.putExtra("companyname", pp.get(position).getSite_Company_Name());
                        intent.putExtra("heading", pp.get(position).getTransactionheading());
                        intent.putExtra("Billed_Heading", pp.get(position).getBilled_Heading());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void HitUrlforWalletAmount() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_walletMoney> userLoginCall = interfaceRetrofit.phpexpert_customer_wallet_moeny(CustomerId);
        userLoginCall.enqueue(new Callback<Model_walletMoney>() {
            @Override
            public void onResponse(Call<Model_walletMoney> call, retrofit2.Response<Model_walletMoney> response) {
                if (response.isSuccessful()) {

                    if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        avamoney.setText(SplashScreenActivity.currency_symbol + response.body().getTotal_wallet_money_available());
                        authPreference = new AuthPreference(DigitalWallet.this);
                        authPreference.setGiftWalletMoney(response.body().getTotal_wallet_money_available());
                    } else {

                    }
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_walletMoney> call, Throwable t) {
                Toast.makeText(DigitalWallet.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


      /*  StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlConstants.WalletMoneyAmount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ressponsee", "" + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 0) {
                        String loyality = jObj.optString("Total_wallet_money_available");
                        avamoney.setText(SplashScreenActivity.currency_symbol + loyality);
                        authPreference = new AuthPreference(DigitalWallet.this);
                        authPreference.setGiftWalletMoney(loyality);
                        String success_msg = jObj.getString("success_msg");
//                        showCustomDialog1decline (success_msg);

                    } else {
                        String success_msg = jObj.getString("success_msg");
//                        showCustomDialog1decline (success_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", CustomerId);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);*/
    }
}
