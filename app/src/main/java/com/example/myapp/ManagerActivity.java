package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.parse.ParseUser;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ManagerActivity extends AppCompatActivity {



    ImageView image_view;

    Button button;

    EditText editText;

    TextView logout;

    public void logout (View view){
        ParseUser.logOut();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        setTitle("Manager Activity");


        image_view = findViewById(R.id.imageView1);

        button = findViewById(R.id.button1);

        editText = findViewById(R.id.editText);

        logout = findViewById(R.id.textView1);


        if (ParseUser.getCurrentUser().toString().equals("")) {

            logout.setVisibility(View.INVISIBLE);
        } else {
            logout.setVisibility(View.VISIBLE);
        }






    }




}
