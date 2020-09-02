package com.justfoodz.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.models.OrderFoodItemModel;

import java.util.ArrayList;

public class ViewOrderDetailCartAdapter extends RecyclerView.Adapter<ViewOrderDetailCartAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<OrderFoodItemModel> orderFoodItemModelArrayList;
    private OrderFoodItemModel orderFoodItemModel;
    String pound = SplashScreenActivity.currency_symbol;

    public ViewOrderDetailCartAdapter(Context context, ArrayList<OrderFoodItemModel> orderFoodItemModelArrayList) {
        this.context = context;
        this.orderFoodItemModelArrayList = orderFoodItemModelArrayList;
    }

    @NonNull
    @Override
    public ViewOrderDetailCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_payment_cart_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOrderDetailCartAdapter.MyViewHolder holder, int position) {

        try {
            orderFoodItemModel = orderFoodItemModelArrayList.get(position);
            holder.tvItemName.setText(orderFoodItemModelArrayList.get(position).getItemsName());

            holder.tvItemPrice.setText(pound.concat(orderFoodItemModel.getMenuprice()));
            holder.tvItemQuantity.setText("" + (orderFoodItemModel.getQuantity())+" X");


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (orderFoodItemModel.getExtraTopping().equals(""))
        {
            holder.tv_item_name_extra.setVisibility(View.INVISIBLE);
        }
        else {
            holder.tv_item_name_extra.setVisibility(View.VISIBLE);
            holder.tv_item_name_extra.setText(""+(orderFoodItemModel.getExtraTopping()));
        }
    }

    @Override
    public int getItemCount() {
        return orderFoodItemModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemQuantity, tvItemName, tvItemPrice,tv_item_name_extra;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tv_item_name_extra = itemView.findViewById(R.id.tv_item_name_extra);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
        }
    }
}
