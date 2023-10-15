package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class SearchTeacherActivity extends AppCompatActivity {

    private Spinner spinner_si_do,spinner_gu_gun,spinner_gender,spinner_rate,spinner_age,spinner_study_type;
    private EditText editText_keyword;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_teacher);

        editText_keyword = findViewById(R.id.editText_keyword);

        sessionManager = new SessionManager(this);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner_gender = findViewById(R.id.spinner_gender);
        ArrayAdapter gender = ArrayAdapter.createFromResource(this,R.array.teacher_gender, android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(gender);

        spinner_rate = findViewById(R.id.spinner_rate);
        ArrayAdapter rate = ArrayAdapter.createFromResource(this,R.array.teacher_rate, android.R.layout.simple_spinner_dropdown_item);
        spinner_rate.setAdapter(rate);

        spinner_age = findViewById(R.id.spinner_age);
        ArrayAdapter age = ArrayAdapter.createFromResource(this,R.array.teacher_age, android.R.layout.simple_spinner_dropdown_item);
        spinner_age.setAdapter(age);

        spinner_si_do = findViewById(R.id.spinner_si_do);
        ArrayAdapter si_do_Adapter = ArrayAdapter.createFromResource(this,R.array.spinner_region2, android.R.layout.simple_spinner_dropdown_item);
        spinner_si_do.setAdapter(si_do_Adapter);

        spinner_study_type = findViewById(R.id.spinner_study_type);
        ArrayAdapter study_type = ArrayAdapter.createFromResource(this,R.array.teacher_study_type, android.R.layout.simple_spinner_dropdown_item);
        spinner_study_type.setAdapter(study_type);

        spinner_gu_gun = findViewById(R.id.spinner_gu_gun);

        spinner_si_do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        spinner_gu_gun.setAdapter(null);
                        break;
                    case 1:
                        setSpinnerAdapterItem(R.array.spinner_region_seoul);
                        break;
                    case 2:
                        setSpinnerAdapterItem(R.array.spinner_region_busan);
                        break;
                    case 3:
                        setSpinnerAdapterItem(R.array.spinner_region_daegu);
                        break;
                    case 4:
                        setSpinnerAdapterItem(R.array.spinner_region_incheon);
                        break;
                    case 5:
                        setSpinnerAdapterItem(R.array.spinner_region_gwangju);
                        break;
                    case 6:
                        setSpinnerAdapterItem(R.array.spinner_region_daejeon);
                        break;
                    case 7:
                        setSpinnerAdapterItem(R.array.spinner_region_ulsan);
                        break;
                    case 8:
                        setSpinnerAdapterItem(R.array.spinner_region_sejong);
                        break;
                    case 9:
                        setSpinnerAdapterItem(R.array.spinner_region_gyeonggi);
                        break;
                    case 10:
                        setSpinnerAdapterItem(R.array.spinner_region_gangwon);
                        break;
                    case 11:
                        setSpinnerAdapterItem(R.array.spinner_region_chung_buk);
                        break;
                    case 12:
                        setSpinnerAdapterItem(R.array.spinner_region_chung_nam);

                        break;
                    case 13:
                        setSpinnerAdapterItem(R.array.spinner_region_jeon_buk);
                        break;
                    case 14:
                        setSpinnerAdapterItem(R.array.spinner_region_jeon_nam);
                        break;
                    case 15:
                        setSpinnerAdapterItem(R.array.spinner_region_gyeong_buk);
                        break;
                    case 16:
                        setSpinnerAdapterItem(R.array.spinner_region_gyeong_nam);
                        break;
                    case 17:
                        setSpinnerAdapterItem(R.array.spinner_region_jeju);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button_search = findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner_si_do.getSelectedItemPosition()>0){
                    String si_do = spinner_si_do.getSelectedItem().toString().trim();
                    String gu_gun = spinner_gu_gun.getSelectedItem().toString().trim();
                    String location = si_do+" "+gu_gun;
                    sessionManager.editor.putString("spinner_location_string",location);
                } else {
                    sessionManager.editor.putString("spinner_location_string",null);
                }

                sessionManager.editor.putString("keyword",editText_keyword.getText().toString().trim());

                sessionManager.editor.putInt("spinner_study_type",spinner_study_type.getSelectedItemPosition());
                sessionManager.editor.putString("spinner_study_type_string",spinner_study_type.getSelectedItem().toString());

                sessionManager.editor.putInt("spinner_gender",spinner_gender.getSelectedItemPosition());
                sessionManager.editor.putString("spinner_gender_string",spinner_gender.getSelectedItem().toString());

                sessionManager.editor.putInt("spinner_rate",spinner_rate.getSelectedItemPosition());

                if(spinner_rate.getSelectedItemPosition()>0){
                    String tmp = spinner_rate.getSelectedItem().toString();
                    String rate = tmp.substring(0,tmp.indexOf("만"));
                    sessionManager.editor.putString("spinner_rate_string",rate);
                } else {
                    sessionManager.editor.putString("spinner_rate_string",spinner_rate.getSelectedItem().toString());
                }
                sessionManager.editor.putInt("spinner_age",spinner_age.getSelectedItemPosition());
                sessionManager.editor.putString("spinner_age_string",spinner_age.getSelectedItem().toString());

                sessionManager.editor.putInt("spinner_si_do",spinner_si_do.getSelectedItemPosition());
                sessionManager.editor.putInt("spinner_gu_gun",spinner_gu_gun.getSelectedItemPosition());

                sessionManager.editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        editText_keyword.setText(sessionManager.sharedPreferences.getString("keyword", ""));
        spinner_study_type.setSelection(sessionManager.sharedPreferences.getInt("spinner_study_type", 0));
        spinner_gender.setSelection(sessionManager.sharedPreferences.getInt("spinner_gender", 0));
        spinner_rate.setSelection(sessionManager.sharedPreferences.getInt("spinner_rate", 0));
        spinner_age.setSelection(sessionManager.sharedPreferences.getInt("spinner_age", 0));
        spinner_si_do.setSelection(sessionManager.sharedPreferences.getInt("spinner_si_do", 0));


    }
    private void setSpinnerAdapterItem(int array_resource) {
        ArrayAdapter gun_gu_adapter = ArrayAdapter.createFromResource(this,array_resource, android.R.layout.simple_spinner_dropdown_item);
        spinner_gu_gun.setAdapter(gun_gu_adapter);
        spinner_gu_gun.setSelection(sessionManager.sharedPreferences.getInt("spinner_gu_gun", 0));
    }
}