package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jyw.laguagetutor.adapter.RecyclerViewPagerAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostTeacherActivity extends AppCompatActivity {

    String oMobileNumber,mMobileNumber,mUserType;
    TextView textView_post_title,textView_post_date
            ,textView_post_nickname
            ,textView_post_location
            ,textView_post_study_type
            ,textView_post_subject
            ,textView_post_schedule
            ,textView_post_offrate
            ,textView_post_onrate
            ,textView_post_description
            ,textView_post_special
            ;

    Button button_update,button_delete,button_profile,button_chat;
    private static final String BASE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/send_teacher_post.php";
    private static final String URL_DELETE = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/delete_teacher_post.php";
    private static final String URL_CREATE_ROOM = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/create_chat_room.php";
    private static final String URL_CREATE_FAVORITE = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/create_favorite.php";
    private static final String TAG = PostTeacherActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_teacher);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Intent dataIntent = getIntent();
        oMobileNumber = dataIntent.getStringExtra("mobile_number");

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String,String> user = sessionManager.getUserDetail();
        mMobileNumber = user.get(sessionManager.MOBILE_NUMBER);
        mUserType = user.get(sessionManager.USER_TYPE);

        button_delete = findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        button_update = findViewById(R.id.button_teacher_update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateTeacherPostActivity.class);
                intent.putExtra("mobile_number", oMobileNumber);
                intent.putExtra("title",textView_post_title.getText());
                intent.putExtra("study_type",textView_post_study_type.getText());
                intent.putExtra("subject",textView_post_subject.getText());
                intent.putExtra("schedule",textView_post_schedule.getText());
                intent.putExtra("description",textView_post_description.getText());

                startActivity(intent);
            }
        });

        button_profile = findViewById(R.id.button_profile);
        button_profile.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeacherProfileActivity.class);
                intent.putExtra("mobile_number", oMobileNumber);
                startActivity(intent);
             }
         });

        button_chat = findViewById(R.id.button_chat);
        button_chat.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                createChatRoom();
                createFavorite();
             }
         });


        if(oMobileNumber.equals(mMobileNumber)){
            button_update.setVisibility(View.VISIBLE);
            button_delete.setVisibility(View.VISIBLE);
            button_chat.setVisibility(View.GONE);

            Log.w(TAG, oMobileNumber +"/"+mMobileNumber);
        }

        if(mUserType.equals("teacher")){
            Log.w(TAG,"dd");
            button_chat.setVisibility(View.GONE);
        }

        textView_post_title = findViewById(R.id.textView_post_title);

        textView_post_date = findViewById(R.id.textView_post_date);

        textView_post_nickname = findViewById(R.id.textView_post_nickname);

        textView_post_location = findViewById(R.id.textView_post_location);

        textView_post_study_type = findViewById(R.id.textView_post_study_type);

        textView_post_subject = findViewById(R.id.textView_post_subject);

        textView_post_schedule = findViewById(R.id.textView_post_schedule);

        textView_post_offrate = findViewById(R.id.textView_post_offrate);

        textView_post_onrate = findViewById(R.id.textView_post_onrate);

        textView_post_description = findViewById(R.id.textView_post_description);

        textView_post_special = findViewById(R.id.textView_post_special);

        setPost();
    }

    private void deletePost() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.w(TAG,response);
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");
                            if(condition.equals("success")){

                            } else {
                                String error = jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
                                Log.w(TAG,error);
                            }
                        } catch (Exception e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(RecyclerViewPagerAdapter.class.getSimpleName(),error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mobile_number", oMobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private void setPost() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.w(TAG,response);
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");
                            if(condition.equals("success")){
                                textView_post_title.setText(jsonObject.getString("title"));
                                textView_post_date.setText(jsonObject.getString("date"));
                                textView_post_nickname.setText(jsonObject.getString("name"));
                                textView_post_location.setText(jsonObject.getString("location"));
                                textView_post_study_type.setText(jsonObject.getString("study_type"));
                                textView_post_subject.setText(jsonObject.getString("subject"));
                                textView_post_schedule.setText(jsonObject.getString("schedule"));
                                textView_post_offrate.setText(jsonObject.getString("rate_offline"));
                                textView_post_onrate.setText(jsonObject.getString("rate_online"));
                                textView_post_description.setText(jsonObject.getString("description"));
                                textView_post_special.setText(jsonObject.getString("specialize"));

                            } else {
                                String error = jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
                                Log.w(TAG,error);
                            }
                        } catch (Exception e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(RecyclerViewPagerAdapter.class.getSimpleName(),error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mobile_number", oMobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private void createFavorite() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_CREATE_FAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                            startActivity(intent);
                            finish();
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
                data.put(getResources().getString(R.string.mobile_number),mMobileNumber);
                data.put(getResources().getString(R.string.other_mobile_number), oMobileNumber);
                data.put("type",mUserType);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void createChatRoom() {
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
                                serviceIntent.putExtra(getResources().getString(R.string.var_room_name),textView_post_nickname.getText().toString());
                                startService(serviceIntent);

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
                data.put(getResources().getString(R.string.mobile_number),mMobileNumber);
                data.put(getResources().getString(R.string.other_mobile_number), oMobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }
}