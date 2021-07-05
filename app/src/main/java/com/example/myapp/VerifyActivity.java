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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

    String cameFromActivity,  mobileNumber, name;


    public void logInSwitch (){
        switch (cameFromActivity) {


            case "CategoriesActivity":

                intent = new Intent(getApplicationContext(), CategoriesActivity.class);

                startActivity(intent);
                Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();
                break;

            case "MainActivity":

                intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();
                break;

            case "ProductsActivity":

                intent = new Intent(getApplicationContext(), ProductsActivity.class);

                startActivity(intent);
                Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();
                break;

            case "CheckoutActivity":

                intent = new Intent(getApplicationContext(), CheckoutActivity.class);

                startActivity(intent);


                Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();
                break;



            // copy all activities
            default:
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();


                break;
        }

    }

    private void counterRun(){

        if (counterValid) {
            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    //here you can have your logic to set text to edittext

                    smsRetriver();

                }

                public void onFinish() {
                    mTextField.setText("done!");

                    counterValid = false;
                }

            }.start();

        } else {
            
        }

    }

    public void smsRetriver (){

        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify2);




        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                randomNumber = 0;
                cameFromActivity ="";
                mobileNumber ="";
                name = "";
            } else {
                randomNumber = extras.getInt("randomNumber");
                cameFromActivity = extras.getString("activityName");
                mobileNumber = extras.getString("mobileNumber");
                name = extras.getString("name");

            }
        } else {
            randomNumber = (int) savedInstanceState.getSerializable("randomNumber");
        }








        _txtVerOTP=(EditText)findViewById(R.id.txtVerOTP);

        _btnVerOTP=(Button)findViewById(R.id.btnVerOTP);


        mTextField = findViewById(R.id.textView);



        new CountDownTimer(90000, 1000) {

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
                    //int random = Integer.getInteger());

                    if (String.valueOf(randomNumber).equals(String.valueOf(_txtVerOTP.getText()))) {

                      //  Log.d("came from", cameFromActivity);

                        _txtVerOTP.setVisibility(View.INVISIBLE);





                        ParseUser.logInInBackground(mobileNumber, "000000", new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {



                                    logInSwitch();


                                                      // Hooray! The user is logged in.
                                } else {
                                    // Signup failed. Look at the ParseException to see what happened.
                                   // Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();

                                    ParseUser user1 = new ParseUser();
                                    user1.setUsername(mobileNumber);
                                    user1.setPassword("000000");
                                    user1.put("name", name);
                                    user1.put("userType", "n");

                                    user1.signUpInBackground(new SignUpCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if (e == null) {

                                                ParseUser.logInInBackground(mobileNumber, "000000",
                                                        new LogInCallback() {
                                                            public void done(ParseUser user, ParseException error) {
                                                                if (error == null) {

                                                                    logInSwitch();
                                                                }
                                                            }
                                                        }
                                                );
                                            } else {
                                                Toast.makeText(VerifyActivity.this, "signup error " +
                                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    });
                                                                }
                                }

                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "WRONG OTP", Toast.LENGTH_LONG).show();
                    }
                }
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        });
    }
}
