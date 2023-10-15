package com.jyw.laguagetutor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jyw.laguagetutor.ChatRoomActivity;
import com.jyw.laguagetutor.R;
import com.jyw.laguagetutor.recyclerData.ChatListData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<ChatListData> mData;
    private Context context;
    private static final String URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/get_image.php";
    private static final String IMAGE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/images/";
    private String mMobileNumber;
    private static final String TAG = ChatListAdapter.class.getSimpleName();
    public ChatListAdapter(ArrayList<ChatListData> arrayList, Context context,String mMobileNumber){
        this.mData = arrayList;
        this.context = context;
        this.mMobileNumber = mMobileNumber;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item,
                parent,
                false);
        ChatListAdapter.ViewHolder vh = new ChatListAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView_room_name.setText(mData.get(position).getRoomName());
        holder.textView_room_message.setText(mData.get(position).getLastMessage());
        holder.textView_room_date.setText(mData.get(position).getDate());


        String unread = mData.get(position).getUnreadMessage();
        if(unread.equals("0")){
            holder.textView_room_unread.setVisibility(View.INVISIBLE);
        } else {
            holder.textView_room_unread.setVisibility(View.VISIBLE);
            holder.textView_room_unread.setText(unread);
        }
        requestImage(Integer.toString(mData.get(position).getRoomId()),holder,position);
        Log.d(TAG,"onBindViewHolder requestImage");

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("room_id",Integer.toString(mData.get(position).getRoomId()));
                intent.putExtra("room_name",mData.get(position).getRoomName());
                intent.putExtra("image",mData.get(position).getImage());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateList(int roomId,int total, String roomName, String msg, String date, String unread){
        if(total==1){
            Log.d(TAG,"updateList 1");
            mData.add(0,new ChatListData(roomId,roomName,msg,date,unread));
            notifyDataSetChanged();
        } else {
            for (int i = 0; i < mData.size(); i++){
                if(mData.get(i).getRoomId()==roomId){
                    Log.d(TAG,"updateList 2");
                    mData.remove(i);
                    mData.add(0,new ChatListData(roomId,roomName,msg,date,unread));
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_room_name,textView_room_message,textView_room_date,textView_room_unread;
        CircleImageView circleImageView_room_profile;
        ConstraintLayout layout;
        ViewHolder(@NonNull View view){
            super(view);
            layout = view.findViewById(R.id.layout);
            textView_room_name = view.findViewById(R.id.textView_room_name);
            textView_room_message = view.findViewById(R.id.textView_room_message);
            textView_room_date = view.findViewById(R.id.textView_room_date);
            textView_room_unread = view.findViewById(R.id.textView_room_unread);
            circleImageView_room_profile = view.findViewById(R.id.circleImageView_room_profile);
        }
    }

    private void requestImage(String roomId,@NonNull ChatListAdapter.ViewHolder holder,int position) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                String id = jsonObject.getString("id");

                                String image = jsonObject.getString("image");
                                if(!mMobileNumber.equals(id)&&!image.equals("null")){
                                    String imageUrl = IMAGE_URL+image;
                                    mData.get(position).setImage(imageUrl);
                                    Picasso.get().load(imageUrl).into(holder.circleImageView_room_profile);
                                } else if(!mMobileNumber.equals(id)){
                                    holder.circleImageView_room_profile.setImageResource(R.drawable.basic_profile);
                                }
                            }
                            holder.circleImageView_room_profile.setVisibility(View.VISIBLE);


                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("room_id",roomId);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(context).add(request);
    }
}
