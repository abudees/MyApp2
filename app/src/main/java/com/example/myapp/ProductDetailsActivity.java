package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {


    ImageView linearLayout;

    int productSelected;

    TextView productTitle, productDescription, productPrice, textCartItemCount;



    SqliteDatabase mDB;

    ArrayList<Products> allProducts ;






    public void addItemToCart(View view) {

        Products newProducts = new Products(productSelected, 1);
        mDB.addProduct(newProducts);
       

        textCartItemCount.setVisibility(View.VISIBLE);

        textCartItemCount.setText(String.valueOf(mDB.listProducts().size()));





        Toast.makeText(ProductDetailsActivity.this, "Item added successfully", Toast.LENGTH_LONG).show();

    }






    public void checkOut (View view){

        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);

        startActivity(intent);

    }

    public void clearCart() {


    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        linearLayout = findViewById(R.id.productDetailImage);
        productTitle = findViewById(R.id.productTitle);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);

       // allProducts = mDB.listProducts();

        mDB = new SqliteDatabase(this);

        //  mCartItemCount = allProducts.size();


        Toast.makeText(ProductDetailsActivity.this, String.valueOf(mDB.listProducts().size()), Toast.LENGTH_LONG).show();


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

                            productPrice.setText( String.valueOf(object.getInt("price")));
                        }
                    }
                }
            }
        });
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

        switch (item.getItemId()) {

            case R.id.action_cart: {
                // Do something

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mDB.listProducts().size() == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {

                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {

                textCartItemCount.setText(String.valueOf(Math.min(mDB.listProducts().size(), 99)));

                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    textCartItemCount.setText(String.valueOf(mDB.listProducts().size()));
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDB != null) {
            mDB.close();
        }


    }


}
