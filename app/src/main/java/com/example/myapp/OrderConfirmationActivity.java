package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class OrderConfirmationActivity extends AppCompatActivity {

    public void clearsharedPreferences (){

      //  SharedPreferences.Editor.clear().commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        clearsharedPreferences();
    }
}
