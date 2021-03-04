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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnKeyListener, AdapterView.OnItemSelectedListener {


    HttpResponse response ;
    HttpEntity entity;
    String ACCOUNT_SID ="ACc2050a7f1942814404b2e15d8f74f9f2";
    String AUTH_TOKEN= "85015ecada0bcbbcf39df344030c1348";

    HttpPost httppost;

    Boolean signUpModeActive = true;
    TextView changeSignUpTextView;
    EditText mobileEditText, nameEditText,  emailEditText;
    Button signUpButton;
    Intent intent;
    Spinner spinner;
    String  myString;
    String callingCode= "";

    List<String> callingC ;

    List<String> userNames ;

    String username ;

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



    //I dont know!
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

        username = callingCode + (mobileEditText.getText().toString());

        callingC = Arrays.asList(callingCode.split(" - "));

        if (mobileEditText.getText().toString().matches("") || !isValidMobile(mobileEditText.getText().toString())) {

            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();

        } else {


            intent = new Intent(getApplicationContext(), OrderConfirmationActivity.class);

            intent.putExtra("mobileNumber", username);

            startActivity(intent);

        }
    }

    /**
     *
     * @param datePicker
     * @return a java.util.Date
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide(); //hide the title bar

        spinner = findViewById(R.id.spinner);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {
                ParseAnalytics.trackAppOpenedInBackground(getIntent());

                if (ParseUser.getCurrentUser() != null) {


                    intent = new Intent(getApplicationContext(), OrderConfirmationActivity.class);

                    intent.putExtra("mobileNumber", username);

                    startActivity(intent);

                } else {
                    ConstraintLayout backgroundReleativeLayout = findViewById(R.id.backgroundConstraintLayout);

                    backgroundReleativeLayout.setOnClickListener(this);

                    mobileEditText = findViewById(R.id.mobileEditText);

                    signUpButton = findViewById(R.id.button2);

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
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }
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

