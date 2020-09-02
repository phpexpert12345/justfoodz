package com.justfoodz.activities;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;

import java.util.ArrayList;
import java.util.List;

public class ApplyCouponActivity extends AppCompatActivity {

    RecyclerView giftrecycler;
    ArrayList<String> GiftRadeem;
    ApplyCouponAdapter applyCouponAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        GiftRadeem= new ArrayList<>();
        GiftRadeem.add("1234545667");
        GiftRadeem.add("5466777798");
        GiftRadeem.add("2345657689");
        GiftRadeem.add("9878574788");
        GiftRadeem.add("9865432343");
        GiftRadeem.add("1234545667");
        GiftRadeem.add("5466777798");
        GiftRadeem.add("2345657689");
        GiftRadeem.add("9878574788");
        GiftRadeem.add("9865432343");


        giftrecycler=(RecyclerView)findViewById(R.id.couponrecycler);

        giftrecycler.setLayoutManager(new LinearLayoutManager(this));
        applyCouponAdapter = new ApplyCouponAdapter(this, GiftRadeem);
        giftrecycler.setAdapter(applyCouponAdapter);
    }

    public class ApplyCouponAdapter extends RecyclerView.Adapter<ApplyCouponAdapter.ViewHolder> {

        private List<String> mData;
        private LayoutInflater mInflater;


        // data is passed into the constructor
        ApplyCouponAdapter(Context context, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.applycouponlist, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String animal = mData.get(position);
            holder.myTextView.setText(animal);
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView myTextView;

            ViewHolder(View itemView) {
                super(itemView);
                myTextView = itemView.findViewById(R.id.voucher);
                itemView.setOnClickListener(this);
            }


            @Override
            public void onClick(View view) {

            }
        }





    }
}
