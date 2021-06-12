package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class MainActivity extends AppCompatActivity  {


    TextView textCartItemCount;

    ImageView cartBadgeIcon;

    String area;

    Intent intent;

    TextView login , logout,  welcomeText;

    Spinner spinner;

    ArrayList<String> areaList;

    ArrayAdapter<String> adapter;

    String welcomeMessage = "Welcome Guest";

    private SqliteDatabase mDatabase;

    int mCartItemCount;



    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    public void logout (View view){

        ParseUser.logOut();

        logout.setVisibility(View.INVISIBLE);

        login.setVisibility(View.VISIBLE);

    }

    public void redirect (View view) {

        if (spinner.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Please select your Area!", Toast.LENGTH_LONG).show();

        } else {

            intent = new Intent(getApplicationContext(), CategoriesActivity.class);

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

    public void areaList () {

        areaList = new ArrayList<>();

        areaList.add("Please Select your Area");

        ParseQuery<ParseObject> query = new ParseQuery<>("Area");

        query.whereEqualTo("status", true);

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






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinner = findViewById(R.id.areaSelect);
        login = findViewById(R.id.redirectToLogin);
        logout = findViewById(R.id.logout1);
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText(welcomeMessage);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {


                mDatabase = new SqliteDatabase(this);

                areaList();









                Map<String, String> dimensions = new HashMap<String, String>();
                // Define ranges to bucket data points into meaningful segments
                dimensions.put("area", "omdur");

                // Send the dimensions to Parse along with the event
                ParseAnalytics.trackEventInBackground("myEventName", dimensions);













                if (ParseUser.getCurrentUser() != null) {

                    welcomeText.setVisibility(View.VISIBLE);

                    welcomeMessage = "Welcome " + ParseUser.getCurrentUser().getString("name");

                    welcomeText.setText(welcomeMessage);

                    login.setVisibility(View.INVISIBLE);

                    logout.setVisibility(View.VISIBLE);

                    areaList();

                } else {

                    logout.setVisibility(View.INVISIBLE);
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

        StrictMode.ThreadPolicy st = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(st);
    }

    class mySpinnerListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView parent, View v, int position, long id) {

            // TODO Auto-generated method stub
            area = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();

        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        cartBadgeIcon = actionView.findViewById(R.id.cartBadgeIcon);

        cartBadgeIcon.setVisibility(View.GONE);

        textCartItemCount.setVisibility(View.GONE);

        mCartItemCount = mDatabase.listAll().size();

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(getApplicationContext(), CheckoutActivity.class);

                intent.putExtra("cameFromActivity", this.getClass().getSimpleName());

                startActivity(intent);
            }
        });

        return true;
    }



    @SuppressLint("NonConstantResourceId")
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

                intent.putExtra("cameFromActivity", this.getClass().getSimpleName());

                startActivity(intent);

                break;

            case R.id.signOutInMenu:

                ParseUser.logOut();

                logout.setVisibility(View.INVISIBLE);

                login.setVisibility(View.VISIBLE);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (mDatabase.listAll().size() > 0) {

            cartBadgeIcon.setVisibility(View.VISIBLE);

            textCartItemCount.setVisibility(View.VISIBLE);

            int sum = 0;

            for (int i = 0; i < mDatabase.listAll().size(); i++)
                sum += mDatabase.listQty().get(i);
            textCartItemCount.setText(String.valueOf(Math.min(sum, 99)));

        } else {
            cartBadgeIcon.setVisibility(View.GONE);
            textCartItemCount.setVisibility(View.GONE);
        }
    }
}
