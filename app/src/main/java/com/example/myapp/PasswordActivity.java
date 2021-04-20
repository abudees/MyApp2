package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class PasswordActivity extends AppCompatActivity {

    EditText editTextTextPassword;
    String userType, mobileNumber, password;
    Intent intent;


    public void login (View view){


        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereEqualTo("username", mobileNumber);

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null & objects != null)  {

                    for (ParseUser object : objects) {

                        password = object.getString("password");
                    }

                  //  if( editTextTextPassword.getText().toString().matches(password)) {


                        ParseUser.logInInBackground(mobileNumber, String.valueOf(editTextTextPassword.getText()), new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // Hooray! The user is logged in.


                                    switch (userType) {


                                        case "m":

                                            intent = new Intent(getApplicationContext(), VendorActivity.class);

                                            startActivity(intent);

                                            break;

                                        case "d":


                                            intent = new Intent(getApplicationContext(), DriverActivity.class);

                                            startActivity(intent);

                                            break;



                                        default:


                                            break;
                                    }

                                } else {
                                    // Signup failed. Look at the ParseException to see what happened.
                                    Toast.makeText(getApplicationContext(), "الرقم السري غير صحيح" + e, Toast.LENGTH_LONG).show();
                                }
                            }
                        });



                }

            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        editTextTextPassword = findViewById(R.id.editTextTextPassword);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                mobileNumber ="";
                userType = "";
            } else {
                mobileNumber = extras.getString("mobileNumber");
                userType = extras.getString("userType");
            }
        }

    }
}