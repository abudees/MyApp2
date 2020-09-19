package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {




    private SqliteDatabase mDatabase;

    RecyclerView cartView;

    LinearLayoutManager linearLayoutManager;

    ProductCartAdapter mAdapter;

    public String getImgageURL(){
        String temp="";
        try{
            final List<String> url = new ArrayList<>();

            final List<String> title = new ArrayList<>();



            ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");

            query.whereEqualTo("status", true);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null && objects.size() > 0) {

                        for (ParseObject object : objects) {

                            url.add(object.getString("imageURL"));

                            Log.i("url", object.getString("imageURL"));
                        }
                    }
                }
            });

        }
        catch(Exception e)
        {

        }
        Log.d("INSIDE READ FUNC", "temp have "+temp);
        return temp;
    }

    public void getImgageURL(String imageURL){



    }

/*
    public ArrayList<Integer> orderItems ;

    private RecyclerView recyclerView;

    private CartAdapter adapter;

    final ArrayList<Integer> price = new ArrayList<>();;

    final List<String> url = new ArrayList<>();

    final ArrayList<String> size = new ArrayList<>();

    final List<String> title = new ArrayList<>();

    final List<Integer> qty = new ArrayList<>();

    public void createOrder (View view){
    }

    public void setData(List<> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    private int grandTotal() {
        int totalPrice = 0;
        for (int i = 0; i < price.size(); i++) {
            totalPrice += price.get(i).getBack();
        }
        return totalPrice;
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        cartView = findViewById(R.id.myCartList);
        linearLayoutManager = new LinearLayoutManager(this);
        cartView.setLayoutManager(linearLayoutManager);
        cartView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        ArrayList<Products> allProducts = mDatabase.listProducts();


        final List<String> url = new ArrayList<>();

        final List<String> title = new ArrayList<>();


        if (allProducts.size() > 0) {

            cartView.setVisibility(View.VISIBLE);
            mAdapter = new ProductCartAdapter(CheckoutActivity.this, allProducts);
            cartView.setAdapter(mAdapter);
        } else {
            cartView.setVisibility(View.GONE);
            Toast.makeText(CheckoutActivity.this, "There is no contact in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //addTaskDialog();
            }
        });


    }









/*
        recyclerView = findViewById(R.id.cartRecycler);

        Log.i("here", "here");

        try {

            SQLiteDatabase cartDB = CheckoutActivity.this.openOrCreateDatabase("tempOrder", MODE_PRIVATE, null);

            Cursor c = cartDB.rawQuery("SELECT * FROM newCart", null);

            int tOne = c.getColumnIndex("tProductId");
            int tTwo = c.getColumnIndex("tPrice");
            int tThree = c.getColumnIndex("tQty");

            c.moveToFirst();

            while (c != null) {

                Log.i("UserResults - one", Integer.toString(c.getInt(tOne)));
                Log.i("UserResults - two", Integer.toString(c.getInt(tTwo)));
                Log.i("UserResults - three", Integer.toString(c.getInt(tThree)));

                c.moveToNext();

                c.close();
            }
        }  catch (Exception error) {

            error.printStackTrace();
        }

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        try {

            SQLiteDatabase cartDB = CheckoutActivity.this.openOrCreateDatabase("tempOrder", MODE_PRIVATE, null);
            Cursor c = cartDB.rawQuery("SELECT * FROM newCart", null);
            //  int tOne = c.getColumnIndex("tProductId");
            int tTwo = c.getColumnIndex("tPrice");
            int tThree = c.getColumnIndex("tQty");
            c.moveToFirst();
            price.add( c.getColumnIndex("tPrice"));
            qty.add(c.getInt(tThree));
            c.moveToNext();
            c.close();

        } catch (Exception error) {

            error.printStackTrace();
        }
        */







    /* private void addTaskDialog() {

         LayoutInflater inflater = LayoutInflater.from(this);
         View subView = inflater.inflate(R.layout.add_contacts, null);
         final EditText nameField = subView.findViewById(R.id.enterName);
         final EditText noField = subView.findViewById(R.id.enterPhoneNum);
         AlertDialog.Builder builder = new AlertDialog.Builder(this);

         builder.setTitle("Add new CONTACT");
         builder.setView(subView);
         builder.create();

         builder.setPositiveButton("ADD CONTACT", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 final String pId = nameField.getText().toString();
                 final String pQty = noField.getText().toString();
                 if (TextUtils.isEmpty(pId)) {/*
                     Toast.makeText(CheckoutActivity.this,
                             "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                 }
                 else {
                     Products newProduct = new Products(pId, pQty);
                     mDatabase.addProduct(newProduct);
                     finish();
                     startActivity(getIntent());
                 }
             }
         });

         builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 Toast.makeText(CheckoutActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
             }
         });
         builder.show();
     }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }


    }
}
