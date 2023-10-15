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
import com.jyw.laguagetutor.adapter.StudentAdapter;
import com.jyw.laguagetutor.recyclerData.Student;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentFragment extends Fragment {

    private RecyclerView recyclerView_item;
    private static final String BASE_URL = "htnaws.com/server1/get_student.php";
    private static final String TAG = StudentFragment.class.getSimpleName();
    public StudentFragment() {
    }

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
        requestStudentList();

    }

    private void requestStudentList() {
        ArrayList<Student> students = new ArrayList<>();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String user_name = object.getString("user_name");
                                String title = object.getString("title");
                                String gender = object.getString("gender");
                                String age = object.getString("age");
                                String location = object.getString("location");
                                String mobileNumber = object.getString("mobile_number");
                                String study_type = object.getString("study_type");
                                Student student = new Student(user_name,title,gender,age,location,mobileNumber,study_type);
                                students.add(student);
                            }
                            Log.w(TAG,array.length()+"");
                            StudentAdapter studentAdapter = new StudentAdapter(getActivity(),students);
                            recyclerView_item.setAdapter(studentAdapter);
                        } catch (Exception e){
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

                SessionManager sessionManager = new SessionManager(getActivity());

                String study_type = sessionManager.sharedPreferences.getString("spinner_student_study_type_string",null);
                if(study_type!=null){
                    if(!study_type.equals("전체")){
                        data.put("study_type",study_type);
                    }
                }

                String gender = sessionManager.sharedPreferences.getString("spinner_student_gender_string",null);
                if(gender!=null){
                    if(!gender.equals("전체")){
                        data.put("gender",gender);
                    }
                }

//                String age = sessionManager.sharedPreferences.getString("spinner_age_string",null);

                String location = sessionManager.sharedPreferences.getString("spinner_student_location_string",null);
                if(location!=null){
                    if(!location.equals("전체")){
                        data.put("location",location);
                    }
                }

                String keyword = sessionManager.sharedPreferences.getString("student_keyword",null);
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
