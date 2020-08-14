package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {


    public void codeFromVendor (View view){

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);

        Log.i("formatted ", formatted);
    }


    ListView driverOrdersList;

    ArrayList<String> dOrders = new ArrayList<String>();
    ArrayList<Integer> orderId = new ArrayList<Integer>();
    ArrayList<Double> requestLatitudes = new ArrayList<Double>();
    ArrayList<Double> requestLongitudes = new ArrayList<Double>();


    ArrayAdapter arrayAdapter;

    String user = "ahmed";

    LocationManager locationManager;

    LocationListener locationListener;


    public void updateListView(Location location) {

        if (location != null) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Orders");

            final ParseGeoPoint geoPointLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

            query.whereEqualTo("status", "ready");

            query.whereDoesNotExist("driverId");

            query.setLimit(10);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {

                        dOrders.clear();
                        requestLongitudes.clear();
                        requestLatitudes.clear();

                        if (objects.size() > 0) {

                            for (ParseObject object : objects) {

                                ParseGeoPoint requestLocation = (ParseGeoPoint) object.get("location");

                                if (requestLocation != null) {

                                    Double distanceInMiles = geoPointLocation.distanceInMilesTo(requestLocation);

                                    Double distanceOneDP = (double) Math.round(distanceInMiles * 10) / 10;

                                    dOrders.add(distanceOneDP.toString() + " miles");

                                    requestLatitudes.add(requestLocation.getLatitude());
                                    requestLongitudes.add(requestLocation.getLongitude());
                                    orderId.add(object.getInt("orderId"));
                                }
                            }
                        } else {

                            dOrders.add("No active requests nearby");

                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateListView(lastKnownLocation);
                }
            }
        }
    }

    public void pickUp (View view){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

/*

        setTitle("Driver- Nearby Requests");

        driverOrdersList = findViewById(R.id.driverOrderList);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dOrders);

        dOrders.clear();

        dOrders.add("Getting nearby requests...");

        driverOrdersList.setAdapter(arrayAdapter);

        driverOrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if ( ContextCompat.checkSelfPermission(DriverActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (requestLatitudes.size() > i && requestLongitudes.size() > i && orderId.size() > i && lastKnownLocation != null) {

                        Intent intent = new Intent(getApplicationContext(), DriverLocationActivity.class);

                        intent.putExtra("requestLatitude", requestLatitudes.get(i));
                        intent.putExtra("requestLongitude", requestLongitudes.get(i));
                        intent.putExtra("driverLatitude", lastKnownLocation.getLatitude());
                        intent.putExtra("driverLongitude", lastKnownLocation.getLongitude());
                        intent.putExtra("orderId", orderId.get(i));

                        startActivity(intent);
                    }
                }
            }
        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            //   updateListView(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {

             //   updateListView(lastKnownLocation);


            }
        }
   */ }
}


