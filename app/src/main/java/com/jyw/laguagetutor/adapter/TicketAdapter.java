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

import com.jyw.laguagetutor.ActivityBuyTicket;
import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.recyclerData.TicketData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private ArrayList<TicketData> mData = null;
    private Context context;
    private static final String IMAGE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/images/";
    public TicketAdapter(ArrayList<TicketData>list, Context context){
        mData = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_ticket ,
                parent,
                false);
        TicketAdapter.ViewHolder vh = new TicketAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.ViewHolder holder, int position) {
        Picasso.get().load(IMAGE_URL+mData.get(position).getImage()).into(holder.cv_ticket);
        String name = "강사 이름 : "+mData.get(position).getName();
        String count = "남은 시간 : "+mData.get(position).getCount()+"시간";
        String date = "결제 일자 : "+mData.get(position).getDate().substring(0,11);
        holder.tv_ticket_name.setText(name);
        holder.tv_ticket_count.setText(count);
        holder.tv_ticket_date.setText(date);

        holder.btn_ticket_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityBuyTicket.class);
                intent.putExtra(context.getResources().getString(R.string.mobile_number),mData.get(position).getMobile_number());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView cv_ticket;
        TextView tv_ticket_name,tv_ticket_count,tv_ticket_date;
        Button btn_ticket_buy;
        ViewHolder(@NonNull View view){
            super(view);
            cv_ticket = view.findViewById(R.id.cv_ticket);
            tv_ticket_name = view.findViewById(R.id.tv_ticket_name);
            tv_ticket_count = view.findViewById(R.id.tv_ticket_count);
            tv_ticket_date = view.findViewById(R.id.tv_ticket_date);
            btn_ticket_buy = view.findViewById(R.id.btn_ticket_buy);
        }
    }
}
