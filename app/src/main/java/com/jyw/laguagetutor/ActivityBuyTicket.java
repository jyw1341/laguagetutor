package com.jyw.laguagetutor;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootUser;

public class ActivityBuyTicket extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView tv_name,tv_subject,tv_rate,tv_total,et_time;

    private Button btn_buy;

    private String phoneNumber,mPhoneNumber;
    private static final String TAG = ActivityBuyTicket.class.getSimpleName();
    public static final String URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/get_teacher2.php";
    public static final String URL_1 = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/create_ticket.php";
    private static final String IMAGE_URL = "http://ec2-52-79-124-122.ap-northeast-2.compute.amazonaws.com/server1/images/";

    private SessionManager sessionManager;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        Intent dataIntent = getIntent();
        phoneNumber = dataIntent.getStringExtra(getResources().getString(R.string.mobile_number));

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        mPhoneNumber = user.get(SessionManager.MOBILE_NUMBER);

        circleImageView = findViewById(R.id.circleImageView);
        tv_name = findViewById(R.id.tv_name);
        tv_subject = findViewById(R.id.tv_subject);
        tv_rate = findViewById(R.id.tv_rate);
        tv_total = findViewById(R.id.tv_total);
        et_time = findViewById(R.id.et_time);

        et_time.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditNumber.class);
                intent.putExtra("num",et_time.getText().toString());
                getNumber.launch(intent);
         }
         });

        ImageView imageView_clear = findViewById(R.id.imageView_clear);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stringRequest();

        //결제 진행
        btn_buy = findViewById(R.id.btn_booking);
        btn_buy.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                String time = et_time.getText().toString();
                onClick_request(v,time);
             }
         });

        //
        BootpayAnalytics.init(this, getResources().getString(R.string.bootPay));
    }

    //뷰 초기화 메서드
    private void setViews(String name, String subject, String rate, String image){
        Picasso.get().load(IMAGE_URL+image).into(circleImageView);
        tv_name.setText(name);
        tv_subject.setText(subject);
        tv_rate.setText(rate);
        tv_total.setText(getTotal(rate,et_time.getText().toString()));
    }

    //수강료 * 횟수 구해주는 메서드
    private String getTotal(String rate, String time){
        int x = Integer.parseInt(rate);
        int y = Integer.parseInt(time);
        return Integer.toString(x*y);
    }


    private void stringRequest() {
            StringRequest request = new StringRequest(
            Request.Method.POST,
            URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String name = jsonObject.getString("name");
                        String subject = jsonObject.getString("subject");
                        String rate = jsonObject.getString("rate");
                        String image = jsonObject.getString("image");

                        setViews(name,subject,rate,image);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put(getResources().getString(R.string.mobile_number),phoneNumber);
                Log.d(TAG,phoneNumber);
                return data;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    //수강권 서버에 생성
        private void createTicket(String teacherNumber, String myNumber, String count, String date,String card, String price) {
                StringRequest request = new StringRequest(
                Request.Method.POST,
                        URL_1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Intent intent = new Intent(getApplicationContext(), ReceiptActivity.class);
                            intent.putExtra("date",date);
                            intent.putExtra("card",card);
                            intent.putExtra("price",price);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Intent serviceIntent = new Intent(getApplicationContext(), ChatService.class);
                            serviceIntent.putExtra(getResources().getString(R.string.var_service_command),"buy_ticket");
                            serviceIntent.putExtra("members",mPhoneNumber+','+phoneNumber);
                            startService(serviceIntent);

                            finish();
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
                    data.put("teacherNumber",teacherNumber);
                    data.put("myNumber",myNumber);
                    data.put("count",count);
                    data.put("date",date);
                    return data;
                }
            };
            request.setShouldCache(false);
            Volley.newRequestQueue(this).add(request);
        }

    public void onClick_request(View v,String count) {
        // 결제호출
        BootUser bootUser = new BootUser().setPhone(mPhoneNumber.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3"));

        Bootpay.init(getFragmentManager())
                .setApplicationId(getResources().getString(R.string.bootPay)) // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.KCP) // 결제할 PG 사
                .setMethod(Method.CARD) // 결제수단
                .setContext(this)
                .setBootUser(bootUser)
                .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setName("수강권") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setPrice(1000) // 결제할 금액
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                       Bootpay.confirm(message);

                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            String date = jsonObject.getString("purchased_at");
                            String card = jsonObject.getString("card_name");
                            String price =jsonObject.getString("price");
                            createTicket(phoneNumber,mPhoneNumber,count,date,card,price);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {

                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d("close", "close");
                            }
                        })
                .request();
    }

     ActivityResultLauncher<Intent> getNumber = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent intent = result.getData();
                    String str = intent.getStringExtra("num");
                    et_time.setText(str);
                    int num = Integer.parseInt(str);
                    int price = Integer.parseInt(tv_rate.getText().toString());
                    String finalPrice = Integer.toString(num*price);
                    tv_total.setText(finalPrice);
                }
            }
        });
}