package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.jyw.laguagetutor.adapter.ReviewAdapter;
import com.jyw.laguagetutor.recyclerData.ReviewData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfileActivity extends AppCompatActivity {

    TextView textView_teacher_nickname,textView_teacher_rating,textView_teacher_university,textView_offline_rate,textView_online_rate,
            textView_teacher_gender,textView_lesson_location,textView_teacher_subjects,textView_teacher_bio,textView_lesson_style;

    RatingBar ratingBar;
    String oMobileNumber;
    CircleImageView circleImageView_profile;

    TextView tv_rating,tv_total;
    RatingBar ratingBar4;
    RecyclerView rv_reviews;

    private TabLayout tab_layout;
    private ScrollView sv_profile;
    private ConstraintLayout constraintLayout;

    private static final String TAG = TeacherProfileActivity.class.getSimpleName();
    static final String BASE_URL =  "zonaws.com/server1/set_teacher_profile.php";
    static final String REVIEW_READ =  "http:amazonaws.com/server1/review_read.php";
    private static final String IMAGE_URL = "httr1/images/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView_teacher_nickname = findViewById(R.id.textView_teacher_nickname);
        textView_teacher_gender = findViewById(R.id.textView_teacher_gender);
        textView_teacher_university = findViewById(R.id.textView_teacher_university);
//        textView_teacher_rating = findViewById(R.id.textView_teacher_rating);

        textView_offline_rate = findViewById(R.id.textView_offline_rate);
        textView_online_rate = findViewById(R.id.textView_online_rate);
        textView_lesson_location = findViewById(R.id.textView_lesson_location);
        textView_teacher_subjects = findViewById(R.id.textView_teacher_subjects);

        textView_teacher_bio = findViewById(R.id.textView_teacher_bio);
        textView_lesson_style = findViewById(R.id.textView_lesson_style);
//        ratingBar = findViewById(R.id.ratingBar);
        circleImageView_profile = findViewById(R.id.circleImageView_profile);



        Intent dataIntent = getIntent();
        oMobileNumber = dataIntent.getStringExtra("mobile_number");

        sv_profile = findViewById(R.id.sv_profile);
        constraintLayout = findViewById(R.id.constraintLayout);

        ratingBar4 = findViewById(R.id.ratingBar4);
        tv_rating = findViewById(R.id.tv_rating);
        tv_total = findViewById(R.id.tv_total);
        rv_reviews = findViewById(R.id.rv_reviews);
        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0 :
                        sv_profile.setVisibility(View.VISIBLE);
                        constraintLayout.setVisibility(View.GONE);
                        break;
                    case 1 :
                        constraintLayout.setVisibility(View.VISIBLE);
                        sv_profile.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        requestTeacherProfile();
        requestTeacherReviews();
    }

    private void requestTeacherReviews() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                REVIEW_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String total = "("+jsonObject.getString("cnt")+")";
                            String avg = jsonObject.getString("avg");
                            tv_total.setText(total);
                            tv_rating.setText(avg);
                            ratingBar4.setRating(Float.parseFloat(avg));

                            ArrayList<ReviewData> list = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("review");

                            for (int i = 0; i < jsonArray.length(); i++){
                                String reviewId = jsonArray.getJSONObject(i).getString("id");
                                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");
                                String name = jsonArray.getJSONObject(i).getString("user_name");
                                String desc = jsonArray.getJSONObject(i).getString("review");
                                String date = jsonArray.getJSONObject(i).getString("date");
                                String rating = jsonArray.getJSONObject(i).getString("rating");

                                list.add(new ReviewData(reviewId,mobile,name,desc,date,rating));
                            }

                            rv_reviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rv_reviews.setAdapter(new ReviewAdapter(list,getApplicationContext()));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(TAG,error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mobile_number",oMobileNumber);
                data.put("code","0");
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

        private void requestTeacherProfile() {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                        BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");
                            if(condition.equals("success")){
                                String name = jsonObject.getString("name");
                                String rating = jsonObject.getString("rating");

                                String gender = jsonObject.getString("gender");
                                String location = jsonObject.getString("location");
                                String subjects = jsonObject.getString("subjects");

                                int tmp = Integer.parseInt(jsonObject.getString("rate_offline"));
                                DecimalFormat myFormatter = new DecimalFormat("###,###");
                                String formattedStringPrice = myFormatter.format(tmp);
                                String rate_offline = formattedStringPrice+"원/1시간";

                                tmp = Integer.parseInt(jsonObject.getString("rate_online"));
                                formattedStringPrice = myFormatter.format(tmp);
                                String rate_online = formattedStringPrice+"원/1시간";

                                String image = jsonObject.getString("image");
                                if(image.equals("null")||image.equals("")){
                                    circleImageView_profile.setImageResource(R.drawable.basic_profile);
                                } else {
                                    Uri uri = Uri.parse(IMAGE_URL+image);
                                    Picasso.get().load(uri).into(circleImageView_profile);
                                }

                                textView_teacher_nickname.setText(name);
                                textView_teacher_gender.setText(gender);
                                textView_teacher_university.setText(jsonObject.getString("education"));

                                textView_offline_rate.setText(rate_offline);
                                textView_online_rate.setText(rate_online);
                                textView_teacher_rating.setText(rating);
                                textView_lesson_location.setText(location);
                                textView_teacher_subjects.setText(subjects);

                                textView_teacher_bio.setText(jsonObject.getString("bio"));
                                textView_lesson_style.setText(jsonObject.getString("lesson_style"));
                                float flt = Float.parseFloat(rating);
                                ratingBar.setRating(flt);
                            } else {
                                String error = jsonObject.getString("message");
                                Log.w(TAG,error);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            Log.e(TAG,""+e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(TAG,error.toString());
                    }
                }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("mobile_number",oMobileNumber);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }
}