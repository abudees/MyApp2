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


            IsNetworkAvailable isNetworkAvailable = new IsNetworkAvailable();
            if (isNetworkAvailable.isNetwork()) {
                //internet is connected do something

                intent =  new Intent(this, MainActivity.class);
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







    }
}
