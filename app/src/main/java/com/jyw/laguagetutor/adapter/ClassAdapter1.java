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
import com.jyw.laguagetutor.recyclerData.ClassData2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassAdapter1 extends RecyclerView.Adapter<ClassAdapter1.ViewHolder> {

    private ArrayList<ClassData1> mData =null;
    Context mContext;
    ButtonClickListener listener;

    public ClassAdapter1(ArrayList<ClassData1> data, Context context,ButtonClickListener listener){
        this.mData =data;
        this.mContext =context;
        this.listener =listener;
    }

    @NonNull
    @Override
    public ClassAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_teacher_item,
                parent,
                false);
        ClassAdapter1.ViewHolder vh = new ClassAdapter1.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter1.ViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.circleImageView_profile);
        holder.tv_class_name.setText(mData.get(position).getName());
        holder.tv_class_education.setText(mData.get(position).getEducation());

        String tmp = mData.get(position).getDate();
        if(tmp.equals("null")){
            holder.tv_date.setText("구매 기록 없음");
        } else {
            holder.tv_date.setText(tmp);
        }
        Log.d("adapter1","dd");
        tmp = mData.get(position).getTime();
        if(tmp.equals("null")){
            holder.tv_time.setText("0");
        } else {
            holder.tv_time.setText(tmp);
            holder.btn_buy.setText("수강권 연장");
        }

        holder.ratingBar2.setRating(mData.get(position).getRating());

        holder.btn_chat.setOnClickListener(v -> {
            listener.onItemClick(mData.get(position));
        });

        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mData.get(position).getMobile();
                Intent intent = new Intent(mContext, ActivityBuyTicket.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(mContext.getResources().getString(R.string.mobile_number),mobile);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface ButtonClickListener {
        void onItemClick(ClassData1 data);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_class_name,tv_class_education,tv_time,tv_date;
        Button btn_chat,btn_buy;
        RatingBar ratingBar2;
        CircleImageView circleImageView_profile;

        ViewHolder(@NonNull View view){
            super(view);
            circleImageView_profile = view.findViewById(R.id.circleImageView_profile);
            tv_class_name = view.findViewById(R.id.tv_class_name);
            tv_class_education = view.findViewById(R.id.tv_class_gender);
            tv_date = view.findViewById(R.id.tv_date);
            tv_time = view.findViewById(R.id.tv_time);
            btn_chat = view.findViewById(R.id.btn_chat);
            btn_buy = view.findViewById(R.id.btn_booking);
            ratingBar2 = view.findViewById(R.id.ratingBar2);

        }
    }
}
