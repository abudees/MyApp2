package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DriverBarcodeActivity extends AppCompatActivity {

    ImageView driverQR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_barcode);

        try {
            CheckConnection checkConnection = new CheckConnection();

            if (checkConnection.isNetworkAvailable()) {


                driverQR = findViewById(R.id.driverBarcodeImage);


                QRGEncoder qrgEncoder = new QRGEncoder(ParseUser.getCurrentUser().getUsername(), null, QRGContents.Type.TEXT, 10);

                try {
                    Bitmap qrbite = qrgEncoder.encodeAsBitmap();
                    driverQR.setImageBitmap(qrbite);
                } catch (WriterException error) {
                    error.printStackTrace();
                }


            }
        } catch (InterruptedException | IOException e) {

            e.printStackTrace();
    }

    }
}
