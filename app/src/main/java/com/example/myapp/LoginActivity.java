package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, AdapterView.OnItemSelectedListener {


    Boolean signUpModeActive = true;

    TextView changeSignUpTextView;

    EditText mobileEditText, nameEditText,  emailEditText;
    Button signUpButton;
    Intent intent;
    Spinner spinner;
    String  myString;
    String callingCode= "";



    MobileVerificationActivity mobileVerificationActivity;











    public void changeMode(View view) {

        if (!signUpModeActive) {

            signUpModeActive = true;
            signUpButton.setText("Login");
            changeSignUpTextView.setText("Or, SignUp");
           // rePassword.setVisibility(View.INVISIBLE);
            nameEditText.setVisibility(View.INVISIBLE);
            emailEditText.setVisibility(View.INVISIBLE);
          //  mobileNumber.setVisibility(View.INVISIBLE);
            mobileEditText.getText();
          //  passwordEditText.getText();


        } else {

            signUpModeActive = false;
            signUpButton.setText("SignUp");
            changeSignUpTextView.setText("Or, Login");
          //  rePassword.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.VISIBLE);
           // lastNameEditText.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.backgroundReleativeLayout || v.getId() == R.id.imageView) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        }



    }


    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() >= 9 && phone.length() <= 15;
        }
        return false;
    }


    //closing the keyboard while clicking anywhere else
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

            //signUp(view);
        }
        return false;
    }


    public void login(View view) {


        if (mobileEditText.getText().toString().matches("") || !isValidMobile(mobileEditText.getText().toString())) {

            Toast.makeText(this, "A username and password are required- login", Toast.LENGTH_SHORT).show();

        } else {

            if (signUpModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(callingCode+(mobileEditText.getText().toString()));

                ParseUser.logInInBackground(callingCode+(mobileEditText.getText().toString()), "000000",
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException error) {
                                if (error == null) {

                                    Log.d("that : ", callingCode+(mobileEditText.getText().toString()) );


                                    // after mobile verification
                                    Toast.makeText(LoginActivity.this, "logging in ", Toast.LENGTH_SHORT).show();

                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(LoginActivity.this, "login error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                if (nameEditText.getText().toString().matches("") || mobileEditText.getText().toString().matches("")) {

                    String mobile = mobileEditText.getText().toString();
                    String name = nameEditText.getText().toString();


                    if (isValidMobile(mobile)) {

                        ParseUser user = new ParseUser();

                        user.setUsername(callingCode + (mobileEditText.getText().toString()));

                        user.put("name", name);

                        user.setPassword("000000");

                        user.put("userType", "c");

                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    signUpButton.setText("Login");
                                    changeSignUpTextView.setText("Or, SignUp");
                                    nameEditText.setVisibility(View.INVISIBLE);

                                    mobileEditText.getText();
                                    nameEditText.getText();

                                    Toast.makeText(LoginActivity.this, "SignUp Successfully",
                                            Toast.LENGTH_SHORT).show();

                                    signUpModeActive = false;
                                } else {
                                    Toast.makeText(LoginActivity.this, "signup error " +
                                            e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {

                        Toast.makeText(LoginActivity.this, "Mobile no not valid!", Toast.LENGTH_SHORT).show();
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
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                ParseAnalytics.trackAppOpenedInBackground(getIntent());

                // userCountryCode =  getCountryDialCode();


               // callingCode = myString;

                spinner = findViewById(R.id.spinner);
                ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.DialingCountryCode));
                spinner.setAdapter(spinnerCountShoesArrayAdapter);

                spinner.setOnItemSelectedListener(this);


                Log.d("that2 : ", callingCode );


               // telephonyMngr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);


                TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

                String countryCodeValue = tm.getNetworkCountryIso();

                myString =  Iso2phone.getPhone(countryCodeValue) ; //the value you want the position for






                int spinnerPosition = spinnerCountShoesArrayAdapter.getPosition(myString);


                // set the default according to value
                spinner.setSelection(spinnerPosition);



                if (ParseUser.getCurrentUser() != null) {

                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    changeSignUpTextView = findViewById(R.id.changeSignUp);


                    ConstraintLayout backgroundReleativeLayout = findViewById(R.id.backgroundReleativeLayout);

                    // ImageView logoImageView = findViewById(R.id.imageView);

                    // logoImageView.setOnClickListener(this);

                    backgroundReleativeLayout.setOnClickListener(this);





                    mobileEditText = findViewById(R.id.mobileEditText);

                    nameEditText = findViewById(R.id.nameEditText);

                  //  lastNameEditText = findViewById(R.id.lastNameEditText);

                  //  rePassword = findViewById(R.id.rePassword);

                  //  passwordEditText.setOnKeyListener(this);

                    signUpButton = findViewById(R.id.button2);

                   // mobileNumber =findViewById(R.id.mobileNumber);

                 //   countryCode =findViewById(R.id.countryCode);

                }
            }
        }

        catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }




    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        callingCode = parent.getItemAtPosition(pos).toString();

        Log.d("thoooose: ", callingCode);



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        callingCode = myString;

    }


}

