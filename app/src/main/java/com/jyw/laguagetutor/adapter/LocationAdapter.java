package com.jyw.laguagetutor.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.TeacherRegisterLocationActivity;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<String> mData = null;
    ArrayList<String> resultList;
    String si_do;
    boolean unSelectedAll = false;
    LocationListener locationListener;

    public LocationAdapter(List<String> list ,ArrayList<String> resultList, String si_do, LocationListener locationListener){
        mData = list;
        this.resultList = resultList;
        this.locationListener = locationListener;
        this.si_do = si_do;
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item,
                parent,
                false);
        LocationAdapter.ViewHolder vh = new LocationAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {

        String text = mData.get(position);
        String location = si_do+" "+text;
        holder.checkBox_location.setText(text);
        if(resultList.contains(location)){
            holder.checkBox_location.setChecked(true);
        }

        if(unSelectedAll && holder.checkBox_location.isChecked()){
            holder.checkBox_location.setChecked(false);
            resultList.remove(location);
            locationListener.onLocationChange(resultList);
            Log.w(TeacherRegisterLocationActivity.class.getSimpleName(),resultList.toString());
        }

        holder.checkBox_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    resultList.add(location);
                } else {
                    resultList.remove(location);
                }
                locationListener.onLocationChange(resultList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void unSelectedAll(){
        Log.w(TeacherRegisterLocationActivity.class.getSimpleName(),"unSelectedAll()");
        unSelectedAll = true;
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox_location;
        ViewHolder(@NonNull View view){
            super(view);
            checkBox_location = view.findViewById(R.id.checkBox_location);
        }
    }
}
