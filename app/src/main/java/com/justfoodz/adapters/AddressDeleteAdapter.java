package com.justfoodz.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.interfaces.AddressItemClickListener;
import com.justfoodz.models.AddressModel;

import java.util.ArrayList;

public class AddressDeleteAdapter extends RecyclerView.Adapter<AddressDeleteAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AddressModel> addressModelArrayList;
    private AddressModel addressModel;
    private AddressItemClickListener addressItemClickListener;

    public AddressDeleteAdapter(Context context, ArrayList<AddressModel> addressModelArrayList, AddressItemClickListener addressItemClickListener) {
        this.context = context;
        this.addressModelArrayList = addressModelArrayList;
        this.addressItemClickListener = addressItemClickListener;
    }

    @NonNull
    @Override
    public AddressDeleteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_delete_address_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddressDeleteAdapter.MyViewHolder holder, final int position) {

        holder.textViewOptions.setText("\u22EE");
        addressModel = addressModelArrayList.get(position);
        holder.tvUserAddressTitle.setText(addressModelArrayList.get(position).getAddress_title());
        holder.tvUserAddress.setText(addressModelArrayList.get(position).getAddress());
        holder.tvUserPhoneNumber.setText(addressModelArrayList.get(position).getUser_phone());
//        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addressItemClickListener.addressEditItemClick(position);
//
//
//            }
//        });
//        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addressItemClickListener.addressDeleteItemClick(position);
//
//
//            }
//        });

        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //handle menu1 click
                                addressItemClickListener.addressEditItemClick(position);
                                return true;
                            case R.id.menu2:
                                //handle menu2 click
                                addressItemClickListener.addressDeleteItemClick(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserAddressTitle, tvUserPhoneNumber, tvUserAddress,textViewOptions;
        private ImageView ivDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvUserAddressTitle = itemView.findViewById(R.id.tv_address_title);
            tvUserAddress = itemView.findViewById(R.id.tv_user_address);
            tvUserPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);
          //  tvEdit = itemView.findViewById(R.id.tv_edit_address);
           // ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
