package com.jyw.laguagetutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jyw.laguagetutor.adapter.ClassAdapter1;
import com.jyw.laguagetutor.adapter.ClassAdapter2;
import com.jyw.laguagetutor.adapter.ClassAdapter3;
import com.jyw.laguagetutor.adapter.ClassAdapter4;
import com.jyw.laguagetutor.recyclerData.ChatRoom;
import com.jyw.laguagetutor.recyclerData.ClassData1;
import com.jyw.laguagetutor.recyclerData.ClassData2;
import com.jyw.laguagetutor.recyclerData.ClassData3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyClassActivity extends AppCompatActivity {

    private TabLayout tab_layout;
    private RecyclerView rv_class;
    private static final String URL = "htonaws.com/server1/get_class_teacher.php";
    private static final String URL_CLASS_READ = "htpute.amazonaws.com/server1/class_read.php";
    private static final String URL_CLASS_UPDATE = "htzonaws.com/server1/class_update.php";
    private static final String URL_CREATE_ROOM = "httm/server1/create_chat_room.php";
    private static final String IMAGE_URL = "hute.amazonaws.com/server1/images/";
    private SessionManager sessionManager;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetail();

        BottomNavigationView nav_view = findViewById(R.id.nav_view);

        nav_view.setSelectedItemId(R.id.navigation_lesson);

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
                        startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.navigation_lesson:
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

        rv_class = findViewById(R.id.rv_class);
        rv_class.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                rv_class.setAdapter(null);
                switch(tab.getPosition()){
                    case 0 :
                        getRequest();
                        break;
                    case 1 :
                        getClassInformation();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch(tab.getPosition()){
                    case 0 :
                        getRequest();
                        break;
                    case 1 :
                        getClassInformation();
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TabLayout.Tab tab = tab_layout.getTabAt(tab_layout.getSelectedTabPosition());
        tab.select();
    }

    private void createChatRoom(String oMobileNumber, String name, String image) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_CREATE_ROOM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString(getResources().getString(R.string.condition));
                            if(condition.equals(getResources().getString(R.string.success))){
                                String room_id = jsonObject.getString(getResources().getString(R.string.var_chat_room_id));
                                Intent serviceIntent = new Intent(getApplicationContext(), ChatService.class);
                                serviceIntent.putExtra(getResources().getString(R.string.var_service_command),getResources().getString(R.string.var_create_chat_room));
                                serviceIntent.putExtra(getResources().getString(R.string.var_chat_room_id),room_id);
                                serviceIntent.putExtra(getResources().getString(R.string.other_mobile_number),oMobileNumber);
                                serviceIntent.putExtra(getResources().getString(R.string.var_room_name),name);
                                startService(serviceIntent);

                                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                                intent.putExtra(getResources().getString(R.string.var_chat_room_id),room_id);
                                intent.putExtra(getResources().getString(R.string.var_room_name),name);
                                intent.putExtra("image",image);
                                startActivity(intent);

                            } else {
                                Log.e("MyClass",jsonObject.getString(getResources().getString(R.string.var_error)));
                            }
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
                data.put(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                data.put(getResources().getString(R.string.other_mobile_number), oMobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private void updateClassInfo(String classId,String mobile,String time) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_CLASS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(),"수업이 완료되었습니다",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("MyClass",response);
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
                data.put("student",mobile);
                data.put("teacher",user.get(SessionManager.MOBILE_NUMBER));
                data.put("classId",classId);
                data.put("time",time);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }


    private void getClassInformation() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_CLASS_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String userType = user.get(SessionManager.USER_TYPE);
                        if(userType.equals("student")){
                            setAdapterClassInfo2(response);
                        } else if (userType.equals("teacher")){
                            setAdapterClassInfo1(response);
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
                data.put(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                data.put("type",user.get(SessionManager.USER_TYPE));
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void setAdapterClassInfo1(String response){
        ArrayList<ClassData3> dataList = new ArrayList<>();
        try{

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("class");
            for (int i = 0; i < jsonArray.length(); i++){
                String image = IMAGE_URL+jsonArray.getJSONObject(i).getString("profile_image");
                String name = jsonArray.getJSONObject(i).getString("user_name");
                String status = jsonArray.getJSONObject(i).getString("status");
                String time = jsonArray.getJSONObject(i).getString("time");
                String date = jsonArray.getJSONObject(i).getString("date");
                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");
                String classId = jsonArray.getJSONObject(i).getString("id");
                dataList.add(new ClassData3(mobile,image,name,date,time,status,classId));
            }

            rv_class.setAdapter(new ClassAdapter3(dataList, getApplicationContext(), new ClassAdapter3.ButtonClickListener() {
                @Override
                public void onChangeButtonClick(ClassData3 data) {
                    Intent intent = new Intent(getApplicationContext(), ClassReservationActivity.class);
                    intent.putExtra(getResources().getString(R.string.mobile_number),data.getMobile());
                    startActivity(intent);
                }

                @Override
                public void onFinishButtonClick(ClassData3 data) {
                    updateClassInfo(data.getClassId(),data.getMobile(),data.getTime());
                }
            }));

        } catch (Exception e){
            e.printStackTrace();

        }
    }

    private void setAdapterClassInfo2(String response){
        ArrayList<ClassData3> dataList = new ArrayList<>();
        try{

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("class");
            for (int i = 0; i < jsonArray.length(); i++){
                String image = IMAGE_URL+jsonArray.getJSONObject(i).getString("profile_image");
                String name = jsonArray.getJSONObject(i).getString("user_name");
                String status = jsonArray.getJSONObject(i).getString("status");
                String time = jsonArray.getJSONObject(i).getString("time");
                String date = jsonArray.getJSONObject(i).getString("date");
                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");
                String classId = jsonArray.getJSONObject(i).getString("id");
                dataList.add(new ClassData3(mobile,image,name,date,time,status,classId));
            }

            rv_class.setAdapter(new ClassAdapter4(dataList,getApplicationContext()));

        } catch (Exception e){
            e.printStackTrace();

        }
    }



    private void getRequest() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String userType = user.get(SessionManager.USER_TYPE);
                        if(userType.equals("student")){
                            setAdapterClass1(response);
                        } else if (userType.equals("teacher")){
                            setAdapterClass2(response);
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
                data.put(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                data.put("type",user.get(SessionManager.USER_TYPE));
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    //
    private void setAdapterClass1(String response){
        ArrayList<ClassData1> dataList = new ArrayList<>();
        try{

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < jsonArray.length(); i++){

                String image = IMAGE_URL+jsonArray.getJSONObject(i).getString("profile_image");
                String name = jsonArray.getJSONObject(i).getString("user_name");
                String education = jsonArray.getJSONObject(i).getString("education")+" "+jsonArray.getJSONObject(i).getString("major");
                String time = jsonArray.getJSONObject(i).getString("count");
                String date = jsonArray.getJSONObject(i).getString("date");
                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");
                float rating = Float.parseFloat(jsonArray.getJSONObject(i).getString("cumulative_rating"))-Float.parseFloat(jsonArray.getJSONObject(i).getString("rating_participants"));
                dataList.add(new ClassData1(image,mobile,name,education,time,date,rating));
            }

            rv_class.setAdapter(new ClassAdapter1(dataList, getApplicationContext(), new ClassAdapter1.ButtonClickListener() {
                @Override
                public void onItemClick(ClassData1 data) {
                    createChatRoom(data.getMobile(),data.getName(),data.getImage());
                }
            }));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAdapterClass2(String response){
        ArrayList<ClassData2> dataList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++){

                String image = IMAGE_URL+jsonArray.getJSONObject(i).getString("profile_image");
                String name = jsonArray.getJSONObject(i).getString("user_name");
                String age = jsonArray.getJSONObject(i).getString("age");
                String gender =jsonArray.getJSONObject(i).getString("gender");
                String time = jsonArray.getJSONObject(i).getString("count");
                String date = jsonArray.getJSONObject(i).getString("date");
                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");

                dataList.add(new ClassData2(mobile,image,name,age,gender,time,date));
            }

            rv_class.setAdapter(new ClassAdapter2(dataList, getApplicationContext(), new ClassAdapter2.ButtonClickListener() {
                @Override
                public void onItemClick(ClassData2 data) {
                    createChatRoom(data.getMobile(),data.getName(),data.getImage());
                }
            }));

        } catch (Exception e){
            e.printStackTrace();

        }
    }
}