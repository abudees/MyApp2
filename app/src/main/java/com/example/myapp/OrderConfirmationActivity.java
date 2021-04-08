package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
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

    public void pay () {
        Intent in = new Intent(OrderConfirmationActivity.this, CheckoutActivity.class);
        in.putExtra("pt_merchant_email", "merchant@myapp.com"); //this a demo account for testing the sdk
        in.putExtra("pt_secret_key",
                "oIUhj8mssa9rTWRAqHg4P9ECOcfs35lsOgJ7p6ARgJjaFbK6S1aIbOlZ1As5GNxu4hCtnclEWEOCPzIIBSrMGMMImeN22kx6C9zZ");//Add your Secret Key Here
        in.putExtra("pt_transaction_title", "Mr. John Doe");
        in.putExtra("pt_amount", "1");
        in.putExtra("pt_currency_code", "USD"); //Use Standard 3 character ISO
        in.putExtra("pt_shared_prefs_name", "myapp_shared"); // write a name of the shared folder between your App and PayTabs SDK
        in.putExtra("pt_customer_email", "test@example.com");
        in.putExtra("pt_customer_phone_number", "0097300001");
        in.putExtra("pt_order_id", "1234567");
        in.putExtra("pt_product_name", "Samsung Galaxy S6");
        in.putExtra("pt_timeout_in_seconds", "300"); //Optional

        // Billing Address
        in.putExtra("pt_address_billing", "Flat 1,Building 123, Road 2345");
        in.putExtra("pt_city_billing", "Juffair");
        in.putExtra("pt_state_billing", "Manama");
        in.putExtra("pt_country_billing", "Bahrain");
        in.putExtra("pt_postal_code_billing", "00973"); //Put Country Phone code if Postal code not available '00973'//

        // Shipping Address
        in.putExtra("pt_address_shipping", "Flat 1,Building 123, Road 2345");
        in.putExtra("pt_city_shipping", "Juffair");
        in.putExtra("pt_state_shipping", "Manama");
        in.putExtra("pt_country_shipping", "Bahrain");
        in.putExtra("pt_postal_code_shipping", "00973"); //Put Country Phone code if Postalcode not available '00973'
        int requestCode = 0;
        startActivityForResult(in, requestCode);
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
    }