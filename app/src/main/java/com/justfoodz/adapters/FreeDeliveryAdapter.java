package com.justfoodz.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.MainMenuActivity;
import com.justfoodz.activities.RestaurantsListActivity;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.models.RestaurantFreeDeliveryModel;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.utils.AuthPreference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

    public class FreeDeliveryAdapter extends RecyclerView.Adapter<FreeDeliveryAdapter.ViewHolder> {

        Context context;
        public static String restaurant_distance_check;
        ArrayList<RestaurantModel> freeDeliveryList;
        private AuthPreference authPreference;
        private String City;
        String replaceStr ="";
        FreeDeliveryAdapterInterface freeDeliveryAdapterInterface;

        public interface FreeDeliveryAdapterInterface {
            public void onClickPlaces(ArrayList<RestaurantModel> freeDeliveryList, int pos);
        }

        public FreeDeliveryAdapter(Context c, ArrayList<RestaurantModel> freedeliveryrList, String city) {
           try
           {
               this.context = c;
               this.freeDeliveryList = freedeliveryrList;
               authPreference = new AuthPreference(context);
               this.City = city;
           }
           catch (Exception ex)
           {
               ex.printStackTrace();
           }

        }

        @Override
        public FreeDeliveryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.freedeliverylayout, parent, false);
            return new FreeDeliveryAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final FreeDeliveryAdapter.ViewHolder holder, final int position) {

            holder.cuisinename.setText(freeDeliveryList.get(position).getRestaurant_name());
            Picasso.get().load(freeDeliveryList.get(position).getRestaurant_Logo()).into(holder.cuisineimage);
            holder.miles.setText(freeDeliveryList.get(position).getRestaurant_deliveryDistance());
            holder.txt_rating.setText(freeDeliveryList.get(position).getRatingValue());

            holder.timee.setText("(" + freeDeliveryList.get(position).getRestaurant_avarage_deliveryTime() + ")");
            holder.txt_view_total_review.setText("(" + freeDeliveryList.get(position).getTotalRestaurantReview() + ")");
            holder.txt_view_preorder.setText(freeDeliveryList.get(position).getRestaurantTimeStatus1());
            holder.txt_view_minorder.setText("$ " + freeDeliveryList.get(position).getRestaurant_minimumorder() + " Minimum Order");

            String YourString = freeDeliveryList.get(position).getRestaurant_address();

            if (freeDeliveryList.get(position).getRestaurantTimeStatus1().equalsIgnoreCase("Preorder") || freeDeliveryList.get(position).getRestaurantTimeStatus1().equalsIgnoreCase("Open"))
            {
                holder.txt_view_open_close.setText("Open");
            }
            if (freeDeliveryList.get(position).getRestaurantTimeStatus1().equalsIgnoreCase("Closed") || freeDeliveryList.get(position).getRestaurantTimeStatus1().equalsIgnoreCase("Busy"))
            {
                holder.txt_view_open_close.setText("Closed");
            }

            holder.featuredaddress.setText(freeDeliveryList.get(position).getRestaurant_address());
            holder.txt_view_cuisinename.setText(freeDeliveryList.get(position).getRestaurant_cuisine());

            Log.e("star", "" + freeDeliveryList.get(position).getRatingValue());
//           String ratingValue = pp.get(position).getRatingValue();
//
            if (freeDeliveryList.get(position).getRatingValue().equalsIgnoreCase("0")) {
               /* holder.iv_review1.setBackgroundResource(R.drawable.review_star);
                holder.iv_review2.setBackgroundResource(R.drawable.review_star);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);*/
                holder.rate_star.setBackgroundResource(R.drawable.review_star);

            }
            else
            {
                holder.rate_star.setBackgroundResource(R.drawable.review_yellow);

            }
            /*else if (freeDeliveryList.get(position).getRatingValue().equalsIgnoreCase("1")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_star);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (freeDeliveryList.get(position).getRatingValue().equalsIgnoreCase("2")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (freeDeliveryList.get(position).getRatingValue().equalsIgnoreCase("3")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (freeDeliveryList.get(position).getRatingValue().equalsIgnoreCase("4")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review4.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (freeDeliveryList.get(position).getRatingValue().equalsIgnoreCase("5")) {
                holder.iv_review1.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
                holder.iv_review2.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
                holder.iv_review3.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
                holder.iv_review4.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
                holder.iv_review5.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
            }*/
        }

        @Override
        public int getItemCount() {
            return freeDeliveryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView cuisinename, featuredaddress, timee, miles, txt_view_cuisinename, txt_view_total_review,txt_view_minorder, txt_view_preorder
                    ,txt_view_open_close, txt_rating;
            ImageView cuisineimage;
            ImageView ivBack, iv_review1, iv_review2, iv_review3, iv_review4, iv_review5, rate_star;

            public ViewHolder(final View itemView) {
                super(itemView);
                cuisinename = (TextView) itemView.findViewById(R.id.featuredname);
                cuisineimage = (ImageView) itemView.findViewById(R.id.featuredimage);
                featuredaddress = (TextView) itemView.findViewById(R.id.featuredaddress);
                iv_review1 = itemView.findViewById(R.id.review_1);
                iv_review2 = itemView.findViewById(R.id.review_2);
                iv_review3 = itemView.findViewById(R.id.review_3);
                iv_review4 = itemView.findViewById(R.id.review_4);
                iv_review5 = itemView.findViewById(R.id.review_5);
                rate_star = itemView.findViewById(R.id.rate_star);
                txt_rating = itemView.findViewById(R.id.txt_rating);
                miles = itemView.findViewById(R.id.miles);
                timee = itemView.findViewById(R.id.timee);
                txt_view_cuisinename = itemView.findViewById(R.id.txt_view_cuisinename);
                txt_view_total_review = itemView.findViewById(R.id.txt_view_total_review);
                txt_view_minorder = itemView.findViewById(R.id.txt_view_minorder);
                txt_view_preorder = itemView.findViewById(R.id.txt_view_preorder);
                txt_view_open_close = itemView.findViewById(R.id.txt_view_open_close);



                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
//                freeDeliveryAdapterInterface.onClickPlaces( freeDeliveryList, position);

                try {
                    if (freeDeliveryList.get(position).getRestaurantTimeStatus1().equals("Closed") || freeDeliveryList.get(position).getRestaurantTimeStatus1().equals("Busy")) {
                        showCustomDialog1decline(freeDeliveryList.get(position).getRestaurantTimeStatusText());
                    } else {

                        restaurant_distance_check = freeDeliveryList.get(position).getrestaurant_distance_check();
                        RestaurantsListActivity.res_lat = freeDeliveryList.get(position).getRestaurant_LatitudePoint();
                        RestaurantsListActivity.res_lng = freeDeliveryList.get(position).getRestaurant_LongitudePoint();
                        RestaurantsListActivity.cash_avaible = freeDeliveryList.get(position).getRestaurant_onlycashonAvailable();
                        RestaurantsListActivity.card_avaible = freeDeliveryList.get(position).getRestaurant_cardacceptAvailable();


                        Intent intent = new Intent(context, MainMenuActivity.class);
                        String id = freeDeliveryList.get(position).getId();
                        RestaurantModel restaurantModel = freeDeliveryList.get(position);
                        authPreference.setRestraurantCity("" + freeDeliveryList.get(position).getRestaurantCity());

                        authPreference.setMinprice(freeDeliveryList.get(position).getRestaurant_minimumorder());
                        intent.putExtra("restaurantModel", (Serializable) restaurantModel);
                        intent.putExtra("restaurantModelArrayListReceived", freeDeliveryList);
                        intent.putExtra("ratingValue", restaurantModel.getRatingValue());
                        intent.putExtra("cityname", City);
                        intent.putExtra("TotalRestaurantReview", restaurantModel.getTotalRestaurantReview());
                        intent.putExtra("Booktable", restaurantModel.getBookaTable());
                        context.startActivity(intent);
                    }


                } catch (Exception e) {

                }


            }
        }

        private void showCustomDialog1decline(String s) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("" + s);

            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton("OK", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();

        }

    }
