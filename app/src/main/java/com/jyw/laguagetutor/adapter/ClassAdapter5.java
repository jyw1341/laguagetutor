package com.jyw.laguagetutor.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.ReviewUpdate;
import com.jyw.laguagetutor.WriteReview;
import com.jyw.laguagetutor.recyclerData.ClassData3;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassAdapter5 extends RecyclerView.Adapter<ClassAdapter5.ViewHolder> {

    private ArrayList<ClassData3> mData =null;

    public ClassAdapter5(ArrayList<ClassData3> data){
        this.mData =data;

    }

    @NonNull
    @Override
    public ClassAdapter5.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class5_item,
                parent,
                false);
        ClassAdapter5.ViewHolder vh = new ClassAdapter5.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter5.ViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.circleImageView_profile);

        holder.tv_class3_name.setText(mData.get(position).getName());
        holder.tv_class3_date.setText(mData.get(position).getDate());
        holder.tv_class3_time.setText(mData.get(position).getTime());

        if(mData.get(position).getStatus().equals("0")){
            holder.tv_class3_status.setText("예약된 수업");
        } else if(mData.get(position).getStatus().equals("1")){
            holder.tv_class3_status.setText("완료된 수업");
        } else if(mData.get(position).getStatus().equals("2")){
            holder.tv_class3_status.setText("완료된 수업");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_class3_name,tv_class3_date,tv_class3_time,tv_class3_status;
        CircleImageView circleImageView_profile;

        ViewHolder(@NonNull View view){
            super(view);
            tv_class3_name = view.findViewById(R.id.tv_class3_name);
            tv_class3_date = view.findViewById(R.id.tv_class3_date);
            tv_class3_time = view.findViewById(R.id.tv_class3_time);
            tv_class3_status = view.findViewById(R.id.tv_class3_status);
            circleImageView_profile = view.findViewById(R.id.circleImageView_profile);
        }
    }
}
