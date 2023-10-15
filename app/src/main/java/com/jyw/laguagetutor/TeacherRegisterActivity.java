package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeacherRegisterActivity extends AppCompatActivity {

    RadioGroup radioGroup_gender;
    EditText editTextText_education,editTextText_major,editTextText_bio,editTextText_lesson_style;
    Spinner spinner_education;
    Button button_register;

    private static final String URL_TEACHER_REGISTER = "naws.com/server1/register_teacher.php";
    private static final String TAG = TeacherRegisterActivity.class.getSimpleName();

    String userId,password,email,userType,name,mobileNumber,subjects,location,gender,education,educationStatus,major,bio,lessonStyle,rateOffline,rateOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        radioGroup_gender = findViewById(R.id.radioGroup_gender);
        editTextText_education = findViewById(R.id.editTextText_education);
        editTextText_major = findViewById(R.id.editTextText_major);
        editTextText_bio = findViewById(R.id.editTextText_bio);
        editTextText_lesson_style = findViewById(R.id.editTextText_lesson_style);
        spinner_education = findViewById(R.id.spinner_education);
        button_register = findViewById(R.id.button_register);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.education_status, android.R.layout.simple_spinner_dropdown_item);
        spinner_education.setAdapter(spinnerAdapter);

        button_register.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                if(radioGroup_gender.getCheckedRadioButtonId()==R.id.radioButton_man){
                    gender = "남자";
                } else {
                    gender = "여자";
                }
                education = editTextText_education.getText().toString().trim();
                educationStatus = spinner_education.getSelectedItem().toString().trim();
                education = education+"("+educationStatus+")";
                major = editTextText_major.getText().toString().trim();
                bio = editTextText_bio.getText().toString().trim();
                lessonStyle = editTextText_lesson_style.getText().toString().trim();

                volley();
                Log.w(TAG,"전송완료");
             }
         });
    }

        private void volley() {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                        URL_TEACHER_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");
                            String message = jsonObject.getString("message");
                            if(condition.equals("success")){
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            } else {
                                String error = jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "회원가입 오류 : "+error);
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
                        Log.e(TAG,""+error.toString());
                    }
                }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    Intent dataIntent = getIntent();
                    String registerType = dataIntent.getStringExtra("registerType");
                    if(registerType.equals("normal")){
                        userId = dataIntent.getStringExtra("userId");
                        password = dataIntent.getStringExtra("password");
                        data.put("userId",userId);
                        data.put("password",password);
                    } else {
                        email = dataIntent.getStringExtra("email");
                        data.put("email",email);
                    }
                    userType = dataIntent.getStringExtra("userType");
                    name = dataIntent.getStringExtra("nickname");
                    mobileNumber = dataIntent.getStringExtra("mobileNumber");
                    subjects = dataIntent.getStringExtra("subjectsList");
                    location = dataIntent.getStringExtra("locationList");
                    rateOffline = dataIntent.getStringExtra("rate_offline");
                    rateOnline = dataIntent.getStringExtra("rate_online");

                    data.put("sex",gender);
                    data.put("registerType",registerType);
                    data.put("user_type",userType);
                    data.put("name",name);
                    data.put("mobile_number",mobileNumber);
                    data.put("subjects",subjects);
                    data.put("location",location);
                    data.put("education",education);
                    data.put("major",major);
                    data.put("bio",bio);
                    data.put("lesson_style",lessonStyle);
                    data.put("rate_offline",rateOffline);
                    data.put("rate_online",rateOnline);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }
}