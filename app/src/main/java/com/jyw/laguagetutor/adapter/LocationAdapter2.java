package com.jyw.laguagetutor.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.R;

import java.util.ArrayList;

public class LocationAdapter2 extends RecyclerView.Adapter<LocationAdapter2.ViewHolder> {

    private ArrayList<String> mData = null;

    public LocationAdapter2(ArrayList<String>list){
        mData = list;
    }

    @NonNull
    @Override
    public LocationAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location2_item,
                parent,
                false);
        LocationAdapter2.ViewHolder vh = new LocationAdapter2.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter2.ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.button_location2.setText(text);
    }

    public void addItem(ArrayList<String> list){
        mData = list;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        Button button_location2;
        ViewHolder(@NonNull View view){
            super(view);
            button_location2 = view.findViewById(R.id.button_location2);
        }
    }
}
