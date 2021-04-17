package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VerifyActivity extends AppCompatActivity {


    Button  _btnVerOTP;

    EditText  _txtVerOTP;

    TextView mTextField;

    Intent intent;

    int randomNumber;

    Boolean counterValid = true;

    String cameFromActivity;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify2);




        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                randomNumber = 0;
                cameFromActivity ="";
            } else {
                randomNumber = extras.getInt("randomNumber");
                cameFromActivity = extras.getString("cameFromActivity");

            }
        } else {
            randomNumber = (int) savedInstanceState.getSerializable("randomNumber");
        }










        _txtVerOTP=(EditText)findViewById(R.id.txtVerOTP);

        _btnVerOTP=(Button)findViewById(R.id.btnVerOTP);


        mTextField = findViewById(R.id.textView);



        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTextField.setText("done!");

                counterValid = false;
            }

        }.start();





        _btnVerOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counterValid) {

                    if (randomNumber == Integer.parseInt(_txtVerOTP.getText().toString())) {


                        Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();

                        switch (Objects.requireNonNull(ParseUser.getCurrentUser().getString("userType"))) {

                            case "c":

                                // after mobile verification
                                Toast.makeText(VerifyActivity.this, "logging in ", Toast.LENGTH_SHORT).show();


                                break;
                            case "m":

                                intent = new Intent(getApplicationContext(), VendorActivity.class);

                                startActivity(intent);
                                break;


                            case "d":

                                intent = new Intent(getApplicationContext(), DriverActivity.class);
                                startActivity(intent);
                                break;

                            default:
                                Toast.makeText(VerifyActivity.this, "logging in ", Toast.LENGTH_SHORT).show();

                                break;
                        }

                        intent = new Intent(getApplicationContext(), MainActivity.class);


                        //intent.putExtra("mobileNumber", mobileNumber);

                    } else {
                        Toast.makeText(getApplicationContext(), "WRONG OTP", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
