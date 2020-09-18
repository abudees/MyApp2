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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity /*implements View.OnClickListener */{

    String area;

    Intent intent;

    Boolean englishLanguageActive = true;

    TextView languageTextView ;

    Button redirectButton, retry ;

    TextView redirectToLogin , logout, noConnection ;

    Spinner spinner, spinner1;





    public void logout (View view){

        ParseUser.logOut();

        logout.setVisibility(View.INVISIBLE);

        redirectToLogin.setVisibility(View.VISIBLE);
    }






    public void redirect (View view) {


        if (area.matches("Please select your Area")) {

         //   if (englishLanguageActive) {

                Toast.makeText(this, "Please select your Area!", Toast.LENGTH_LONG).show();
/*
            } else {

                Toast.makeText(this, "يرجى اختيار منطقةالتوصيل!", Toast.LENGTH_LONG).show();
            }

*/
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

    public void retryInternet (View view){

        finish();
        startActivity(getIntent());
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


    private boolean isNetworkAvailable() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;

       // ConnectivityManager connectivityManager
         //       = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); //hide the title bar





        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("49cd35a5b5f6610271ebdebb749464d7770ea2ad")
                .clientKey("36e7f81fcbe7caa26452001f6c6b31f6591f263c")
                .server("http://18.190.25.222:80/parse/")
                .build()
        );

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        noConnection = findViewById(R.id.textView4);

        // spinner
        spinner = findViewById(R.id.areaSelect);


        spinner1 = findViewById(R.id.spinner);


        final ArrayList<String> areaList = new ArrayList<>();

        languageTextView = findViewById(R.id.languageTextView);

        //languageTextView.setOnClickListener(this);

        redirectButton = findViewById(R.id.redirect);

        redirectToLogin = findViewById(R.id.redirectToLogin);

        logout = findViewById(R.id.logout1);

        retry = findViewById(R.id.button3);

        retry.setVisibility(View.INVISIBLE);





       // try {
         //   if (isNetworkAvailable()) {
                //internet is connected do something

        if (ParseUser.getCurrentUser() != null) {

            redirectToLogin.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.VISIBLE);


            Toast.makeText(this, ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();

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

            //  Toast.makeText(this, ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
        }


        ParseQuery<ParseObject> query = new ParseQuery<>("Area");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        areaList.add(object.getString("areaName"));
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, areaList);

                    // Apply the adapter to the spinner
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new mySpinnerListener());

                    ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, areaList);

                    spinner1.setAdapter(adapter1);

                    spinner1.setOnItemSelectedListener(new mySpinnerListener());

                }
            }
        });


        /*    } else {
                //do something, net is not connected

                //Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show();

                noConnection.setVisibility(View.VISIBLE);

                spinner.setVisibility(View.INVISIBLE);

                languageTextView.setVisibility(View.INVISIBLE);

                redirectButton.setVisibility(View.INVISIBLE);

                redirectToLogin.setVisibility(View.INVISIBLE);

                logout.setVisibility(View.INVISIBLE);

                retry.setVisibility(View.VISIBLE);

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }*/
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


