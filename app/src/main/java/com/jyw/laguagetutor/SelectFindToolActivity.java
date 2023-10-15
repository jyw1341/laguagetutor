package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SelectFindToolActivity extends AppCompatActivity {

    private TextView textView_description,textView_userInfo;
    private RadioButton radioButton_mobile_number,radioButton_email;
    private Button button_next;
    private RadioGroup radioGroup;

    private static final String TAG = SelectFindToolActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_find_tool);

        textView_description = findViewById(R.id.textView_description);
        textView_userInfo = findViewById(R.id.textView_userInfo);
        radioButton_mobile_number = findViewById(R.id.radioButton_mobile_number);
        radioButton_email = findViewById(R.id.radioButton_email);
        radioGroup = findViewById(R.id.radioGroup);
        button_next = findViewById(R.id.button_next);

        Intent dataIntent = getIntent();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton_mobile_number){

                    textView_description.setText(R.string.select_find_tool_mobile_description);
                    String mobile = dataIntent.getStringExtra("mobile");
                    mobile = replaceMobileNumber(mobile);
                    textView_userInfo.setText(mobile);

                } else if(checkedId==R.id.radioButton_email){
                    String email = dataIntent.getStringExtra("email");
                    if(email.equals("null")){
                        textView_description.setText(R.string.select_find_tool_email_non_exist);
                        textView_userInfo.setText("");
                    } else {
                        textView_description.setText(R.string.select_find_tool_email_description);
                        textView_userInfo.setText(email);
                    }
                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                if(radioButtonId==R.id.radioButton_mobile_number){
                    Intent intent = new Intent(getApplicationContext(), SmsVerificationActivity.class);
                    String userId = dataIntent.getStringExtra("userId");
                    intent.putExtra("userId",userId);
                    intent.putExtra("verification_type","findPassword");
                    startActivity(intent);
                } else if(radioButtonId==R.id.radioButton_email){

                }
            }
         });

    }

    private String replaceMobileNumber(String src) {
        if (src == null) {
            return "";
        }
        //010 3895 7494
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(src);
        stringBuffer.replace(4,7,"***");
        stringBuffer.replace(8,11,"***");
        String result = stringBuffer.toString();
        return result.replaceFirst("(^02|[0-9]{3})([0-9*]{3,4})([0-9*]{4})$", "$1-$2-$3");
    }
}