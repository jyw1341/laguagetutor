package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jyw.laguagetutor.adapter.RecyclerViewPagerAdapter;
import com.jyw.laguagetutor.recyclerData.Teacher;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterTeacherWaitingActivity extends AppCompatActivity {

    String mobile_number, study_type,title,time,subject,description;

    private static final String BASE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/register_teacher_waiting.php";
    private static final String TAG = RegisterTeacherWaitingActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher_waiting);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        mobile_number = user.get(sessionManager.MOBILE_NUMBER);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText editText_time = findViewById(R.id.editText_time);
        EditText editText_title = findViewById(R.id.editText_title);
        EditText editText_subject = findViewById(R.id.editText_subject);
        EditText editText_description = findViewById(R.id.editText_description);

        RadioGroup radioGroup_study_type = findViewById(R.id.radioGroup_study_type);
        radioGroup_study_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_all :
                        study_type = "대면/비대면";
                        break;
                    case R.id.radioButton_offline :
                        study_type = "대면";
                        break;
                    case R.id.radioButton_online :
                        study_type = "비대면";
                        break;
                }
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editText_title.getText().toString().trim();
                time = editText_time.getText().toString().trim();
                subject = editText_subject.getText().toString().trim();
                description = editText_description.getText().toString().trim();
                registerWaitingList();
            }
        });
    }

    private void registerWaitingList() {

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
                                Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
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
                data.put("mobile_number",mobile_number);
                data.put("study_type",study_type);
                data.put("title",title);
                data.put("time",time);
                data.put("subject",subject);
                data.put("description",description);

                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }
}