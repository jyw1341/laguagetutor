package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class UpdateStudentPostActivity extends AppCompatActivity {

    EditText editText_post_title,editText_post_subject,editText_post_schedule,editText_post_rate,editText_post_condition,editText_post_description;
    Button button_update;
    RadioGroup radioGroup_study_type;
    RadioButton radioButton_all,radioButton_offline,radioButton_online;

    String mobile_number;

    private static final String BASE_URL = "erver1/update_student_post.php";
    private static final String TAG = UpdateStudentPostActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_post);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent dataIntent = getIntent();

        mobile_number = dataIntent.getStringExtra("mobile_number");

        editText_post_title = findViewById(R.id.editText_post_title);
        editText_post_title.setText(dataIntent.getStringExtra("title"));

        editText_post_subject = findViewById(R.id.editText_post_subject);
        editText_post_subject.setText(dataIntent.getStringExtra("subject"));

        editText_post_schedule = findViewById(R.id.editText_post_schedule);
        editText_post_schedule.setText(dataIntent.getStringExtra("schedule"));

        editText_post_rate = findViewById(R.id.editText_post_rate);
        editText_post_rate.setText(dataIntent.getStringExtra("rate"));

        editText_post_condition = findViewById(R.id.editText_post_condition);
        editText_post_condition.setText(dataIntent.getStringExtra("condition"));

        editText_post_description = findViewById(R.id.editText_post_description);
        editText_post_description.setText(dataIntent.getStringExtra("description"));

        radioGroup_study_type = findViewById(R.id.radioGroup_study_type);
        radioButton_all = findViewById(R.id.radioButton_all);
        radioButton_offline = findViewById(R.id.radioButton_offline);
        radioButton_online = findViewById(R.id.radioButton_online);

        String position = dataIntent.getStringExtra("study_type");
        switch (position){
            case "대면/비대면" :
                radioButton_all.setChecked(true);
                break;
            case "대면" :
                radioButton_offline.setChecked(true);
                break;
            case "비대면" :
                radioButton_online.setChecked(true);
                break;
        }

        button_update = findViewById(R.id.button_teacher_update);
        button_update.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                updatePost();
             }
         });
    }

    private void updatePost() {
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
                data.put("title",editText_post_title.getText().toString().trim());

                int id = radioGroup_study_type.getCheckedRadioButtonId();
                switch (id){
                    case R.id.radioButton_all :
                        data.put("study_type","대면/비대면");
                        break;
                    case R.id.radioButton_offline:
                        data.put("study_type","대면");
                        break;
                    case R.id.radioButton_online:
                        data.put("study_type","비대면");
                        break;
                }

                data.put("subject",editText_post_subject.getText().toString().trim());
                data.put("schedule",editText_post_schedule.getText().toString().trim());
                data.put("rate",editText_post_rate.getText().toString().trim());
                data.put("description",editText_post_description.getText().toString().trim());
                data.put("condition",editText_post_condition.getText().toString().trim());

                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }
}