package com.jyw.laguagetutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private TextView textView_update,textView_logout,textView_withdrawal,tv_ticket;

    SessionManager sessionManager;
    GoogleSignInClient mGoogleSignInClient;
    HashMap<String, String> user;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        userType = user.get(SessionManager.USER_TYPE);
        sessionManager.checkLogin();

        textView_update = findViewById(R.id.textView_update);
        textView_update.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                if(userType.equals(getResources().getString(R.string.register_type_student))){
                    Intent intent = new Intent(getApplicationContext(), ProfileUpdateActivity.class);
                    intent.putExtra(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                    intent.putExtra(getResources().getString(R.string.user_type),userType);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), TeacherProfileUpdateActivity.class);
                    intent.putExtra(getResources().getString(R.string.mobile_number),user.get(SessionManager.MOBILE_NUMBER));
                    intent.putExtra(getResources().getString(R.string.user_type),userType);
                    startActivity(intent);
                }
             }
         });
        textView_logout = findViewById(R.id.textView_logout);
        textView_withdrawal = findViewById(R.id.textView_withdrawal);

        tv_ticket = findViewById(R.id.tv_ticket);
        if(userType.equals("student")){
            tv_ticket.setVisibility(View.VISIBLE);
            tv_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TicketActivity.class);
                    startActivity(intent);
                }
            });
        }




        BottomNavigationView nav_view = findViewById(R.id.nav_view);

        nav_view.setSelectedItemId(R.id.navigation_setting);

        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;


                    case R.id.navigation_favorite:
                        startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return  true;

                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.navigation_lesson:
                        startActivity(new Intent(getApplicationContext(),MyClassActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.navigation_setting:

                        return true;
                }
                return false;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        textView_logout.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
             sessionManager.clearSession();
             if (account != null) {
                 googleSignOut();
             }
             Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             onStopService();
             startActivity(intent);
             finish();
             }
         });

        textView_withdrawal.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {

             }
         });
    }

    private void googleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    public void onStopService(){
        Intent intent = new Intent(this, ChatService.class);

        stopService(intent);
    }
}