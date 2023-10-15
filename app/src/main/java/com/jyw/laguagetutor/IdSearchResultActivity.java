package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class IdSearchResultActivity extends AppCompatActivity {

    private Button button_login,button_findPassword,button_register;
    private TextView textView_userId,textView_date,textView_error;
    private LinearLayout layout_result;

    private static final String TAG = IdSearchResultActivity.class.getSimpleName();
    private static final String URL_FIND_ID = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/find_id.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_search_result);

        textView_userId = findViewById(R.id.textView_userId);
        textView_date = findViewById(R.id.textView_date);
        textView_error = findViewById(R.id.textView_error);
        button_login = findViewById(R.id.button_login);
        button_findPassword = findViewById(R.id.button_findPassword);
        button_register = findViewById(R.id.button_register);
        layout_result = findViewById(R.id.layout_result);

        Intent dataIntent = getIntent();
        String mobile = dataIntent.getStringExtra("mobile");

        findId(mobile);

        button_login.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
             }
         });

        button_findPassword.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), InsertIdActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
             }
         });

        button_register.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
             }
         });
    }

        private void findId(String mobile) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                        URL_FIND_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String condition = jsonObject.getString("condition");
                            if(condition.equals("success")) {
                                String userId = jsonObject.getString("user_id");
                                String date = jsonObject.getString("register_date");
                                textView_userId.setText(userId);
                                textView_date.setText(date);
                            } else {
                                updateErrorUI();
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
                    data.put("mobile_number",mobile);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }

        private void updateErrorUI(){
            layout_result.setVisibility(View.GONE);
            textView_error.setVisibility(View.VISIBLE);
            button_findPassword.setVisibility(View.GONE);
            button_login.setVisibility(View.GONE);
            button_register.setVisibility(View.VISIBLE);
        }
}