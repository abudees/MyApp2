package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendorActivity extends AppCompatActivity {



    ListView vendorOrdersList;
    ArrayList<String> vOrders = new ArrayList<>();
    ArrayList<Integer> orderNo = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    TextView textView;

    Intent intent;



    public void goToInventory(View view){

        Intent intent = new Intent(getApplicationContext(), VendorActivity.class);

        startActivity(intent);
    }






    public void logout (View view){

        ParseUser.logOut();

        intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        getSupportActionBar().hide(); //hide the title bar

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                textView = findViewById(R.id.textView3);

                textView.setText(ParseUser.getCurrentUser().getString("name") + " Orders");


                vendorOrdersList = findViewById(R.id.venOrdersList);

                arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vOrders);

                vOrders.clear();

                vOrders.add("Getting orders...");

                vendorOrdersList.setAdapter(arrayAdapter);

                vendorOrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                          intent = new Intent(getApplicationContext(), PreparingCheckListActivity.class);


                          startActivity(intent);
                    }
                });


                vendorOrdersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);


                android.text.format.DateFormat df = new android.text.format.DateFormat();
                df.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Orders");

                // change to loggedin vendor
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
               // query.whereEqualTo("prepared", false);

                query.orderByAscending("orderItemId");

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null) {

                            vOrders.clear();
                            orderNo.clear();

                            if (objects.size() > 0) {

                                for (ParseObject object : objects) {



                                    vOrders.add(object.getInt("orderNo")
                                            + " - " + DateFormat.getDateInstance(DateFormat.SHORT).format(object.getDate("deliveryDate"))
                                            + " - " + object.getString("status")
                                            + " - " + object.getInt("totalQty"));

                                    orderNo.add(object.getInt("orderId"));
                                }
                            } else {

                                vOrders.add("No active orders ");
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

// preparer activity

/*
ListView preparerOrdersList;
    ArrayList<String> orders = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    ArrayList<String> usernames = new ArrayList<String>();

    int orderSelected;
    String user;


    public void updateListView() {


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparer);





        setTitle("Your Orders");

        preparerOrdersList = findViewById(R.id.ordersListView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, orders);

        orders.clear();

        orders.add("Getting orders...");

        preparerOrdersList.setAdapter(arrayAdapter);

        preparerOrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(getApplicationContext(), PreparingCheckListActivity.class);

                intent.putExtra("orderSelected", orderSelected= i);

                intent.putExtra("username", usernames.get(i));

                startActivity(intent);

                Log.i("order selected", String.valueOf(orderSelected));



            }
        });




        ParseQuery<ParseObject> query = ParseQuery.getQuery("Orders");


        //query.whereEqualTo("vendorId", 1);




        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    orders.clear();

                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {


                            orders.add(object.getInt("orderId")
                                    +" - "+ object.getDate("confirmationDate")
                                    +" - "+ object.getString("username"));

                        }

                    } else {

                        orders.add("No active orders ");
                    }

                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

    }
 */