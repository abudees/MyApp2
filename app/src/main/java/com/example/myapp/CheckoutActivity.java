package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

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


    EditText nameTxt,posTxt;
    RecyclerView rv;
    CartAdapter adapter;
    ArrayList<Cart> players=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });

            //recycler
            rv= (RecyclerView) findViewById(R.id.myRecycler);

            //SET ITS PROPS
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setItemAnimator(new DefaultItemAnimator());

            //ADAPTER
            adapter=new CartAdapter(this,players);

            retrieve();

        }

        private void showDialog()
        {
            Dialog d=new Dialog(this);

            //NO TITLE
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);

            //layout of dialog
            d.setContentView(R.layout.custom_layout);

            nameTxt = findViewById(R.id.nameEditTxt);
            posTxt = findViewById(R.id.posEditTxt);
            Button savebtn = findViewById(R.id.saveBtn);
            Button retrieveBtn= findViewById(R.id.retrieveBtn);

            //ONCLICK LISTENERS
            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save(nameTxt.getText().toString(),posTxt.getText().toString());
                }
            });

            retrieveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retrieve();
                }
            });

            //SHOW DIALOG
            d.show();
        }

        //SAVE
        private void save(String name,String pos)
        {
            DBAdapter db=new DBAdapter(this);

            //OPEN
            db.openDB();

            //INSERT
            long result=db.add(name,pos);

            if(result>0)
            {
                nameTxt.setText("");
                posTxt.setText("");
            }else
            {
                Snackbar.make(nameTxt,"Unable To Insert",Snackbar.LENGTH_SHORT).show();
            }

            //CLOSE
            db.close();

            //refresh
            retrieve();

        }

        //RETRIEVE
        private void retrieve()
        {
            DBAdapter db=new DBAdapter(this);

            //OPEN
            db.openDB();

            players.clear();

            //SELECT
            Cursor c=db.getAllPlayers();

            //LOOP THRU THE DATA ADDING TO ARRAYLIST
            while (c.moveToNext())
            {
                int id=c.getInt(0);
                String name=c.getString(1);
                String pos=c.getString(2);

                //CREATE PLAYER
                Cart p=new Cart(name,pos,id);

                //ADD TO PLAYERS
                players.add(p);
            }

            //SET ADAPTER TO RV
            if(!(players.size()<1))
            {
                rv.setAdapter(adapter);
            }

        }

        @Override
        protected void onResume() {
            super.onResume();
            retrieve();
        }
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

        //  recyclerView.setLayoutManager(new LinearLayoutManager(this));

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






        adapter = new CartAdapter(CheckoutActivity.this, url, title, qty, price );

        recyclerView.setAdapter(adapter);

        GridLayoutManager mLayoutManager = new GridLayoutManager(CheckoutActivity.this,1);

        recyclerView.setLayoutManager(mLayoutManager);





