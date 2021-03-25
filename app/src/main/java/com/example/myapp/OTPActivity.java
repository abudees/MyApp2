package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OTPActivity extends AppCompatActivity/* implements View.OnClickListener */{
    private EditText phoneNumber;
    private Button sendOtpBtn;
    char[] otp;

    Intent intent;




    Editable n;

    public void verifySMS (View view){


        try {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(
                    "https://api.twilio.com/2010-04-01/Accounts/ACc2050a7f1942814404b2e15d8f74f9f2/SMS/Messages");
            String base64EncodedCredentials = "Basic "
                    + Base64.encodeToString(
                    ("ACc2050a7f1942814404b2e15d8f74f9f2" + ":" + "767f698a63deb1a7f057a9fd8d4be8e0").getBytes(),
                    Base64.NO_WRAP);

            httppost.setHeader("Authorization",
                    base64EncodedCredentials);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("From",
                    "+14142460442"));
            nameValuePairs.add(new BasicNameValuePair("To",
                    "+" + n ));
            nameValuePairs.add(new BasicNameValuePair("Body",
                    "Welcome to Twilio"));

            httppost.setEntity(new UrlEncodedFormEntity(
                    nameValuePairs));



            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            System.out.println("Entity post is: "
                    + EntityUtils.toString(entity));

            intent = new Intent(getApplicationContext(), OTPVerificationActivity.class);



            startActivity(intent);


        } catch (IOException e) {

            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        phoneNumber = findViewById(R.id.phoneNumber);
        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        n= phoneNumber.getText();

     //   sendOtpBtn.setOnClickListener(this);


    }
/*
    @Override
    public void onClick(View v) {
        Random random = new Random();


        //Generate random integer in range 0 to 9

        otp = new char[4];
        for (int i = 0; i < 4; i++) {
            otp[i] = (char) (random.nextInt(10) + 48);
        }

//        Toast.makeText(getApplicationContext(), String.valueOf(otp), Toast.LENGTH_SHORT).show();

        String number = phoneNumber.getText().toString();

        if (!(phoneNumber.getText().toString().equals(""))) {

            intent = new Intent(getApplicationContext(), OTPVerificationActivity.class);



            startActivity(intent);

            new MyAsyncTask().execute("https://api.msg91.com/api/sendhttp.php?route=4&sender=TESTIN&message=OTP for your OTP Verification App is : " + String.valueOf(otp) + "&country=91&flash=&unicode=&mobiles=" + number + "&authkey=297116AFCGQdLuvdm25d96f3f7");
        } else {
            phoneNumber.setError("Please Enter your mobile number");
        }


    }*/

    public class MyAsyncTask extends AsyncTask implements com.example.myapp.MyAsyncTask {
        URL Url;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream;


        @Override
        public Void doInBackground(String... urls) {

            try {
                Url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection)Url.openConnection();
                inputStream = httpURLConnection.getInputStream();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(OTPActivity.this, OTPVerificationActivity.class);
            intent.putExtra("otp_number",String.valueOf(otp));
            startActivity(intent);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}