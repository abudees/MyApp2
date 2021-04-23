package com.example.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.common.detector.MathUtils;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Math.min;

public class CheckoutActivity extends AppCompatActivity {



    Location someLocation;

    List<Location> locations;

    private SqliteDatabase mDatabase;

    RecyclerView cartView;

    LinearLayoutManager linearLayoutManager;

    RecyclerViewWithFooterAdapter mAdapter;

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

    Location a ;

    ArrayList<Integer> vendorsLocations ;
    ArrayList<Integer> distance ;

    String cameFromActivity ="";

    Intent intent;

    int productSelected;

    TextView currentQty;

    int categoryNumber;











    public double countTotal (List<Integer> pID, List<Integer> qty){

        double total =0;
        for(int i = 0; i < pID.size(); i++){

            total += (pID.get(i) * qty.get(i));
        }
        return total;
    }









   // double subTotal = 0;

    int sum;



    public void addItemToCart(View view) {

        try {
            mDatabase = new SqliteDatabase(this);



            if (mDatabase.checkProduct(productSelected)) {

                // Log.i("price  ", String.valueOf(price));

                mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) + 1);



                Toast.makeText(this, "added twice", Toast.LENGTH_LONG).show();

            } else {

                Products newProduct = new Products(productSelected);

                mDatabase.addProduct(newProduct);
                currentQty.setText(String.valueOf(1));


                Toast.makeText(this, "added", Toast.LENGTH_LONG).show();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public void removeItem(View view) {

        try {

            if (mDatabase.checkProduct(productSelected)) {

                if (mDatabase.getQty(productSelected) == 1) {

                    mDatabase.deleteProduct(productSelected);

                    currentQty.setText(0);

                } else if (mDatabase.getQty(productSelected) > 1) {

                    mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) - 1);

                    currentQty.setText(String.valueOf(mDatabase.getQty(productSelected)));

                    Toast.makeText(this, mDatabase.getQty(productSelected), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, ("Item selected not in cart " + mDatabase.getQty(productSelected)), Toast.LENGTH_LONG).show();
            }
        } catch (Exception error) {

            error.printStackTrace();

        }
    }


    public void continueShopping (View view){

        switch (Objects.requireNonNull(cameFromActivity)) {

            case "ProductsActivity":

                intent = new Intent(getApplicationContext(), ProductsActivity.class);
                intent.putExtra("categoryNumber", categoryNumber);

                startActivity(intent);


                break;
            case "MainActivity":

                intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                break;


            case "CategoriesActivity":

                intent = new Intent(getApplicationContext(), CategoriesActivity.class);
                startActivity(intent);
                break;

            default:
                Toast.makeText(CheckoutActivity.this, "logging in ", Toast.LENGTH_SHORT).show();

                break;


        }
    }


    public void checkOut (View view){
        if (ParseUser.getCurrentUser() != null) {

            intent = new Intent(getApplicationContext(), RecipentsDetailsActivity.class);

            startActivity(intent);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);






        mDatabase = new SqliteDatabase(this);
        allProducts = mDatabase.listAll();
        pIDs = mDatabase.listProductIds();
        qty = mDatabase.listQty();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                categoryNumber = 0;
            } else {
                categoryNumber = extras.getInt("categoryNumber");
            }
        } else {
            categoryNumber = (int) savedInstanceState.getSerializable("categoryNumber");
        }







        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                ParseAnalytics.trackAppOpenedInBackground(getIntent());

                linearLayoutManager = new LinearLayoutManager(this);
                cartView = findViewById(R.id.myCartList);
                cartView.setLayoutManager(linearLayoutManager);
                cartView.setHasFixedSize(true);
                cartView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                totalText = findViewById(R.id.textView2);


                url = new ArrayList<>();
                productTitle = new ArrayList<>();


                vendorsLocations = new ArrayList<>();

                distance = new ArrayList<>();

                cameFromActivity = getIntent().getStringExtra("cameFromActivity");

             //   z = "";

                /*


                int vendorLoc1 = 1;
                int vendorLoc2 = 2;
                int vendorLoc3 = 11;
                int vendorLoc4 = 7;
                int vendorLoc5 = 5;
                int vendorLoc6 = 9;
                int vendorLoc7 = 8;
                int vendorLoc8 = 6;

                vendorsLocations.add(vendorLoc1);
                vendorsLocations.add(vendorLoc2);
                vendorsLocations.add(vendorLoc3);
                vendorsLocations.add(vendorLoc4);
                vendorsLocations.add(vendorLoc5);
                vendorsLocations.add(vendorLoc6);
                vendorsLocations.add(vendorLoc7);
                vendorsLocations.add(vendorLoc8);

                for (int i = 0; i < vendorsLocations.size(); i++) {

                    int r = 17;

                    distance.add(r-vendorsLocations.get(i));

                }
             //   Collections.sort(distance);

               //  calculateDistance(15.556500, 32.582611, 15.577144, 32.548822));













                final Location deliveryLocation = someLocation;

                Collections.sort(locations, new Comparator<Location>() {
                    @Override
                    public int compare(Location o1, Location o2) {
                        Float dist1 = o1.distanceTo(deliveryLocation);
                        Float dist2 = o2.distanceTo(deliveryLocation);
                        return dist1.compareTo(dist2);
                    }
                });*/


                String area = "";

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    area = extras.getString("area");
                }

                Log.d("area5: ", area);


                Log.d("products in cart are: ", String.valueOf(pIDs));

                Log.d("distance: ", String.valueOf(locations));

                for (int i = 0; i < pIDs.size(); i++) {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");

                    query.orderByAscending("productNo");


                    query.whereEqualTo("productNo", pIDs.get(i));

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
                                mAdapter = new RecyclerViewWithFooterAdapter(CheckoutActivity.this, pIDs, url, productTitle, allProducts, price, qty);
                                cartView.setAdapter(mAdapter);

                                // int all = mDatabase.sumPriceCartItems(price);

                                for (int g = 0; g < price.size(); g++) {

                                    sum += price.get(g) * mDatabase.getQty(pIDs.get(g));
                                }

                                totals.add(sum);

                                Log.d("totals: ", String.valueOf(totals.get(totals.size() - 1)));


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
