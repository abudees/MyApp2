package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseAnalytics;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    DataBeanAdapter dbAdapter;




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

    public DataBean getData(String name){

        DataBean bean = null;
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(DataBaseHelper.UID);
            int index2 = cursor.getColumnIndex(DataBaseHelper.NAME);
            int index3 = cursor.getColumnIndex(DataBaseHelper.CARD);
            int index4 = cursor.getColumnIndex(DataBaseHelper.CODE);
            int id = cursor.getInt(index);
            String personName = cursor.getString(index2);
            String card = cursor.getString(index3);
            String code = cursor.getString(index4);
            bean = new DataBean(id, name, card, code);
        }
        return bean;
    }

    public List<DataBean> getAllData() {
        List<DataBean> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DataBaseHelper.UID);
            int index2 = cursor.getColumnIndex(DataBaseHelper.NAME);
            int index3 = cursor.getColumnIndex(DataBaseHelper.CARD);
            int index4 = cursor.getColumnIndex(DataBaseHelper.CODE);
            int cid = cursor.getInt(index);
            String name = cursor.getString(index2);
            String card = cursor.getString(index3);
            String code = cursor.getString(index4);
            DataBean bean = new DataBean(cid, name, card, code);
            list.add(bean);
        }
        return list;
    }














    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mRecyclerView = findViewById(R.id.cartRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new DataBeanAdapter(dbAdapter.getAllData(), R.layout.cart_card));









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
//



*/

    }
}
