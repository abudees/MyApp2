package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnKeyListener, AdapterView.OnItemSelectedListener {


    EditText mobileEditText;

    String numberConfirmed = "n";

    Intent intent;

    Spinner spinner;

    List<String> callingC, userNames ;

    String username, apiKey, token, url, url2, cameFromActivity, url1, sudanOr, myString, sender, number ;
    String userType ;

    String callingCode= "";

    TextView numberConfirmation, message;

    Button signUpButton;


    ImageButton editButton1 ;





    //closes the keyboard if the user clicks anywhere else
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.backgroundConstraintLayout || v.getId() == R.id.logoLogin) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        }
    }


    // closing the keyboard if the user hit Enter
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

            login(view);
        }
        return false;
    }


    //check entered mobile
    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() >= 9 && phone.length() <= 15;
        }
        return false;
    }


    // Login or SignUp
    public void login(View view) {


        intent = new Intent(getApplicationContext(), PayTabActivity.class);

        // intent.putExtra("mobileNumber", username);
        intent.putExtra("userType", userType);
        startActivity(intent);

        /*
        username = callingCode + (mobileEditText.getText().toString());

        number = (mobileEditText.getText().toString());

        callingC = Arrays.asList(callingCode.split(" - "));


        if (mobileEditText.getText().toString().matches("") || !isValidMobile(mobileEditText.getText().toString())) {

            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();

        } else {

            if (numberConfirmed.equals("t")) {

                // Log.d("username ya man", username);

                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.whereEqualTo("username", username);

                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            if (objects != null) {
                                // The query was successful.

                                Log.d("here", "1");
                                for (ParseUser object : objects) {
                                    Log.d("here", "2");
                                    userType = object.getString("userType");

                                }


                                if (userType != null) {

                                    Log.d("here", "3");
                                    Log.d("type", userType);

                                    switch (userType) {


                                        case "c":


                                            ParseQuery<ParseObject> queryAPIs = new ParseQuery<>("APIs");
                                            if (username.startsWith("+249")) {
                                                // api for Sudan numbers

                                                queryAPIs.whereEqualTo("name", "smsSudan");

                                                queryAPIs.findInBackground(new FindCallback<ParseObject>() {

                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {

                                                        if (e == null) {

                                                            for (ParseObject object : objects) {

                                                                apiKey = object.getString("apiKey1");
                                                                token = object.getString("password");
                                                                url = object.getString("url");
                                                                //   url2 = object.getString("url2");
                                                                sender = object.getString("apiKey2");
                                                            }

                                                            intent = new Intent(getApplicationContext(), OTPActivity.class);

                                                            intent.putExtra("mobileNumber", username);
                                                            intent.putExtra("apiKey", apiKey);
                                                            intent.putExtra("token", token);
                                                            intent.putExtra("url", url);
                                                            intent.putExtra("sender", sender);
                                                            intent.putExtra("activityName", cameFromActivity);
                                                            //  intent.putExtra("sudanOr", sudanOr);

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

                                                                apiKey = object.getString("apiKey1");
                                                                token = object.getString("password");
                                                                url = object.getString("url");
                                                                url2 = object.getString("apiKey2");
                                                            }
                                                        }
                                                        intent = new Intent(getApplicationContext(), OTPActivity.class);

                                                        intent.putExtra("mobileNumber", username);
                                                        intent.putExtra("apiKey", apiKey);
                                                        intent.putExtra("token", token);
                                                        intent.putExtra("url", url);
                                                        intent.putExtra("url2", url2);
                                                        intent.putExtra("activityName", cameFromActivity);
                                                        intent.putExtra("sudanOr", sudanOr);

                                                        startActivity(intent);
                                                    }
                                                });
                                            }


                                            break;

                                        case "d":

                                        case "m":


                                            intent = new Intent(getApplicationContext(), PasswordActivity.class);

                                            intent.putExtra("mobileNumber", username);
                                            intent.putExtra("userType", userType);
                                            startActivity(intent);

                                            break;

                                        case "n":

                                            Toast.makeText(MainActivity.this, "حبابك تطبيقنا لسه ما انطلق وان شاء قريب بنبلغك", Toast.LENGTH_LONG).show();

                                            mobileEditText.setVisibility(View.INVISIBLE);
                                            signUpButton.setVisibility(View.INVISIBLE);
                                            editButton1.setVisibility(View.INVISIBLE);
                                            spinner.setVisibility(View.INVISIBLE);
                                            numberConfirmation.setVisibility(View.INVISIBLE);

                                            message.setVisibility(View.VISIBLE);
                                            message.setText("حبابك تطبيقنا لسه ما انطلق وان شاء قريب بنبلغك");

                                        default:


                                            break;
                                    }

                                    Log.d("here", "4");

                                } else  {
                                    Log.d("here", "10");
                                    //  intent = new Intent(getApplicationContext(), SignUpActivity.class);

                                    //   intent.putExtra("mobileNumber", username);
                                    //   intent.putExtra("activityName", cameFromActivity);

                                    //   startActivity(intent);
                                }
                            }
                        }
                    }
                });
            } else {

                setNumberConfirmed();

            }
        }*/
    }


    public void setNumberConfirmed (){



        spinner.setVisibility(View.INVISIBLE);
        mobileEditText.setVisibility(View.INVISIBLE);
        numberConfirmation.setVisibility(View.VISIBLE);
        signUpButton.setText("Confirm Number?");
        numberConfirmation.setText(username);
        editButton1.setVisibility(View.VISIBLE);
        numberConfirmed = "t";

    }





    public void editButton (View view){

        spinner.setVisibility(View.VISIBLE);
        mobileEditText.setVisibility(View.VISIBLE);
        numberConfirmation.setVisibility(View.INVISIBLE);
        signUpButton.setText("Login");
        mobileEditText.setText(number);
        editButton1.setVisibility(View.INVISIBLE);
        numberConfirmed = "e";

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {

                if (ParseUser.getCurrentUser() != null) {


                    userType = Objects.requireNonNull(ParseUser.getCurrentUser().getString("userType"));
                    switch (userType) {


                        case "c":


                            intent = new Intent(getApplicationContext(), CustomerMainActivity.class);


                            startActivity(intent);

                            break;


                        case "d":

                            break;

                        case "m":
                            intent = new Intent(getApplicationContext(), PasswordActivity.class);

                            // intent.putExtra("mobileNumber", username);
                            intent.putExtra("userType", userType);
                            startActivity(intent);

                            break;

                        default:

                            break;

                    }
                } else {


                    ParseAnalytics.trackAppOpenedInBackground(getIntent());


                    if (savedInstanceState == null) {
                        Bundle extras = getIntent().getExtras();

                        if (extras == null) {

                            cameFromActivity = "";
                        } else {

                            cameFromActivity = extras.getString("activityName");
                        }
                    }


                    // final List<String> usernames = new ArrayList<>();

                    ConstraintLayout backgroundReleativeLayout = findViewById(R.id.backgroundConstraintLayout);

                    backgroundReleativeLayout.setOnClickListener(this);

                    mobileEditText = findViewById(R.id.mobileEditText);

                    signUpButton = findViewById(R.id.button2);

                    numberConfirmation = findViewById(R.id.numberConfirmation);

                    editButton1 = findViewById(R.id.editButton1);

                    spinner = findViewById(R.id.spinner);

                    message = findViewById(R.id.textView17);

                    userNames = new ArrayList<>();

                    TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

                    String countryCodeValue = tm.getNetworkCountryIso();

                    myString = countryCodeValue + " - " + Iso2phone.getPhone(countryCodeValue);  //the value you want the position for

                    ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            getResources().getStringArray(R.array.DialingCountryCode));
                    spinner.setAdapter(spinnerCountShoesArrayAdapter);

                    spinner.setOnItemSelectedListener(this);

                    //let spinner stops on user country code
                    int spinnerPosition = spinnerCountShoesArrayAdapter.getPosition(myString);
                    // set the default according to value
                    spinner.setSelection(spinnerPosition);

                    Log.d("that2 : ", callingCode);

                    callingC = Arrays.asList(myString.split(" - "));

                    callingCode = callingC.get(1);
                }


            } else {
                //do something, net is not connected
                intent = new Intent(getApplicationContext(), InternetFailActivity.class);

                intent.putExtra("activityName", this.getClass().getSimpleName());

                startActivity(intent);
            }
        } catch (InterruptedException | IOException e) {

            e.printStackTrace();
        }

        getSupportActionBar().hide(); //hide the title bar




    }




    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        String c = parent.getItemAtPosition(pos).toString();
        callingC =  Arrays.asList(c.split(" - "));

        callingCode = callingC.get(1);

        Log.d("thoooose: ", callingCode);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

