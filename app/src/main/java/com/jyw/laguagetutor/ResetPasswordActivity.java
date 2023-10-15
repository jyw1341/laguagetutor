package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView textView_userId;
    private EditText editText_password,editText_password_confirm;
    private Button button_confirm;

    private static final String TAG = ResetPasswordActivity.class.getSimpleName();
    private static final int MINIMUM_LENGTH = 8;
    private static final String URL_RESET_PASSWORD = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/reset_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        textView_userId = findViewById(R.id.textView_userId);
        editText_password = findViewById(R.id.editText_password);
        editText_password_confirm = findViewById(R.id.editText_password_confirm);
        button_confirm = findViewById(R.id.button_confirm);

        Intent dataIntent = getIntent();
        String userId = dataIntent.getStringExtra("userId");
        textView_userId.setText(userId);

        button_confirm.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String password = editText_password.getText().toString().trim();
                String passwordConfirm = editText_password_confirm.getText().toString().trim();
                if(password.length()<MINIMUM_LENGTH){
                    Toast.makeText(getApplicationContext(),R.string.reset_password_password_short,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals(passwordConfirm)){
                    resetPassword(userId, password, passwordConfirm);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.reset_password_password_mismatch,Toast.LENGTH_SHORT).show();
                }
             }
         });
    }

        private void resetPassword(String userId, String password, String passwordConfirm) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                        URL_RESET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");
                            String message = jsonObject.getString("message");
                            if(condition.equals("success")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                    data.put("user_id", userId);
                    data.put("password",password);
                    data.put("password_confirm", passwordConfirm);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }
}