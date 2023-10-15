package com.jyw.laguagetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNumber extends AppCompatActivity {

    private EditText editTextNumber;
    private Button btn_cancel,btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_number);

        Intent dataIntent = getIntent();

        editTextNumber = findViewById(R.id.editTextNumber);
        editTextNumber.setText(dataIntent.getStringExtra("num"));

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                finish();
             }
         });

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
         @Override
             public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityBuyTicket.class);
                intent.putExtra("num",editTextNumber.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
             }
         });
    }
}