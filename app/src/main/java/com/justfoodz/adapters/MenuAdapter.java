package com.justfoodz.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.fragments.MenuFragment;
import com.justfoodz.interfaces.MenuItemInterface;
import com.justfoodz.models.MenuModel;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.models.SubList.RestaurantMencategory;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private Context context;
    private List<RestaurantMencategory> menuModelArrayList;
    private RestaurantMencategory menuModel;
    private MenuItemInterface menuItemInterface;

    public MenuAdapter(Context context, MenuItemInterface menuItemInterface, ArrayList<RestaurantMencategory> menuModelArrayList) {
        this.context = context;
        this.menuModelArrayList = menuModelArrayList;
        this.menuItemInterface=menuItemInterface;
    }

    public MenuAdapter(Context context, MenuFragment menuItemInterface, List<RestaurantMencategory> menuModelArrayList) {
        this.context = context;
        this.menuModelArrayList = menuModelArrayList;
        this.menuItemInterface=menuItemInterface;
    }

    @NonNull
    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_menu_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MyViewHolder holder, final int position) {


        menuModel = menuModelArrayList.get(position);
        holder.tvMenuCategoryName.setText(menuModel.getCategoryName());

        holder.rlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               menuItemInterface.menuItemPosition(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return menuModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMenuCategoryName;
        private RelativeLayout rlLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMenuCategoryName = itemView.findViewById(R.id.tv_menu_category_name);
            rlLayout = itemView.findViewById(R.id.rl_layout);
        }
    }
}
