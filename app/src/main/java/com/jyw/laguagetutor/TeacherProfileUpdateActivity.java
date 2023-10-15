package com.jyw.laguagetutor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TeacherProfileUpdateActivity extends AppCompatActivity {

    private ImageView imageView_profile;
    private EditText editText_nickname;
    private Bitmap bitmapImage;

    private String mMobileNumber,userType;

    private static final String TAG = TeacherProfileUpdateActivity.class.getSimpleName();
    private static final String IMAGE_URL = "compute.amazonaws.com/server1/images/";
    private static final String URL = "he.amazonaws.com/server1/insert_image.php";
    private static final String URL_GET_PROFILE = "ht-2.compute.amazonaws.com/server1/get_profile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        Intent dataIntent = getIntent();
        mMobileNumber = dataIntent.getStringExtra(getResources().getString(R.string.mobile_number));
        userType = dataIntent.getStringExtra(getResources().getString(R.string.user_type));

        editText_nickname = findViewById(R.id.editText_nickname);

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView_profile = findViewById(R.id.imageView_profile);
        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                imageResult.launch(galleryIntent);
            }
        });

        Button button_confirm = findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });

        setProfile();
    }

    ActivityResultLauncher<Intent> imageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()== Activity.RESULT_OK){
                if(result.getData()!=null){
                    Intent intent = result.getData();
                    Uri uri = intent.getData();
                    imageView_profile.setImageURI(uri);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        try {
                            bitmapImage = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),uri));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    });

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    private void exit(){
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


    private void uploadImage() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Toast.makeText(getApplicationContext(),"수정 완료",Toast.LENGTH_SHORT).show();
                            exit();
                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"수정 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,error.toString());
                        Toast.makeText(getApplicationContext(),"수정 실패",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("image",bitmapToString(bitmapImage));
                data.put(getResources().getString(R.string.mobile_number),mMobileNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    private void setProfile() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_GET_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d(TAG,response);
                            String image = jsonObject.getString("profile_image");
                            Log.d(TAG,image);
                            if(image.equals("null")){
                                imageView_profile.setImageResource(R.drawable.basic_profile);
                            } else {
                                Picasso.get().load(IMAGE_URL+image).into(imageView_profile);
                            }
                            editText_nickname.setText(jsonObject.getString("user_name"));

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
                data.put(getResources().getString(R.string.mobile_number),mMobileNumber);
                Log.d(TAG,mMobileNumber);
                data.put(getResources().getString(R.string.user_type),userType);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }
}