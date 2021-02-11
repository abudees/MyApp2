package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class SmsVerificationActivity extends AppCompatActivity {


    String mobileNumber;
    List<String> userNames ;
    Intent intent;




    public void verified (View view) {

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereEqualTo("username", mobileNumber);

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    // The query was successful.
                    for (ParseUser object : objects) {
                        //  userNames.add(object.getUsername());

                        Log.d("haa", "fdfdfd");
                    }
                }

                if ((userNames).contains(mobileNumber)) {


                    ParseUser user = new ParseUser();

                    user.setUsername(mobileNumber);

                    ParseUser.logInInBackground(mobileNumber, "000000",
                            new LogInCallback() {
                                public void done(ParseUser user, ParseException error) {
                                    if (error == null) {

                                        // after mobile verification
                                        Toast.makeText(SmsVerificationActivity.this, "logging in ", Toast.LENGTH_SHORT).show();

                                         intent = new Intent(getApplicationContext(), MainActivity.class);
                                         startActivity(intent);
                                    }
                                }
                            });
                } else {

                    // delete this toast
                    Toast.makeText(SmsVerificationActivity.this, "Signup ", Toast.LENGTH_SHORT).show();

                    intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    intent.putExtra("mobileNumber", mobileNumber);
                    startActivity(intent);
                }
            }
        });

    }


    public void notVerified(){

        Toast.makeText(SmsVerificationActivity.this, "Number not verified", Toast.LENGTH_SHORT).show();


        intent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        Log.d("dddddddd", Objects.requireNonNull(mobileNumber));

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {

                ParseAnalytics.trackAppOpenedInBackground(getIntent());

                userNames = new ArrayList<>();

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
