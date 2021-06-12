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

import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ProductsActivity extends AppCompatActivity {


    private ProductsAdapter adapter;

    private RecyclerView recyclerView;

    int categoryNumber, mCartItemCount;

    final List<String> url = new ArrayList<>();

    final List<String> title = new ArrayList<>();

    final List<Integer> price = new ArrayList<>();
    final List<Integer> rate = new ArrayList<>();

    final List<Integer> finalPrice = new ArrayList<>();

    final List<Integer> ptoductId = new ArrayList<>();

    TextView textCartItemCount, login, logout;

    private SqliteDatabase mDatabase;

    Intent intent;

    private MenuItem sigInMenu;
    private MenuItem signoutMenu;

    String area, currency;

    ImageView cartBadgeIcon;

    int exchange =0;



    public void requestRate(){


        try {
            // Construct data
          //  Log.d("respo","here");
            String apiKey = "ab050eb24ddc479087a57f4945571e46";
          //  String pass = "&pwd=" + token;
          //  String text1 = "&smstext=" + message;
          //  String sender2 = "&Sender=" + sender;
         //   String numbers = "&Nums=" + mobileNumber.replace("+","");
             //Send data;
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.currencyfreaks.com/latest?apikey=" + apiKey ).openConnection();
            String data = apiKey ;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));

             BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         //    StringBuffer stringBuffer = new StringBuffer();



          //  String result = EntityUtils.toString(resp_entity);



          //  if ((conn.getResponseMessage().matches("OK"))){

                //  if (line.matches("OK")) {
            //    intent = new Intent(getApplicationContext(), VerifyActivity.class);

              //  intent.putExtra("randomNumber", randomNumber);

             //   intent.putExtra("mobileNumber", mobileNumber);
             //   intent.putExtra("cameFromActivity", this.getClass().getSimpleName());

             //   Log.d("here", "here");
                //  }

            //  Log.d("line" = rd.readLine()) != null) {
            //     stringBuffer.append(line);
            //    Log.d("line", line);
            //  }



            Log.d("respo",conn.getResponseMessage().toString());


            rd.close();

        } catch (Exception e) {
            System.out.println("Error SMS " + e);
        }
    }

    public void requestProducts(){

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
                        Log.d("updated at", String.valueOf(object.getUpdatedAt()));
                        if (currency.matches("SDG")) {
                            Log.i("customer in sudan", String.valueOf(price));
                            price.add(object.getInt("priceSDG"));
                            rate.add(object.getInt("rateSDG"));
                            for (int i = 0; i < objects.size(); i++) {
                                finalPrice.add(price.get(i) * rate.get(i));
                            }
                        } else {


                            price.add((int) (object.getInt("priceUSD") * 1.12 * exchange));
                            Log.i("customer in sudan", "not in sudan");
                        }
                        ptoductId.add(object.getInt("productNo"));
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
        currency = Currency.getInstance(locale).getCurrencyCode();


        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {









                mDatabase = new SqliteDatabase(this);

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

                                    if (!currency.equals("SDG")  && diff <= 30) {
                                        Log.d("diffrent", String.valueOf(diff));

                                        exchange = object1.getInt("exchangeRate");

                                        requestProducts();

                                    } else {
                                        requestRate();
                                    }
                                } catch (java.text.ParseException parseException) {
                                    parseException.printStackTrace();
                                }
                            }
                        } else {
                            requestRate();
                            requestProducts();
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