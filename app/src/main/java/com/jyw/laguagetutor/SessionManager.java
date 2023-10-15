package com.jyw.laguagetutor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String USER_NAME = "USER_NAME";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String mobile_number, String user_type,String userName){
        editor.putBoolean(LOGIN, true);
        editor.putString(MOBILE_NUMBER, mobile_number);
        editor.putString(USER_TYPE, user_type);
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public boolean isLogIn(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){

        if (!this.isLogIn()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(MOBILE_NUMBER, sharedPreferences.getString(MOBILE_NUMBER, null));
        user.put(USER_NAME,sharedPreferences.getString(USER_NAME,null));
        user.put(USER_TYPE, sharedPreferences.getString(USER_TYPE, null));
        return user;
    }


    public void clearSession(){
        editor.clear();
        editor.commit();
    }
}
