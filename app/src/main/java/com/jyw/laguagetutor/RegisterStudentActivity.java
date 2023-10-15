package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

public class RegisterStudentActivity extends AppCompatActivity {

    private TextView textView_sex_error,textView_age_error,textView_address_error,textView_terms_error;
    private RadioGroup radioGroup_sex;
    private RadioButton radioButton_man,radioButton_woman;
    private Spinner spinner_student_age,spinner_si_do,spinner_gu_gun;
    private Button button_register,button_terms1,button_terms2;
    private CheckBox checkBox_terms1,checkBox_terms2;
    private ProgressBar progressBar_loading;

    private static final String TAG = RegisterStudentActivity.class.getSimpleName();
    private static final String URL_REGISTER ="http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        textView_sex_error = findViewById(R.id.textView_sex_error);
        textView_age_error = findViewById(R.id.textView_age_error);
        textView_address_error = findViewById(R.id.textView_address_error);
        textView_terms_error = findViewById(R.id.textView_terms_error);
        radioGroup_sex = findViewById(R.id.radioGroup_sex);
        radioButton_man = findViewById(R.id.radioButton_man);
        radioButton_woman = findViewById(R.id.radioButton_woman);
        spinner_student_age = findViewById(R.id.spinner_student_age);
        spinner_si_do = findViewById(R.id.spinner_si_do);
        spinner_gu_gun = findViewById(R.id.spinner_gu_gun);
        button_register = findViewById(R.id.button_register);
        button_terms1 = findViewById(R.id.button_terms1);
        button_terms2 = findViewById(R.id.button_terms2);
        checkBox_terms1 = findViewById(R.id.checkBox_terms1);
        checkBox_terms2 = findViewById(R.id.checkBox_terms2);
        progressBar_loading = findViewById(R.id.progressBar_loading);

