package com.example.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.detector.MathUtils;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.lang.Math.min;

public class CheckoutActivity extends AppCompatActivity {




    private SqliteDatabase mDatabase;

    RecyclerView cartView;

    LinearLayoutManager linearLayoutManager;

    ProductCartAdapter mAdapter;

    //Button btnAdd;

    public ArrayList<Integer> orderItems ;

     List<Integer> price = new ArrayList<>();

    List<String> url = new ArrayList<>();

   // List<Integer> stack = new ArrayList<Integer>();

  //  final ArrayList<String> size = new ArrayList<>();

    List<String> productTitle = new ArrayList<>();

    ArrayList<Integer>  pIDs ;

    ArrayList<Integer> qty;

  //  ArrayList<Integer> pId;

    ArrayList<Products> allProducts;

     ArrayList<Integer> totals = new ArrayList<>();


    //int total ;

    TextView totalText;







    public void placeOrder (View view){



    }

   // double subTotal = 0;


    int sum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        mDatabase = new SqliteDatabase(this);
        allProducts = mDatabase.listAll();
        pIDs = mDatabase.listProducts();
        qty = mDatabase.listQty();






        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                linearLayoutManager = new LinearLayoutManager(this);
                cartView = findViewById(R.id.myCartList);
                cartView.setLayoutManager(linearLayoutManager);
                cartView.setHasFixedSize(true);
                cartView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                totalText = findViewById(R.id.textView2);


                url = new ArrayList<>();
                productTitle = new ArrayList<>();





                Log.d("products in cart are: ", String.valueOf(pIDs));


                for (int i = 0; i < pIDs.size(); i++) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");

                query.orderByAscending("productId");


                query.whereEqualTo("productId", pIDs.get(i));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null && objects.size() > 0) {

                            for (ParseObject object : objects) {

                                url.add(object.getString("imageURL"));

                                productTitle.add(object.getString("title"));

                                price.add(object.getInt("price"));


                            }
                        }

                        if (allProducts.size() > 0) {


                            cartView.setVisibility(View.VISIBLE);
                            mAdapter = new ProductCartAdapter(CheckoutActivity.this, pIDs, url, productTitle, allProducts, price, qty);
                            cartView.setAdapter(mAdapter);

                            // int all = mDatabase.sumPriceCartItems(price);



                            for (int g = 0; g < price.size(); g++) {

                                sum += price.get(g) * mDatabase.getQty(pIDs.get(g));
                            }

                            totals.add(sum);

                            Log.d("totals: ",  String.valueOf(totals.get(totals.size()-1)));


                        } else {

                            cartView.setVisibility(View.GONE);


                            Toast.makeText(CheckoutActivity.this, "There is no contact in the database. Start adding now", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }




            }
        } catch (InterruptedException | IOException e) {

            e.printStackTrace();
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
