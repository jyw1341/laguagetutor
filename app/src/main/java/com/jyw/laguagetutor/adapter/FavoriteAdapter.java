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
import com.jyw.laguagetutor.TeacherProfileActivity;
import com.jyw.laguagetutor.recyclerData.FavoriteForStudentData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<FavoriteForStudentData> mData = null;
    private Context context;
    private ButtonClickListener listener;
    private int tabPosition;
    public FavoriteAdapter(ArrayList<FavoriteForStudentData> mData, Context context, int tabPosition,ButtonClickListener listener){

        this.mData = mData;
        this.context = context;
        this.listener = listener;
        this.tabPosition = tabPosition;

    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item,
                parent,
                false);
        FavoriteAdapter.ViewHolder vh = new FavoriteAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.circleImageView_profile);
        holder.tv_user_name.setText(mData.get(position).getName());
        holder.tv_education.setText(mData.get(position).getEducation());
        holder.tv_region.setText(mData.get(position).getRegion());
        holder.tv_subject.setText(mData.get(position).getSubject());
        holder.tv_price.setText(mData.get(position).getPrice());
        holder.ratingBar2.setRating(mData.get(position).getRating());

        holder.circleImageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeacherProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mobile_number",mData.get(position).getMobile());
                context.startActivity(intent);
            }
        });

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
        void onItemClick(FavoriteForStudentData data);
        void onChatClick(FavoriteForStudentData data);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView_profile;
        TextView tv_education,tv_user_name,tv_region,tv_subject,tv_price;
        RatingBar ratingBar2;
        Button btn_chat,btn_okay;

        ViewHolder(@NonNull View view){
            super(view);
            circleImageView_profile = view.findViewById(R.id.circleImageView_profile);
            tv_education = view.findViewById(R.id.tv_gender);
            tv_user_name = view.findViewById(R.id.tv_user_name);
            tv_region = view.findViewById(R.id.tv_region);
            tv_subject = view.findViewById(R.id.tv_subject);
            tv_price = view.findViewById(R.id.tv_price);
            ratingBar2 = view.findViewById(R.id.ratingBar2);
            btn_chat = view.findViewById(R.id.btn_chat);
            btn_okay = view.findViewById(R.id.btn_okay);
        }
    }
}
