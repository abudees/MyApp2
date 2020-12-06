package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDetailsActivity extends  AppCompatActivity {


    DatePicker picker;
    Button btnGet;
    TextView tvw, countryCode;
    Intent intent;

    int area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();
                    if (extras == null) {
                        area = 0;
                    } else {
                        area = extras.getInt("area");
                    }
                } else {
                    area = (int) savedInstanceState.getSerializable("area");
                }

                countryCode = findViewById(R.id.textView33);


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Area");

                query.whereEqualTo("AreaNo", 1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null && objects.size() > 0) {

                            for (ParseObject object : objects) {

                                String countryCodeSign = "+" + String.valueOf(object.getInt("countryCode"));

                                countryCode.setText(countryCodeSign);

                            }

                        }
                    }
                });


            } else {
                //do something, net is not connected



                intent = new Intent(getApplicationContext(), InternetFailActivity.class);

                intent.putExtra("activityName", this.getClass().getSimpleName());

                startActivity(intent);


                tvw=(TextView)findViewById(R.id.textView1);
                picker=(DatePicker)findViewById(R.id.datePicker1);
                btnGet=(Button)findViewById(R.id.button1);
                btnGet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvw.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());
                    }
                });


            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }
    }
}
