package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {



    ImageView productDetailImage;

    int productSelected;

    TextView productTitle, productDescription, productPrice;

    private SqliteDatabase mDatabase;

    ArrayList<Integer> pIDs;

    ArrayList<Integer> qty;


    TextView currentQty;

    int mCartItemCount;

    TextView textCartItemCount;


    public void addItemToCart(View view) {

        try {
            mDatabase = new SqliteDatabase(this);



            if (mDatabase.checkProduct(productSelected)) {

               // Log.i("price  ", String.valueOf(price));

                mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) + 1);
                currentQty.setText(String.valueOf(mDatabase.getQty(productSelected)));

                setupBadge();

                Toast.makeText(this, "added twice", Toast.LENGTH_LONG).show();

            } else {

                Products newProduct = new Products(productSelected);

                mDatabase.addProduct(newProduct);
                currentQty.setText(String.valueOf(1));

                setupBadge();

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
                    setupBadge();

                } else if (mDatabase.getQty(productSelected) > 1) {

                    mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) - 1);

                    currentQty.setText(String.valueOf(mDatabase.getQty(productSelected)));
                    setupBadge();

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

            setupBadge();

            currentQty.setText(String.valueOf(0));

        } catch (Exception error) {

            error.printStackTrace();
        }
    }


    public void checkOut(View view) {

        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);

        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                //retrive selected product
                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();
                    if (extras == null) {
                        productSelected = 0;
                    } else {
                        productSelected = extras.getInt("productId");
                    }
                } else {
                    productSelected = (int) savedInstanceState.getSerializable("productId");
                }

                mDatabase = new SqliteDatabase(this);

                setupBadge();
                productDetailImage = findViewById(R.id.productDetailImage);
                productTitle = findViewById(R.id.productTitle);
                productDescription = findViewById(R.id.productDescription);
                productPrice = findViewById(R.id.productPrice);
                //  updateTextView(String.valueOf(mDatabase.getQty(productSelected)));

                currentQty = findViewById(R.id.currentQty);

                if (mDatabase.checkProduct(productSelected)) {

                    currentQty.setText(String.valueOf(mDatabase.getQty(productSelected)));
                }


              //  Log.i("all DB ", String.valueOf(mDatabase.listAll().size()));


                //Log.i("i ", String.valueOf(mDatabase.getQty(productSelected)));


                pIDs = mDatabase.listProducts();

                qty = mDatabase.listQty();


                Log.i("pIDs", String.valueOf(pIDs));

                Log.i("qty", String.valueOf(qty));


                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Products");

                query.whereEqualTo("productId", productSelected);

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null) {

                            if (objects.size() > 0) {

                                for (ParseObject object : objects) {

                                    productTitle.setText(object.getString("title"));

                                    Glide.with(ProductDetailsActivity.this).load(object.getString("imageURL")).fitCenter().into(productDetailImage);


                                    productPrice.setText(String.valueOf(object.getInt("price")));
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

        if (item.getItemId() == R.id.action_cart) {// Do something
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
