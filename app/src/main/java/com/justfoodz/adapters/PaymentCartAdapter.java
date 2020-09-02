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
import com.justfoodz.models.SubMenuModel;

import java.util.ArrayList;

public class PaymentCartAdapter extends RecyclerView.Adapter<PaymentCartAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<SubMenuModel> subMenuModelArrayList = null;
    String pound = SplashScreenActivity.currency_symbol;

    public PaymentCartAdapter(Context context, ArrayList<SubMenuModel> cartFoods) {
        this.mContext = context;
        this.subMenuModelArrayList = cartFoods;
    }

    @NonNull
    @Override
    public PaymentCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_payment_cart_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentCartAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvItemName.setText(subMenuModelArrayList.get(position).getRestaurantPizzaItemName());
            holder.tvItemPrice.setText(pound.concat(subMenuModelArrayList.get(position).getRestaurantPizzaItemPrice()));
            holder.tvItemQuantity.setText("" + (subMenuModelArrayList.get(position).getQuantity()));



        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return subMenuModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemQuantity, tvItemName, tvItemPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
        }
    }
}
