package com.jyw.laguagetutor.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.StudentProfileActivity;
import com.jyw.laguagetutor.TeacherProfileActivity;
import com.jyw.laguagetutor.recyclerData.FavoriteForStudentData;
import com.jyw.laguagetutor.recyclerData.FavoriteForTeacherData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter2 extends RecyclerView.Adapter<FavoriteAdapter2.ViewHolder> {

    private ArrayList<FavoriteForTeacherData> mData = null;
    private Context context;
    private ButtonClickListener listener;
    private int tabPosition;

    public FavoriteAdapter2(ArrayList<FavoriteForTeacherData> mData, Context context, int tabPosition, ButtonClickListener listener){

        this.mData = mData;
        this.context = context;
        this.listener = listener;
        this.tabPosition = tabPosition;

    }

    @NonNull
    @Override
    public FavoriteAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item2,
                parent,
                false);
        FavoriteAdapter2.ViewHolder vh = new FavoriteAdapter2.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter2.ViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.circleImageView_profile);
        holder.tv_user_name.setText(mData.get(position).getName());
        holder.tv_region.setText(mData.get(position).getRegion());
        holder.tv_age.setText(mData.get(position).getAge());
        holder.tv_gender.setText(mData.get(position).getGender());

        if(tabPosition==1){
            holder.btn_okay.setVisibility(View.GONE);
        }
        holder.btn_okay.setOnClickListener(v -> {
            listener.onItemClick(mData.get(position));
        });

        holder.btn_chat.setOnClickListener(v -> {
            listener.onChatClick(mData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface ButtonClickListener {
        void onItemClick(FavoriteForTeacherData data);
        void onChatClick(FavoriteForTeacherData data);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView_profile;
        TextView tv_gender,tv_user_name,tv_region,tv_age;
        Button btn_chat,btn_okay;

        ViewHolder(@NonNull View view){
            super(view);
            circleImageView_profile = view.findViewById(R.id.circleImageView_profile);
            tv_gender = view.findViewById(R.id.tv_gender);
            tv_user_name = view.findViewById(R.id.tv_user_name);
            tv_region = view.findViewById(R.id.tv_region);
            tv_age = view.findViewById(R.id.tv_age);
            btn_chat = view.findViewById(R.id.btn_chat);
            btn_okay = view.findViewById(R.id.btn_okay);
        }
    }
}
