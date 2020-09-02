package com.justfoodz.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.interfaces.CartInterfacePlusMinus;
import com.justfoodz.models.SubMenuModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    String pound = SplashScreenActivity.currency_symbol;
    private String mRestaurantName, mRestaurantAddress, mId, mRestaurantCategoryId;
    private ArrayList<SubMenuModel> subMenuModelArrayList;
    private CartInterfacePlusMinus mCartInterface;

    public CartAdapter(CartInterfacePlusMinus cartInterface, Context context, String restaurantName, String restaurantAddress, String id, String restaurantCategoryId, ArrayList<SubMenuModel> cartFoods, int recyclerview_cart) {
        this.context = context;
        this.mRestaurantAddress = restaurantAddress;
        this.mRestaurantName = restaurantName;
        this.mId = id;
        this.mRestaurantCategoryId = restaurantCategoryId;
        this.subMenuModelArrayList = cartFoods;
        this.mCartInterface = cartInterface;
    }


    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_cart_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.MyViewHolder holder, final int position) {
        try {
            Double price = Double.parseDouble(subMenuModelArrayList.get(position).getRestaurantPizzaItemPrice());
            price = price * subMenuModelArrayList.get(position).getQuantity();
            holder.tvMenuItemName.setText(subMenuModelArrayList.get(position).getRestaurantPizzaItemName());
            holder.tvItemPrice.setText(pound.concat(String.format("%.2f", price)));
            holder.tvQuantity.setText("" + (subMenuModelArrayList.get(position).getQuantity()));

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
         holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartInterface.plusMinusMethod(position, "plus");

            }
        });
        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartInterface.plusMinusMethod(position, "minus");
            }
        });

        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartInterface.plusMinusMethod(position, "delete");
            }
        });
    }


    @Override
    public int getItemCount() {
        return subMenuModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMenuItemName, tvItemPrice, tvQuantity;
        private ImageView ivPlus, ivMinus, cartDelete;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvMenuItemName = itemView.findViewById(R.id.tv_menu_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            ivPlus = itemView.findViewById(R.id.iv_plus);
            ivMinus = itemView.findViewById(R.id.iv_minus);
            cartDelete = itemView.findViewById(R.id.cart_delete);


        }
    }
}
