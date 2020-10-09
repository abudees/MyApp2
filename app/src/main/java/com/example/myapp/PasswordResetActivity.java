package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.io.IOException;

public class PasswordResetActivity extends AppCompatActivity {

    EditText editText, editText2;



    public void passReset(View view){

        ParseUser.requestPasswordResetInBackground(String.valueOf(editText.getText()), new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // An email was successfully sent with reset instructions.



                } else {
                    // Something went wrong. Look at the ParseException to see what's up.
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        try {
            IsNetworkAvailable checkConnection = new IsNetworkAvailable();

            if (checkConnection.isNetwork()) {

                editText = findViewById(R.id.editText2);
                editText2 = findViewById(R.id.editText3);

            }
        }

        catch (InterruptedException | IOException e) {
                e.printStackTrace();
        }
    }
}

