package com.jyw.laguagetutor.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.ActivityBuyTicket;
import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.recyclerData.ClassData1;
import com.jyw.laguagetutor.recyclerData.ReviewData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<ReviewData> mData =null;
    Context mContext;

    public ReviewAdapter(ArrayList<ReviewData> data, Context context){
        this.mData =data;
        this.mContext =context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,
                parent,
                false);
        ReviewAdapter.ViewHolder vh = new ReviewAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_date.setText(mData.get(position).getDate());
        holder.tv_description.setText(mData.get(position).getDesc());

        holder.rb_review.setRating(Float.parseFloat(mData.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_date,tv_description;
        RatingBar rb_review;

        ViewHolder(@NonNull View view){
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_date = view.findViewById(R.id.tv_date);
            tv_description = view.findViewById(R.id.tv_description);
            rb_review = view.findViewById(R.id.rb_review);
        }
    }
}
