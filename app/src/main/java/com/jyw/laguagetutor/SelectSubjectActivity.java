
package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jyw.laguagetutor.adapter.SubjectAdapter;
import com.jyw.laguagetutor.adapter.SubjectListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SelectSubjectActivity extends AppCompatActivity implements SubjectListener {

    RecyclerView recyclerView_math,recyclerView_english,recyclerView_korean,recyclerView_science;

    TextView textView_math;
    Button button_next;

    ArrayList<String> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);

        textView_math = findViewById(R.id.textView_math);
        button_next = findViewById(R.id.button_next);
        recyclerView_math = findViewById(R.id.recyclerView_math);
        recyclerView_english = findViewById(R.id.recyclerView_english);
        recyclerView_korean = findViewById(R.id.recyclerView_korean);
        recyclerView_science = findViewById(R.id.recyclerView_science);

        resultList = new ArrayList<String>();

        String[] subjects =  getResources().getStringArray(R.array.recycler_math);
        List<String> list = Arrays.asList(subjects);
        recyclerView_math.setLayoutManager(new GridLayoutManager(this,4));
        SubjectAdapter adapter = new SubjectAdapter(list,this, resultList);
        recyclerView_math.setAdapter(adapter);


        subjects = getResources().getStringArray(R.array.recycler_english);
        list = Arrays.asList(subjects);
        recyclerView_english.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new SubjectAdapter(list,this, resultList);
        recyclerView_english.setAdapter(adapter);

        subjects = getResources().getStringArray(R.array.recycler_korean);
        list = Arrays.asList(subjects);
        recyclerView_korean.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new SubjectAdapter(list,this, resultList);
        recyclerView_korean.setAdapter(adapter);

        subjects = getResources().getStringArray(R.array.recycler_science);
        list = Arrays.asList(subjects);
        recyclerView_science.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new SubjectAdapter(list,this, resultList);
        recyclerView_science.setAdapter(adapter);

        button_next.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeacherRegisterLocationActivity.class);

                Intent dataIntent = getIntent();
                String registerType = dataIntent.getStringExtra("registerType");
                intent.putExtra("registerType",registerType);

                if(dataIntent.getStringExtra("registerType").equals("normal")){
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

                String subjectsList = resultList.toString();
                subjectsList = subjectsList.replace("[","");
                subjectsList = subjectsList.replace("]","");
                intent.putExtra("subjectsList",subjectsList);

                Toast.makeText(getApplicationContext(),subjectsList,Toast.LENGTH_SHORT).show();
                startActivity(intent);
             }
         });
    }

    @Override
    public void onSubjectChange(ArrayList<String> list) {

    }
}