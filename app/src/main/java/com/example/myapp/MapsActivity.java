package com.example.myapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Map;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView selectedAddress;

    Intent intent;

    String area,  cameFromActivity;

    List<String>  allPoints= new ArrayList<>();

    Double areaLatitude , arealongitude, selectedLatitude, selectedLongitude ;

    Geocoder geoCoder ;
    List<Address> matches ;
    Address bestMatch;


    private SupportMapFragment mMapFragment;










  //  LocationManager locationManager;
   // LocationListener locationListener;

   // boolean isTapOnMap;


  //  private TextView mTapTextView;

   // Context context;


    public void selectArea(View view){




        if (selectedLatitude != null && selectedLongitude != null) {

            Log.i("newFriends", String.valueOf(selectedLatitude + selectedLongitude));

            intent = new Intent(getApplicationContext(), OrderConfirmationActivity.class);

            intent.putExtra("selectedLatitude", selectedLatitude);
            intent.putExtra("selectedLongitude", selectedLongitude);

            startActivity(intent);
        } else {



            Toast.makeText(MapsActivity.this, "Please select Area", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));




        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      //  mTapTextView = findViewById(R.id.tap_text);

     //   setUpMapIfNeeded();

        selectedAddress = findViewById(R.id.textView10);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.myapp", Context.MODE_PRIVATE);

        ArrayList<String> newFriends = new ArrayList<>();



      //  map = findViewById(R.id.map);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {

                cameFromActivity ="";
            } else {

                cameFromActivity = extras.getString("cameFromActivity");
            }
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {

                area ="";

            } else {

                area = extras.getString("area");
            }
        }


        try {

            newFriends = (ArrayList<String>)  ObjectSerializer.deserialize(sharedPreferences.getString("friends", ObjectSerializer.serialize(new ArrayList<String>())));


        } catch (IOException e) {
            e.printStackTrace();
        }



    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




        ParseQuery<ParseObject> query = new ParseQuery<>("Area");

        query.whereEqualTo("areaName", area);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        areaLatitude = object.getDouble("centerLatitude");

                        arealongitude = object.getDouble("centerLongitude");
                    }


                    Log.d("latitude", String.valueOf(areaLatitude));
                    Log.d("longitude", String.valueOf(arealongitude));

                    // Add a marker in Sydney and move the camera
                    LatLng areaMap = new LatLng(areaLatitude, arealongitude);


                    //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(areaMap));

                    float zoomLevel = 15.5f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(areaMap, zoomLevel));


                    // Enable the zoom controls for the map
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("New Marker");
                            googleMap.addMarker(marker);
                            //System.out.println(latLng.latitude+"---"+ latLng.longitude);

                            allPoints.add(String.valueOf(latLng));
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng));

                            Log.d("are1:", latLng.latitude+"---"+ latLng.longitude);

                            selectedLatitude = latLng.latitude;
                            selectedLongitude = latLng.longitude;


                            try {

                                 geoCoder = new Geocoder(MapsActivity.this);
                                 matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                 bestMatch = (matches.isEmpty() ? null : matches.get(0));

                                String address = matches.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = matches.get(0).getLocality();
                                String state = matches.get(0).getAdminArea();
                                String country = matches.get(0).getCountryName();
                                String postalCode = matches.get(0).getPostalCode();
                                String knownName = matches.get(0).getFeatureName(); // Only if available else return NULL

                                selectedAddress.setText(address + ", " + city + ", " + state + ", " + country + ", " + knownName);

                                Log.d("are2:", String.valueOf(bestMatch));




                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });





/*


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));



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






              //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

               // Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

               // LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();

               // mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(getResources().getResourceName
                        (R.drawable.pin), "drawable", getPackageName()));
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 38, 38, false);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .anchor(0.5f, 0.1f)
                        .title("")
                        .snippet("")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    List<Address> listAddresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

                    if (listAddresses != null && listAddresses.size() > 0) {

                        Log.i("PlaceInfo", listAddresses.get(0).toString());

                        String address = "";

                        if (listAddresses.get(0).getSubThoroughfare() != null) {

                            address += listAddresses.get(0).getSubThoroughfare() + " ";

                        }

                        if (listAddresses.get(0).getThoroughfare() != null) {

                            address += listAddresses.get(0).getThoroughfare() + ", ";

                        }

                        if (listAddresses.get(0).getLocality() != null) {

                            address += listAddresses.get(0).getLocality() + ", ";

                        }

                        if (listAddresses.get(0).getPostalCode() != null) {

                            address += listAddresses.get(0).getPostalCode() + ", ";

                        }

                        if (listAddresses.get(0).getCountryName() != null) {

                            address += listAddresses.get(0).getCountryName();

                        }

                        Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();

                    }

                } catch (IOException e) {

                    e.printStackTrace();

                }


            }
        });

*/


        // PASTE THE LINES BELOW THIS COMMENT





    }


}