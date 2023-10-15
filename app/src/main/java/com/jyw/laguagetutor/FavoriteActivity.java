package com.jyw.laguagetutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jyw.laguagetutor.adapter.FavoriteAdapter;
import com.jyw.laguagetutor.adapter.FavoriteAdapter2;
import com.jyw.laguagetutor.recyclerData.FavoriteForStudentData;
import com.jyw.laguagetutor.recyclerData.FavoriteForTeacherData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    private TabLayout tab_layout;

    private static final String URL_GET_FAVORITE = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/get_favorite.php";
    private static final String URL_UPDATE = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/update_favorite.php";
    private static final String IMAGE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/images/";
    private static final String URL_CREATE_ROOM = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/create_chat_room.php";
    private SessionManager sessionManager;
    private HashMap<String, String> user;

    private RecyclerView rv_favorite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();

        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_favorite);
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

                        break;

                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                        overridePendingTransition(0,0);
                        finish();
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


        rv_favorite = findViewById(R.id.rv_favorite);
        rv_favorite.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                createFavorite(Integer.toString(tab.getPosition()));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        createFavorite("0");
    }

    private void createChatRoom(String oMobileNumber,String name,String image) {
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

    private void createFavorite(String position) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_GET_FAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String type = user.get(SessionManager.USER_TYPE);
                        if(type.equals("teacher")){

                            setAdapterForTeacher(response);
                        } else if(type.equals("student")){

                            setAdapterForStudent(response);
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
                data.put("position",position);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void setAdapterForStudent(String response){
        ArrayList<FavoriteForStudentData> dataList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < jsonArray.length(); i++){

                String image = IMAGE_URL+jsonArray.getJSONObject(i).getString("profile_image");
                String name = jsonArray.getJSONObject(i).getString("user_name");
                String education = jsonArray.getJSONObject(i).getString("education")+" "+jsonArray.getJSONObject(i).getString("major");
                String region = jsonArray.getJSONObject(i).getString("location");
                String subjects = jsonArray.getJSONObject(i).getString("subjects");
                String price = jsonArray.getJSONObject(i).getString("rate_offline");
                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");
                float rating = Float.parseFloat(jsonArray.getJSONObject(i).getString("cumulative_rating"))-Float.parseFloat(jsonArray.getJSONObject(i).getString("rating_participants"));
                dataList.add(new FavoriteForStudentData(mobile,image,name,education,region,subjects,price,rating));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        rv_favorite.setAdapter(new FavoriteAdapter(dataList, getApplicationContext(), tab_layout.getSelectedTabPosition(),new FavoriteAdapter.ButtonClickListener() {
            @Override
            public void onItemClick(FavoriteForStudentData data) {
                String tabPosition = Integer.toString(tab_layout.getSelectedTabPosition());
                updateRequest(tabPosition,data.getMobile());
            }

            @Override
            public void onChatClick(FavoriteForStudentData data) {
                createChatRoom(data.getMobile(),data.getName(),data.getImage());
            }

        }));
    }

    private void setAdapterForTeacher(String response){
        ArrayList<FavoriteForTeacherData> dataList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            Log.d("set",jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++){

                String image = IMAGE_URL+jsonArray.getJSONObject(i).getString("profile_image");
                String name = jsonArray.getJSONObject(i).getString("user_name");
                String gender= jsonArray.getJSONObject(i).getString("gender");
                String age = jsonArray.getJSONObject(i).getString("age");
                String region = jsonArray.getJSONObject(i).getString("location");
                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");

                dataList.add(new FavoriteForTeacherData(mobile,name,age,gender,region,image));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        rv_favorite.setAdapter(new FavoriteAdapter2(dataList, getApplicationContext(), tab_layout.getSelectedTabPosition(),new FavoriteAdapter2.ButtonClickListener() {
            @Override
            public void onItemClick(FavoriteForTeacherData data) {
                String tabPosition = Integer.toString(tab_layout.getSelectedTabPosition());
                updateRequest(tabPosition,data.getMobile());
            }

            @Override
            public void onChatClick(FavoriteForTeacherData data) {
                createChatRoom(data.getMobile(),data.getName(),data.getImage());
            }
        }));
    }



        private void updateRequest(String position, String pMobile) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equals("success")){
                                Intent intent = new Intent(getApplicationContext(), MyClassActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
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
                    data.put(getResources().getString(R.string.other_mobile_number),pMobile);
                    data.put("type",user.get(SessionManager.USER_TYPE));
                    data.put("position",position);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }
}