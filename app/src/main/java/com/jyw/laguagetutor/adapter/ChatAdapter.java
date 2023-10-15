package com.jyw.laguagetutor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.recyclerData.ChatData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatData> myDataList = null;
    private Bitmap bitmap;

    public ChatAdapter(ArrayList<ChatData> dataList,Bitmap bitmap){
        this.myDataList = dataList;
        this.bitmap = bitmap;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == 0){
            view = inflater.inflate(R.layout.chat_item_center,parent,false);
            return new CenterViewHolder(view);
        }else if(viewType == 1){
            view = inflater.inflate(R.layout.chat_item_left,parent,false);
            return new LeftViewHolder(view);
        }else{
            view = inflater.inflate(R.layout.chat_item_right,parent,false);
            return new RightViewHolder(view);
        }

    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof CenterViewHolder){
            ((CenterViewHolder)viewHolder).textv.setText(myDataList.get(position).getContent());
        }else if(viewHolder instanceof LeftViewHolder){
            ((LeftViewHolder)viewHolder).textv_nicname.setText(myDataList.get(position).getName());
            ((LeftViewHolder)viewHolder).textv_msg.setText(myDataList.get(position).getContent());
            ((LeftViewHolder)viewHolder).textv_time.setText(getTime(myDataList.get(position).getDate()));
            if(myDataList.get(position).getUnread().equals("0")){
                ((LeftViewHolder)viewHolder).textView_unread.setVisibility(View.INVISIBLE);
            } else {
                ((LeftViewHolder)viewHolder).textView_unread.setVisibility(View.VISIBLE);
                ((LeftViewHolder)viewHolder).textView_unread.setText(myDataList.get(position).getUnread());
            }
            if(bitmap==null){
                ((LeftViewHolder) viewHolder).imgv.setImageResource(R.drawable.basic_profile);
            } else {
                ((LeftViewHolder) viewHolder).imgv.setImageBitmap(bitmap);
            }
        }else{
            ((RightViewHolder)viewHolder).textv_msg.setText(myDataList.get(position).getContent());
            ((RightViewHolder)viewHolder).textv_time.setText(getTime(myDataList.get(position).getDate()));
            ((RightViewHolder)viewHolder).textView_unread.setText(myDataList.get(position).getUnread());
            if(myDataList.get(position).getUnread().equals("0")){
                ((RightViewHolder)viewHolder).textView_unread.setVisibility(View.INVISIBLE);
            } else {
                ((RightViewHolder)viewHolder).textView_unread.setVisibility(View.VISIBLE);
                ((RightViewHolder)viewHolder).textView_unread.setText(myDataList.get(position).getUnread());
            }
        }

    }

    public void updateReaders(String num){
        for (int i = 0; i < myDataList.size(); i++){
            if(myDataList.get(i).getRoomMembers().contains(num)){
                myDataList.get(i).addReaders(num);
            }
        }
        notifyDataSetChanged();
        Log.w("ChatAdapter","updateReaders");;
    }


    public void updateList(String roomId, String sender, String message,String date, String viewType, String roomMembers,String readers){
        myDataList.add(new ChatData(roomId,sender,message,date,viewType,roomMembers,readers));
        notifyDataSetChanged();
    }

    public void addData(ChatData data){
        myDataList.add(data);
        notifyDataSetChanged();
    }

    public String getTime(String date) {
       return date.substring(11,16);
    }

    // 리사이클러뷰안에서 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    // ★★★
    // 위에 3개만 오버라이드가 기본 셋팅임,
    // 이 메소드는 ViewType때문에 오버라이딩 했음(구별할려고)
    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(myDataList.get(position).getViewType());
    }

    // "리사이클러뷰에 들어갈 뷰 홀더", 그리고 "그 뷰 홀더에 들어갈 아이템들을 셋팅"
    public class CenterViewHolder extends RecyclerView.ViewHolder{
        TextView textv;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            textv = (TextView)itemView.findViewById(R.id.textv);
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgv;
        TextView textv_nicname;
        TextView textv_msg;
        TextView textv_time;
        TextView textView_unread;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv = (CircleImageView)itemView.findViewById(R.id.imgv);
            textv_nicname = (TextView)itemView.findViewById(R.id.textv_nicname);
            textv_msg = (TextView)itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);
            textView_unread = itemView.findViewById(R.id.textView_unread);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView textv_msg;
        TextView textv_time;
        TextView textView_unread;
        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            textv_msg = (TextView)itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);
            textView_unread = itemView.findViewById(R.id.textView_unread);
        }
    }

}