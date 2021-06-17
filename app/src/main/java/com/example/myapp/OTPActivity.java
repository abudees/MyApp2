package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OTPActivity extends AppCompatActivity {


    EditText mobileEditText, nameEditText, emailEditText;
    Button signUpButton;
    Intent intent;
    Spinner spinner;

    String callingCode = "";


    int randomNumber;

    String  message, mobileNumber, sender, cameFromActivity, sudanOr, apiKey, token, url, url2;


    private void sendSms(){

        try {

            Random random = new Random();
            randomNumber = random.nextInt(999999);

            //check avilabilty of sudan cart
            message ="<#> Your verification code is " + randomNumber ;

            if (mobileNumber.startsWith("+249")) {

                try {
                    // Construct data


                    String user = "?user=" + apiKey;
                    String pass = "&pwd=" + token;
                    String text1 = "&smstext=" + message;
                    String sender2 = "&Sender=" + sender;
                    String numbers = "&Nums=" + mobileNumber.replace("+","");


                   // String url = "http://212.0.129.229/bulksms/webacc.aspx";
                   // String encodedurl = URLEncoder.encode(url,"UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) new URL("http://212.0.129.229/bulksms/webacc.aspx" +  user + pass + text1 + sender2 + numbers + "").openConnection();

                  //  URLEncoder.encode("http://212.0.129.229/bulksms/webacc.aspx", "utf-8");
                    String data = user + pass + text1 + sender2 + numbers;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));

                   conn.getOutputStream().write(data.getBytes("UTF-8"));

                    //   final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //  final StringBuffer stringBuffer = new StringBuffer();
                    //  String line;


                    if ((conn.getResponseMessage().matches("OK"))){

                        //  if (line.matches("OK")) {
                        intent = new Intent(getApplicationContext(), VerifyActivity.class);

                        intent.putExtra("randomNumber", randomNumber);

                        intent.putExtra("mobileNumber", mobileNumber);
                        intent.putExtra("cameFromActivity", this.getClass().getSimpleName());

                        Log.d("here", "here");
                         }

                    //  Log.d("line" = rd.readLine()) != null) {
                    //     stringBuffer.append(line);
                    //    Log.d("line", line);







                    //  rd.close();
                    Log.d("respo",conn.getResponseMessage().toString());



                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error SMS " + e);
                }


                Intent intent2 = new Intent(getApplicationContext(), VerifyActivity.class);

                intent2.putExtra("randomNumber", randomNumber);

                intent2.putExtra("mobileNumber", mobileNumber);

                intent2.putExtra("cameFromActivity", this.getClass().getSimpleName());

            } else {


                OkHttpClient client = new OkHttpClient();

                String apiUrl = url + apiKey + url2;

                String base64EncodedCredentials = "Basic " + Base64.encodeToString((apiKey + ":" + token).getBytes(), Base64.NO_WRAP);

                RequestBody body = new FormBody.Builder()
                        .add("From", "+14142460442")
                        .add("To", mobileNumber)
                        .add("Body", message)
                        .build();

                Request request = new Request.Builder()
                        .url(apiUrl)
                        .post(body)
                        .header("Authorization", base64EncodedCredentials)
                        .build();

                // Response response = null;
                try {
                    Response response = client.newCall(request).execute();

                    Log.d("TAG", "sendSms: " + response.body().string());

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), "OTP SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();


           }
        } catch (Exception e) {

            //System.out.println("Error SMS "+e);
            // /return "Error "+e;
            Toast.makeText(getApplicationContext(), "ERROR SMS " + e, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "ERROR " + e, Toast.LENGTH_LONG).show();


            Log.d("error", e.toString());

        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);


      //  Toast.makeText(getApplicationContext(), "OTP SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();
            if (checkConnection.isNetwork()) {
                ParseAnalytics.trackAppOpenedInBackground(getIntent());


                nameEditText = findViewById(R.id.nameEditText);

                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();

                    if (extras == null) {
                        mobileNumber ="";
                        apiKey = "";
                        token = "";
                        url = "";
                        url2 = "";
                        sender = "";
                        cameFromActivity = "";
                        sudanOr = "";

                    } else {
                        mobileNumber = extras.getString("mobileNumber");
                        cameFromActivity = extras.getString("cameFromActivity");
                        apiKey = extras.getString("apiKey");
                        token = extras.getString("token");
                        url = extras.getString("url");
                        url2 = extras.getString("url2");
                        sender = extras.getString("sender");
                        sudanOr = extras.getString("sudanOr");
                    }
                }


                sendSms();


                intent = new Intent(getApplicationContext(), VerifyActivity.class);

                intent.putExtra("randomNumber", randomNumber);
                intent.putExtra("mobileNumber", mobileNumber);
                intent.putExtra("cameFromActivity", cameFromActivity);

                startActivity(intent);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }


    }
}