package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jyw.laguagetutor.adapter.LocationAdapter;
import com.jyw.laguagetutor.adapter.LocationAdapter2;
import com.jyw.laguagetutor.adapter.LocationListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherRegisterLocationActivity extends AppCompatActivity implements LocationListener {

    RadioGroup radioGroup_online,radioGroup_offline,radioGroup_location;
    RadioButton radioButton_location_all,radioButton_location_chose;
    RecyclerView recyclerView_location,recyclerView_location2;
    EditText editText_hourly_rate_on,editText_hourly_rate_off;
    Spinner spinner_location;
    LocationAdapter locationAdapter;
    ArrayList<String> resultList;
    Button button_next;
    LinearLayout linearLayout_offline,linearLayout_online;
    LocationAdapter2 locationAdapter2;

    private static final String TAG = TeacherRegisterLocationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register_location);

        radioGroup_location = findViewById(R.id.radioGroup_location);
        radioButton_location_all = findViewById(R.id.radioButton_location_all);
        radioButton_location_chose = findViewById(R.id.radioButton_location_chose);
        recyclerView_location = findViewById(R.id.recyclerView_location);
        recyclerView_location2 = findViewById(R.id.recyclerView_location2);
        spinner_location = findViewById(R.id.spinner_location);
        button_next = findViewById(R.id.button_next);
        editText_hourly_rate_off = findViewById(R.id.editTextText_hourly_rate_off);
        editText_hourly_rate_on = findViewById(R.id.editTextText_hourly_rate_on);
        linearLayout_offline = findViewById(R.id.linearLayout_offline);
        linearLayout_online = findViewById(R.id.linearLayout_online);

        recyclerView_location2.setLayoutManager(new GridLayoutManager(this,3));
        resultList = new ArrayList<String>();

        Intent intent = new Intent(getApplicationContext(), TeacherRegisterActivity.class);

        ArrayAdapter si_do_Adapter = ArrayAdapter.createFromResource(this,R.array.spinner_region, android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(si_do_Adapter);

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                radioGroup_location.clearCheck();
                recyclerView_location.setAdapter(null);
                switch (position) {
                    case 0:
                        radioGroup_location.setVisibility(View.GONE);
                        break;
                    case 1:
                        setLocationRecyclerView(R.array.recycler_region_seoul);
                        break;
                    case 2:
                        setLocationRecyclerView(R.array.recycler_region_busan);
                        break;
                    case 3:
                        setLocationRecyclerView(R.array.recycler_region_daegu);
                        break;
                    case 4:
                        setLocationRecyclerView(R.array.recycler_region_incheon);
                        break;
                    case 5:
                        setLocationRecyclerView(R.array.recycler_region_gwangju);
                        break;
                    case 6:
                        setLocationRecyclerView(R.array.recycler_region_daejeon);
                        break;
                    case 7:
                        setLocationRecyclerView(R.array.recycler_region_ulsan);
                        break;
                    case 8:
                        setLocationRecyclerView(R.array.recycler_region_sejong);
                        break;
                    case 9:
                        setLocationRecyclerView(R.array.recycler_region_gyeonggi);
                        break;
                    case 10:
                        setLocationRecyclerView(R.array.recycler_region_gangwon);
                        break;
                    case 11:
                        setLocationRecyclerView(R.array.recycler_region_chung_buk);
                        break;
                    case 12:
                        setLocationRecyclerView(R.array.recycler_region_chung_nam);

                        break;
                    case 13:
                        setLocationRecyclerView(R.array.recycler_region_jeon_buk);
                        break;
                    case 14:
                        setLocationRecyclerView(R.array.recycler_region_jeon_nam);
                        break;
                    case 15:
                        setLocationRecyclerView(R.array.recycler_region_gyeong_buk);
                        break;
                    case 16:
                        setLocationRecyclerView(R.array.recycler_region_gyeong_nam);
                        break;
                    case 17:
                        setLocationRecyclerView(R.array.recycler_region_jeju);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        radioGroup_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //여기 고쳐
                String title = radioButton_location_all.getText().toString();
                if(checkedId==R.id.radioButton_location_all){
                    locationAdapter.unSelectedAll();
                    recyclerView_location.setAdapter(locationAdapter);
                    recyclerView_location.setVisibility(View.INVISIBLE);
                    if(!resultList.contains(title)){
                        resultList.add(title);
                    }
                } else if(checkedId==R.id.radioButton_location_chose){
                    if(resultList.contains(title)){
                        resultList.remove(title);
                    }
                    recyclerView_location.setVisibility(View.VISIBLE);
                    recyclerView_location.setAdapter(locationAdapter);
                }

                if(locationAdapter2==null){
                    locationAdapter2 = new LocationAdapter2(resultList);
                    recyclerView_location2.setAdapter(locationAdapter2);
                } else {
                    locationAdapter2.addItem(resultList);
                    locationAdapter2.notifyDataSetChanged();
                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {

                 Intent dataIntent = getIntent();
                 String registerType = dataIntent.getStringExtra("registerType");
                 intent.putExtra("registerType",registerType);

                 if(registerType.equals("normal")){
                     String userId = dataIntent.getStringExtra("userId");
                     intent.putExtra("userId",userId);

                     String password = dataIntent.getStringExtra("password");
                     intent.putExtra("password",password);

                 } else {
                     String email = dataIntent.getStringExtra("email");
                     intent.putExtra("email",email);

                 }
                 String userType = dataIntent.getStringExtra("userType");
                 intent.putExtra("userType",userType);

                 String nickname = dataIntent.getStringExtra("nickname");
                 intent.putExtra("nickname",nickname);

                 String mobileNumber = dataIntent.getStringExtra("mobileNumber");
                 intent.putExtra("mobileNumber",mobileNumber);

                 String subjectsList = dataIntent.getStringExtra("subjectsList");
                 intent.putExtra("subjectsList",subjectsList);

                 String rateOffline = editText_hourly_rate_off.getText().toString().trim();
                 intent.putExtra("rate_offline",rateOffline);

                 String rateOnline = editText_hourly_rate_on.getText().toString();
                 intent.putExtra("rate_online",rateOnline);

                 String locationList = resultList.toString();
                 locationList = locationList.replace("[","");
                 locationList = locationList.replace("]","");
                 intent.putExtra("locationList",locationList);
                 startActivity(intent);
                 Toast.makeText(getApplicationContext(),resultList.toString(), Toast.LENGTH_SHORT).show();
             }
         });
    }

    private void setLocationRecyclerView(int array_resource) {
        String si_do = spinner_location.getSelectedItem().toString();
        String si_do_all = si_do+" 전체";
        radioGroup_location.setVisibility(View.VISIBLE);
        radioButton_location_all.setText(si_do_all);

        recyclerView_location.setLayoutManager(new GridLayoutManager(this,5));
        String[] locations =  getResources().getStringArray(array_resource);
        //시,도 지명 리스트
        List<String> list = Arrays.asList(locations);
        //결과 리스트
        locationAdapter = new LocationAdapter(list, resultList, si_do,this);
    }

    @Override
    public void onLocationChange(ArrayList<String> list) {
        locationAdapter2 = new LocationAdapter2(list);
        recyclerView_location2.setAdapter(locationAdapter2);
    }
}