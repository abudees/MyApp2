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
import com.parse.ParseAnalytics;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {



    private SqliteDatabase mDatabase;







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

        RecyclerView contactView = findViewById(R.id.myContactList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contactView.setLayoutManager(linearLayoutManager);
        contactView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        ArrayList<Contacts> allContacts = mDatabase.listContacts();
        if (allContacts.size() > 0) {
            contactView.setVisibility(View.VISIBLE);
            ContactAdapter mAdapter = new ContactAdapter(this, allContacts);
            contactView.setAdapter(mAdapter);
        }
        else {
            contactView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no contact in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });
    }
    private void addTaskDialog() {
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
                final String name = nameField.getText().toString();
                final String ph_no = noField.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CheckoutActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else {
                    Contacts newContact = new Contacts(name, ph_no);
                    mDatabase.addContacts(newContact);
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
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}
