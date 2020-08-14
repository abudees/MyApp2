package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PreparingCheckListActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    ListView preparingList;

    ArrayList<String> orderItems = new ArrayList<String>();

    ArrayAdapter arrayAdapter;

    final int orderNo =0;

    Intent intent;

    ImageView imageView;







    public void allChecked(View view){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);




            //change status to prepared

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Order_items");

            query.whereEqualTo("orderId", orderNo);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {


                    if (e == null && objects.size() > 0) {


                        for (ParseObject object : objects) {

                        }
                    }
                }
            });
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing_check_list);



        intent = getIntent();
        final int orderNo = intent.getIntExtra("orderNo", 0);

        setTitle("Order no: "+ orderNo + "رقم الطلب: " );


        preparingList = findViewById(R.id.preparingList);

        imageView = findViewById(R.id.imageView2);



        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, orderItems);

        orderItems.clear();

        orderItems.add("Getting order details...");

        preparingList.setAdapter(arrayAdapter);


        preparingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order_items");

        query.whereEqualTo("orderId", orderNo);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    orderItems.clear();

                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {

                            orderItems.add(object.getInt("productId")
                                    +" - "+ object.getString("title")
                                    +"  -  "+ object.getInt("Qty") + "pcs"
                                    +" x " + "SDG"+ object.getInt("price")
                                    + " = "+ " SDG" +(object.getInt("Qty") * object.getInt("price")));

                        }

                    } else {

                        orderItems.add("No active orders ");
                    }

                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