        button_register.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                textView_sex_error.setVisibility(View.GONE);
                textView_age_error.setVisibility(View.GONE);
                textView_address_error.setVisibility(View.GONE);
                textView_terms_error.setVisibility(View.GONE);
                String sex = getSex();
                String studentAge = getAge();
                String address = getAddress();
                if(sex.equals("")){
                    textView_sex_error.setVisibility(View.VISIBLE);
                    textView_sex_error.setText(R.string.register_sex_error);
                    Toast.makeText(getApplicationContext(),R.string.register_sex_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(studentAge.equals("")){
                    textView_age_error.setVisibility(View.VISIBLE);
                    textView_age_error.setText(R.string.register_age_error);
                    Toast.makeText(getApplicationContext(),R.string.register_age_error,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(address.equals("")){
                    textView_address_error.setVisibility(View.VISIBLE);
                    textView_address_error.setText(R.string.register_address_error);
                    Toast.makeText(getApplicationContext(),R.string.register_address_error,Toast.LENGTH_SHORT).show();
                    return;
                }

                 if(isCheckedTerms()){
                     return;
                 }

                progressBar_loading.setVisibility(View.VISIBLE);
                button_register.setVisibility(View.GONE);
                register(sex,studentAge,address);

             }
         });

        ArrayAdapter studentAgeAdapter = ArrayAdapter.createFromResource(this,R.array.student_age, android.R.layout.simple_spinner_dropdown_item);
        spinner_student_age.setAdapter(studentAgeAdapter);

        ArrayAdapter si_do_Adapter = ArrayAdapter.createFromResource(this,R.array.spinner_region, android.R.layout.simple_spinner_dropdown_item);
        spinner_si_do.setAdapter(si_do_Adapter);

        spinner_si_do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        spinner_gu_gun.setAdapter(null);
                        break;
                    case 1:
                        setSpinnerAdapterItem(R.array.spinner_region_seoul);
                        break;
                    case 2:
                        setSpinnerAdapterItem(R.array.spinner_region_busan);
                        break;
                    case 3:
                        setSpinnerAdapterItem(R.array.spinner_region_daegu);
                        break;
                    case 4:
                        setSpinnerAdapterItem(R.array.spinner_region_incheon);
                        break;
                    case 5:
                        setSpinnerAdapterItem(R.array.spinner_region_gwangju);
                        break;
                    case 6:
                        setSpinnerAdapterItem(R.array.spinner_region_daejeon);
                        break;
                    case 7:
                        setSpinnerAdapterItem(R.array.spinner_region_ulsan);
                        break;
                    case 8:
                        setSpinnerAdapterItem(R.array.spinner_region_sejong);
                        break;
                    case 9:
                        setSpinnerAdapterItem(R.array.spinner_region_gyeonggi);
                        break;
                    case 10:
                        setSpinnerAdapterItem(R.array.spinner_region_gangwon);
                        break;
                    case 11:
                        setSpinnerAdapterItem(R.array.spinner_region_chung_buk);
                        break;
                    case 12:
                        setSpinnerAdapterItem(R.array.spinner_region_chung_nam);

                        break;
                    case 13:
                        setSpinnerAdapterItem(R.array.spinner_region_jeon_buk);
                        break;
                    case 14:
                        setSpinnerAdapterItem(R.array.spinner_region_jeon_nam);
                        break;
                    case 15:
                        setSpinnerAdapterItem(R.array.spinner_region_gyeong_buk);
                        break;
                    case 16:
                        setSpinnerAdapterItem(R.array.spinner_region_gyeong_nam);
                        break;
                    case 17:
                        setSpinnerAdapterItem(R.array.spinner_region_jeju);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void register(String sex, String age, String address) {

        StringRequest request = new StringRequest(
            Request.Method.POST,
                    URL_REGISTER,
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
                            updateUI();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG,""+e.toString());
                        updateUI();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    updateUI();
                }
            }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Intent dataIntent = getIntent();
                String registerType = dataIntent.getStringExtra("registerType");
                if(registerType.equals("normal")){
                    data.put("userId",dataIntent.getStringExtra("userId"));
                    data.put("password",dataIntent.getStringExtra("password"));
                } else {
                    data.put("email",dataIntent.getStringExtra("email"));
                }
                data.put("registerType",registerType);
                data.put("name",dataIntent.getStringExtra("nickname"));
                data.put("userType",dataIntent.getStringExtra("userType"));
                data.put("mobileNumber",dataIntent.getStringExtra("mobileNumber"));
                data.put("gender",sex);
                data.put("age",age);
                data.put("location",address);
                Log.w(TAG,age+address);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private String getSex(){
        if(radioGroup_sex.getCheckedRadioButtonId()==R.id.radioButton_man){
            return "남자";
        } else if (radioGroup_sex.getCheckedRadioButtonId()==R.id.radioButton_woman){
            return "여자";
        }
        return "";
    }

    private String getAge(){
        if(spinner_student_age.getSelectedItemPosition()==0){
            return "";
        } else {
            return spinner_student_age.getSelectedItem().toString();
        }
    }

    private String getAddress(){
        if(spinner_si_do.getSelectedItemPosition()==0){
            return "";
        } else {
            String si_do = spinner_si_do.getSelectedItem().toString();
            String gun_gu = spinner_gu_gun.getSelectedItem().toString();
            return  si_do+" "+gun_gu;
        }
    }

    private void setSpinnerAdapterItem(int array_resource) {
        ArrayAdapter gun_gu_adapter = ArrayAdapter.createFromResource(this,array_resource, android.R.layout.simple_spinner_dropdown_item);
        spinner_gu_gun.setAdapter(gun_gu_adapter);
    }

    private void updateUI(){
        progressBar_loading.setVisibility(View.GONE);
        button_register.setVisibility(View.VISIBLE);
    }

    private boolean isCheckedTerms(){
        if(checkBox_terms1.isChecked()&&checkBox_terms2.isChecked()){
            return false;
        }
        textView_terms_error.setVisibility(View.VISIBLE);
        textView_terms_error.setText(R.string.register_terms_error);
        Toast.makeText(getApplicationContext(),R.string.register_terms_error,Toast.LENGTH_SHORT).show();
        return true;
    }
}