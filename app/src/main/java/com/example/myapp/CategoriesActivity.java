package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class CategoriesActivity extends AppCompatActivity {

     CategoriesAdapter adapter;

     RecyclerView recyclerView;

     Intent intent;

     TextView textCartItemCount;

     private SqliteDatabase mDatabase;

     int mCartItemCount;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);










        try {

            

            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {




                ParseAnalytics.trackAppOpenedInBackground(getIntent());


                mDatabase = new SqliteDatabase(this);



                    setupBadge();



                recyclerView = findViewById(R.id.recyclerview);

                recyclerView.setHasFixedSize(true);

                recyclerView.setAdapter(adapter);


                final List<String> url = new ArrayList<>();

                final List<String> title = new ArrayList<>();

                final List<Integer> id = new ArrayList<>();

                final List<String> images = new ArrayList<>();




                ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");

                query.whereEqualTo("status", true);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null && objects.size() > 0) {

                            for (ParseObject object : objects) {

                                url.add(object.getString("imageURL"));

                                title.add(object.getString("categoryName"));

                                id.add(object.getInt("categoryNo"));
                            }

                            adapter = new CategoriesAdapter(CategoriesActivity.this, url, title, id);

                            recyclerView.setAdapter(adapter);

                            GridLayoutManager mLayoutManager = new GridLayoutManager(CategoriesActivity.this, 2);

                            recyclerView.setLayoutManager(mLayoutManager);

                        }
                    }
                });








            } else {
                //do something, net is not connected



                intent = new Intent(getApplicationContext(), InternetFailActivity.class);

                intent.putExtra("activityName", this.getClass().getSimpleName());

                startActivity(intent);




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

        if (item.getItemId() == R.id.action_cart) {
            // Do something
            intent = new Intent(getApplicationContext(), CheckoutActivity.class);
            intent.putExtra("cameFromActivity", "CategoriesActivity");

            startActivity(intent);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {


        if (ParseUser.getCurrentUser() != null) {


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
}