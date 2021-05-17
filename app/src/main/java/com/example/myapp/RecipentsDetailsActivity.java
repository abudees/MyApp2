package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RecipentsDetailsActivity extends AppCompatActivity {

    EditText  recipientMobile;

    EditText recipientName;

    Intent intent;

    public void proceed (View view){

        intent = new Intent(getApplicationContext(), OrderConfirmationActivity.class);

        intent.putExtra("recipientName", recipientName.getText());
        intent.putExtra("recipientMobile", recipientMobile.getText());

        startActivity(intent);

    }

    public void setLocation (View view){


        intent = new Intent(getApplicationContext(), MapsActivity.class);

        intent.putExtra("recipientName", recipientName.getText());


        startActivity(intent);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipents_details);


        recipientName = findViewById(R.id.recipientName);
        recipientMobile = findViewById(R.id.recipientMobile);

        recipientMobile.getText();





    }
}
