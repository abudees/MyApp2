package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    String mobileNumber;
    EditText nameEditText;
    Intent intent;


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

                                        intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
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

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        nameEditText = findViewById(R.id.nameEditText);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {
                ParseAnalytics.trackAppOpenedInBackground(getIntent());






            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }
    }

}

