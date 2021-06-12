package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;





public class CategoriesActivity extends AppCompatActivity {

    CategoriesAdapter adapter;

    RecyclerView recyclerView;

    Intent intent;

    TextView textCartItemCount;

    private SqliteDatabase mDatabase;

    int mCartItemCount;

    private MenuItem sigInMenu;
    private MenuItem signoutMenu;

    ImageView cartBadgeIcon;

    TextView login, logout;

    final List<String> url = new ArrayList<>();

    final List<String> title = new ArrayList<>();

    final List<Integer> id = new ArrayList<>();

    String area = "";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        recyclerView = findViewById(R.id.recyclerview);


        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {








                mDatabase = new SqliteDatabase(this);

                recyclerView.setHasFixedSize(true);

                recyclerView.setAdapter(adapter);




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
                                Log.d("date", String.valueOf(Calendar.getInstance().getTime().compareTo(object.getUpdatedAt())));

                            }

                            Bundle extras = getIntent().getExtras();

                            if (extras != null) {
                                area = extras.getString("area");
                            }

                            adapter = new CategoriesAdapter(CategoriesActivity.this, url, title, id, area);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();

        sigInMenu = menu.findItem(R.id.signInMenu);

        signoutMenu = menu.findItem(R.id.signOutInMenu);

        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        cartBadgeIcon = actionView.findViewById(R.id.cartBadgeIcon);

        cartBadgeIcon.setVisibility(View.GONE);

        textCartItemCount.setVisibility(View.GONE);

        mCartItemCount = mDatabase.listAll().size();

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(getApplicationContext(), CheckoutActivity.class);

                intent.putExtra("cameFromActivity", this.getClass().getSimpleName());

                startActivity(intent);
            }
        });

        if (ParseUser.getCurrentUser() != null) {

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

        if (mDatabase.listAll().size() > 0) {

            cartBadgeIcon.setVisibility(View.VISIBLE);

            textCartItemCount.setVisibility(View.VISIBLE);

            int sum = 0;

            for (int i = 0; i < mDatabase.listAll().size(); i++)
                sum += mDatabase.listQty().get(i);
                textCartItemCount.setText(String.valueOf(Math.min(sum, 99)));

        } else {
            cartBadgeIcon.setVisibility(View.GONE);
            textCartItemCount.setVisibility(View.GONE);
        }
    }
}