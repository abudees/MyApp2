package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    int randomNumber;

    String apiKey, token, url1, url2, message, defaultSenderNo, mobileNumber, cameFromActivity ;

    Intent intent;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify2);

        defaultSenderNo= "+14142460442";






        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                apiKey = "";
                token = "";
                url1 ="";
                url2="";
                mobileNumber ="";

                cameFromActivity ="";

            } else {
                apiKey = extras.getString("key");
                token = extras.getString("token");
                url1 = extras.getString("url");
                url2 = extras.getString("url2");
                mobileNumber = extras.getString("mobileNumber");
                cameFromActivity = extras.getString("cameFromActivity");
            }
        }




        _txtVerOTP=(EditText)findViewById(R.id.txtVerOTP);

        _btnVerOTP=(Button)findViewById(R.id.btnVerOTP);






        try {


            // Construct data






                if (mobileNumber.startsWith("+249")){
                 // api for Sudan numbers


                } else {

                    // all numbers except Sudan
                    Random random = new Random();
                    randomNumber = random.nextInt(999999);
                    message = "<#> " + randomNumber + " Happy Birthday from  YazSeed .";

                    OkHttpClient client = new OkHttpClient();

                    String url = url1 + apiKey + url2;

                    String base64EncodedCredentials = "Basic " + Base64.encodeToString((apiKey + ":" + token).getBytes(), Base64.NO_WRAP);

                    RequestBody body = new FormBody.Builder()
                            .add("From", defaultSenderNo)
                            .add("To", mobileNumber)
                            .add("Body", message)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .header("Authorization", base64EncodedCredentials)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        Log.d("TAG", "sendSms: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(getApplicationContext(), "OTP SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();
                }
        } catch (Exception e) {

            //System.out.println("Error SMS "+e);
            // /return "Error "+e;
            Toast.makeText(getApplicationContext(), "ERROR SMS " + e, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "ERROR " + e, Toast.LENGTH_LONG).show();

            Log.d("error", e.toString());
        }



        _btnVerOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(randomNumber==Integer.valueOf(_txtVerOTP.getText().toString())){
                    Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();

                    switch (Objects.requireNonNull(ParseUser.getCurrentUser().getString("userType"))) {

                        case "c":

                            // after mobile verification
                            Toast.makeText(SmsVerificationActivity.this, "logging in ", Toast.LENGTH_SHORT).show();


                            Log.d("ooooo",cameFromActivity);

                            if (cameFromActivity.matches("MainActivity")) {

                                intent = new Intent(getApplicationContext(), MainActivity.class);

                                startActivity(intent);
                            }

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
                            Toast.makeText(SmsVerificationActivity.this, "logging in ", Toast.LENGTH_SHORT).show();

                            break;
                    }

                    intent = new Intent(getApplicationContext(), cameFromActivity.class);


                    intent.putExtra("mobileNumber", username);

                }else{
                    Toast.makeText(getApplicationContext(), "WRONG OTP", Toast.LENGTH_LONG).show();
                }

            }
        });



        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
