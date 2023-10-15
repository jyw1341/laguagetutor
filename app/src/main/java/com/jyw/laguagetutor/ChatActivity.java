package com.jyw.laguagetutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jyw.laguagetutor.adapter.ChatListAdapter;
import com.jyw.laguagetutor.recyclerData.ChatListData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView_chat_list;

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatListAdapter chatListAdapter;

    private SessionManager sessionManager;
    private HashMap<String, String> user;
    private String mMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView_chat_list = findViewById(R.id.recyclerView_chat_list);
        recyclerView_chat_list.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        mMobileNumber = user.get(SessionManager.MOBILE_NUMBER);

        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_chat);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.navigation_favorite:
                        startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.navigation_chat:
                        break;

                    case R.id.navigation_lesson:
                        startActivity(new Intent(getApplicationContext(),MyClassActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.navigation_setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG,"onStart");
        //채팅방 목록을 불러오기 위해 서비스에 데이터 전달
        Intent intent = new Intent(getApplicationContext(), ChatService.class);
        intent.putExtra(getResources().getString(R.string.var_service_command),getResources().getString(R.string.var_get_room_data));
        startService(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String command = intent.getStringExtra(getResources().getString(R.string.var_activity_command));

        if (command!=null){

            //채팅방 목록 RecyclerView 데이터 클래서 생성, 어댑터 적용
            if(command.equals(getResources().getString(R.string.var_create))){
                ArrayList<String> tmp = intent.getStringArrayListExtra(getResources().getString(R.string.var_chat_room_list));
                ArrayList<ChatListData> chatRoomList = new ArrayList<>();
                if(tmp!=null){
                    for (int i = 0; i < tmp.size(); i++){
                        try {
                            JSONObject jsonObject = new JSONObject(tmp.get(i));

                            int roomId = Integer.parseInt(jsonObject.getString(getResources().getString(R.string.var_chat_room_id)));
                            String roomName = jsonObject.getString(getResources().getString(R.string.var_room_name));
                            String lastMessage = jsonObject.getString(getResources().getString(R.string.var_last_message));
                            String date = jsonObject.getString(getResources().getString(R.string.var_last_message_date));

                            int x = Integer.parseInt(jsonObject.getString(getResources().getString(R.string.var_total_message)));
                            int y = Integer.parseInt(jsonObject.getString(getResources().getString(R.string.var_checked_message)));
                            String unread = Integer.toString(x-y);
                            chatRoomList.add(new ChatListData(roomId,roomName,lastMessage,date,unread));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    chatListAdapter = new ChatListAdapter(chatRoomList,getApplicationContext(),mMobileNumber);
                    recyclerView_chat_list.setAdapter(chatListAdapter);
                }


            } else if(command.equals(getResources().getString(R.string.var_update))){
                String roomData = intent.getStringExtra(getResources().getString(R.string.var_chat_room_data));
                try {
                    JSONObject jsonObject = new JSONObject(roomData);

                    int roomId = Integer.parseInt(jsonObject.getString(getResources().getString(R.string.var_chat_room_id)));
                    String roomName = jsonObject.getString(getResources().getString(R.string.var_room_name));
                    String lastMessage = jsonObject.getString(getResources().getString(R.string.var_last_message));
                    String date = jsonObject.getString(getResources().getString(R.string.var_last_message_date));

                    int x = Integer.parseInt(jsonObject.getString(getResources().getString(R.string.var_total_message)));
                    int y = Integer.parseInt(jsonObject.getString(getResources().getString(R.string.var_checked_message)));
                    String unread = Integer.toString(x-y);

                    chatListAdapter.updateList(roomId,x,roomName,lastMessage,date,unread);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}