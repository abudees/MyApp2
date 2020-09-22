package com.example.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {




    private SqliteDatabase mDatabase;

    RecyclerView cartView;

    LinearLayoutManager linearLayoutManager;

    ProductCartAdapter mAdapter;

    Button btnAdd;






    public ArrayList<Integer> orderItems ;

    final ArrayList<Integer> price = new ArrayList<>();;

    final List<String> url = new ArrayList<>();

    final ArrayList<String> size = new ArrayList<>();

    final List<String> title = new ArrayList<>();

    final List<Integer> qty = new ArrayList<>();

    ArrayList<Products> allProducts;




    public void createOrder (View view){
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        //super.onResume();


        cartView = findViewById(R.id.myCartList);

        linearLayoutManager = new LinearLayoutManager(this);
        cartView.setLayoutManager(linearLayoutManager);
        cartView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allProducts = mDatabase.listProducts();


        if (allProducts.size() > 0) {


            cartView.setVisibility(View.VISIBLE);
            mAdapter = new ProductCartAdapter(CheckoutActivity.this, allProducts, url);
            cartView.setAdapter(mAdapter);






        } else {

            cartView.setVisibility(View.GONE);
            Toast.makeText(CheckoutActivity.this, "There is no contact in the database. Start adding now",
                    Toast.LENGTH_LONG).show();



        }





        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //addTaskDialog();
            }
        });




        try {
            final List<String> url = new ArrayList<>();

            final List<String> title = new ArrayList<>();


            ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");

            query.whereEqualTo("status", true);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null && objects.size() > 0) {

                        for (ParseObject object : objects) {

                            url.add(object.getString("imageURL"));

                            Log.i("url", object.getString("imageURL"));
                        }
                    }
                }
            });
        }

        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }


    }
}
