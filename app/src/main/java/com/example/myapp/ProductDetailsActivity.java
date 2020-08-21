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
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {


    ImageView linearLayout;

    int productSelected;

    TextView productTitle, productDescription, productPrice;


    TextView textCartItemCount;
    int mCartItemCount = 0;






    public void addItemToCart(View view) {

        mCartItemCount = mCartItemCount + 1;

        textCartItemCount.setVisibility(View.VISIBLE);

        textCartItemCount.setText(String.valueOf(mCartItemCount));

        Log.i("quantity is ", String.valueOf(mCartItemCount));

        ParseQuery<ParseObject> query = new ParseQuery<>("Product");

        query.whereEqualTo("productId", productSelected);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null && objects.size() > 0) {

                    for (ParseObject object : objects) {

                        try {

                            SQLiteDatabase cartDB = ProductDetailsActivity.this.openOrCreateDatabase("tempOrder", MODE_PRIVATE, null);

                            Cursor checkC = cartDB.rawQuery("SELECT * FROM newCart WHERE tProductId = " + productSelected + " ", null);

                            checkC.moveToFirst();

                            cartDB.execSQL("UPDATE newCart SET tQty = " + mCartItemCount + " WHERE tProductId = " + productSelected);

                            Log.i("product is there", "true");

                            if (checkC.getColumnIndex("tProductId") == 0) {

                                cartDB.execSQL("INSERT INTO newCart (tProductId, tPrice, tQty) VALUES (" + object.getInt("productId") + ", " + object.getInt("price") + ", " + mCartItemCount + ")");

                                Log.i("product is there", "false");
                            }

                            checkC.moveToNext();

                            checkC.close();

                            Cursor c = cartDB.rawQuery("SELECT * FROM newCart", null);

                            int tOne = c.getColumnIndex("tProductId");

                            int tTwo = c.getColumnIndex("tPrice");

                            int tThree = c.getColumnIndex("tQty");

                            c.moveToFirst();

                            Log.i("UserResults - one", Integer.toString(c.getInt(tOne)));

                            Log.i("UserResults - two", Integer.toString(c.getInt(tTwo)));

                            Log.i("UserResults - three", Integer.toString(c.getInt(tThree)));

                            c.moveToNext();


                        } catch (Exception error) {

                            error.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    public void removeItemToCart(View view) {

        Log.i("quantity is ", String.valueOf(mCartItemCount));

        try {

            SQLiteDatabase cartDB = ProductDetailsActivity.this.openOrCreateDatabase("tempOrder", MODE_PRIVATE, null);

            Cursor checkC = cartDB.rawQuery("SELECT * FROM newCart WHERE tProductId = " + productSelected + " ", null);

            Log.i("product selected", String.valueOf(productSelected));

            checkC.moveToFirst();

            int nameIndex = checkC.getColumnIndex("tQty");

            mCartItemCount =  checkC.getInt(nameIndex);

            if (mCartItemCount > 0) {

                mCartItemCount = mCartItemCount - 1;

                if (mCartItemCount == 0){

                    textCartItemCount.setVisibility(View.INVISIBLE);

                } else {

                    textCartItemCount.setVisibility(View.VISIBLE);
                    textCartItemCount.setText(String.valueOf(mCartItemCount));
                }
                cartDB.execSQL("UPDATE newCart SET tQty = " + mCartItemCount + " WHERE tProductId = " + productSelected);

                Log.i("qty was", String.valueOf(checkC.getColumnIndex("tQty")));

                Toast.makeText(this, "Item removed" + mCartItemCount, Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Cart is Empty!" + mCartItemCount, Toast.LENGTH_LONG).show();
            }

            checkC.moveToNext();

            checkC.close();

        } catch (Exception error) {

            error.printStackTrace();

        }
    }


    public void checkOut (View view){

        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);


        //  intent.putExtra("categoryNumber", id.get(position));


        startActivity(intent);


        if (mCartItemCount > 0) {


            try {
                SQLiteDatabase cartDB = ProductDetailsActivity.this.openOrCreateDatabase("tempOrder", MODE_PRIVATE, null);

                Cursor c = cartDB.rawQuery("SELECT * FROM newCart", null);

                int tOne = c.getColumnIndex("tProductId");

                int tTwo = c.getColumnIndex("tPrice");

                int tThree = c.getColumnIndex("tQty");

                c.moveToFirst();

                ParseObject object = new ParseObject("Order_items");

                object.put("productId", c.getInt(tOne));

                object.put("price", c.getInt(tTwo));

                object.put("qty", c.getInt(tThree));

                object.saveInBackground();

                c.moveToNext();

                c.close();


            } catch (Exception error) {

                error.printStackTrace();
            }
        }

    }
/*
    public void clearCart(){

        try {

            SQLiteDatabase cartDB = ProductDetailsActivity.this.openOrCreateDatabase("tempOrder", MODE_PRIVATE, null);

            cartDB.execSQL("CREATE TABLE IF NOT EXISTS newCart (tProductId INTEGER(5), tPrice INTEGER(8), tQty INTEGER(5))");

            // to clear cart DB
            // cartDB.execSQL("DELETE FROM newCart");
        } catch (Exception error) {

            error.printStackTrace();
        }
    }


*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        CartQty.qtyCheck();






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
