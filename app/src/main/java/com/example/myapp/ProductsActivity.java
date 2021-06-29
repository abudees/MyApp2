package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ProductsActivity extends AppCompatActivity {


    private ProductsAdapter adapter;

    private RecyclerView recyclerView;

    int categoryNumber, mCartItemCount;

    final List<String> url = new ArrayList<>();

    final List<String> title = new ArrayList<>();

    final List<Integer> price = new ArrayList<>();
    final List<Integer> rate = new ArrayList<>();

    final List<Integer> sudanPrice = new ArrayList<>();

    final List<Integer> ptoductId = new ArrayList<>();

    TextView textCartItemCount, login, logout;

    private SqliteDatabase mDatabase;

    Intent intent;

    private MenuItem sigInMenu;
    private MenuItem signoutMenu;

    String area, currency;

    ImageView cartBadgeIcon;

    int exchange =0;

    Double protein;

    String yourCurruncy = "SAR";



    final ArrayList<String> apiKey = new ArrayList<>();
    final ArrayList<String> apiKeyURL = new ArrayList<>();





    public void updateRate(){



        ParseQuery<ParseObject> query = new ParseQuery<>("APIs");
        query.whereEqualTo("type", "currency");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        apiKey.add(object.getString("apiKey1"));
                        apiKeyURL.add(object.getString("url"));
                    }

                    try {

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpResponse response = httpclient.execute(new HttpGet(apiKeyURL.get(0) + apiKey.get(0)));
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            response.getEntity().writeTo(out);
                            String responseString = out.toString();

                            String[] separated = responseString.split(currency);

                            String textGot = separated[1].substring(3);

                            String[] separatedFinal = textGot.split("\"");


                            try {

                                protein = Double.parseDouble(separatedFinal[0]); // Make use of autoboxing.  It's also easier to read.


                            } catch (NumberFormatException err) {
                                System.out.println(err);
                            }


                            ParseObject rate = new ParseObject("CurrencyRate");
                            rate.put("currency", currency);
                            rate.put("exchangeRate", protein);
                            rate.saveInBackground();

                            out.close();

                            requestExchangeRate();

                        } else {
                            //Closes the connection.
                            response.getEntity().getContent().close();
                            throw new IOException(statusLine.getReasonPhrase());
                        }
                    } catch (Exception er) {
                        System.out.println(er);
                    }
                }

            }
        });


    }



    public void requestProducts(int eRate){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");

        query.whereEqualTo("categoryNo", categoryNumber);
        query.whereGreaterThanOrEqualTo("stock", 0.5);
        query.whereGreaterThanOrEqualTo("preOrder", 0.5);
        query.whereEqualTo("status", true);
        query.orderByAscending("productNo");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    Log.d("ahaaa", String.valueOf("ghghg"));
                    for (ParseObject object : objects) {
                        url.add(object.getString("imageURL"));
                        title.add(object.getString("productTitle"));
                        ptoductId.add(object.getInt("productNo"));
                        Log.d("updated at", String.valueOf(object.getUpdatedAt()));

                        if (currency.matches("SDG")) {

                            price.add((object.getInt("priceSDG")) * (object.getInt("rateSDG")));


                        } else {

                            price.add((int) (object.getInt("priceUSD") * 1.12 * eRate));
                        }



                        }


                    String area = "";
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        area = extras.getString("area");
                    }
                    Log.d("area2", area);
                    GridLayoutManager mLayoutManager = new GridLayoutManager(ProductsActivity.this, 2);
                    adapter = new ProductsAdapter(ProductsActivity.this, url, title, price, ptoductId, currency, area);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(mLayoutManager);
                }

            }
        });

    }

    public void requestExchangeRate(){

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);



      //  this.getSupportActionBar().hide();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                categoryNumber = 0;
                area = "";
            } else {
                categoryNumber = extras.getInt("categoryNumber");
                area = extras.getString("area", area);

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
       // currency = Currency.getInstance(locale).getCurrencyCode();

        currency = "SDG";

        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {











                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("CurrencyRate");
                query1.whereEqualTo("currency", currency);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects1, ParseException e) {

                        if (e == null && objects1.size() > 0) {

                            for (ParseObject object1 : objects1) {

                                String date = String.valueOf(object1.getUpdatedAt());

                                SimpleDateFormat spf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");

                                try {

                                    Date newDate = null;
                                    newDate = spf.parse(date);

                                    spf = new SimpleDateFormat("dd-MMM-yyyy");
                                    date = spf.format(newDate);
                                    String dateToday = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                    Date date5 = format.parse(dateToday);
                                    Date date6 = format.parse(date);
                                    long diff;
                                    diff = (date5.getTime() - date6.getTime()) / 1000 / 60 / 60 / 24;


                                    exchange = object1.getInt("exchangeRate");


                                    if (!currency.equals("SDG")  && diff <= 30) {

                                        Log.d("diffrent", String.valueOf(diff));
                                        requestProducts(exchange);

                                    } else {
                                        updateRate();
                                        requestProducts(exchange);
                                    }
                                } catch (java.text.ParseException parseException) {
                                    parseException.printStackTrace();
                                }
                            }

                        } else {
                            updateRate();
                          //  requestProducts(exchange);

                        }
                    }
                });


                Log.d("exchang", String.valueOf(exchange));

                mDatabase = new SqliteDatabase(this);

                requestExchangeRate();


            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();

        sigInMenu = menu.findItem(R.id.signInMenu);
        signoutMenu = menu.findItem(R.id.signOutInMenu);
        // View signInActionView = menuSignIn.getActionView();

        // View signoutView = menuSignOut.getActionView();

        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        cartBadgeIcon = actionView.findViewById(R.id.cartBadgeIcon);


        cartBadgeIcon.setVisibility(View.GONE);
        textCartItemCount.setVisibility(View.GONE);

        mCartItemCount = mDatabase.listAll().size();
        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                intent.putExtra("cameFromActivity", this.getClass().getSimpleName());

                startActivity(intent);

            }
        });

        if(ParseUser.getCurrentUser() != null) {

            MenuItem item = menu.findItem(R.id.signInMenu);
            item.setVisible(false);//
            MenuItem item1 = menu.findItem(R.id.signOutInMenu);
            item1.setVisible(true);

        } else {

            MenuItem item = menu.findItem(R.id.signInMenu);
            item.setVisible(true);//
            MenuItem item1 = menu.findItem(R.id.signOutInMenu);
            item1.setVisible(false);
        }
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.signInMenu:
                // do something

                intent = new Intent(getApplicationContext(), LoginActivity.class);

                intent.putExtra("cameFromActivity", this.getClass().getSimpleName());

                startActivity(intent);
                break;

            case R.id.signOutInMenu:

                ParseUser.logOut();

                logout.setVisibility(View.INVISIBLE);

                login.setVisibility(View.VISIBLE);

                signoutMenu.setVisible(false);
                // show the menu item
                sigInMenu.setVisible(true);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() {

        if (mDatabase.listAll().size() > 0) {

            //    if (textCartItemCount != null) {
            //  if (mCartItemCount == 0) {
            //      if (textCartItemCount.getVisibility() != View.GONE) {
            //    textCartItemCount.setVisibility(View.GONE);
            //     }
            //  } else {

            cartBadgeIcon.setVisibility(View.VISIBLE);
            textCartItemCount.setVisibility(View.VISIBLE);


            int sum = 0;
            for (int i = 0; i < mDatabase.listAll().size(); i++)
                sum += mDatabase.listQty().get(i);
            textCartItemCount.setText(String.valueOf(Math.min(sum, 99)));
            // if (textCartItemCount.getVisibility() != View.VISIBLE) {
            //   textCartItemCount.setVisibility(View.VISIBLE);
            //     }

            //   }
        } else {
            cartBadgeIcon.setVisibility(View.GONE);
            textCartItemCount.setVisibility(View.GONE);

        }
    }
}