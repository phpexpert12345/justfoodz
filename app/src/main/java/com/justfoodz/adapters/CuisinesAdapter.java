package com.justfoodz.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.R;
import com.justfoodz.interfaces.CuisinesItemClickListener;
import com.justfoodz.models.CuisinesModel;

import java.util.ArrayList;

public class CuisinesAdapter extends RecyclerView.Adapter<CuisinesAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<CuisinesModel> cuisinesModelArrayList;
    private CuisinesModel cuisinesModel;
    private ArrayList<CuisinesModel> cuisinesFilteredList;
    private CuisinesItemClickListener cuisinesItemClickListener;


    public CuisinesAdapter(Context context, ArrayList<CuisinesModel> cuisinesModelArrayList, CuisinesItemClickListener cuisinesItemClickListener) {
        this.context = context;
        this.cuisinesModelArrayList = cuisinesModelArrayList;
        this.cuisinesFilteredList = cuisinesModelArrayList;
        this.cuisinesItemClickListener = cuisinesItemClickListener;
    }


    public void setCuisineList(Context context, final ArrayList<CuisinesModel> cuisinesModelArrayList) {
        this.context = context;
        if (this.cuisinesModelArrayList == null) {
            this.cuisinesModelArrayList = cuisinesModelArrayList;
            this.cuisinesFilteredList = cuisinesModelArrayList;
            notifyItemChanged(0, cuisinesFilteredList.size());

        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CuisinesAdapter.this.cuisinesModelArrayList.size();
                }

                @Override
                public int getNewListSize() {
                    return cuisinesModelArrayList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return CuisinesAdapter.this.cuisinesModelArrayList.get(oldItemPosition).getCuisine_name() == cuisinesModelArrayList.get(newItemPosition).getCuisine_name();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    CuisinesModel newCuisines = CuisinesAdapter.this.cuisinesModelArrayList.get(oldItemPosition);

                    CuisinesModel oldCuisines = cuisinesModelArrayList.get(newItemPosition);

                    return newCuisines.getCuisine_name() == oldCuisines.getCuisine_name();
                }
            });
            this.cuisinesModelArrayList = cuisinesModelArrayList;
            this.cuisinesFilteredList = cuisinesModelArrayList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public CuisinesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cuisines_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CuisinesAdapter.MyViewHolder holder, final int position) {
        cuisinesModel = cuisinesModelArrayList.get(position);
        holder.tvCuisinesName.setText(cuisinesFilteredList.get(position).getCuisine_name());
        holder.cuisinesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisinesItemClickListener.cuisinesItemClick(position, cuisinesFilteredList.get(position).getCuisine_name());
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        if (cuisinesModelArrayList != null) {
            return cuisinesFilteredList.size();
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
                    cuisinesFilteredList = cuisinesModelArrayList;
                } else {
                    ArrayList<CuisinesModel> filteredList = new ArrayList<>();
                    for (CuisinesModel cuisinesModel : cuisinesModelArrayList) {
                        if (cuisinesModel.getCuisine_name().toLowerCase().contains(charString)) {
                            filteredList.add(cuisinesModel);
                        }
                    }
                    cuisinesFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cuisinesFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cuisinesFilteredList = (ArrayList<CuisinesModel>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCuisinesName;
        private LinearLayout cuisinesLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCuisinesName = itemView.findViewById(R.id.tv_cuisines_name);
            cuisinesLayout = itemView.findViewById(R.id.cuisines_layout);
        }
    }
}
