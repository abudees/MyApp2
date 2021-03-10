package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity {

    private ProductsAdapter adapter;

    private RecyclerView recyclerView;

    int categoryNumber;

    final List<String> url = new ArrayList<>();

    final List<String> title = new ArrayList<>();

    final List<Integer> price = new ArrayList<>();

    final List<Integer> ptoductId = new ArrayList<>();


    TextView textCartItemCount;

    private SqliteDatabase mDatabase;

    int mCartItemCount;

    Intent intent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);



      //  this.getSupportActionBar().hide();

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


        Log.i("categ is", String.valueOf(categoryNumber));
        //local price
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        String lang = Locale.getDefault().getDisplayLanguage();
        Locale locale = new Locale(lang, countryCode);
        final String currency = Currency.getInstance(locale).getCurrencyCode();


        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        try {



            IsNetworkAvailable checkConnection = new IsNetworkAvailable();



            if (checkConnection.isNetwork()) {

                ParseAnalytics.trackAppOpenedInBackground(getIntent());


                mDatabase = new SqliteDatabase(this);
                if (ParseUser.getCurrentUser() != null) {


                    setupBadge();

                }


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
                query.whereEqualTo("categoryNo", categoryNumber);
                query.orderByAscending("productNo");
              //  query.whereEqualTo("status", true);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (ParseObject object : objects) {

                                Log.d("ahaaa", String.valueOf(categoryNumber));

                                url.add(object.getString("imageURL"));
                                title.add(object.getString("title"));

                                if (currency.matches("SDG")) {

                                    Log.i("customer in sudan", "customer in sudan");

                                    price.add(object.getInt("price"));
                                } else {
                                    price.add(object.getInt("price") * object.getInt("rate"));
                                    Log.i("customer in sudan", "not in sudan");
                                }
                                ptoductId.add(object.getInt("productNo"));
                            }

                            GridLayoutManager mLayoutManager = new GridLayoutManager(ProductsActivity.this, 2);
                            adapter = new ProductsAdapter(ProductsActivity.this, url, title, price, ptoductId, currency);

                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(mLayoutManager);
                        }
                    }
                });
            }
        } catch (InterruptedException | IOException e) {
        e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);


        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();


        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        mCartItemCount = mDatabase.listAll().size();
        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_cart) {
            // Do something

            intent = new Intent(getApplicationContext(), CheckoutActivity.class);
            intent.putExtra("cameFromActivity", "ProductsActivity");
            intent.putExtra("categoryNumber",categoryNumber);

            startActivity(intent);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}