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

public class ClassAdapter4 extends RecyclerView.Adapter<ClassAdapter4.ViewHolder> {

    private ArrayList<ClassData3> mData =null;
    Context mContext;

    public ClassAdapter4(ArrayList<ClassData3> data, Context context){
        this.mData =data;
        this.mContext =context;
    }

    @NonNull
    @Override
    public ClassAdapter4.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class4_item,
                parent,
                false);
        ClassAdapter4.ViewHolder vh = new ClassAdapter4.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter4.ViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.circleImageView_profile);

        holder.tv_class3_name.setText(mData.get(position).getName());
        holder.tv_class3_date.setText(mData.get(position).getDate());
        holder.tv_class3_time.setText(mData.get(position).getTime());

        if(mData.get(position).getStatus().equals("0")){
            holder.tv_class3_status.setText("예약된 수업");
            holder.btn_finish.setVisibility(View.GONE);
        } else if(mData.get(position).getStatus().equals("1")){
            holder.tv_class3_status.setText("완료된 수업");
            holder.btn_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WriteReview.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("classId",mData.get(position).getClassId());
                    intent.putExtra("image",mData.get(position).getImage());
                    intent.putExtra("name",mData.get(position).getName());
                    intent.putExtra("mobile",mData.get(position).getMobile());
                    mContext.startActivity(intent);
                }
            });
        } else if(mData.get(position).getStatus().equals("2")){
            holder.tv_class3_status.setText("완료된 수업");
            holder.btn_finish.setVisibility(View.GONE);
            holder.btn_finish2.setVisibility(View.VISIBLE);
            holder.btn_finish2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReviewUpdate.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("classId",mData.get(position).getClassId());
                    intent.putExtra("image",mData.get(position).getImage());
                    intent.putExtra("name",mData.get(position).getName());
                    intent.putExtra("mobile",mData.get(position).getMobile());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_class3_name,tv_class3_date,tv_class3_time,tv_class3_status;
        Button btn_finish,btn_finish2;
        CircleImageView circleImageView_profile;

        ViewHolder(@NonNull View view){
            super(view);
            tv_class3_name = view.findViewById(R.id.tv_class3_name);
            tv_class3_date = view.findViewById(R.id.tv_class3_date);
            tv_class3_time = view.findViewById(R.id.tv_class3_time);
            tv_class3_status = view.findViewById(R.id.tv_class3_status);
            btn_finish = view.findViewById(R.id.btn_finish);
            btn_finish2 = view.findViewById(R.id.btn_finish2);
            circleImageView_profile = view.findViewById(R.id.circleImageView_profile);
        }
    }
}
