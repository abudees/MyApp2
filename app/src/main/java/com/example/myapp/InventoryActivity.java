package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    ListView productsList;
    ArrayList<String> vProducts = new ArrayList<String>();
    ArrayList<Integer> productNo = new ArrayList<Integer>();
    ArrayAdapter arrayAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setTitle(ParseUser.getCurrentUser().getString("firstName") +" Products");



        productsList = findViewById(R.id.inventoryList);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vProducts);

        vProducts.clear();

        vProducts.add("Getting ...");

        productsList.setAdapter(arrayAdapter);

        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });



        productsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");

        // change to loggedin vendor
        query.whereEqualTo("vendorId", ParseUser.getCurrentUser().getInt("vendorId"));
        query.whereEqualTo("status", true);

        query.orderByAscending("productId");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    vProducts.clear();
                    productNo.clear();

                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {

                            vProducts.add( object.getInt("orderId")
                                    +" - "+ object.getDate("confirmationDate")
                                    +" - "+ object.getString("username"));

                            productNo.add( object.getInt("orderId"));
                        }
                    } else {

                        vProducts.add("No active orders ");
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });


    }
}
