package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {


    ImageView linearLayout;

    int productSelected;

    TextView productTitle, productDescription, productPrice, textCartItemCount;

    private SqliteDatabase mDatabase;

    int mCartItemCount = 0;



    ArrayList<Integer> listpIDs;





    public void addItemToCart(View view) {

        mDatabase = new SqliteDatabase(this);



        try {

       /*     if (mDatabase.checkProduct(productSelected) ){

               mDatabase.updateQty(productSelected, mDatabase.getQty(productSelected)+1);
               Toast.makeText(this, "already "+ mDatabase.getQty(productSelected), Toast.LENGTH_LONG).show();

            } else {*/
                mDatabase.addProduct(productSelected, 1);
                Toast.makeText(this, "adding "+ mDatabase.getQty(productSelected), Toast.LENGTH_LONG).show();

        //   }


/*

            listpIDs = mDatabase.listPIDs();

            StringBuilder builder = new StringBuilder();
            for(int i : listpIDs)
            {
                builder.append("" + i + " ");
            }
            Toast.makeText(this, builder, Toast.LENGTH_LONG).show();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void removeItem (View view) {

        try {


            mDatabase.clearCart();

            Toast.makeText(this, String.valueOf(mDatabase.checkProduct(productSelected)), Toast.LENGTH_LONG).show();

           //     mDatabase.deleteQty(productSelected);

        } catch (Exception error) {

            error.printStackTrace();

        }
    }


    public void checkOut (View view){

        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);

        startActivity(intent);
  }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();


        //CartQty.qtyCheck();

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {





                linearLayout = findViewById(R.id.productDetailImage);
                productTitle = findViewById(R.id.productTitle);
                productDescription = findViewById(R.id.productDescription);
                productPrice = findViewById(R.id.productPrice);


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


                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Product");

                query.whereEqualTo("productId", productSelected);

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null) {

                            if (objects.size() > 0) {

                                for (ParseObject object : objects) {

                                    productTitle.setText(object.getString("title"));

                                    Glide.with(ProductDetailsActivity.this).load(object.getString("imageURL")).fitCenter().into(linearLayout);

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
        textCartItemCount =  actionView.findViewById(R.id.cart_badge);

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
