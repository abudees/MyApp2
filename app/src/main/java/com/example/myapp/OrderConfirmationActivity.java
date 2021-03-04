package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderConfirmationActivity extends AppCompatActivity {


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private TextView dateText;

    int orderNo;

    int mobile ;

    List<String> callingC ;

    Date myDate;

    ParseGeoPoint location;

    int totalQty = 2;

    SqliteDatabase mDatabase ;

    int[] cartProducts = new int[]{1,22};
    int[] cartQty = new int[]{5654,545};

    Button btnGet ;


    String g;

    public int y;
    public int m;
    public int d;




    public void clearsharedPreferences (){

      //  SharedPreferences.Editor.clear().commit();
    }



    public void createOrder(View view){

        if (ParseUser.getCurrentUser() != null) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Orders");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {



                        String username = ParseUser.getCurrentUser().getUsername();

                        username = username.replaceAll("\\D", "");
                        mobile = 0;

                        mobile = Integer.parseInt(username) ;

                        orderNo = mobile + 11112222  + objects.size();
                        //  if (cart.size() > 0) {

                        ParseObject newOrder = new ParseObject("Orders");
                        newOrder.put("orderNo", orderNo);
                        newOrder.put("username", ParseUser.getCurrentUser().getUsername());
                        newOrder.put("deliveryDate", myDate);
                        //  newOrder.put("total", );
                        newOrder.put("exchangeRate", 1);
                        newOrder.put("deliveryLocation", location);
                        newOrder.put("totalQty", mDatabase.listAll().size());
                        newOrder.put("status", "n");
                        newOrder.put("recipientMobile", 655656565);

                        //  placeOrder.put("message", 1337);
                        //   placeOrder.put("voucher", 1337);

                        newOrder.saveInBackground();


                        for (int i = 0; i < cartProducts.length; i++) {

                            ParseObject newOrderItem = new ParseObject("OrdersDetails");

                            int a = cartProducts[i];
                            int b = cartQty[i];
                            newOrderItem.put("orderNo", orderNo);
                            newOrderItem.put("productNo", a);
                            newOrderItem.put("qty", b);

                            newOrderItem.saveInBackground();
                        }

                    } else {

                        String username = ParseUser.getCurrentUser().getUsername();

                        username = username.replaceAll("\\D", "");
                        mobile = 0;

                        mobile = Integer.parseInt(username) + 11112222;

                        orderNo = mobile + 11112222;


                        //  if (cart.size() > 0) {


                        ParseObject newOrder = new ParseObject("Orders");
                        newOrder.put("orderNo", orderNo);
                        newOrder.put("username", ParseUser.getCurrentUser().getUsername());
                        newOrder.put("deliveryDate", myDate);
                        //  newOrder.put("total", );
                        newOrder.put("exchangeRate", 1);
                        newOrder.put("deliveryLocation", location);
                        newOrder.put("totalQty", mDatabase.listAll().size());
                        newOrder.put("status", "n");
                        newOrder.put("recipientMobile", 655656565);

                        //  placeOrder.put("message", 1337);
                        //   placeOrder.put("voucher", 1337);

                        newOrder.saveInBackground();


                        for (int i = 0; i < cartProducts.length; i++) {

                            ParseObject newOrderItem = new ParseObject("OrdersDetails");

                            int a = cartProducts[i];
                            int b = cartQty[i];
                            newOrderItem.put("orderNo", orderNo);
                            newOrderItem.put("productNo", a);
                            newOrderItem.put("qty", b);

                            newOrderItem.saveInBackground();
                        }

                    }
                }
            });






            // }
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

                ParseAnalytics.trackAppOpenedInBackground(getIntent());

                mDatabase = new SqliteDatabase(this);


               //   cartProducts = mDatabase.listProducts();

                 // cartQty = mDatabase.listQty();




                myDate = new Date();

                location = new ParseGeoPoint(40.0, -30.0);





                //        mobile = Integer.parseInt(Objects.requireNonNull(ParseUser.getCurrentUser().getString("mobileNumber")));


                //put 11112222 + user mobile (as mask)+ serial = order number

                //      int v = mobile + 11112222;

                // check previus orders and add 1 to last 1

                //    int lastOrderNo = v + 3;

                //  int newOrderNo = lastOrderNo + 1;

                //     Log.d("numver: ", String.valueOf(newOrderNo));

                dateText = findViewById(R.id.date_text);
/*
                findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
                    }
                });
*/

            }



            } catch (InterruptedException | IOException e) {
                e.printStackTrace();

            }
        }
/*    public void showDatePickerDialog () {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet (DatePicker view,int year, int month, int dayOfMonth){
        String date = "day/month/year: " + month + "/" + dayOfMonth + "/" + year;
        dateText.setText(date);
        Log.d("a", String.valueOf(dayOfMonth));
        Log.d("b", String.valueOf(myDate));
    }*/

    }