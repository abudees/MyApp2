package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class InternetFailActivity extends AppCompatActivity {

    Intent intent;
    String activityN;


    public void retry (View view){

        try {

            CheckConnection checkConnection = new CheckConnection();
            if (checkConnection.isNetworkAvailable()) {
                //internet is connected do something
            } else {
                //do something, net is not connected

                intent =  new Intent(getApplicationContext(), InternetFailActivity.class);
                startActivity(intent);

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_fail);


        activityN = intent.getStringExtra("activityName");

        Toast.makeText(this, activityN, Toast.LENGTH_LONG).show();



    }
}
