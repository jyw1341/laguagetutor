package com.jyw.laguagetutor.adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jyw.laguagetutor.PostTeacherActivity;
import com.jyw.laguagetutor.R;

import com.jyw.laguagetutor.recyclerData.Teacher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private ArrayList<Teacher> mData = null;
    private Context mContext = null;


    private static final String IMAGE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/images/";

    public TeacherAdapter(Context mContext,  ArrayList<Teacher> list){
        this.mData = list;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public TeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_item,
                parent,
                false);
        TeacherAdapter.ViewHolder vh = new TeacherAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.ViewHolder holder, int position) {
        holder.textView_title.setText(mData.get(position).getTitle());
        holder.textView_name.setText(mData.get(position).getName());
        holder.textView_education.setText(mData.get(position).getEducation());
        holder.textView_gender.setText(mData.get(position).getGender());
        holder.textView_location.setText(mData.get(position).getLocation());
        holder.textView_study_type.setText(mData.get(position).getStudy_type());

        String image = mData.get(position).getImage();
        if(image.equals("null")||image.equals("")){
            holder.circleImageView_profile.setImageResource(R.drawable.basic_profile);
        } else {
            Uri uri = Uri.parse(IMAGE_URL+image);
            Picasso.get().load(uri).into(holder.circleImageView_profile);
        }


        holder.constraintLayout_teacher_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostTeacherActivity.class);
                intent.putExtra("mobile_number",mData.get(position).getMobileNumber());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_title,textView_study_type,textView_education,textView_name,textView_gender,textView_location;
        CircleImageView circleImageView_profile;
        ConstraintLayout constraintLayout_teacher_item;
        ViewHolder(@NonNull View view){
            super(view);
            constraintLayout_teacher_item = view.findViewById(R.id.constraintLayout_teacher_item);
            textView_title = view.findViewById(R.id.textView_title);
            textView_study_type = view.findViewById(R.id.textView_study_type);
            textView_education = view.findViewById(R.id.textView_education);
            textView_name = view.findViewById(R.id.textView_name);
            textView_gender = view.findViewById(R.id.textView_gender);
            textView_location = view.findViewById(R.id.textView_location);
            circleImageView_profile = view.findViewById(R.id.circleImageView_profile);
        }
    }
}
