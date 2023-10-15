package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class WriteReview extends AppCompatActivity {

    private EditText et_review;
    private RatingBar ratingBar3;
    private ProgressBar pb_review;
    private Button btn_confirm;

    String classId,mobile;

    SessionManager sessionManager;
    HashMap<String, String> user;

    private static final String URL_CREATE_REVIEW = "server1/review_create.php";
    private static final String TAG = WriteReview.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetail();

        //MyClass 액티에서 리뷰하려는 수업의 id 넘겨받음
        Intent dataIntent = getIntent();
        classId = dataIntent.getStringExtra("classId");
        mobile = dataIntent.getStringExtra("mobile");
        String image = dataIntent.getStringExtra("image");
        String name = dataIntent.getStringExtra("name");

        //선생님 프로필 이미지 초기화
        CircleImageView cv_profile = findViewById(R.id.cv_profile);
        Picasso.get().load(image).into(cv_profile);

        //선생님 닉네임 초기화
        TextView tv_name = findViewById(R.id.tv_name);
        tv_name.setText(name);

        //액티비티 닫기 기능 이미지뷰
        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pb_review = findViewById(R.id.pb_review);

        ratingBar3 = findViewById(R.id.ratingBar3);
        et_review = findViewById(R.id.et_review);

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_review.setVisibility(View.VISIBLE);
                btn_confirm.setVisibility(View.GONE);
                createReview();
            }
        });
    }

    private void createReview() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_CREATE_REVIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.equals("1")){
                                Toast.makeText(getApplicationContext(),"평가가 완료되었습니다",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                pb_review.setVisibility(View.GONE);
                                btn_confirm.setVisibility(View.VISIBLE);
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
                data.put("id",classId);
                data.put("rating",Float.toString(ratingBar3.getRating()));
                data.put("review",et_review.getText().toString());
                data.put("student",user.get(SessionManager.MOBILE_NUMBER));
                data.put("teacher",mobile);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}