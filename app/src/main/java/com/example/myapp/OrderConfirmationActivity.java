package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

import java.util.Objects;

public class OrderConfirmationActivity extends AppCompatActivity {

    public void clearsharedPreferences (){

      //  SharedPreferences.Editor.clear().commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        clearsharedPreferences();

        Integer.parseInt(Objects.requireNonNull(ParseUser.getCurrentUser().getString("mobileNumber")));
        int mobile = 0;

        try {
            mobile = Integer.parseInt(Objects.requireNonNull(ParseUser.getCurrentUser().getString("mobileNumber")));
        } catch (NumberFormatException nfe) {
            Log.d("Could not parse ", String.valueOf(nfe));
        }


        //put 11112222 + user mobile (as mask)+ serial = order number

        int v = mobile + 11112222;

        // check previus orders and add 1 to last 1

        int lastOrderNo = v + 3;

        int newOrderNo = lastOrderNo + 1;

        Log.d("numver: ", String.valueOf(newOrderNo));
    }
}
