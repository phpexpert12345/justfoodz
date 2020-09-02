package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.models.BuyGiftModel;
import com.justfoodz.models.FeatureAminitiesModel;
import com.justfoodz.models.FeaturedPartnerModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class BuyGiftActivity extends AppCompatActivity {

    RecyclerView buygiftrecycler;
    private ArrayList<BuyGiftModel> buygiftList;

    ProgressDialog pDialog;
    ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_gift);
        buygiftrecycler = findViewById(R.id.buygiftrecycler);
        iv_back = findViewById(R.id.iv_back);

        buygiftList = new ArrayList<>();

        HitURLforBuyGiftData();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void HitURLforBuyGiftData() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_GiftSearchList> userLoginCall = interfaceRetrofit
                .phpexpert_buy_gift_card_list("https://www.justfoodz.com/api-access/phpexpert_buy_gift_card_list.php");
        userLoginCall.enqueue(new Callback<Model_GiftSearchList>() {
            @Override
            public void onResponse(Call<Model_GiftSearchList> call, retrofit2.Response<Model_GiftSearchList> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getGiftSearchList().size(); i++) {
                        BuyGiftModel buyGiftModel = response.body().getGiftSearchList().get(i);

                        buygiftList.add(buyGiftModel);
                    }

                    BuyGiftAdapter buyGiftAdapter = new BuyGiftAdapter(BuyGiftActivity.this, buygiftList);
                    LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(BuyGiftActivity.this, LinearLayoutManager.VERTICAL, false);
                    buygiftrecycler.setLayoutManager(horizontalLayoutManager2);
                    buygiftrecycler.setAdapter(buyGiftAdapter);


                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_GiftSearchList> call, Throwable t) {
                Toast.makeText(BuyGiftActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });





/*
        pDialog = new ProgressDialog(BuyGiftActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.BuyGiftlist, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray buylist = jsonObj.getJSONArray("GiftSearchList");
                    buygiftList.clear();
                    for (int i = 0; i < buylist.length(); i++) {
                        JSONObject c = buylist.getJSONObject(i);

                        String id = c.getString("id");
                        String giftname = c.getString("GiftVoucherTitle");
                        String giftprice = c.getString("GiftVoucherPurchaseAmount");
                        String giftde  = c.getString("voucher_long_description");
                        String giftdescrption = Html.fromHtml(giftde).toString();
                        String giftterms = c.getString("voucher_terms_description");
                        String gifttermscondition = Html.fromHtml(giftterms).toString();
                        String giftre  = c.getString("voucher_redeem_instructions_description");
                        String giftredeemption = Html.fromHtml(giftre).toString();
                        String giftimage = c.getString("GiftVoucherImage");
                        String error = c.getString("error");

                        buygiftList.add(new BuyGiftModel(id, giftname, giftprice, giftimage, giftdescrption, gifttermscondition, giftredeemption));


                        BuyGiftAdapter buyGiftAdapter = new BuyGiftAdapter(BuyGiftActivity.this, buygiftList);
                        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(BuyGiftActivity.this, LinearLayoutManager.VERTICAL, false);
                        buygiftrecycler.setLayoutManager(horizontalLayoutManager2);
                        buygiftrecycler.setAdapter(buyGiftAdapter);
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
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);*/
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

    public class BuyGiftAdapter extends RecyclerView.Adapter<BuyGiftAdapter.ViewHolder> {

        Context context;

        ArrayList<BuyGiftModel> buygiftList;

        public BuyGiftAdapter(Context c, ArrayList<BuyGiftModel> p) {
            this.context = c;
            this.buygiftList = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.buygiftlist, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.giftname.setText(buygiftList.get(position).getGiftVoucherTitle());
            holder.giftprice.setText(SplashScreenActivity.currency_symbol + buygiftList.get(position).getGiftVoucherPurchaseAmount());
            Picasso.get().load(buygiftList.get(position).getGiftVoucherImage()).into(holder.giftimage);

        }

        @Override
        public int getItemCount() {
            return buygiftList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView giftprice, giftname;
            ImageView giftimage;


            public ViewHolder(View itemView) {
                super(itemView);
                giftprice = (TextView) itemView.findViewById(R.id.giftprice);
                giftname = (TextView) itemView.findViewById(R.id.giftname);
                giftimage = (ImageView) itemView.findViewById(R.id.giftimage);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(BuyGiftActivity.this, BuyGiftDetailsActivity.class);
                        intent.putExtra("giftname", buygiftList.get(position).getGiftVoucherTitle());
                        intent.putExtra("giftprice", buygiftList.get(position).getGiftVoucherPurchaseAmount());
                        intent.putExtra("giftdescription", buygiftList.get(position).getVoucher_long_description());
                        intent.putExtra("gifttermscondition", buygiftList.get(position).getVoucher_terms_description());
                        intent.putExtra("giftredemption", buygiftList.get(position).getVoucher_redeem_instructions_description());
                        intent.putExtra("giftcardID", buygiftList.get(position).getId());

                        AuthPreference authPreference = new AuthPreference(BuyGiftActivity.this);
                        authPreference.setBuyGiftImage(buygiftList.get(position).getGiftVoucherImage());
                        startActivity(intent);


                    }
                });
            }
        }
    }


}
