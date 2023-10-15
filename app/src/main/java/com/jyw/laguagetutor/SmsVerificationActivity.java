package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class SmsVerificationActivity extends AppCompatActivity {

    private EditText editText_phone_number,editText_verification_number;
    private Button button_send_number,button_confirm;
    private TextView textView_description,textView_error;
    private static final String TAG = SmsVerificationActivity.class.getSimpleName();

    private static final String URL_SMS_OTP = "pute.amazonaws.com/server1/send_otp.php";
    private static final String URL_SMS_VERIFY = "httute.amazonaws.com/server1/verify_otp.php";

    boolean getOTP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        editText_phone_number = findViewById(R.id.editText_phone_number);
        editText_verification_number = findViewById(R.id.editText_verification_number);
        textView_description = findViewById(R.id.textView_description);
        textView_error = findViewById(R.id.textView_error);

        Intent dataIntent = getIntent();
        String verificationType = dataIntent.getStringExtra("verification_type");

        if(verificationType.equals("register")){
            textView_description.setText("");
        } else {
            textView_description.setText(R.string.sms_verification_description);
        }

        //핸드폰번호 숫자만 입력되게 제한
        InputFilter filterNum = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[0-9]+$");

                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        };

        //핸드폰번호 최대입력 11자리로 제한
        InputFilter[] phoneNumberFilters = new InputFilter[]{
                new InputFilter.LengthFilter(11),
                filterNum
        };

        InputFilter[] otpNumberFilters = new InputFilter[]{
                new InputFilter.LengthFilter(5),
                filterNum
        };

        editText_phone_number.setFilters(phoneNumberFilters);
        editText_verification_number.setFilters(otpNumberFilters);

        button_send_number = findViewById(R.id.button_send_number);
        button_send_number.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String mobileNumber = editText_phone_number.getText().toString().trim();

                //11자리 미만으로 입력했을때 경고 토스트메시지
                if(mobileNumber.length()!=11){
                    Toast.makeText(getApplicationContext(),R.string.sms_verification_mobile_number_error,Toast.LENGTH_SHORT).show();
                } else {
                    getOTP(mobileNumber);
                    //인증번호가 발송되면 인증번호 입력창 입력 가능하게 변경,
                    editText_verification_number.setFocusableInTouchMode(true);
                    Toast.makeText(getApplicationContext(),R.string.sms_verification_send_done,Toast.LENGTH_SHORT).show();
                }
             }
         });

        button_confirm = findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {

             String mobileNumber = editText_phone_number.getText().toString().trim();
             String otpNumber = editText_verification_number.getText().toString().trim();

             String registerType = dataIntent.getStringExtra("registerType");
             String userType = dataIntent.getStringExtra("user_type");
             if(registerType.equals("normal")){
                 Intent intent3 = new Intent(getApplicationContext(), RegisterActivity.class);
                 intent3.putExtra("registerType",registerType);
                 intent3.putExtra("user_type",userType);
                 intent3.putExtra("mobileNumber",mobileNumber);
                 startActivity(intent3);

                 //registerType.equals("sns")
             }


//                if(mobileNumber.equals("")||mobileNumber.length()!=11){
//                    Toast.makeText(getApplicationContext(),R.string.sms_verification_mobile_number_error,Toast.LENGTH_SHORT).show();
//                } else if(otpNumber.equals("")||otpNumber.length()!=5){
//                    Toast.makeText(getApplicationContext(),R.string.sms_verification_otp_number_error,Toast.LENGTH_SHORT).show();
//                } else if(getOTP){
//                    verifyOTP(mobileNumber,otpNumber);
//                }
             }
         });
    }

    private void getOTP(String mobileNumber) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_SMS_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.i(TAG,response);
                            getOTP = true;
                            Toast.makeText(getApplicationContext(),R.string.sms_verification_send_done,Toast.LENGTH_SHORT).show();
                        } catch (Exception e){
                            e.printStackTrace();
                            Log.w(TAG,"error : "+e.toString());
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
                data.put("submit_mobile", "true");
                data.put("mobile_no", mobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private void verifyOTP(String mobileNumber, String otpNumber) {

        Intent dataIntent = getIntent();
        String verificationType = dataIntent.getStringExtra("verification_type");

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_SMS_VERIFY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");
                            if(condition.equals("success")){
                                switch(verificationType){
                                    //LoginActivity 에서 아이디찾기로 넘어온 경우
                                    case "findId" :
                                        Intent intent = new Intent(getApplicationContext(), IdSearchResultActivity.class);
                                        intent.putExtra("mobile",mobileNumber);
                                        startActivity(intent);
                                    break;
                                    //SelectFindToolActivity 에서 휴대폰번호로 찾기로 넘어온 경우
                                    case "findPassword" :
                                        String userId = dataIntent.getStringExtra("userId");
                                        Intent intent2 = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                                        intent2.putExtra("userId",userId);
                                        startActivity(intent2);
                                    break;
                                    //RegisterTypeActivity 에서 넘어온 경우
                                    case "register" :
                                        String registerType = dataIntent.getStringExtra("registerType");
                                        String userType = dataIntent.getStringExtra("user_type");
                                        if(registerType.equals("normal")){
                                            Intent intent3 = new Intent(getApplicationContext(), RegisterActivity.class);
                                            intent3.putExtra("registerType",registerType);
                                            intent3.putExtra("user_type",userType);
                                            intent3.putExtra("mobileNumber",mobileNumber);
                                            startActivity(intent3);

                                            //registerType.equals("sns")
                                        } else {
                                            if(userType.equals("student")){
                                                Intent intent4 = new Intent(getApplicationContext(), RegisterStudentActivity.class);
                                                intent4.putExtra("registerType",registerType);
                                                intent4.putExtra("email",dataIntent.getStringExtra("email"));
                                                intent4.putExtra("nickname",dataIntent.getStringExtra("name"));
                                                intent4.putExtra("userType",userType);
                                                intent4.putExtra("mobileNumber",mobileNumber);
                                                startActivity(intent4);

                                                //userType.equals("teacher")
                                            } else {

                                            }
                                        }

                                    break;
                                }
                            } else if(condition.equals("fail")){
                                String message = jsonObject.getString("message");
                                textView_error.setText(message);
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            Log.e(TAG,"Exception : "+e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG,"VolleyError : "+error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("verificationType",verificationType);
                data.put("submit_otp", "true");
                data.put("otp_code", otpNumber);
                data.put("mobile_no", mobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }
}