package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class PreparerActivity extends AppCompatActivity {

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
}