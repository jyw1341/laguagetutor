package com.jyw.laguagetutor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.R;

import java.util.ArrayList;
import java.util.List;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<String> mData = null;
    ArrayList<String> subjects;
    SubjectListener subjectListener;

    public SubjectAdapter(List<String> list, SubjectListener subjectListener, ArrayList<String> subjects){
        mData = list;
        this.subjectListener = subjectListener;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item,
                parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position) {
        String text = mData.get(position);

        holder.checkBox.setText(text);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.checkBox.isChecked()){
                    subjects.add(text);
                } else {
                    subjects.remove(text);
                }
                subjectListener.onSubjectChange(subjects);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ViewHolder(@NonNull View view){
            super(view);

            checkBox = view.findViewById(R.id.checkBox);
        }
    }
}
