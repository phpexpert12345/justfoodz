package com.justfoodz.adapters;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.interfaces.MenuInterface;
import com.justfoodz.models.MenuSizeModel;
import java.util.ArrayList;
public class MenuSizeAdapter extends RecyclerView.Adapter<MenuSizeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<MenuSizeModel> menuSizeModelArrayList;
    private MenuSizeModel menuSizeModel;
    String pound = SplashScreenActivity.currency_symbol;
    private MenuInterface mMenuInterface;
    public int mSelectedItem = -1;
    private RadioButton lastCheckedRB = null;
    public MenuSizeAdapter(Context context, MenuInterface menuInterface, ArrayList<MenuSizeModel> menuSizeModelArrayList) {
        this.context = context;
        this.menuSizeModelArrayList = menuSizeModelArrayList;
        this.mMenuInterface = menuInterface;
    }
    @NonNull
    @Override
    public MenuSizeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_extra_item_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MenuSizeAdapter.MyViewHolder holder, final int position) {
        menuSizeModel = menuSizeModelArrayList.get(position);
        holder.tvItemSize.setText(menuSizeModel.getFoodItemName().concat(" ").concat(menuSizeModel.getRestaurantPizzaItemName()));
        holder.tvItemSizePrice.setText(pound.concat(menuSizeModel.getRestaurantPizzaItemPrice()));
        holder.tvItemSizeid =  menuSizeModel.getId();
//        if (menuSizeModel.isTrue()) {
//            holder.radioButton.setBackgroundResource(R.drawable.radio_button);
//        } else {
//            holder.radioButton.setBackgroundResource(R.drawable.un_selected_button);
//        }
//        holder.radioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                boolean istrue = menuSizeModelArrayList.get(position).isTrue();
////                if (istrue != true) {
////                    mMenuInterface.menuItemPosition(true, position);
////                    holder.radioButton.setBackgroundResource(R.drawable.radio_button);
////                } else {
////                    mMenuInterface.menuItemPosition(false, position);
////                    holder.radioButton.setBackgroundResource(R.drawable.un_selected_button);
////
////                }
//                holder.radioButton.setBackgroundResource(R.drawable.radio_button);
//            }
//        });
        holder.radioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checked_rb = (RadioButton) radioGroup.findViewById(i);
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = checked_rb;
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuSizeModelArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private LinearLayout layout;
        private TextView tvItemSize, tvItemSizePrice;
        private  Integer tvItemSizeid;
        private RadioGroup radioButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemSize = itemView.findViewById(R.id.tv_item_size);
            tvItemSizePrice = itemView.findViewById(R.id.tv_item_size_price);
            radioButton = itemView.findViewById(R.id.radioButton);
//            radioButton.setBackgroundResource(R.drawable.un_selected_button);
           /* View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, menuSizeModelArrayList.size());
                }
            };
            itemView.setOnClickListener(clickListener);
            radioButton.setOnClickListener(clickListener);*/
        }
    }
}