package com.justfoodz.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.interfaces.MenuInterface;
import com.justfoodz.models.ExtrasModel;

import java.util.ArrayList;

public class ExtraAdapter extends RecyclerView.Adapter<ExtraAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ExtrasModel> extrasModelArrayList;
    private ExtrasModel extrasModel;
    String pound = SplashScreenActivity.currency_symbol;
    private MenuInterface mMenuInterface;
    private int lastSelectedPosition = -1;



    public ExtraAdapter(Context context, MenuInterface menuInterface, ArrayList<ExtrasModel> extrasModelArrayList) {
        this.context = context;
        this.extrasModelArrayList = extrasModelArrayList;
        this.mMenuInterface = menuInterface;
    }

    @NonNull
    @Override
    public ExtraAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_extra_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExtraAdapter.MyViewHolder holder, final int position) {
        extrasModel = extrasModelArrayList.get(position);
        holder.tvItemSize.setText(extrasModel.getFood_Addons());
        holder.tvItemSizePrice.setText(pound.concat(extrasModel.getFood_PriceAddons()));
        String addOnSelection = extrasModelArrayList.get(position).getFood_addonsselection();
        //Condition for add Extra with single selection from radio button

        holder.radioButton.setChecked(lastSelectedPosition == position);

        if (addOnSelection.equalsIgnoreCase("2") || addOnSelection.equalsIgnoreCase("3")) {
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
//            if (extrasModel.isTrue()) {
//                holder.radioButton.setBackgroundResource(R.drawable.radio_button);
//            } else {
//                holder.radioButton.setBackgroundResource(R.drawable.un_selected_button);
//            }
//            holder.radioButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean isTrue = extrasModelArrayList.get(position).isTrue();
//                    if (isTrue != true) {
//                        mMenuInterface.menuItemPosition(true, position);
//                    } else {
//                        mMenuInterface.menuItemPosition(false, position);
//
//                    }
//                }
//            });
        }
        if (addOnSelection.equalsIgnoreCase("1")) {
            //Condition for add Extra with multiple Selection
            holder.radioButton.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    holder.checkBox.setBackgroundResource(R.drawable.checkbox_selected);
//                    boolean isTrue = extrasModelArrayList.get(position).isTrue();
//                    if (isTrue != true) {
//                        mMenuInterface.menuItemPosition(true, position);
//                    } else {
//                        mMenuInterface.menuItemPosition(false, position);
//                    }
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return extrasModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout;
        private TextView tvItemSize, tvItemSizePrice;
        private RadioButton radioButton;
        private CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            // layout = itemView.findViewById(R.id.layout);
            tvItemSize = itemView.findViewById(R.id.tv_item_size);
            tvItemSizePrice = itemView.findViewById(R.id.tv_item_size_price);
            radioButton = itemView.findViewById(R.id.radioButton);
            checkBox = itemView.findViewById(R.id.checkBox);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    int position = getAdapterPosition();
                    mMenuInterface.menuItemPosition(true, position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}