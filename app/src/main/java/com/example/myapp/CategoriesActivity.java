package com.example.myapp;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class CategoriesActivity extends FragmentActivity {

    private CategoriesAdapter adapter;

    private ImageAdapter imageAdapter;

    ViewPager viewpager ;

    private RecyclerView recyclerView;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);





        viewpager =  findViewById(R.id.pager);

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);

        //  recyclerView.setLayoutManager(new LinearLayoutManager(this));



        recyclerView.setAdapter(adapter);

        loadData();


        ParseAnalytics.trackAppOpenedInBackground(getIntent());





    }


    public void loadData() {

        final List<String> url = new ArrayList<>();

        final List<String> title = new ArrayList<>();

        final List<Integer> id = new ArrayList<>();

        final List<String> images=new ArrayList<>();






        ParseQuery<ParseObject> query = ParseQuery.getQuery("Categories");

        query.whereEqualTo("status", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null && objects.size() > 0) {

                    for (ParseObject object : objects) {


                        url.add(object.getString("imageURL"));

                        title.add(object.getString("categoryName"));


                        id.add(object.getInt("catId"));

                        Log.i("url", object.getString("imageURL"));

                    }


                    adapter = new CategoriesAdapter(CategoriesActivity.this, url, title, id);

                    recyclerView.setAdapter(adapter);

                }
            }
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(CategoriesActivity.this, 2);

        recyclerView.setLayoutManager(mLayoutManager);






        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Product");


        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null && objects.size() > 0) {

                    for (ParseObject object : objects) {

                        images.add(object.getString("imageURL"));
                    }


                    imageAdapter = new ImageAdapter (CategoriesActivity.this, images);

                    viewpager.setAdapter(imageAdapter);

                }
            }
        });
    }

}