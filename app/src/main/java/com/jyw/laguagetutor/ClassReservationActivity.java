package com.jyw.laguagetutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jyw.laguagetutor.adapter.ClassAdapter3;
import com.jyw.laguagetutor.adapter.ClassAdapter4;
import com.jyw.laguagetutor.adapter.ClassAdapter5;
import com.jyw.laguagetutor.recyclerData.ClassData3;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ClassReservationActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private MaterialCalendarView calendarView;
    private RadioButton rb_booking1,rb_booking2;
    private RecyclerView rv_booking;
    private Button btn_booking;

    private SessionManager sessionManager;
    private HashMap<String, String> user;
    String bookingDate,bookingTime,oMobile;

    CalendarDay calendarDay,futureDay;

    private HashSet<String> dateSet;
    private static final String URL_DATE_READ = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/class_date_read.php";
    private static final String URL_CLASS_READ = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/class_specific_read.php";
    private static final String IMAGE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/images/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_reservation);

        Intent dataIntent = getIntent();
        oMobile = dataIntent.getStringExtra(getResources().getString(R.string.mobile_number));

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetail();

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                finish();
             }
         });

        rb_booking1 = findViewById(R.id.rb_booking1);
        rb_booking1.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                timePicker.setVisibility(View.INVISIBLE);
                calendarView.setVisibility(View.VISIBLE);
             }
         });

        rb_booking2 = findViewById(R.id.rb_booking2);
        rb_booking2.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 timePicker.setVisibility(View.VISIBLE);
                 calendarView.setVisibility(View.INVISIBLE);
             }
         });

        Calendar calender = Calendar.getInstance();
        int currentYear = calender.get(Calendar.YEAR);
        int currentMonth = calender.get(Calendar.MONTH);
        int currentDate = calender.get(Calendar.DATE);
        Calendar future = Calendar.getInstance();
        future.set(Calendar.MONTH,currentMonth+1);

        calendarDay = new CalendarDay(currentYear,currentMonth,currentDate);
        futureDay = new CalendarDay(future.get(Calendar.YEAR),future.get(Calendar.MONTH),future.get(Calendar.DATE));


        calendarView = findViewById(R.id.calenderView);
        calendarView.addDecorator(new MinMaxDecorator(calendarDay,futureDay));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull @NotNull MaterialCalendarView widget, @NonNull @NotNull CalendarDay date, boolean selected) {
                Date from = date.getDate();

                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

                bookingDate = transFormat.format(from);

                getClassInformation(bookingDate);
            }
        });

        timePicker = findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                bookingTime = hourOfDay+":"+minute;
            }
        });

        btn_booking = findViewById(R.id.btn_booking);
        btn_booking.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                if(bookingDate!=null&&bookingTime!=null){
                    Intent intent = new Intent(getApplicationContext(), Booking2.class);
                    intent.putExtra("date",bookingDate+" "+bookingTime);
                    intent.putExtra(getResources().getString(R.string.other_mobile_number),oMobile);
                    intent.putExtra(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                    startActivity(intent);
                }
             }
         });

        rv_booking = findViewById(R.id.rv_booking);
        rv_booking.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        readCalenderDate();
    }


    private void readCalenderDate() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_DATE_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            dateSet = new HashSet<>();
                            for (int i = 0; i < jsonArray.length(); i++){
                                String date = jsonArray.getJSONObject(i).getString("date").substring(0,10);
                                dateSet.add(date);
                            }
                            calendarView.addDecorators(new MinMaxDecorator(calendarDay,futureDay),new BookedDecorator(dateSet));
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                data.put(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void getClassInformation(String date) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_CLASS_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setAdapterClassInfo1(response);
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
                data.put(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                data.put("date",date);
                data.put("type",user.get(SessionManager.USER_TYPE));
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void setAdapterClassInfo1(String response){
        ArrayList<ClassData3> dataList = new ArrayList<>();
        try{

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("class");
            for (int i = 0; i < jsonArray.length(); i++){
                String image = IMAGE_URL+jsonArray.getJSONObject(i).getString("profile_image");
                String name = jsonArray.getJSONObject(i).getString("user_name");
                String status = jsonArray.getJSONObject(i).getString("status");
                String time = jsonArray.getJSONObject(i).getString("time");
                String date = jsonArray.getJSONObject(i).getString("date");
                String mobile = jsonArray.getJSONObject(i).getString("mobile_number");
                String classId = jsonArray.getJSONObject(i).getString("id");
                dataList.add(new ClassData3(mobile,image,name,date,time,status,classId));
            }
            rv_booking.setAdapter(new ClassAdapter5(dataList));

        } catch (Exception e){
            e.printStackTrace();

        }
    }
}