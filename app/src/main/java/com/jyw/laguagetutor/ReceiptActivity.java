package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptActivity extends AppCompatActivity {

    private TextView tv_time,tv_card,tv_price;
    private Button btn_home,btn_ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);



        tv_time = findViewById(R.id.tv_time);
        tv_card = findViewById(R.id.tv_card);
        tv_price = findViewById(R.id.tv_price);

        setView();

        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
             }
         });

        btn_ticket = findViewById(R.id.btn_ticket);
        btn_ticket.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), MyClassActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intent);
             }
         });
    }

    private void setView(){
        Intent dataIntent = getIntent();
        tv_time.setText(dataIntent.getStringExtra("date"));
        tv_card.setText(dataIntent.getStringExtra("card"));
        tv_price.setText(dataIntent.getStringExtra("price"));

    }
}