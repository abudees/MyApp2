package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {





   // Boolean englishLanguageActive = true;

    String area;

    Intent intent;

    TextView languageTextView, login , logout, noConnection ;

    Button redirectButton, retry ;

    Spinner spinner;

    ArrayList<String> areaList;

    ArrayAdapter<String> adapter;

  //  String user;

    //Button button;






    public void logout (View view){

        ParseUser.logOut();

        logout.setVisibility(View.INVISIBLE);

        login.setVisibility(View.VISIBLE);
    }


    public void customer(){

        areaList = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>("Area");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        areaList.add(object.getString("AreaName"));




                     //   int z = Integer.parseInt(object.getObjectId());

                    //    Log.d("zzzzzzz: ", String.valueOf(z));


                    }



                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, areaList);

                    // Apply the adapter to the spinner
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new mySpinnerListener());
                }
            }
        });
    }






    public void redirect (View view) {

        if (ParseUser.getCurrentUser() != null) {

            login.setVisibility(View.INVISIBLE);

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

                case "c":
                case "":

                    customer();

                    if (area.matches("Please select your Area")) {

                        Toast.makeText(this, "Please select your Area!", Toast.LENGTH_LONG).show();

                    } else {

                        intent = new Intent(getApplicationContext(), CategoriesActivity.class);

                        intent.putExtra("area", area);

                        startActivity(intent);

                    }

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + ParseUser.getCurrentUser().getString("userType"));
            }
        } else {


            if (area.matches("Please select your Area")) {

                Toast.makeText(this, "Please select your Area!", Toast.LENGTH_LONG).show();

            } else {

                intent = new Intent(getApplicationContext(), CategoriesActivity.class);

                intent.putExtra("area", area);

                startActivity(intent);

            }
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
    





    public void incrementNumber(){
        int count = 1;
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        int defaultValue = getPreferences(MODE_PRIVATE).getInt("count_key",count);
        ++defaultValue;
        getPreferences(MODE_PRIVATE).edit().putInt("count_key",defaultValue).apply();
    }
*/







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); //hide the title bar

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        languageTextView = findViewById(R.id.languageTextView);
        spinner = findViewById(R.id.areaSelect);

        redirectButton = findViewById(R.id.redirect);

        login = findViewById(R.id.redirectToLogin);

        logout = findViewById(R.id.logout1);



        try {

            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {

                if (ParseUser.getCurrentUser() != null) {




                    int f = (int) ParseUser.getCurrentUser().getNumber("mobileNumber");
                    int v = f + 11112222;

                    Log.d("numver: ", String.valueOf(v) );

                    login.setVisibility(View.INVISIBLE);

                    switch (ParseUser.getCurrentUser().getString("userType")) {

                        case "Vendor":

                            break;

                        case "Driver":

                            break;

                        case "Manager":

                            break;

                        case "c":
                        case "":

                            customer();
                            break;

                        default:
                            throw new IllegalStateException("Unexpected value: " + ParseUser.getCurrentUser().getString("userType"));
                    }
                } else {

                  logout.setVisibility(View.INVISIBLE);

                  customer();

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


