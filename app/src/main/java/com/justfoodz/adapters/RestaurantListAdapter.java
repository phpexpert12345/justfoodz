package com.justfoodz.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.RestaurantsListActivity;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.interfaces.RestaurantItemClickListener;
import com.justfoodz.models.RestaurantModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.logging.Handler;

import static android.content.Context.MODE_PRIVATE;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<RestaurantModel> restaurantModelArrayList;//original list
    private RestaurantModel restaurantModel;
    String pound = SplashScreenActivity.currency_symbol;
    private ArrayList<RestaurantModel> restaurantArrayListFiltered;
    private RestaurantItemClickListener restaurantItemClickListener;
    SharedPreferences sharedPreferences;

    public RestaurantListAdapter(Context context, ArrayList<RestaurantModel> restaurantModelArrayList, RestaurantItemClickListener restaurantItemClickListener) {
        this.context = context;
        this.restaurantModelArrayList = restaurantModelArrayList;
        this.restaurantArrayListFiltered = restaurantModelArrayList;
        this.restaurantItemClickListener = restaurantItemClickListener;
    }

    public void setRestaurantList(Context context, final ArrayList<RestaurantModel> restaurantModelArrayList) {
        this.context = context;
        if (this.restaurantModelArrayList == null) {
            this.restaurantModelArrayList = restaurantModelArrayList;
            this.restaurantArrayListFiltered = restaurantModelArrayList;
            notifyItemChanged(0, restaurantArrayListFiltered.size());

        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RestaurantListAdapter.this.restaurantModelArrayList.size();
                }

                @Override
                public int getNewListSize() {
                    return restaurantModelArrayList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RestaurantListAdapter.this.restaurantModelArrayList.get(oldItemPosition).getRestaurant_name() == restaurantModelArrayList.get(newItemPosition).getRestaurant_name();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    RestaurantModel newRestaurant = RestaurantListAdapter.this.restaurantModelArrayList.get(oldItemPosition);

                    RestaurantModel oldRestaurant = restaurantModelArrayList.get(newItemPosition);

                    return newRestaurant.getRestaurant_name() == oldRestaurant.getRestaurant_name();
                }
            });
            this.restaurantModelArrayList = restaurantModelArrayList;
            this.restaurantArrayListFiltered = restaurantModelArrayList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public RestaurantListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_restaurants_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPreferences = context.getSharedPreferences("HotelName", MODE_PRIVATE);
        holder.rlRestaurantList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("hname", holder.tvRestaurantName.getText().toString());
                editor.commit();

                restaurantItemClickListener.restaurantItemClick(position);

            }
        });

        holder.share.setOnClickListener(view -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            sharingIntent.setType("text/plain");
            int pos = holder.getAdapterPosition();
            String tempName = restaurantArrayListFiltered.get(pos).getSocialSharingMessage();
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Order Food");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, tempName);
            Intent startIntent = Intent.createChooser(sharingIntent, "Share using");
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);


        });
        try {
            restaurantModel = restaurantModelArrayList.get(position);
            holder.tvRestaurantName.setText(restaurantArrayListFiltered.get(position).getRestaurant_name());
            holder.tvRestaurantAddress.setText(restaurantModel.getRestaurant_address());
            holder.tvRestaurantMinOrderPrice.setText(pound.concat(restaurantModel.getRestaurant_minimumorder()));
            holder.tvItemDiscountCost.setText((restaurantModel.getRestaurantOfferPrice()));


            if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Open")) {
                holder.tvItemOrderStatus.setImageDrawable(context.getDrawable(R.drawable.openred));
            }
            if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Preorder")) {
                holder.tvItemOrderStatus.setImageDrawable(context.getDrawable(R.drawable.preordergreen));

            }
            if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Closed")) {
                holder.tvItemOrderStatus.setImageDrawable(context.getDrawable(R.drawable.closedred));

            }
            if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Busy")) {
                holder.tvItemOrderStatus.setImageDrawable(context.getDrawable(R.drawable.busy));

            }
            String image = restaurantModel.getRestaurant_Logo();
            Picasso.get().load(restaurantModel.getRestaurant_Logo()).into(holder.ivRestaurantLogo);
            String ratingCount = (restaurantModel.getRatingValue());
            if (ratingCount.equalsIgnoreCase("0")) {
                holder.review_1.setBackgroundResource(R.drawable.review_star);
                holder.review_2.setBackgroundResource(R.drawable.review_star);
                holder.review_3.setBackgroundResource(R.drawable.review_star);
                holder.review_4.setBackgroundResource(R.drawable.review_star);
                holder.review_5.setBackgroundResource(R.drawable.review_star);

            } else if (ratingCount.equalsIgnoreCase("1")) {
                holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                holder.review_2.setBackgroundResource(R.drawable.review_star);
                holder.review_3.setBackgroundResource(R.drawable.review_star);
                holder.review_4.setBackgroundResource(R.drawable.review_star);
                holder.review_5.setBackgroundResource(R.drawable.review_star);

            } else if (ratingCount.equalsIgnoreCase("2")) {
                holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                holder.review_3.setBackgroundResource(R.drawable.review_star);
                holder.review_4.setBackgroundResource(R.drawable.review_star);
                holder.review_5.setBackgroundResource(R.drawable.review_star);

            } else if (ratingCount.equalsIgnoreCase("3")) {
                holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                holder.review_3.setBackgroundResource(R.drawable.review_yellow);
                holder.review_4.setBackgroundResource(R.drawable.review_star);
                holder.review_5.setBackgroundResource(R.drawable.review_star);

            } else if (ratingCount.equalsIgnoreCase("4")) {
                holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                holder.review_3.setBackgroundResource(R.drawable.review_yellow);
                holder.review_4.setBackgroundResource(R.drawable.review_yellow);
                holder.review_5.setBackgroundResource(R.drawable.review_star);

            } else if (ratingCount.equalsIgnoreCase("5")) {
                holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                holder.review_3.setBackgroundResource(R.drawable.review_yellow);
                holder.review_4.setBackgroundResource(R.drawable.review_yellow);
                holder.review_5.setBackgroundResource(R.drawable.review_yellow);
            }

            //////icon/////
            if (restaurantModel.getKidFriendly().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                holder.kidgreen.setVisibility(View.VISIBLE);
                holder.kidred.setVisibility(View.GONE);

            } else {
                holder.kidred.setVisibility(View.VISIBLE);
                holder.kidgreen.setVisibility(View.GONE);
            }

            if (restaurantModel.getPetFriendly().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                holder.petgreen.setVisibility(View.VISIBLE);
                holder.petred.setVisibility(View.GONE);

            } else {
                holder.petred.setVisibility(View.VISIBLE);
                holder.petgreen.setVisibility(View.GONE);
            }

            if (restaurantModel.getDelivery().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                holder.deliverycheck.setVisibility(View.VISIBLE);
                holder.deliverynotcheck.setVisibility(View.GONE);

            } else {
                holder.deliverynotcheck.setVisibility(View.VISIBLE);
                holder.deliverycheck.setVisibility(View.GONE);
            }

            if (restaurantModel.getCollection().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                holder.collectioncheck.setVisibility(View.VISIBLE);
                holder.collectionnotcheck.setVisibility(View.GONE);

            } else {
                holder.collectionnotcheck.setVisibility(View.VISIBLE);
                holder.collectioncheck.setVisibility(View.GONE);
            }
            if (restaurantModel.getDINEin().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                holder.dinincheck.setVisibility(View.VISIBLE);
                holder.dininnotcheck.setVisibility(View.GONE);

            } else {
                holder.dininnotcheck.setVisibility(View.VISIBLE);
                holder.dinincheck.setVisibility(View.GONE);
            }

            if (restaurantModel.getBookaTable().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                holder.booktablecheck.setVisibility(View.VISIBLE);
                holder.booktablenotcheck.setVisibility(View.GONE);

            } else {
                holder.booktablenotcheck.setVisibility(View.VISIBLE);
                holder.booktablecheck.setVisibility(View.GONE);
            }
            if (restaurantModel.getDineInAvailable().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                holder.dinincheck.setVisibility(View.VISIBLE);
                holder.dininnotcheck.setVisibility(View.GONE);

            } else {
                holder.dininnotcheck.setVisibility(View.VISIBLE);
                holder.dinincheck.setVisibility(View.GONE);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (restaurantModelArrayList != null) {
            return restaurantArrayListFiltered.size();
        } else {
            return 0;
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    restaurantArrayListFiltered = restaurantModelArrayList;
                } else {
                    ArrayList<RestaurantModel> filteredList = new ArrayList<>();
                    for (RestaurantModel restaurantModel : restaurantModelArrayList) {
                        if (restaurantModel.getRestaurant_name().toLowerCase().contains(charString)) {
                            filteredList.add(restaurantModel);
                        }
                    }
                    restaurantArrayListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = restaurantArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                restaurantArrayListFiltered = (ArrayList<RestaurantModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlRestaurantList;
        LinearLayout share;
        private TextView tvRestaurantName, tvRestaurantMinOrderPrice,
                tvRestaurantAddress, tvItemDiscount, tvItemDiscountCost;
        private ImageView ivRestaurantLogo, review_1, review_2, review_3, review_4, review_5;
        private ImageView kidred, kidgreen, petred, petgreen, deliverynotcheck, deliverycheck, collectioncheck, collectionnotcheck, dininnotcheck, dinincheck, booktablenotcheck, booktablecheck;
        public ImageView tvItemOrderStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlRestaurantList = itemView.findViewById(R.id.rl_restaurant_list);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
            tvRestaurantMinOrderPrice = itemView.findViewById(R.id.tv_restaurant_min_order_price);
            tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurant_address);
            tvItemOrderStatus = itemView.findViewById(R.id.tv_item_order_status);
            tvItemDiscount = itemView.findViewById(R.id.tv_item_discount);
            tvItemDiscountCost = itemView.findViewById(R.id.tv_item_discount_cost);
            ivRestaurantLogo = itemView.findViewById(R.id.iv_restaurant_logo);
            review_1 = itemView.findViewById(R.id.review_1);
            review_2 = itemView.findViewById(R.id.review_2);
            review_3 = itemView.findViewById(R.id.review_3);
            review_4 = itemView.findViewById(R.id.review_4);
            review_5 = itemView.findViewById(R.id.review_5);
            kidred = itemView.findViewById(R.id.kidred);
            kidgreen = itemView.findViewById(R.id.kidgreen);
            petred = itemView.findViewById(R.id.petred);
            petgreen = itemView.findViewById(R.id.petgreen);
            deliverynotcheck = itemView.findViewById(R.id.deliverynotcheck);
            deliverycheck = itemView.findViewById(R.id.deliverycheck);
            collectioncheck = itemView.findViewById(R.id.collectioncheck);
            collectionnotcheck = itemView.findViewById(R.id.collectionnotcheck);
            dininnotcheck = itemView.findViewById(R.id.dininnotcheck);
            dinincheck = itemView.findViewById(R.id.dinincheck);
            booktablenotcheck = itemView.findViewById(R.id.booktablenotcheck);
            booktablecheck = itemView.findViewById(R.id.booktablecheck);
            share = itemView.findViewById(R.id.share);
        }
    }
}