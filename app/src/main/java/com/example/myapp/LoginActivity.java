package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{


    Boolean signUpModeActive = true;

    TextView changeSignUpTextView;

    EditText passwordEditText, usernameEditText, firstNameEditText, lastNameEditText, rePassword;

    Button signUpButton;

    Intent intent;



    public void changeMode(View view){

        if (signUpModeActive == false){

            signUpModeActive = true;
            signUpButton.setText("Login");
            changeSignUpTextView.setText("Or, SignUp");
            rePassword.setVisibility(View.INVISIBLE);
            firstNameEditText.setVisibility(View.INVISIBLE);
            lastNameEditText.setVisibility(View.INVISIBLE);
            usernameEditText.getText().toString();
            passwordEditText.getText().toString();

        } else {

            signUpModeActive = false;
            signUpButton.setText("SignUp");
            changeSignUpTextView.setText("Or, Login");
            rePassword.setVisibility(View.VISIBLE);
            firstNameEditText.setVisibility(View.VISIBLE);
            lastNameEditText.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.backgroundReleativeLayout || v.getId() == R.id.imageView){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),0);
            }
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }




    //closing the keyboard while clicking anywhere else
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

            //signUp(view);
        }
        return false;
    }


    public void userType() {

        Log.i("here ", ParseUser.getCurrentUser().getString("userType"));

        switch (ParseUser.getCurrentUser().getString("userType")) {

            case "Vendor":

                intent = new Intent(getApplicationContext(), VendorActivity.class);
                startActivity(intent);

                break;

            case "Driver":

                intent = new Intent(getApplicationContext(), DriverActivity.class);
                startActivity(intent);
                break;

            case "Manager":
                intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);
                break;

            case "":

                intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + ParseUser.getCurrentUser().getString("userType"));


        }
    }




    public void login (View view){

        Toast.makeText(this, signUpModeActive.toString(), Toast.LENGTH_SHORT).show();

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

            Toast.makeText(this, "A username and password are required- login", Toast.LENGTH_SHORT).show();

        } else {

            if (signUpModeActive) {


                ParseUser user = new ParseUser();

                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException error) {
                        if (error == null) {

                            userType();

                            Toast.makeText(LoginActivity.this, "logging in ", Toast.LENGTH_SHORT).show();


                        } else {

                            Toast.makeText(LoginActivity.this, "login error " + error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            } else {



                if ( firstNameEditText.getText().toString().matches("") || lastNameEditText.getText().toString().matches("")
                        || usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")
                        || rePassword.getText().toString().matches("")) {


                    Toast.makeText(this, "All fields are required - signup", Toast.LENGTH_SHORT).show();


                } else {

                    String email = usernameEditText.getText().toString();

                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        if (passwordEditText.getText().length() >= 6 && passwordEditText.getText().length() < 15) {

                            if (passwordEditText.getText().toString().matches(rePassword.getText().toString())) {

                                ParseUser user = new ParseUser();

                                user.setUsername(usernameEditText.getText().toString());
                                user.setEmail(usernameEditText.getText().toString());
                                user.setPassword(passwordEditText.getText().toString());
                                user.put("firstName", firstNameEditText.getText().toString());
                                user.put("lastName", lastNameEditText.getText().toString());


                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if (e == null) {

                                            signUpModeActive = false;
                                            signUpButton.setText("Login");
                                            changeSignUpTextView.setText("Or, SignUp");
                                            rePassword.setVisibility(View.INVISIBLE);
                                            firstNameEditText.setVisibility(View.INVISIBLE);
                                            lastNameEditText.setVisibility(View.INVISIBLE);
                                            usernameEditText.getText();
                                            passwordEditText.getText();

                                            Toast.makeText(LoginActivity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(LoginActivity.this, "signup error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            } else {

                                Toast.makeText(LoginActivity.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            Toast.makeText(this, "Password must be 6 digits or more", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(this, "email is invalid", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        }

    }











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        try {
            CheckConnection checkConnection = new CheckConnection();

            if (checkConnection.isNetworkAvailable()) {













        changeSignUpTextView = findViewById(R.id.changeSignUp);



        ConstraintLayout backgroundReleativeLayout = findViewById(R.id.backgroundReleativeLayout);

        // ImageView logoImageView = findViewById(R.id.imageView);

        // logoImageView.setOnClickListener(this);

        backgroundReleativeLayout.setOnClickListener(this);

        passwordEditText = findViewById(R.id.passwordEditText);

        usernameEditText = findViewById(R.id.usernameEditText);

        firstNameEditText = findViewById(R.id.firstNameEditText);

        lastNameEditText = findViewById(R.id.lastNameEditText);

        rePassword = findViewById(R.id.rePassword);

        passwordEditText.setOnKeyListener(this);

        signUpButton = findViewById(R.id.button2);


            }
        }

        catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


}

