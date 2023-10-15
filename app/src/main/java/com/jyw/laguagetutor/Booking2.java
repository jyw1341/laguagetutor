package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Booking2 extends AppCompatActivity {
    private static final String URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/class_create.php";
    TextView tv_date;
    Spinner spinner;
    Button btn_cancel,btn_confirm;
    String date,oMobile,mMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking2);

        Intent dataIntent = getIntent();
        date = dataIntent.getStringExtra("date");
        oMobile= dataIntent.getStringExtra(getResources().getString(R.string.other_mobile_number));
        mMobile = dataIntent.getStringExtra(getResources().getString(R.string.mobile_number));

        tv_date= findViewById(R.id.tv_date);
        tv_date.setText(date);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.class_time, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                finish();
             }
         });

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                bookingRequest();
             }
         });
    }

    private String spinnerConverter(int position){
        String time=null;
        switch(position){
            case 0:
                time= "0.5";
                break;
            case 1:
                time= "1";
                break;
            case 2:
                time= "1.5";
                break;
            case 3:
                time= "2";
                break;
            case 4:
                time= "2.5";
                break;
            case 5:
                time= "3";
                break;
            case 6:
                time= "3.5";
                break;
            case 7:
                time= "4";
                break;
            case 8:
                time= "4.5";
                break;
            case 9:
                time= "5";
                break;
            case 10:
                time= "5.5";
                break;
            case 11:
                time= "6";
                break;
        }
        return time;
    }

    private void bookingRequest() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("booking2",response);
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(),"예약이 완료되었습니다",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MyClassActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"예약에 실패했습니다",Toast.LENGTH_SHORT).show();
                            Log.d("Booking2",response);
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
                data.put(getResources().getString(R.string.mobile_number),mMobile);
                data.put(getResources().getString(R.string.other_mobile_number),oMobile);
                data.put("date",date);
                data.put("time",spinnerConverter(spinner.getSelectedItemPosition()));
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }
}