package com.jyw.laguagetutor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jyw.laguagetutor.adapter.RecyclerViewPagerAdapter;
import com.jyw.laguagetutor.adapter.TeacherAdapter;
import com.jyw.laguagetutor.recyclerData.Teacher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherFragment extends Fragment {

    private RecyclerView recyclerView_item;
    private static final String TAG = TeacherFragment.class.getSimpleName();
    public TeacherFragment() {

    }

    private static final String URL_SEARCH_TEACHER = "azonaws.com/server1/get_teacher.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();
        recyclerView_item = view.findViewById(R.id.recyclerView_item);
        recyclerView_item.setLayoutManager(new LinearLayoutManager(context));
        requestTeacherList();
    }

    private void requestTeacherList() {
        ArrayList<Teacher> teachers = new ArrayList<>();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_SEARCH_TEACHER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String title = object.getString("title");
                                String name = object.getString("user_name");
                                String gender = object.getString("gender");
                                String education = object.getString("education");
                                String study_type = object.getString("study_type");
                                String location = object.getString("location");
                                String mobileNumber = object.getString("mobile_number");
                                String image = object.getString("profile_image");
                                Teacher teacher = new Teacher(title,name,gender,education,study_type,location,mobileNumber,image);
                                teachers.add(teacher);
                            }

                            TeacherAdapter teacherAdapter = new TeacherAdapter(getActivity(),teachers);
                            recyclerView_item.setAdapter(teacherAdapter);
                        } catch (Exception e){
                            e.printStackTrace();
                            Log.e(RecyclerViewPagerAdapter.class.getSimpleName(),""+e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(RecyclerViewPagerAdapter.class.getSimpleName(),error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                SessionManager sessionManager = new SessionManager(getActivity());

                String study_type = sessionManager.sharedPreferences.getString("spinner_study_type_string",null);
                if(study_type!=null){
                    if(!study_type.equals("전체")){
                        data.put("study_type",study_type);
                    }
                }

                String gender = sessionManager.sharedPreferences.getString("spinner_gender_string",null);
                if(gender!=null){
                    if(!gender.equals("전체")){
                        data.put("gender",gender);
                    }
                }

                String rate = sessionManager.sharedPreferences.getString("spinner_rate_string",null);
                if(rate!=null){
                    if(!rate.equals("전체")){
                        data.put("rate",rate);
                    }
                }

//                String age = sessionManager.sharedPreferences.getString("spinner_age_string",null);

                String location = sessionManager.sharedPreferences.getString("spinner_location_string",null);
                if(location!=null){
                    if(!location.equals("전체")){
                        data.put("location",location);
                    }
                }

                String keyword = sessionManager.sharedPreferences.getString("keyword",null);
                if(keyword!=null){
                    if(!keyword.equals("")){
                        data.put("keyword",keyword);
                    }
                }

                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getActivity()).add(request);
    }
}
