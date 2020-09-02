package com.justfoodz.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.justfoodz.CalculateDistanceTime;
import com.justfoodz.R;

import com.justfoodz.activities.RestaurantsListActivity;
import com.justfoodz.activities.SelectAddressActivity;
import com.justfoodz.interfaces.AddressEditItemClickListener;
import com.justfoodz.models.AddressModel;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AddressModel> aaddressModelArrayList;
    private AddressModel addressModel;
    private AddressEditItemClickListener addressEditItemClickListener;
    private String date, time, itemId, id;
    RadioButton globalradio=null;
    double lt1,ln1,lt2,ln2;


    public AddressAdapter(Context context, ArrayList<AddressModel> addressModelArrayList, AddressEditItemClickListener addressEditItemClickListener) {
        this.context = context;
        this.aaddressModelArrayList = addressModelArrayList;
        this.addressEditItemClickListener = addressEditItemClickListener;
    }

    @NonNull
    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_address_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddressAdapter.MyViewHolder holder, final int position) {
        addressModel = aaddressModelArrayList.get(position);
        holder.tvUserAddressTitle.setText(aaddressModelArrayList.get(position).getAddress_title());
        holder.tvUserAddress.setText(aaddressModelArrayList.get(position).getAddress());
        holder.tvUserPhoneNumber.setText(aaddressModelArrayList.get(position).getUser_phone());
//       holder.tvEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addressEditItemClickListener.addressEditItemClick(position);
//            }
//        });


//        holder.checkBox.setOnCheckedChangeListener(null);
//        holder.checkBox.setSelected(addressModel.isTrue());
//        if (holder.checkBox.isChecked()) {
//            holder.checkBox.setChecked(false);
//        } else {
//            holder.checkBox.setChecked(true);
//        }
//
//        if (!holder.checkBox.isChecked()) {
//            Toast.makeText(context, R.string.select_address_txt, Toast.LENGTH_SHORT).show();
//
//        } else {
//            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                    if (isChecked) {
//                        holder.checkBox.setChecked(true);
//                        addressEditItemClickListener.singleAddressItemClick(true, position);
//                    } else {
//                        holder.checkBox.setChecked(false);
//                        addressEditItemClickListener.singleAddressItemClick(false, position);
//                    }
//                }
//            });
//        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectAddressActivity.rbcurrent.setChecked(false);
                RadioButton checked_rb = (RadioButton) v;
                lt1 = Double.parseDouble(RestaurantsListActivity.res_lat);
                ln1 = Double.parseDouble(RestaurantsListActivity.res_lng);
                lt2 = Double.parseDouble(aaddressModelArrayList.get(position).getcustomer_address_lat());
                ln2 = Double.parseDouble(aaddressModelArrayList.get(position).getcustomer_address_long());
                final LatLng startLatLng = new LatLng(lt1, ln1);
                final LatLng endLatLng = new LatLng(lt2, ln2);
                CalculateDistanceTime distance_task = new CalculateDistanceTime(context);

                distance_task.getDirectionsUrl(startLatLng, endLatLng);

                distance_task.setLoadListener(new CalculateDistanceTime.taskCompleteListener() {
                    @Override
                    public void taskCompleted(String[] time_distance) {
                        Log.e("final",""+time_distance[1]+"  "+time_distance[0]);
                        String final_distance = time_distance[0];
                        final_distance = final_distance.replaceAll("km", "");
                        SelectAddressActivity.dist = Double.parseDouble(final_distance);
                    }

                });
                SelectAddressActivity.miles = 0.621371 * SelectAddressActivity.dist;
                SelectAddressActivity.miles = Double.parseDouble(String.format("%.2f",  SelectAddressActivity.miles));
                SelectAddressActivity.address_id = ""+aaddressModelArrayList.get(position).getId();

                if(globalradio != null){
                    globalradio.setChecked(false);
                   // addressEditItemClickListener.singleAddressItemClick(true, position);
                }
                else {

                }
                globalradio=checked_rb;
            }
        });



        holder.tvEdit.setText("\u22EE");
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.tvEdit);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu1);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu11:
                                //handle menu1 click
                                addressEditItemClickListener.addressEditItemClick(position);
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
        return aaddressModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserAddressTitle, tvUserPhoneNumber, tvUserAddress, tvEdit;
        private RadioButton checkBox;
        private LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvUserAddressTitle = itemView.findViewById(R.id.tv_address_title);
            tvUserAddress = itemView.findViewById(R.id.tv_user_address);
            tvUserPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            checkBox = itemView.findViewById(R.id.checkBox);
            tvEdit = itemView.findViewById(R.id.tv_edit_address);
            layout = itemView.findViewById(R.id.layout);

        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        SelectAddressActivity.dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        SelectAddressActivity.dist = Math.acos(SelectAddressActivity.dist);
        SelectAddressActivity.dist = rad2deg(SelectAddressActivity.dist);
        SelectAddressActivity.dist = SelectAddressActivity.dist * 60 * 1.1515;
        return (SelectAddressActivity.dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void refreshEvents(ArrayList<AddressModel> events) {
        this.aaddressModelArrayList.clear();
        this.aaddressModelArrayList.addAll(events);
        notifyDataSetChanged();
    }
}
