package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Filter;
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
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewAllTransactionDigital extends AppCompatActivity {
    RecyclerView transactionrecycler;
    private ArrayList<TransactionDataModel> transactionList;
    private TransactionAdapter transactionAdapter;
    String CustomerId;
    ProgressDialog pDialog;
    AuthPreference authPreference;
    LinearLayout startdatedialog,enddatedialog;
    TextView startdate,enddate;
    Button FilterBtn;
    private int mYear, mMonth, mDay;
     ImageView iv_back;
    String STARTDATE,ENDDATE;

    ImageView imageblank;
    TextView emptytxt;
    LinearLayout layout2;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ViewAllTransactionDigital.this,MainWalletactivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_transaction_digital);
        startdatedialog=findViewById(R.id.startdatedialog);
        enddatedialog=findViewById(R.id.enddatedialog);
        startdate=findViewById(R.id.startdate);
        enddate=findViewById(R.id.enddate);
        iv_back=findViewById(R.id.iv_back);
        emptytxt=findViewById(R.id.emptytxt);
        imageblank=findViewById(R.id.imageblank);
        layout2=findViewById(R.id.layout);
        FilterBtn=findViewById(R.id.Filter);
        authPreference =new AuthPreference(this);
        CustomerId = authPreference.getCustomerId();

        transactionrecycler = findViewById(R.id.transactionrecycler);
        transactionList = new ArrayList<>();

        HitURLforDigitalWaletData();

        FilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionList.clear();
                HitURLforDigitalWaletData();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewAllTransactionDigital.this,MainWalletactivity.class);
                startActivity(intent);
                finish();
            }
        });

        startdatedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewAllTransactionDigital.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                startdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
        enddatedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewAllTransactionDigital.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                enddate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
    }
    public void HitURLforDigitalWaletData() {

       /* params.put("CustomerId", );
        params.put("transaction_start_date",);
        params.put("transaction_end_date",);
        params.put("digital_wallet_card_number",);

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_location> userLoginCall = interfaceRetrofit.phpexpert_wallet_payment_report(CustomerId,STARTDATE,ENDDATE, ""+authPreference.getDigitalWallet_CardNumber());
        userLoginCall.enqueue(new Callback<Model_location>() {
            @Override
            public void onResponse(Call<Model_location> call, retrofit2.Response<Model_location> response) {
                if (response.isSuccessful()) {

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_location> call, Throwable t) {
                Toast.makeText(ViewAllTransactionDigital.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });*/



        STARTDATE=startdate.getText().toString();
        ENDDATE=enddate.getText().toString();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.DigitalAllList, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    transactionList.clear();
                    int success= jsonObj.getInt("success");
                    if (success==1){
                        String success_msg= jsonObj.getString("success_msg");
                        transactionrecycler.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        imageblank.setVisibility(View.VISIBLE);
                        emptytxt.setVisibility(View.VISIBLE);
                        emptytxt.setText(success_msg);
                    }else {
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
                            String wallet_payment_status_icon=c.getString("wallet_payment_status_icon");
                            String Billed_Heading=c.getString("Billed_Heading");

                            transactionList.add(new TransactionDataModel(transaction_id, transactionheading, Billed_to_name, Billed_to_wallet_number,
                                    Billed_to_email_address, Billed_to_mobile_number, Site_Company_Name, Site_Company_Address, wallet_payment_date, wallet_payment_time, wallet_payment_status, wallet_recieve_amount, wallet_money_display,
                                    wallet_payment_status_icon,Billed_Heading));

                             TransactionAdapter transactionAdapter = new  TransactionAdapter(ViewAllTransactionDigital.this, transactionList);
                            LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(ViewAllTransactionDigital.this, LinearLayoutManager.VERTICAL, false);
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
                params.put("transaction_start_date",STARTDATE);
                params.put("transaction_end_date",ENDDATE);
                params.put("digital_wallet_card_number", ""+authPreference.getDigitalWallet_CardNumber());
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
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
            holder.wallettime.setText(pp.get(position).getWallet_payment_time()+",");
            holder.walletdate.setText(pp.get(position).getWallet_payment_date());
            Picasso.get().load(pp.get(position).getWallet_payment_status_icon()).into(holder.walletimage);

            String walletStatus = pp.get(position).getWallet_payment_status();
            if (walletStatus.equalsIgnoreCase("debited")){
                holder.transactionprice.setTextColor(getResources().getColor(R.color.redColor));
                holder.transactionprice.setText("- "+SplashScreenActivity.currency_symbol + pp.get(position).getWallet_money_display());
            }else {
                holder.transactionprice.setTextColor(getResources().getColor(R.color.green));
                holder.transactionprice.setText("+ "+SplashScreenActivity.currency_symbol + pp.get(position).getWallet_money_display());
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
                walletimage=(ImageView)itemView.findViewById(R.id.walletimage);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Intent intent=new Intent(ViewAllTransactionDigital.this,ViewTransactionInfo.class);
                        intent.putExtra("transactionid",pp.get(position).getTransaction_id());
                        intent.putExtra("date",pp.get(position).getWallet_payment_date());
                        intent.putExtra("time",pp.get(position).getWallet_payment_time());
                        intent.putExtra("walletmoney",pp.get(position).getWallet_money_display());
                        intent.putExtra("name",pp.get(position).getBilled_to_name());
                        intent.putExtra("mobileno",pp.get(position).getBilled_to_mobile_number());
                        intent.putExtra("emailid",pp.get(position).getBilled_to_email_address());
                        intent.putExtra("companyaddress",pp.get(position).getSite_Company_Address());
                        intent.putExtra("companyname",pp.get(position).getSite_Company_Name());
                        intent.putExtra("heading",pp.get(position).getTransactionheading());
                        intent.putExtra("Billed_Heading",pp.get(position).getBilled_Heading());
                        startActivity(intent);
                    }
                });
            }

        }
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




}
