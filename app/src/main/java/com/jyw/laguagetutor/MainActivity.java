package com.jyw.laguagetutor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jyw.laguagetutor.adapter.RecyclerViewPagerAdapter;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tab_layout;
    FragmentManager fragmentManager;
    RecyclerViewPagerAdapter recyclerViewPagerAdapter;


    ImageView imageView_search_teacher;
    ImageView imageView_search_student;

    SessionManager sessionManager;
    HashMap<String, String> user;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        sessionManager.checkLogin();

        onStartService();
        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setSelectedItemId(R.id.navigation_search);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_search:
                        break;

                    case R.id.navigation_favorite:
                        startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.navigation_lesson:
                        startActivity(new Intent(getApplicationContext(),MyClassActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.navigation_setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }
                return false;
            }
        });

        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2 = findViewById(R.id.viewpager2);
        fragmentManager = getSupportFragmentManager();
        recyclerViewPagerAdapter = new RecyclerViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(recyclerViewPagerAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tab_layout.selectTab(tab_layout.getTabAt(position));
                switch (position){
                    case 0 :
                        imageView_search_teacher.setVisibility(View.VISIBLE);
                        imageView_search_student.setVisibility(View.GONE);
                        break;
                    case 1 :
                        imageView_search_student.setVisibility(View.VISIBLE);
                        imageView_search_teacher.setVisibility(View.GONE);
                        break;
                }
            }
        });

        imageView_search_teacher = findViewById(R.id.imageView_search_teacher);
        imageView_search_teacher.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchTeacherActivity.class);
                mGetContent.launch(intent);

             }
         });

        imageView_search_student = findViewById(R.id.imageView_search_student);
        imageView_search_student.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), SearchStudentActivity.class);
                 mGetContent.launch(intent);

             }
         });

        Button button_post = findViewById(R.id.button_post);
        button_post.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String userType = user.get(sessionManager.USER_TYPE);
                String mobileNumber = user.get(sessionManager.MOBILE_NUMBER);
                Log.w(TAG,userType);
                if(userType!=null){
                    switch (userType) {
                        case "student" :
                            Intent intent = new Intent(getApplicationContext(), RegisterStudentWaitingActivity.class);
                            intent.putExtra("mobile_number",mobileNumber);
                            startActivity(intent);
                            break;
                        case "teacher" :
                            Intent intent2 = new Intent(getApplicationContext(), RegisterTeacherWaitingActivity.class);
                            intent2.putExtra("mobile_number",mobileNumber);
                            startActivity(intent2);
                            break;
                    }
                }
             }
         });

    }

     ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    recyclerViewPagerAdapter = new RecyclerViewPagerAdapter(fragmentManager, getLifecycle());
                    viewPager2.setAdapter(recyclerViewPagerAdapter);
                }
            }
        });


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onStartService(){
        Intent intent = new Intent(this, ChatService.class);
        intent.putExtra(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
        intent.putExtra(getResources().getString(R.string.var_username),user.get(SessionManager.USER_NAME));
        startService(intent);
    }



    public void onStopService(){
        Intent intent = new Intent(this, ChatService.class);

        stopService(intent);
    }
}