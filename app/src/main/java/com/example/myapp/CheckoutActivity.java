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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {




    private SqliteDatabase mDatabase;

    RecyclerView cartView;

    LinearLayoutManager linearLayoutManager;

    ProductCartAdapter mAdapter;

    Button btnAdd;

    public ArrayList<Integer> orderItems ;

    final List<Integer> price = new ArrayList<>();

    List<String> url = new ArrayList<>();

    final ArrayList<String> size = new ArrayList<>();

    List<String> productTitle = new ArrayList<>();

    ArrayList<Integer>  pIDs ;

    ArrayList<Integer> qty;


    ArrayList<Products> allProducts;






    public void createOrder (View view){
    }




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

                cartView = findViewById(R.id.myCartList);
                linearLayoutManager = new LinearLayoutManager(this);
                cartView.setLayoutManager(linearLayoutManager);
                cartView.setHasFixedSize(true);



                pIDs = mDatabase.listProducts();
                url = new ArrayList<>();

               // productTitle = new ArrayList<>();

              /*  btnAdd = findViewById(R.id.btnAdd);
                btnAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //addTaskDialog();

                        mAdapter.notifyDataSetChanged();
                    }
                });


*/



                Log.d("products in cart are: ",  String.valueOf(pIDs));

                for (int i = 0; i < pIDs.size(); i++) {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");

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
                                mAdapter = new ProductCartAdapter(CheckoutActivity.this, url, productTitle, allProducts, price, qty);
                                cartView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                            } else {

                                cartView.setVisibility(View.GONE);
                                Toast.makeText(CheckoutActivity.this, "There is no contact in the database. Start adding now",
                                        Toast.LENGTH_LONG).show();
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
