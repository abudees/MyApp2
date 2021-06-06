package com.example.myapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class OrderConfirmationActivity extends AppCompatActivity {




   // List<String> callingC ;

    Date myDate;

    ParseGeoPoint location;

    int minOrderDate = 0;

    SqliteDatabase mDatabase ;

    Intent intent;

    TextView pickDate, dateSelected;

    EditText recipentName, recipentMobile;

    DatePickerDialog datePicker;

    // initialising the calender
    final Calendar calendar = Calendar.getInstance();

    // initialising the layout
    //  editText = findViewById(R.id.editext);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);


    String area ;


    View guideLine;


    String cameFromActivity;



    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public void clearsharedPreferences (){

      //  SharedPreferences.Editor.clear().commit();
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }






    public void createOrder (View view){



    }




  /*  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            Log.d("hooo", "" + i2 + "/" + (i1 + 1) + "/" + i);
        }
    };
*/

      /*  @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            Log.d("date", dayOfMonth + "/" + monthOfYear + "/" + year);
        }

    };

*/



    @RequiresApi(api = Build.VERSION_CODES.N)



    public void showDatePickerDialog(View v) {



        // initialising the datepickerdialog
        datePicker = new DatePickerDialog(OrderConfirmationActivity.this);


        datePicker = new DatePickerDialog(OrderConfirmationActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                // adding the selected date in the edittext


                Log.d("kkkk", dayOfMonth + "/" + (month + 1) + "/" + year);

                dateSelected.setVisibility(View.VISIBLE);
                dateSelected.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);




        Log.d("gggg", String.valueOf(calendar.getTime() ));


        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis()+(86400000 * minOrderDate));

        // show the dialog
        datePicker.show();


    }




    public void selectAddress (View view){



        intent = new Intent(getApplicationContext(), MapsActivity.class);

        intent.putExtra("area", area);

        startActivity(intent);

    }


    public void confirmRecipentName (View view){

        if (recipentName.getText() != null) {


            recipentName.setTextColor(Color.parseColor("#669933"));
            recipentName.setTypeface(recipentName.getTypeface(), Typeface.BOLD);

            recipentName.setBackgroundResource(android.R.color.transparent);
        }
    }


    public void confirmRecipentMobile (View view){

        if (recipentName.getText() != null) {


            recipentName.setTextColor(Color.parseColor("#669933"));
            recipentName.setTypeface(recipentName.getTypeface(), Typeface.BOLD);

            recipentName.setBackgroundResource(android.R.color.transparent);
        }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

   //     clearsharedPreferences();


        try {


            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {


                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();

                    if (extras == null) {

                        cameFromActivity ="";
                    } else {

                        cameFromActivity = extras.getString("cameFromActivity");
                    }
                }




                ParseAnalytics.trackAppOpenedInBackground(getIntent());

                mDatabase = new SqliteDatabase(this);


               //   cartProducts = mDatabase.listProducts();

                 // cartQty = mDatabase.listQty();


                myDate = new Date();

                location = new ParseGeoPoint(40.0, -30.0);


                pickDate = findViewById(R.id.textView11);
                dateSelected = findViewById(R.id.textView12);

                recipentName = findViewById(R.id.recipentName);
                recipentMobile = findViewById(R.id.recipentName);
                recipentName = findViewById(R.id.recipentName);
                recipentName = findViewById(R.id.recipentName);


                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();

                    if (extras == null) {

                        area = "riyadh";

                    } else {

                        area = extras.getString("area");
                    }
                }

                //        mobile = Integer.parseInt(Objects.requireNonNull(ParseUser.getCurrentUser().getString("mobileNumber")));


                //put 11112222 + user mobile (as mask)+ serial = order number

                //      int v = mobile + 11112222;

                // check previus orders and add 1 to last 1

                //    int lastOrderNo = v + 3;

                //  int newOrderNo = lastOrderNo + 1;

                //     Log.d("numver: ", String.valueOf(newOrderNo));


/*
                findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
                    }
                });
*/


                 guideLine =  findViewById(R.id.guideline47);
                 ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();



               //  params.guidePercent = 0.77f; // 45% // range: 0 <-> 1
              //  guideLine.setLayoutParams(params);



            }

        } catch (InterruptedException | IOException e) {
                e.printStackTrace();

        }
    }
}