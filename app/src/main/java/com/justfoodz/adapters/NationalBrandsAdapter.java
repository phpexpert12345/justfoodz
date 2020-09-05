package com.justfoodz.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.activities.QuickSearchLocation;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.models.FeaturedPartnerModel;
import com.justfoodz.models.RestaurantNatonalBrandsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


    public class NationalBrandsAdapter extends RecyclerView.Adapter<NationalBrandsAdapter.ViewHolder> {

        Context context;
        ArrayList<RestaurantNatonalBrandsModel> pp;

        public NationalBrandsAdapter(Context c, ArrayList<RestaurantNatonalBrandsModel> p) {
            this.context = c;
            this.pp = p;
        }

        @Override
        public NationalBrandsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.national_brands_layout, parent, false);
            return new NationalBrandsAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final NationalBrandsAdapter.ViewHolder holder, final int position) {
            holder.servicename.setText(pp.get(position).getRestaurantCuisineName());
            Picasso.get().load(pp.get(position).getCuisine_img()).into(holder.serviceimage);
        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView servicename;
            //   CircleImageView serviceimage;
            ImageView serviceimage;


            public ViewHolder(View itemView) {
                super(itemView);
                servicename = itemView.findViewById(R.id.servicename);
                //   serviceimage = (CircleImageView) itemView.findViewById(R.id.serviceimage);
                serviceimage = itemView.findViewById(R.id.serviceimage);

                itemView.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, QuickSearchLocation.class);
                    intent.putExtra("serviceName", pp.get(position).getRestaurantCuisineName());
                    intent.putExtra("data", "1");
                    context.startActivity(intent);
                });
            }
        }
    }

