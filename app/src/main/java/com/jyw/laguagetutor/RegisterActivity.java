package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText_nickname,editText_userId,editText_password,editText_confirmPassword;
    private TextView textView_id_error,textView_password_error,textView_password_confirm_error,textView_nickname_error;
    private Button button_next;

    private ProgressBar loading;

    private static final String URL_VALIDATE = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/validate_user_info.php";
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int ID_MINIMUM_LENGTH = 5;
    private static final int PASSWORD_MINIMUM_LENGTH = 8;
    private static final int NICKNAME_MINIMUM_LENGTH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_nickname = findViewById(R.id.editText_nickname);
        editText_userId = findViewById(R.id.editText_userId);
        editText_password = findViewById(R.id.editText_password);
        editText_confirmPassword = findViewById(R.id.editText_confirmPassword);
        textView_id_error = findViewById(R.id.textView_id_error);
        textView_password_error = findViewById(R.id.textView_password_error);
        textView_password_confirm_error = findViewById(R.id.textView_password_confirm_error);
        textView_nickname_error = findViewById(R.id.textView_nickname_error);
        button_next = findViewById(R.id.button_next);
        loading = findViewById(R.id.loading);

        //아이디 입력필터. 영문과 숫자만 허용
        InputFilter englishNumFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-z0-9]+$");

                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        };

        //닉네임 입력필터. 한글 영문 숫자만 허용
        InputFilter koreanEnglishNumFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣]+$");

                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        };

        InputFilter[] idFilter = new InputFilter[]{
                        new InputFilter.LengthFilter(20),
                        englishNumFilter
        };

        InputFilter[] nicknameFilter = new InputFilter[]{
                        new InputFilter.LengthFilter(12),
                        koreanEnglishNumFilter
                };
        editText_userId.setFilters(idFilter);



        //서버에 유저정보 유효성검증 요청 보냄
        button_next.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String userId = editText_userId.getText().toString().trim();
                String password = editText_password.getText().toString().trim();
                String passwordConfirm = editText_confirmPassword.getText().toString().trim();
                String nickname = editText_nickname.getText().toString().trim();
                textView_id_error.setVisibility(View.GONE);
                textView_password_error.setVisibility(View.GONE);
                textView_password_confirm_error.setVisibility(View.GONE);
                textView_nickname_error.setVisibility(View.GONE);
                if(validateId(userId)){
                    return;
                }

                if(validatePassword(password,passwordConfirm)){
                    return;
                }

                if(validateNickname(nickname)){
                    return;
                }

                validate(userId, password, passwordConfirm, nickname);
             }
         });
    }

    private boolean validateId(String userId){
        if(userId.length()<ID_MINIMUM_LENGTH){
            textView_id_error.setVisibility(View.VISIBLE);
            textView_id_error.setText(R.string.register_id_error);
            Toast.makeText(getApplicationContext(),R.string.register_id_error,Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean validatePassword(String password, String passwordConfirm){
        if(password.length()<PASSWORD_MINIMUM_LENGTH){
            textView_password_error.setVisibility(View.VISIBLE);
            textView_password_error.setText(R.string.reset_password_password_short);
            Toast.makeText(getApplicationContext(),R.string.reset_password_password_short,Toast.LENGTH_SHORT).show();
            return true;
        }
        if(password.equals(passwordConfirm)){
            return false;
        } else {
            textView_password_confirm_error.setVisibility(View.VISIBLE);
            textView_password_confirm_error.setText(R.string.reset_password_password_mismatch);
            Toast.makeText(getApplicationContext(),R.string.reset_password_password_mismatch,Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private boolean validateNickname(String nickname){
        if(nickname.length()<NICKNAME_MINIMUM_LENGTH){
            textView_nickname_error.setVisibility(View.VISIBLE);
            textView_nickname_error.setText(R.string.register_nickname_error);
            Toast.makeText(getApplicationContext(),R.string.register_nickname_error,Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void validate(String userId, String password, String passwordConfirm, String nickname){
        loading.setVisibility(View.VISIBLE);
        button_next.setVisibility(View.GONE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_VALIDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");

                            if (condition.equals("success")) {
                                //SmsVerificationActivity 에서 받아옴
                                Intent dataIntent = getIntent();
                                String userType = dataIntent.getStringExtra("user_type");
                                String mobileNumber = dataIntent.getStringExtra("mobileNumber");
                                switch(userType){
                                    case "student" :
                                      Intent intent = new Intent(getApplicationContext(), RegisterStudentActivity.class);
                                      intent.putExtra("registerType","normal");
                                      intent.putExtra("userId",userId);
                                      intent.putExtra("password",password);
                                      intent.putExtra("userType",userType);
                                      intent.putExtra("nickname",nickname);
                                      intent.putExtra("mobileNumber",mobileNumber);
                                      startActivity(intent);
                                    break;

                                    case "teacher" :
                                        Intent intent2 = new Intent(getApplicationContext(), SelectSubjectActivity.class);
                                        intent2.putExtra("registerType","normal");
                                        intent2.putExtra("userId",userId);
                                        intent2.putExtra("password",password);
                                        intent2.putExtra("userType",userType);
                                        intent2.putExtra("nickname",nickname);
                                        intent2.putExtra("mobileNumber",mobileNumber);
                                        startActivity(intent2);
                                    break;
                                }

                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                editText_userId.setVisibility(View.VISIBLE);
                                editText_userId.setText(R.string.used_id);
                                updateUI();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            Log.e(TAG,e.toString());
                            Toast.makeText(getApplicationContext(),R.string.register_failed,Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG,error.toString());
                        Toast.makeText(getApplicationContext(),R.string.register_failed,Toast.LENGTH_SHORT).show();
                        updateUI();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("userId",userId);
                parameters.put("password",password);
                parameters.put("passwordConfirm",passwordConfirm);
                return parameters;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private void updateUI(){
        loading.setVisibility(View.GONE);
        button_next.setVisibility(View.VISIBLE);
    }
}