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

public class InsertIdActivity extends AppCompatActivity {

    private EditText editText_insert_id;
    private TextView textView_error;
    private Button button_next;

    private static final String URL_VERIFY_ID = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/verify_id.php";
    private static final String TAG = InsertIdActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_id);

        editText_insert_id = findViewById(R.id.editText_insert_id);
        button_next = findViewById(R.id.button_next);
        textView_error = findViewById(R.id.textView_error);

        InputFilter filterAlphaNum = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        };
        editText_insert_id.setFilters(new InputFilter[]{filterAlphaNum});

        button_next.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String userId = editText_insert_id.getText().toString().trim();
                verifyId(userId);
             }
         });
    }


    private void verifyId(String userId) {
            StringRequest request = new StringRequest(
            Request.Method.POST,
                    URL_VERIFY_ID,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String condition = jsonObject.getString("condition");
                        JSONArray jsonArray = jsonObject.getJSONArray("user_info");
                        if(condition.equals("success")){
                            updateUI();
                            String mobile="";
                            String email="";
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                mobile = object.getString("mobile").trim();
                                email = object.getString("email").trim();
                            }
                            Intent intent = new Intent(getApplicationContext(), SelectFindToolActivity.class);
                            intent.putExtra("userId",userId);
                            intent.putExtra("mobile",mobile);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        } else {
                            updateErrorUI();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG,""+e.toString());
                        updateErrorUI();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    updateErrorUI();
                }
            }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("user_id",userId);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private void updateUI(){
        textView_error.setVisibility(View.INVISIBLE);
    }

    private void updateErrorUI(){
        textView_error.setVisibility(View.VISIBLE);
        textView_error.setText(R.string.insert_id_error);
        Toast.makeText(getApplicationContext(),R.string.insert_id_error,Toast.LENGTH_SHORT).show();
    }

}

