package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity  {




   // Boolean englishLanguageActive = true;

    TextView textCartItemCount;

    String area;

    Intent intent;

    TextView languageTextView, login , logout,  welcomeText;

   // Button redirectButton, retry ;

    Spinner spinner;

    ArrayList<String> areaList;

    ArrayAdapter<String> adapter;

   // String name = "Guest";

    String welcomeMessage = "Welcome Guest";

    String userType;

    private SqliteDatabase mDatabase;

    int mCartItemCount;

    private MenuItem sigInMenu;
    private MenuItem signoutMenu;


    String  apiKey, token, url, url2;









    public void logout (View view){

        ParseUser.logOut();

        logout.setVisibility(View.INVISIBLE);

        login.setVisibility(View.VISIBLE);

        setupBadge();
    }







    public void redirect (View view) {


        if (spinner.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Please select your Area!", Toast.LENGTH_LONG).show();

        } else {

            intent = new Intent(getApplicationContext(), OTPActivity.class);

            Log.d("area", area);
          //  intent.putExtra("area", area);
            intent.putExtra("area",area);

            startActivity(intent);
        }
    }










    // redirect to Login Page if clicked on Login text
    public void login (View view){

        intent = new Intent(getApplicationContext(), LoginActivity.class);

        intent.putExtra("cameFromActivity", "MainActivity");

        startActivity(intent);
    }


    public void loginMode(){


    }



    public void areaList () {

        areaList = new ArrayList<>();
        areaList.add("Please Select your Area");

        ParseQuery<ParseObject> query = new ParseQuery<>("Area");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        areaList.add(object.getString("areaName"));
                    }

                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                            areaList);

                    // Apply the adapter to the spinner
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new mySpinnerListener());
                }
            }
        });
    }



    public void verify (View view){


        ParseQuery<ParseObject> query = new ParseQuery<>("APIs");

        query.whereEqualTo("name", "sms");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        apiKey = object.getString("accountSID");
                        token = object.getString("authToken");
                        url = object.getString("url");
                        url2 =object.getString("url2");
                    }

                    intent = new Intent(getApplicationContext(), VerifyActivity.class);

                    intent.putExtra("key", apiKey);
                    intent.putExtra("token", token);
                    intent.putExtra("url", url);
                    intent.putExtra("url2", url2);

                    startActivity(intent);

                }
            }
        });

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



       // getSupportActionBar().hide(); //hide the title bar

        languageTextView = findViewById(R.id.languageTextView);
        spinner = findViewById(R.id.areaSelect);

       // redirectButton = findViewById(R.id.redirect);
        login = findViewById(R.id.redirectToLogin);
        logout = findViewById(R.id.logout1);
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText(welcomeMessage);




        try {

            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                ParseAnalytics.trackAppOpenedInBackground(getIntent());

                mDatabase = new SqliteDatabase(this);


                Log.d("cart has", String.valueOf(mDatabase.listProducts()));

                setupBadge();




                if (ParseUser.getCurrentUser() != null) {


                    ParseQuery<ParseUser> query = ParseUser.getQuery();

                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null && objects.size() > 0) {
                                // The query was successful.
                                for (ParseUser object : objects) {

                                    userType = object.getString("userType");
                                }
                            }


                            switch (userType) {
                                case "c":
                                    Log.d("haa", userType);

                                    break;
                                case "m":
                                    Log.d("haa", userType);
                                    intent = new Intent(getApplicationContext(), VendorActivity.class);
                                    startActivity(intent);
                                    break;
                                case "d":
                                    Log.d("haa", userType);
                                    break;
                                default:

                                    break;
                            }

                        }
                    });

                    welcomeText.setVisibility(View.VISIBLE);

                    welcomeMessage = "Welcome " + ParseUser.getCurrentUser().getString("name");

                    welcomeText.setText(welcomeMessage);

                    login.setVisibility(View.INVISIBLE);

                    logout.setVisibility(View.VISIBLE);

                    areaList();

                } else {

                    logout.setVisibility(View.INVISIBLE);



                    areaList();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();

        sigInMenu = menu.findItem(R.id.signInMenu);
        signoutMenu = menu.findItem(R.id.signOutInMenu);
       // View signInActionView = menuSignIn.getActionView();

       // View signoutView = menuSignOut.getActionView();

        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        mCartItemCount = mDatabase.listAll().size();
        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                intent.putExtra("cameFromActivity", "MainActivity");

                startActivity(intent);

            }
        });

        if(ParseUser.getCurrentUser() != null) {



            MenuItem item = menu.findItem(R.id.signInMenu);
            item.setVisible(false);//
            MenuItem item1 = menu.findItem(R.id.signOutInMenu);
            item1.setVisible(true);


        } else {

            MenuItem item = menu.findItem(R.id.signInMenu);
            item.setVisible(true);//
            MenuItem item1 = menu.findItem(R.id.signOutInMenu);
            item1.setVisible(false);


        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {


            case R.id.signInMenu:
                // do something

                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("cameFromActivity", "MainActivity");

                startActivity(intent);
                break;

            case R.id.signOutInMenu:

                ParseUser.logOut();

                logout.setVisibility(View.INVISIBLE);

                login.setVisibility(View.VISIBLE);

                signoutMenu.setVisible(false);
                // show the menu item
                sigInMenu.setVisible(true);


                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}



