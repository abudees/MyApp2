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
import com.parse.ParseUser;

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

    Intent intent;

    String area = "";

    ArrayList<Integer> g = new ArrayList<>();


    private MenuItem sigInMenu;
    private MenuItem signoutMenu;

    ImageView cartBadgeIcon;

    TextView login , logout;

   // int sum = 0;


    ArrayList<Integer> storeCart = new ArrayList<>();



    public void addItemToCart(View view) {

        try {

            mDatabase = new SqliteDatabase(this);


            Log.i("all qty ", String.valueOf(mDatabase.listQty()));


//            Log.d("1", String.valueOf(sum));

            if (mDatabase.checkProduct(productSelected)) {


                mDatabase.updateQty(productSelected, (mDatabase.getQty(productSelected)) + 1);

                //  Log.d("total cart", String.valueOf(mDatabase.listQty().size()));

                //  if (mDatabase.listAll().size()  == 0 ) {

                //     textCartItemCount.setVisibility(View.VISIBLE);

                //  sum = sum +1;


                //   } else {

                //       Log.d("3", String.valueOf(sum));


                //  }

                setupBadge();

                // currentQty.setText(String.valueOf(mDatabase.getQty(productSelected)));


            } else {

                Products newProduct = new Products(productSelected);

                mDatabase.addProduct(newProduct);


                setupBadge();

            Toast.makeText(this, "added", Toast.LENGTH_LONG).show();

            //   mCartItemCount = sum;

            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }





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


    public void checkOut(View view) {

        intent = new Intent(getApplicationContext(), CheckoutActivity.class);

        intent.putExtra("area", area);

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


                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    area = extras.getString("area");
                }





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


                Log.i("all qty ", String.valueOf(mDatabase.listQty()));


                //Log.i("i ", String.valueOf(mDatabase.getQty(productSelected)));


                pIDs = mDatabase.listProductIds();

                qty = mDatabase.listQty();


                Log.i("pIDs", String.valueOf(pIDs));

                Log.i("qty", String.valueOf(qty));


                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Products");

                query.whereEqualTo("productNo", productSelected);

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null) {

                            if (objects.size() > 0) {

                                for (ParseObject object : objects) {

                                    productTitle.setText(object.getString("title"));

                                    Glide.with(ProductDetailsActivity.this).load(object.getString("imageURL"))
                                            .centerInside().into(productDetailImage);


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


        textCartItemCount.setVisibility(View.INVISIBLE);
        cartBadgeIcon.setVisibility(View.INVISIBLE);


      //  mCartItemCount = mDatabase.listAll().size();
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

              //  if (mCartItemCount == 0) {

                   // if (textCartItemCount.getVisibility() != View.GONE) {

                    //    textCartItemCount.setVisibility(View.GONE);
              //   }
           //   } else {
            cartBadgeIcon.setVisibility(View.VISIBLE);
            textCartItemCount.setVisibility(View.VISIBLE);



//           if (textCartItemCount.getVisibility() != View.VISIBLE) {

                storeCart = mDatabase.listQty();

                int sum = 0;

                for (int i = 0; i < mDatabase.listAll().size(); i++)
                sum += storeCart.get(i);
//

            //  textCartItemCount.setVisibility(View.VISIBLE);
           //   cartBadgeIcon.setVisibility(View.VISIBLE);
           textCartItemCount.setText(String.valueOf(Math.min(sum, 99)));

         //       }

             }
    /*    } else if (mDatabase.listAll().size() > 1) {

            for (int i = 0; i < mDatabase.listAll().size(); i++)
                sum += mDatabase.listQty().get(i);
//

            textCartItemCount.setText(String.valueOf(Math.min(sum, 99)));



*/

        }
    }


}


