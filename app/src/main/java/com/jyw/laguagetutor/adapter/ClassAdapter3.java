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

import com.jyw.laguagetutor.ClassReservationActivity;
import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.recyclerData.ClassData2;
import com.jyw.laguagetutor.recyclerData.ClassData3;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassAdapter3 extends RecyclerView.Adapter<ClassAdapter3.ViewHolder> {

    private ArrayList<ClassData3> mData =null;
    Context mContext;
    private ButtonClickListener listener;

    public ClassAdapter3(ArrayList<ClassData3> data, Context context, ButtonClickListener listener){
        this.mData =data;
        this.mContext =context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class3_item,
                parent,
                false);
        ClassAdapter3.ViewHolder vh = new ClassAdapter3.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter3.ViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.circleImageView_profile);

        holder.tv_class3_name.setText(mData.get(position).getName());
        holder.tv_class3_date.setText(mData.get(position).getDate());
        holder.tv_class3_time.setText(mData.get(position).getTime());

        if(mData.get(position).getStatus().equals("0")){
            holder.tv_class3_status.setText("예약된 수업");
        } else {
            holder.tv_class3_status.setText("완료된 수업");
            holder.btn_change.setVisibility(View.GONE);
            holder.btn_finish.setVisibility(View.GONE);
        }

        holder.btn_change.setOnClickListener(v -> {
            listener.onChangeButtonClick(mData.get(position));
        });

        holder.btn_finish.setOnClickListener(v -> {
            listener.onFinishButtonClick(mData.get(position));
            holder.btn_finish.setVisibility(View.GONE);
            holder.tv_class3_status.setText("완료된 수업");
            holder.btn_change.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface ButtonClickListener {
        void onChangeButtonClick(ClassData3 data);
        void onFinishButtonClick(ClassData3 data);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_class3_name,tv_class3_date,tv_class3_time,tv_class3_status;
        Button btn_change,btn_finish;
        CircleImageView circleImageView_profile;

        ViewHolder(@NonNull View view){
            super(view);
            tv_class3_name = view.findViewById(R.id.tv_class3_name);
            tv_class3_date = view.findViewById(R.id.tv_class3_date);
            tv_class3_time = view.findViewById(R.id.tv_class3_time);
            tv_class3_status = view.findViewById(R.id.tv_class3_status);
            btn_change = view.findViewById(R.id.btn_change);
            btn_finish = view.findViewById(R.id.btn_finish);
            circleImageView_profile = view.findViewById(R.id.circleImageView_profile);
        }
    }
}
