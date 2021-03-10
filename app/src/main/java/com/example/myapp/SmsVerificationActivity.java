package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class SmsVerificationActivity extends AppCompatActivity {


    String mobileNumber="";
    String cameFromActivity ="";
  //  List<String> userNames ;
    Intent intent;
  //  String userType;







    public void verified (View view) {


        ParseUser user = new ParseUser();

        user.setUsername(mobileNumber);

        ParseUser.logInInBackground(mobileNumber, "000000",
                new LogInCallback() {
                    public void done(ParseUser user, ParseException error) {
                        if (error == null) {

                            switch (Objects.requireNonNull(ParseUser.getCurrentUser().getString("userType"))) {

                                case "c":

                                    // after mobile verification
                                    Toast.makeText(SmsVerificationActivity.this, "logging in ", Toast.LENGTH_SHORT).show();


                                    Log.d("ooooo",cameFromActivity);

                                    if (cameFromActivity.matches("mainActivity")) {

                                        intent = new Intent(getApplicationContext(), MainActivity.class);

                                        startActivity(intent);
                                    }

                                    break;
                                case "m":

                                    intent = new Intent(getApplicationContext(), VendorActivity.class);

                                    startActivity(intent);
                                    break;


                                case "d":

                                    intent = new Intent(getApplicationContext(), DriverActivity.class);
                                    startActivity(intent);
                                    break;

                                default:
                                    Toast.makeText(SmsVerificationActivity.this, "logging in ", Toast.LENGTH_SHORT).show();

                                    break;
                            }
                        } else {

                            // delete this toast
                            Toast.makeText(SmsVerificationActivity.this, "Signup ", Toast.LENGTH_SHORT).show();

                            intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            intent.putExtra("mobileNumber", mobileNumber);
                            startActivity(intent);
                        }
                    }
                });

    }


         //       } else {


           //     }
    //        }
      //  });




    public void notVerified(View view){

        Toast.makeText(SmsVerificationActivity.this, "Number not verified", Toast.LENGTH_SHORT).show();


        intent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(intent);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        Log.d("dddddddd", Objects.requireNonNull(mobileNumber));

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {

                ParseAnalytics.trackAppOpenedInBackground(getIntent());

               // userNames = new ArrayList<>();

               // user = new ParseUser();


                cameFromActivity = getIntent().getStringExtra("cameFromActivity");




                //                HttpClient httpclient = new DefaultHttpClient();

//                httppost = new HttpPost(
//                        "https://api.twilio.com/2010-04-01/Accounts/ACc2050a7f1942814404b2e15d8f74f9f2/SMS/Messages");
//                String base64EncodedCredentials = "Basic "
//                        + Base64.encodeToString(
//                        (ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(),
//                        Base64.NO_WRAP);

//                httppost.setHeader("Authorization",
//                        base64EncodedCredentials);

//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                    nameValuePairs.add(new BasicNameValuePair("From",
//                            "+249904009994"));
//                    nameValuePairs.add(new BasicNameValuePair("To",
//                            "+966547414030"));
//                    nameValuePairs.add(new BasicNameValuePair("Body",
//                            "Welcome to Twilio"));

//                    httppost.setEntity(new UrlEncodedFormEntity(
//                            nameValuePairs));

                // Execute HTTP Post Request
                //   response = httpclient.execute(httppost);
                //  entity = response.getEntity();

//                    Log.d("hgjghj", String.valueOf(httppost));


//                    Log.d("Entity post is: ",  EntityUtils.toString(entity));


                // Get an instance of SmsRetrieverClient, used to start listening for a matching
                // SMS message.
//        SmsRetrieverClient client = SmsRetriever.getClient(this // context
//        );

                // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
                // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
                // action SmsRetriever#SMS_RETRIEVED_ACTION.
//        Task<Void> task = client.startSmsRetriever();

                // Listen for success/failure of the start Task. If in a background thread, this
                // can be made blocking using Tasks.await(task, [timeout]);
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
//            }
//        });

//        task.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
//            }
//        });

/*
                ParseQuery<ParseObject> query = ParseQuery.getQuery("APIs");
                query.whereEqualTo("name", "sms");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (ParseObject object : objects) {

                                Log.d("sid", Objects.requireNonNull(object.getString("accountSID")));

                                Log.d("token", Objects.requireNonNull(object.getString("authToken")));
                            }
                        }
                    }
                });
*/




            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
