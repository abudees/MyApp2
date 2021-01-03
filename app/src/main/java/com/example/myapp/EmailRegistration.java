package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EmailRegistration extends AppCompatActivity {

    /*
    if (   emailEditText.getText().toString().matches(""))
    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if (passwordEditText.getText().length() >= 6 && passwordEditText.getText().length() < 15) {
            if (passwordEditText.getText().toString().matches(rePassword.getText().toString())) {

                user.setEmail(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
            }

            rePassword.setVisibility(View.INVISIBLE);
            mobileNumber.setVisibility(View.INVISIBLE);
            firstNameEditText.setVisibility(View.INVISIBLE);
            lastNameEditText.setVisibility(View.INVISIBLE);
            usernameEditText.getText();
            passwordEditText.getText();

        } else {
            Toast.makeText(LoginActivity.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
        }
    } else {
        Toast.makeText(this, "Password must be 6 digits or more", Toast.LENGTH_SHORT).show();
    }
} else {
        Toast.makeText(this, "email is invalid", Toast.LENGTH_SHORT).show();
        }
        */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);


        //emailEditText = findViewById(R.id.emailEditText);
    }
}
