package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterTypeActivity extends AppCompatActivity {

    private Button button_student,button_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_type);

        button_student = findViewById(R.id.button_student);
        button_teacher = findViewById(R.id.button_teacher);

        Intent dataIntent = getIntent();
        String registerType = dataIntent.getStringExtra("registerType");


        button_student.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SmsVerificationActivity.class);
                intent.putExtra("user_type","student");
                intent.putExtra("registerType",registerType);
                if(registerType.equals("sns")){
                    intent.putExtra("email",dataIntent.getStringExtra("email"));
                    intent.putExtra("name",dataIntent.getStringExtra("name"));
                }

                intent.putExtra("verification_type","register");
                startActivity(intent);
             }
         });

        button_teacher.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), SmsVerificationActivity.class);
                 intent.putExtra("user_type","teacher");
                 intent.putExtra("registerType",dataIntent.getStringExtra("registerType"));
                 intent.putExtra("verification_type","register");
                 startActivity(intent);
             }
         });
    }
}