package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {


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


                ParseAnalytics.trackAppOpenedInBackground(getIntent());
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
                query.whereEqualTo("catId", categoryNumber);
                query.orderByAscending("productNo");
                query.whereEqualTo("status", true);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (ParseObject object : objects) {

                                url.add(object.getString("imageURL"));
                                title.add(object.getString("title"));

                                if (currency.matches("SDG")) {

                                    Log.i("customer in sudan", "cstomer in sudan");

                                    price.add(object.getInt("price"));
                                } else {
                                    price.add(object.getInt("price") * object.getInt("rate"));
                                    Log.i("customer in sudan", "not in sudan");
                                }
                                ptoductId.add(object.getInt("productId"));
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
}

