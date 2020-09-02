package com.justfoodz.adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.WriteReviewActivity;
import com.justfoodz.interfaces.witereview;
import com.justfoodz.models.OrderModel;

import java.util.ArrayList;

public class RestaurantNameAdapter extends RecyclerView.Adapter<RestaurantNameAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<OrderModel> mOrderModelArrayList;
    private OrderModel orderModel;

    private witereview writereviewlist;

    public RestaurantNameAdapter(Context context, ArrayList<OrderModel> mOrderModelArrayList,witereview writereviewlist) {
        this.context = context;
        this.mOrderModelArrayList = mOrderModelArrayList;
        this.writereviewlist = writereviewlist;
    }

    @NonNull
    @Override
    public RestaurantNameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_restaurant_name, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantNameAdapter.MyViewHolder holder,final int position) {
        orderModel = mOrderModelArrayList.get(position);
        holder.tvRestaurantName.setText(orderModel.getRestaurant_name()+" ("+orderModel.getOrder_identifyno()+")");

        holder.rlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                writereviewlist.writereviewlist(position);
                Intent intent = new Intent(context, WriteReviewActivity.class);
                intent.putExtra("restnameposition", position);
                ((WriteReviewActivity)context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurantName,tvrestnamedeep;
        private RelativeLayout rlLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
            rlLayout = itemView.findViewById(R.id.write_touch);
            tvrestnamedeep = itemView.findViewById(R.id.rl_restaurant_name);

        }
    }
}