package com.jyw.laguagetutor.adapter;
import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.PostStudentActivity;
import com.jyw.laguagetutor.R;

import com.jyw.laguagetutor.StudentProfileActivity;
import com.jyw.laguagetutor.TeacherProfileActivity;
import com.jyw.laguagetutor.recyclerData.Student;

import java.util.ArrayList;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private ArrayList<Student> mData = null;
    private Context mContext = null;
    public StudentAdapter(Context mContext, ArrayList<Student> list){
        this.mData = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,
                parent,
                false);
        StudentAdapter.ViewHolder vh = new StudentAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        holder.textView_title.setText(mData.get(position).getTitle());
        holder.textView_name.setText(mData.get(position).getName());
        holder.textView_age.setText(mData.get(position).getAge());
        holder.textView_gender.setText(mData.get(position).getGender());
        holder.textView_location.setText(mData.get(position).getLocation());
        holder.textView_study_type.setText(mData.get(position).getStudyType());

        holder.constraintLayout_student_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostStudentActivity.class);
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
        TextView textView_title,textView_name,textView_age,textView_gender,textView_location,textView_study_type;
        ConstraintLayout constraintLayout_student_item;
        ViewHolder(@NonNull View view){
            super(view);
            constraintLayout_student_item = view.findViewById(R.id.constraintLayout_student_item);
            textView_title = view.findViewById(R.id.textView_title);
            textView_name = view.findViewById(R.id.textView_name);
            textView_age = view.findViewById(R.id.textView_age);
            textView_gender = view.findViewById(R.id.textView_gender);
            textView_location = view.findViewById(R.id.textView_location);
            textView_study_type = view.findViewById(R.id.textView_study_type);
        }
    }
}