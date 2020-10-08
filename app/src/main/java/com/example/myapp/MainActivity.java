package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {





   // Boolean englishLanguageActive = true;

    String area;

    Intent intent;

    TextView languageTextView, redirectToLogin , logout, noConnection ;

    Button redirectButton, retry ;

    Spinner spinner;

    ArrayList<String> areaList;

    ArrayAdapter adapter;







    public void logout (View view){

        ParseUser.logOut();

        logout.setVisibility(View.INVISIBLE);

        redirectToLogin.setVisibility(View.VISIBLE);
    }






    public void redirect (View view) {


        if (area.matches("Please select your Area")) {

            Toast.makeText(this, "Please select your Area!", Toast.LENGTH_LONG).show();

        } else {


            intent = new Intent(getApplicationContext(), CategoriesActivity.class);

            intent.putExtra("area", area);

            startActivity(intent);

        }
    }


    public void login (View view){

        intent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(intent);
    }






/*
    @Override
    public void onClick(View view) {

        if ( view.getId() == R.id.languageTextView) {

            if (englishLanguageActive) {

                englishLanguageActive = false;
                languageTextView.setText("English");
                redirectButton.setText("اهلا");
                redirectToLogin.setText("تسجيل الدخول");

            } else {

                englishLanguageActive = true;
                languageTextView.setText("عربي");
                redirectButton.setText("Welcome");
                redirectToLogin.setText("Login");
            }
        }
    }
    */












    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); //hide the title bar

        ParseAnalytics.trackAppOpenedInBackground(getIntent());



        try {

            CheckConnection checkConnection = new CheckConnection();
            if (checkConnection.isNetworkAvailable()) {
                //internet is connected do something


                // spinner
                spinner = findViewById(R.id.areaSelect);

                areaList = new ArrayList<>();

                languageTextView = findViewById(R.id.languageTextView);

                redirectButton = findViewById(R.id.redirect);

                redirectToLogin = findViewById(R.id.redirectToLogin);

                logout = findViewById(R.id.logout1);


                if (ParseUser.getCurrentUser() != null) {

                    redirectToLogin.setVisibility(View.INVISIBLE);

                    logout.setVisibility(View.VISIBLE);

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

                        default:
                            throw new IllegalStateException("Unexpected value: " + ParseUser.getCurrentUser().getString("userType"));
                    }


                } else {

                    redirectToLogin.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.INVISIBLE);

                }


                ParseQuery<ParseObject> query = new ParseQuery<>("Area");

                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null) {

                            for (ParseObject object : objects) {

                                areaList.add(object.getString("areaName"));

                                // a = object.getString("areaName");
                            }


                            adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, areaList);

                            // Apply the adapter to the spinner
                            spinner.setAdapter(adapter);

                            spinner.setOnItemSelectedListener(new mySpinnerListener());


                        }

                    }
                });


            } else {
                //do something, net is not connected

                //do something, net is not connected



                intent = new Intent(getApplicationContext(), InternetFailActivity.class);

                intent.putExtra("activityName", this.getClass().getSimpleName());

                startActivity(intent);

            }
        } catch (InterruptedException | IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }


    class mySpinnerListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView parent, View v, int position, long id) {

            // TODO Auto-generated method stub
            area = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView parent) {

            // TODO Auto-generated method stub
            // Do nothing.
        }
    }
}


