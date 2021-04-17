package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    String mobileNumber;
    EditText nameEditText;
    Intent intent;

    String username, apiKey, token, url, url2, cameFromActivity, url1, sender, sudanOr ;



    public void enterName(View view){
        if (nameEditText.getText().toString().matches("")) {

            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();

        } else {

            signUp();
        }
    }


    public void signUp() {


        ParseUser user = new ParseUser();
        user.setUsername(mobileNumber);
        user.setPassword("000000");
        user.put("name", nameEditText.getText().toString());
        user.put("userType", "c");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {

                    ParseUser.logInInBackground(mobileNumber, "000000",
                            new LogInCallback() {
                                public void done(ParseUser user, ParseException error) {
                                    if (error == null) {

                                        ParseQuery<ParseObject> queryAPIs = new ParseQuery<>("APIs");
                                        if (mobileNumber.startsWith("+249")) {
                                            // api for Sudan numbers

                                            queryAPIs.whereEqualTo("name", "smsSudan");

                                            queryAPIs.findInBackground(new FindCallback<ParseObject>() {

                                                @Override
                                                public void done(List<ParseObject> objects, ParseException e) {

                                                    if (e == null) {

                                                        for (ParseObject object : objects) {

                                                            apiKey = object.getString("accountSID");
                                                            token = object.getString("authToken");
                                                            url = object.getString("url");
                                                            url2 = object.getString("url2");
                                                            sender = object.getString("sender");
                                                        }
                                                        intent = new Intent(getApplicationContext(), OTPActivity.class);

                                                        intent.putExtra("mobileNumber", username);
                                                        intent.putExtra("apiKey", apiKey);
                                                        intent.putExtra("token", token);
                                                        intent.putExtra("url1", url1);
                                                        intent.putExtra("sender", sender);
                                                        intent.putExtra("cameFromActivity", cameFromActivity);
                                                        intent.putExtra("sudanOr",sudanOr);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        } else {

                                            queryAPIs.whereEqualTo("name", "sms");

                                            queryAPIs.findInBackground(new FindCallback<ParseObject>() {

                                                @Override
                                                public void done(List<ParseObject> objects, ParseException e) {

                                                    if (e == null) {

                                                        for (ParseObject object : objects) {

                                                            apiKey = object.getString("accountSID");
                                                            token = object.getString("authToken");
                                                            url1 = object.getString("url");
                                                            url2 = object.getString("url2");
                                                        }
                                                    }
                                                    intent = new Intent(getApplicationContext(), OTPActivity.class);

                                                    intent.putExtra("mobileNumber", username);
                                                    intent.putExtra("apiKey", apiKey);
                                                    intent.putExtra("token", token);
                                                    intent.putExtra("url1", url1);
                                                    intent.putExtra("url2", url2);
                                                    intent.putExtra("cameFromActivity", cameFromActivity);
                                                    intent.putExtra("sudanOr",sudanOr);

                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                    );
                } else {
                    Toast.makeText(SignUpActivity.this, "signup error " +
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {
                ParseAnalytics.trackAppOpenedInBackground(getIntent());



                nameEditText = findViewById(R.id.nameEditText);

                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();

                    if (extras == null) {

                        mobileNumber ="";
                    } else {

                        mobileNumber = extras.getString("mobileNumber");
                    }
                }


            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }
    }

}

