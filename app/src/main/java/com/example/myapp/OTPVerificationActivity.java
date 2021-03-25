package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OTPVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText otpNumber;
    private Button submitBtn;
    private TextView successMsg;
    String otpValue;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);


            otpNumber = findViewById(R.id.otpNumber);
            submitBtn = findViewById(R.id.submitBtn);
            successMsg = findViewById(R.id.successMsg);



            Intent intent = getIntent();
            otpValue = intent.getStringExtra("otp_number");



            otpNumber.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.VISIBLE);

            submitBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            boolean checkOtpNumber = TextUtils.isEmpty(otpNumber.getText().toString());

            if(checkOtpNumber == false){

                if(otpValue.equals(otpNumber.getText().toString())){

                    otpNumber.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);


                    successMsg.setText("Successfully Verified!");

                }else{

                    successMsg.setText("OTP doesn't match! Please Enter again!");


                }
            }else{
                otpNumber.setError("Please Enter OTP First!");
            }
        }
    }
