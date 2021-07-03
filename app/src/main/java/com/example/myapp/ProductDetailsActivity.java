package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {



    ImageView productDetailImage;

    int productSelected, price;

    TextView productTitle, productDescription, productPrice;

    private SqliteDatabase mDatabase;

    Integer spinnerQty;

    TextView textCartItemCount;

    Intent intent;

    String area = "";
    String currency = "";

    private MenuItem sigInMenu;
    private MenuItem signoutMenu;

    ImageView cartBadgeIcon;

    TextView login , logout;

    Spinner spinner;

    ArrayList<Integer> storeCart = new ArrayList<>();

    // int sum = 0;
    // ArrayList<Integer> g = new ArrayList<>();
    //  ArrayList<Integer> pIDs;
    //  ArrayList<Integer> qty;
    // TextView currentQty;


    public void addItemToCart(View view) {

        try {

            mDatabase = new SqliteDatabase(this);

            if (mDatabase.checkProduct(productSelected)) {


                if (mDatabase.getQty(productSelected) <= 10 && mDatabase.getQty(productSelected) + spinnerQty <= 10) {
                    mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) + spinnerQty);

                    setupBadge();
                } else  {

                    Toast.makeText(this, "You can get more than 10!", Toast.LENGTH_LONG).show();
                }
            } else {

                    Products newProduct = new Products(productSelected);

                    mDatabase.addProduct(newProduct);

                if (spinnerQty > 1) {

                    mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) + spinnerQty-1);
                }

                setupBadge();

            Toast.makeText(this, "added", Toast.LENGTH_LONG).show();

            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }




/*
    public void removeItem(View view) {

        try {


            mDatabase = new SqliteDatabase(this);




            if (mDatabase.checkProduct(productSelected)) {

                if (mDatabase.getQty(productSelected) == 1) {

                    mDatabase.deleteProduct(productSelected);

                   // currentQty.setText(0);

                   setupBadge();

                    if (mDatabase.listAll().size() == 0 ){

                        cartBadgeIcon.setVisibility(View.INVISIBLE);
                        textCartItemCount.setVisibility(View.INVISIBLE);
                    }


                } else {

                    mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) - 1);

                   // currentQty.setText(String.valueOf(mDatabase.getQty(productSelected)));

                    Toast.makeText(this, mDatabase.getQty(productSelected), Toast.LENGTH_LONG).show();


                }

            } else {
                Toast.makeText(this, ("Item selected not in cart " + mDatabase.getQty(productSelected)), Toast.LENGTH_LONG).show();
            }
        } catch (Exception error) {

            error.printStackTrace();

        }
    }




    public void clearCart(View view) {

        try {

            mDatabase.clearCart();

            Toast.makeText(this, "cleared", Toast.LENGTH_LONG).show();


               cartBadgeIcon.setVisibility(View.INVISIBLE);
               textCartItemCount.setVisibility(View.INVISIBLE);



        } catch (Exception error) {

            error.printStackTrace();
        }
    }

    public void checkOut2(View view) {

        if (mDatabase.listAll() != null) {

            intent = new Intent(getApplicationContext(), CheckoutActivity.class);

            intent.putExtra("area", area);

            startActivity(intent);

        }
    }
*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {


                spinner = findViewById(R.id.spinner2);
                productDetailImage = findViewById(R.id.productDetailImage);
                productTitle = findViewById(R.id.productTitle);
                productDescription = findViewById(R.id.productDescription);
                productPrice = findViewById(R.id.productPrice);

                spinner.setOnItemSelectedListener(this);
                mDatabase = new SqliteDatabase(this);
                Bundle extras = getIntent().getExtras();
                Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};

                if (savedInstanceState == null) {
                     extras = getIntent().getExtras();
                    if (extras == null) {
                        productSelected = 0;
                        price =0;
                    } else {
                        productSelected = extras.getInt("productId");
                        price = extras.getInt("price");

                        area = extras.getString("area");
                        currency = extras.getString("currency");
                    }
                } else {
                    productSelected = (int) savedInstanceState.getSerializable("productId");
                }



                setupBadge();

                ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                        android.R.layout.simple_spinner_item, items);
                spinner.setAdapter(adapter);

                int spinnerPosition = adapter.getPosition(spinnerQty);
                // set the default according to value
                spinner.setSelection(spinnerPosition);

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Product");

                query.whereEqualTo("productNo", productSelected);
                Log.d("is     ", String.valueOf(productSelected));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null) {

                            if (objects.size() > 0) {

                                for (ParseObject object : objects) {

                                    productTitle.setText(object.getString("productTitle"));


                                    Glide.with(ProductDetailsActivity.this).load(object.getString("imageURL"))
                                            .centerInside().into(productDetailImage);
                                    String priceText = currency + "  " + String.valueOf(price);

                                    productPrice.setText(priceText);

                                }


                            }
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
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        cartBadgeIcon = actionView.findViewById(R.id.cartBadgeIcon);
        textCartItemCount.setVisibility(View.INVISIBLE);
        cartBadgeIcon.setVisibility(View.INVISIBLE);

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

        if (mDatabase.listAll().size() > 0  ) {

            if (textCartItemCount != null) {


                cartBadgeIcon.setVisibility(View.VISIBLE);
                textCartItemCount.setVisibility(View.VISIBLE);

                storeCart = mDatabase.listQty();

                int sum = 0;

                for (int i = 0; i < mDatabase.listAll().size(); i++)
                    sum += storeCart.get(i);

                textCartItemCount.setText(String.valueOf(Math.min(sum, 99)));
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerQty = (Integer) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


