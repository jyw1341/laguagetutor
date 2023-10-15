package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jyw.laguagetutor.adapter.TicketAdapter;
import com.jyw.laguagetutor.recyclerData.TicketData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private HashMap<String, String> user;
    private RecyclerView rv_ticket;

    private TicketAdapter ticketAdapter;

    private static final String URL = "ws.com/server1/get_ticket.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        String mPhoneNumber = user.get(SessionManager.MOBILE_NUMBER);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_ticket = findViewById(R.id.rv_ticket);

        requestTicket(mPhoneNumber);
    }

        private void requestTicket(String mPhoneNumber) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<TicketData> arrayList = new ArrayList<>();
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TicketData ticketData = new TicketData(
                                        jsonObject.getString("profile_image"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("count"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("mobile_number"));
                                arrayList.add(ticketData);
                            }
                        } catch (Exception e){
                          e.printStackTrace();
                        } finally {
                            rv_ticket.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rv_ticket.setAdapter(new TicketAdapter(arrayList,getApplicationContext()));
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
                    data.put(getResources().getString(R.string.mobile_number),mPhoneNumber);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }
}