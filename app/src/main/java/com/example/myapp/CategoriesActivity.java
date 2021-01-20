package com.example.myapp;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class CategoriesActivity extends FragmentActivity {

     CategoriesAdapter adapter;

     RecyclerView recyclerView;

     Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);








        try {

            

            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {




                ParseAnalytics.trackAppOpenedInBackground(getIntent());

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
}