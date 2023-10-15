package com.jyw.laguagetutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputFilter;
import android.text.Spanned;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static long start = System.currentTimeMillis();

    private EditText editText_userId, editText_password;
    private TextView textView_findPassword,textView_register,textView_login_error,textView_findId;
    private Button button_login;
    private ProgressBar loading;

    SignInButton button_googleLogin;
    SessionManager sessionManager;
    GoogleSignInClient mGoogleSignInClient;

    private static final String URL_NORMAL_LOGIN = "";
    private static final String URL_GOOGLE_LOGIN = "";
    private static final String URL_GET_USER_DETAIL = "";
    private static final String TAG = "LoginActivity";

    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = findViewById(R.id.loading);

        getTime(start,"시작");
        //로그인 상태 유지 부분
        sessionManager = new SessionManager(this);
        if(sessionManager.isLogIn()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            getTime(start,"세션매니저 생성 완료");
        }

        //구글 로그인 관련 오브젝트 생성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getTime(start,"gso 생성완료" );

        //아이디 입력필터. 영문과 숫자만 허용
        editText_userId = findViewById(R.id.editText_userId);
        InputFilter idFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editText_userId.setFilters(new InputFilter[] {idFilter});
        getTime(start,"인풋필터 생성완료" );


        //일반 로그인 처리
        editText_password = findViewById(R.id.editText_password);
        textView_login_error = findViewById(R.id.textView_login_error);
        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String userId = editText_userId.getText().toString().trim();
                String password = editText_password.getText().toString().trim();

                if(!userId.isEmpty() || !password.isEmpty()) {
                    normalLogin(userId,password);
                    getTime(start, "일반 로그인 처리 완료");
                } else {
                    textView_login_error.setText(R.string.login_error);
                }
             }
         });
        

        textView_findId = findViewById(R.id.textView_findId);
        textView_findId.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), SmsVerificationActivity.class);
                 intent.putExtra("verification_type","findId");
                 startActivity(intent);
             }
         });

        //비밀번호 찾기 화면으로 이동
        textView_findPassword = findViewById(R.id.textView_findPassword);
        textView_findPassword.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsertIdActivity.class);
                startActivity(intent);
             }
         });

        //회원가입 화면으로 전환
        textView_register = findViewById(R.id.textView_register);
        textView_register.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterTypeActivity.class);
                intent.putExtra("registerType","normal");
                startActivity(intent);
             }
         });

        //구글 로그인
        button_googleLogin = findViewById(R.id.button_googleLogin);
        button_googleLogin.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 switch (v.getId()) {
                     case R.id.button_googleLogin:
                         signIn();
                         getTime(start, "구글 로그인 완료");
                         break;
                 }
             }
         });

        signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        //전에 구글 로그인 했던 계정이 있으면 바로 메인 화면으로 이동
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            getTime(start, "이전에 로그인했던 구글 계정 불러오기 완료 ");
        }
    }

    private void normalLogin(String userId, String password) {
        loading.setVisibility(View.VISIBLE);
        button_login.setVisibility(View.GONE);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                URL_NORMAL_LOGIN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            JSONArray jsonArray = jsonObject.getJSONArray("login");
//
//                            if(success.equals("0")){
//                                //success.equals("0") : 모든 과정이 정상임
//
//                                JSONObject object = jsonArray.getJSONObject(0);
//                                String user_type = object.getString("user_type").trim();
//                                String mobile_number = object.getString("mobile_number").trim();
//                                getUserDetail(mobile_number,user_type);
//                            } else if(success.equals("1")){
//                                //success.equals("1")
//                                updateUI();
//                            } else if(success.equals("2")){
//                                //success.equals("2")
//                                updateUI();
//                            }
//
//                        } catch (Exception e){
//                            e.printStackTrace();
//                            updateUI();
//                            Log.w(TAG,"login error : "+e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        updateUI();
//                    }
//                }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                data.put("userId", userId);
//                data.put("password", password);
//                return data;
//            }
//        };
//        request.setShouldCache(false);
//        Volley.newRequestQueue(this).add(request);
//        getTime(start, "서버에 일반 로그인 데이터 전송 완료");
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        getTime(start, "signIn 메소드");
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            getTime(start, "온액티비티리절트");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String name = account.getDisplayName();
            String idToken = account.getIdToken();
            sendToken(idToken, email, name);
            getTime(start, "핸들사인인리절트");
            Log.w(TAG,"idToken : "+idToken);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

        private void getUserDetail(String mobileNumber, String userType) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_GET_USER_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString(getResources().getString(R.string.condition));
                            if(condition.equals(getResources().getString(R.string.success))){
                                String userName = jsonObject.getString(getResources().getString(R.string.var_username)).trim();
                                sessionManager.createSession(mobileNumber,userType,userName);
                                Log.w(TAG,mobileNumber+"/"+userType+"/"+userName);
                                loading.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                String error = jsonObject.getString(getResources().getString(R.string.var_error));
                                Log.w(TAG,error);
                                updateUI();
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

                    }
                }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put(getResources().getString(R.string.mobile_number),mobileNumber);
                    data.put(getResources().getString(R.string.user_type),userType);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }


    private void sendToken(String idToken,String email, String name) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_GOOGLE_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                            if (success.equals("0")) {
                                //success.equals("0") 이미 가입된 회원으로 메인 화면으로 이동
                                JSONArray jsonArray = jsonObject.getJSONArray("login");

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String user_type = object.getString("user_type").trim();
                                    String mobile_number = object.getString("mobile_number").trim();
                                    getUserDetail(mobile_number,user_type);
                                    startActivity(intent);
                                }

                                getTime(start, "구글 로그인 성공");
                            } else if (success.equals("1")) {
                                //success.equals("1") 미가입 회원으로 sns 회원등록 화면으로 이동
                                intent = new Intent(getApplicationContext(), RegisterTypeActivity.class);
                                intent.putExtra("registerType","sns");
                                intent.putExtra("email",email);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            Log.w(TAG,"login error : "+e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"login error : "+error.toString(),Toast.LENGTH_SHORT).show();
                        Log.w(TAG,"login error : "+error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("idToken",idToken);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
        getTime(start, "서버에 토큰 보냄");
    }

    private void updateUI(){
        loading.setVisibility(View.GONE);
        button_login.setVisibility(View.VISIBLE);
        textView_login_error.setVisibility(View.VISIBLE);
        textView_login_error.setText(R.string.login_error);
    }

    private void getTime(long start, String process){
        long end = System.currentTimeMillis();
        Log.w(TAG,process+" : "+((end-start)/1000.0));
    }
}