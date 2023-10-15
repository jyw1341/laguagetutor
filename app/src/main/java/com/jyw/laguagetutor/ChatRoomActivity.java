package com.jyw.laguagetutor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.jyw.laguagetutor.adapter.ChatAdapter;
import com.jyw.laguagetutor.recyclerData.ChatData;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ChatRoomActivity extends AppCompatActivity {

    private EditText editText_message;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView_chat;

    private String mUserName,mMobileNumber,oMobileNumber,roomId,roomName,roomMembers,image;
    private static final String TAG = ChatRoomActivity.class.getSimpleName();
    private static final String URL_1 = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/get_image2.php";

    private DrawerLayout drawerLayout;
    private View drawer;
    private Bitmap bitmap;

    private SessionManager sessionManager;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent dataIntent = getIntent();
        roomId = dataIntent.getStringExtra(getResources().getString(R.string.var_chat_room_id));
        roomName = dataIntent.getStringExtra(getResources().getString(R.string.var_room_name));
        image = dataIntent.getStringExtra("image");
        bitmap = stringToBitmap(dataIntent.getStringExtra("bitmap"));

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();

        ImageView iv_chat_room_profile = findViewById(R.id.iv_chat_room_profile);
        Picasso.get().load(image).into(iv_chat_room_profile);

        if(bitmap!=null){
            iv_chat_room_profile.setImageBitmap(bitmap);
        } else if(image!=null){
            bitmap = ((BitmapDrawable) iv_chat_room_profile.getDrawable()).getBitmap();
        }

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        mUserName = user.get(sessionManager.USER_NAME);
        mMobileNumber = user.get(sessionManager.MOBILE_NUMBER);

        TextView textView_room_name = findViewById(R.id.textView_room_name);
        textView_room_name.setText(roomName);

        recyclerView_chat = findViewById(R.id.recyclerView_chat);
        recyclerView_chat.setLayoutManager(new LinearLayoutManager(this));

        drawerLayout = findViewById(R.id.drawerLayout);
        drawer = findViewById(R.id.drawer);

        ImageView imageView_menu = findViewById(R.id.imageView_menu);
        imageView_menu.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                drawerLayout.openDrawer(drawer);
             }
         });


        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
             }
         });


        Button btn_chat_room_buy =findViewById(R.id.btn_chat_room_buy);
        //학생계정일때만 수강권 구매 버튼 활성화
        if(user.get(SessionManager.USER_TYPE).equals("student")){

            btn_chat_room_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ActivityBuyTicket.class);
                    intent.putExtra(getResources().getString(R.string.mobile_number),oMobileNumber);
                    startActivity(intent);
                }
            });
        } else {
            btn_chat_room_buy.setVisibility(View.GONE);
        }

        Button btn_chat_room_out = findViewById(R.id.btn_chat_room_out);
        btn_chat_room_out.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), ChatService.class);
                 intent.putExtra(getResources().getString(R.string.var_service_command),getResources().getString(R.string.var_delete));
                 intent.putExtra(getResources().getString(R.string.var_chat_room_id),roomId);
                 startService(intent);
                 finish();
             }
         });


        
        editText_message = findViewById(R.id.editText_message);

        //채팅 보내기
        Button button_send = findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
         @RequiresApi(api = Build.VERSION_CODES.O)
         @Override
             public void onClick(View v) {
                String content = editText_message.getText().toString().trim();
                if(!content.equals("")){
                     String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
                     chatAdapter.addData(new ChatData(roomId,mUserName,content,formatDate,"2",roomMembers,mMobileNumber));
                     scrollRecyclerView();

                     Intent intent = new Intent(getApplicationContext(), ChatService.class);
                     intent.putExtra(getResources().getString(R.string.var_service_command),getResources().getString(R.string.var_send_message));
                     intent.putExtra(getResources().getString(R.string.var_chat_message),content);
                     intent.putExtra(getResources().getString(R.string.var_chat_room_id),roomId);
                     startService(intent);
                     editText_message.setText(null);
                 }
             }
         });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(getApplicationContext(), ChatService.class);
        intent.putExtra(getResources().getString(R.string.var_service_command),getResources().getString(R.string.var_get_message_data));
        intent.putExtra(getResources().getString(R.string.var_chat_room_id),roomId);
        startService(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String command = intent.getStringExtra(getResources().getString(R.string.var_activity_command));

        if (command!=null){
            if(command.equals(getResources().getString(R.string.var_create))){
                //채팅 메세지 데이터 클래스 리스트 초기화
                ArrayList<String> tmp = intent.getStringArrayListExtra(getResources().getString(R.string.var_chat_message_list));
                ArrayList<ChatData> chatList = new ArrayList<>();
                for (int i = 0; i < tmp.size(); i++){
                    try {
                        JSONObject jsonObject = new JSONObject(tmp.get(i));
                        String roomId = jsonObject.getString(getResources().getString(R.string.var_chat_room_id));
                        String senderName = jsonObject.getString(getResources().getString(R.string.var_sender_name));
                        String chatMessage = jsonObject.getString(getResources().getString(R.string.var_chat_message));
                        String date = jsonObject.getString(getResources().getString(R.string.var_date));
                        String viewType = jsonObject.getString(getResources().getString(R.string.var_view_type));
                        String readers = jsonObject.getString(getResources().getString(R.string.var_readers));
                        String roomMembers = jsonObject.getString(getResources().getString(R.string.var_room_members));

                        chatList.add(new ChatData(roomId,senderName,chatMessage,date,viewType,roomMembers,readers));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //읽음 처리 위해 채팅방 멤버 변수 초기화
                roomMembers = intent.getStringExtra(getResources().getString(R.string.var_room_members));
                chatAdapter = new ChatAdapter(chatList,bitmap);
                recyclerView_chat.setAdapter(chatAdapter);
                recyclerView_chat.scrollToPosition(chatList.size()-1);

                StringTokenizer tokenizer = new StringTokenizer(roomMembers,",");
                ArrayList<String> arrayList = new ArrayList<>();
                while(tokenizer.hasMoreTokens()){
                    String str = tokenizer.nextToken();
                    Log.d(TAG,str);
                    arrayList.add(str);
                }

                oMobileNumber = arrayList.get(arrayList.size()-1);

                //채팅방에 입장하고 어댑터가 초기화되면 상대방에 읽음처리 위한 메세지 전송
                Intent service = new Intent(getApplicationContext(), ChatService.class);
                service.putExtra(getResources().getString(R.string.var_service_command),getResources().getString(R.string.var_check_message));
                service.putExtra(getResources().getString(R.string.var_chat_room_id),roomId);
                startService(service);

            //새로 수신한 메세지 업데이트
            } else if(command.equals(getResources().getString(R.string.var_update))){

                String chatData = intent.getStringExtra(getResources().getString(R.string.var_chat_data));


                try {
                    JSONObject jsonObject = new JSONObject(chatData);
                    String roomId = jsonObject.getString(getResources().getString(R.string.var_chat_room_id));
                    String senderName = jsonObject.getString(getResources().getString(R.string.var_sender_name));
                    String chatMessage = jsonObject.getString(getResources().getString(R.string.var_chat_message));
                    String date = jsonObject.getString(getResources().getString(R.string.var_date));
                    String viewType = jsonObject.getString(getResources().getString(R.string.var_view_type));
                    String readers = jsonObject.getString(getResources().getString(R.string.var_readers));
                    String roomMembers = jsonObject.getString(getResources().getString(R.string.var_room_members));

                    chatAdapter.updateList(roomId,senderName,chatMessage,date,viewType,roomMembers,readers);
                    scrollRecyclerView();

                    //새로운 메세지가 도착해서 어댑터가 업데이트되면 상대방에 읽음처리 위한 메세지 전송
                    Intent service = new Intent(getApplicationContext(), ChatService.class);
                    service.putExtra(getResources().getString(R.string.var_service_command),getResources().getString(R.string.var_check_message));
                    service.putExtra(getResources().getString(R.string.var_chat_room_id),roomId);
                    startService(service);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            //메세지 읽음 처리 업데이트
            } else if(command.equals(getResources().getString(R.string.var_check_message))){
                String num = intent.getStringExtra(getResources().getString(R.string.mobile_number));
                chatAdapter.updateReaders(num);
                scrollRecyclerView();

            }
        }
    }

    public void scrollRecyclerView(){
        if(recyclerView_chat.getAdapter()!=null){
            recyclerView_chat.scrollToPosition(recyclerView_chat.getAdapter().getItemCount()-1);
        }
    }

    private void volley(String otherUserMobileNumber) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.d(TAG,response);
                        } catch (Exception e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mobile_number",otherUserMobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}