package com.justfoodz.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.justfoodz.R;
import com.justfoodz.fragments.ReviewFragment;
import com.justfoodz.models.NewReviewModel;
import com.justfoodz.models.ReviewImageModel;
import com.justfoodz.models.ReviewModel;
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NewReviewModel> reviewModelArrayList;
    private NewReviewModel reviewModel;
    private RecyclerView reviewimagerecycler;
    private ReviewImageAdapter reviewImageAdapter;

    public ReviewAdapter(Context context, ArrayList<NewReviewModel> reviewModelArrayList) {
        this.context = context;
        this.reviewModelArrayList = reviewModelArrayList;



    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_review_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {

        reviewModel = reviewModelArrayList.get(position);
//        REVIEWID= reviewModelArrayList.get(position).getReview_id();
        holder.tvReviewerName.setText(reviewModel.getCustomerName());
        holder.tvReviewDate.setText(reviewModel.getReviewpostedDate());
        holder.tvReviewedContent.setText(reviewModel.getCustomerReviewComment());


        if (reviewModel.getRestaurantReviewRating().equals("0")) {
            holder.iv_review1.setBackgroundResource(R.drawable.review_star);
            holder.iv_review2.setBackgroundResource(R.drawable.review_star);
            holder.iv_review3.setBackgroundResource(R.drawable.review_star);
            holder.iv_review4.setBackgroundResource(R.drawable.review_star);
            holder.iv_review5.setBackgroundResource(R.drawable.review_star);

        } else if (reviewModel.getRestaurantReviewRating().equals("1.0")) {
            holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review2.setBackgroundResource(R.drawable.review_star);
            holder.iv_review3.setBackgroundResource(R.drawable.review_star);
            holder.iv_review4.setBackgroundResource(R.drawable.review_star);
            holder.iv_review5.setBackgroundResource(R.drawable.review_star);

        } else if (reviewModel.getRestaurantReviewRating().equals("2.0")) {
            holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review3.setBackgroundResource(R.drawable.review_star);
            holder.iv_review4.setBackgroundResource(R.drawable.review_star);
            holder.iv_review5.setBackgroundResource(R.drawable.review_star);

        } else if (reviewModel.getRestaurantReviewRating().equals("3.0")) {
            holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review4.setBackgroundResource(R.drawable.review_star);
            holder.iv_review5.setBackgroundResource(R.drawable.review_star);

        } else if (reviewModel.getRestaurantReviewRating().equals("4.0")) {
            holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review4.setBackgroundResource(R.drawable.review_yellow);
            holder.iv_review5.setBackgroundResource(R.drawable.review_star);

        } else if (reviewModel.getRestaurantReviewRating().equals("5.0")) {
            holder.iv_review1.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
            holder.iv_review2.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
            holder.iv_review3.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
            holder.iv_review4.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
            holder.iv_review5.setBackground(ContextCompat.getDrawable(context, R.drawable.review_yellow));
        }
        if (reviewModel.getReview_img().equals(""))
        {

        }else {
            ArrayList<ReviewImageModel> aList= new ArrayList(Arrays.asList(reviewModel.getReview_img().split(",")));
            List<String> items = Arrays.asList(reviewModel.getReview_img().split("\\s*,\\s*"));
//            reviewimageListt.add(new ReviewImageModel(aList));
            reviewImageAdapter=new ReviewImageAdapter(items,context );
            LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            reviewimagerecycler.setLayoutManager(horizontalLayoutManager2);
            reviewimagerecycler.setAdapter(reviewImageAdapter);
        }
//        HitUrlForReviewImage();
    }

    @Override
    public int getItemCount() {
        return reviewModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvReviewerName,tvReviewDate,tvReviewedContent;
        ImageView ivBack, iv_review1, iv_review2, iv_review3, iv_review4, iv_review5,reviewsimage;


        public MyViewHolder(View itemView) {
            super(itemView);
            iv_review1 = itemView.findViewById(R.id.review_1);
            iv_review2 = itemView.findViewById(R.id.review_2);
            iv_review3 = itemView.findViewById(R.id.review_3);
            iv_review4 = itemView.findViewById(R.id.review_4);
            iv_review5 = itemView.findViewById(R.id.review_5);
            tvReviewerName = itemView.findViewById(R.id.tv_reviewer_name);
            reviewimagerecycler=itemView.findViewById(R.id.reviewimagerecycler);
            tvReviewDate = itemView.findViewById(R.id.tv_review_date);
            tvReviewedContent = itemView.findViewById(R.id.tv_reviewed_content);
        }
    }

    public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.MyViewHolder> {


        List<String> reviewimageListt ;
        Context context;


        public ReviewImageAdapter(List<String> reviewimageListt, Context context) {
            this.reviewimageListt = reviewimageListt;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView reviewimagee;

              public MyViewHolder(View view) {
                super(view);
                reviewimagee=(ImageView) view.findViewById(R.id.reviewimagee);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewimagelist, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
////            reviewImageModel = reviewimageListt.get(position);
//            String REVIEWIMAGE = reviewImageModel.getReview_img();
////            Glide.with(context).load(REVIEWIMAGE).into(holder.reviewimagee);

            Picasso.get().load(reviewimageListt.get(position)).into(holder.reviewimagee);
        }


        @Override
        public int getItemCount()
        {
            return reviewimageListt.size();
        }
    }
}
