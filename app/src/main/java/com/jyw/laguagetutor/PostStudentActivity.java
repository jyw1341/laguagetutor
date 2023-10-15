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

public class PostStudentActivity extends AppCompatActivity {

    String oMobileNumber,mMobileNumber;
    TextView textView_post_title,textView_post_date
            ,textView_post_nickname
            ,textView_post_location
            ,textView_post_study_type
            ,textView_post_subject
            ,textView_post_schedule
            ,textView_post_rate
            ,textView_post_condition
            ,textView_post_description
            ,textView_post_info
            ;

    Button button_update,button_delete,button_chat;

    SessionManager sessionManager;
    HashMap<String, String> user;

    private static final String BASE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/send_student_post.php";
    private static final String URL_DELETE = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/delete_student_post.php";
    private static final String URL_CREATE_ROOM = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/create_chat_room.php";
    private static final String URL_CREATE_FAVORITE = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/create_favorite.php";
    private static final String TAG = PostStudentActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_student);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        user = sessionManager.getUserDetail();

        button_chat = findViewById(R.id.button_chat);
        button_chat.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                createFavorite();
             }
         });
        String user_type =user.get(sessionManager.USER_TYPE);
        if(user_type!=null){
            if(user_type.equals("student")){
                button_chat.setVisibility(View.GONE);
            }
        }

        Intent dataIntent = getIntent();
        oMobileNumber = dataIntent.getStringExtra("mobile_number");

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String,String> user = sessionManager.getUserDetail();
        mMobileNumber = user.get(sessionManager.MOBILE_NUMBER);
        String mUserType = user.get(sessionManager.USER_TYPE);


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

        button_update = findViewById(R.id.button_update);
        button_update.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateStudentPostActivity.class);
                intent.putExtra("mobile_number", oMobileNumber);
                intent.putExtra("title",textView_post_title.getText());
                intent.putExtra("location",textView_post_location.getText());
                intent.putExtra("study_type",textView_post_study_type.getText());
                intent.putExtra("subject",textView_post_subject.getText());
                intent.putExtra("schedule",textView_post_schedule.getText());
                intent.putExtra("rate",textView_post_rate.getText());
                intent.putExtra("condition",textView_post_condition.getText());
                intent.putExtra("description",textView_post_description.getText());
                intent.putExtra("info",textView_post_info.getText());
                startActivity(intent);
             }
         });


        if(oMobileNumber.equals(mMobileNumber)){
            button_update.setVisibility(View.VISIBLE);
            button_delete.setVisibility(View.VISIBLE);
            button_chat.setVisibility(View.GONE);

            Log.w(TAG, oMobileNumber +"/"+mMobileNumber);
        }

        if(mUserType.equals("student")){
            button_chat.setVisibility(View.GONE);
        }

        textView_post_title = findViewById(R.id.textView_post_title);

        textView_post_date = findViewById(R.id.textView_post_date);

        textView_post_nickname = findViewById(R.id.textView_post_nickname);

        textView_post_location = findViewById(R.id.textView_post_location);

        textView_post_study_type = findViewById(R.id.textView_post_study_type);

        textView_post_subject = findViewById(R.id.textView_post_subject);

        textView_post_schedule = findViewById(R.id.textView_post_schedule);

        textView_post_rate = findViewById(R.id.textView_post_rate);

        textView_post_condition = findViewById(R.id.textView_post_condition);

        textView_post_description = findViewById(R.id.textView_post_description);

        textView_post_info = findViewById(R.id.textView_post_info);
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
                                textView_post_rate.setText(jsonObject.getString("rate"));
                                textView_post_condition.setText(jsonObject.getString("conditions"));
                                textView_post_description.setText(jsonObject.getString("description"));
                                String info = jsonObject.getString("age")+"/"+jsonObject.getString("gender");
                                textView_post_info.setText(info);
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
                    data.put("type",user.get(SessionManager.USER_TYPE));
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }
}