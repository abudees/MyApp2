package com.example.myapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String dateString = String.format("%1$ta %1$tb %1$td %1$tI:%1$tM:%1$tS GMT+03:00 %1$tY", calendar);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
          //  String temp = "Mon Sep 17 00:18:23 GMT+07:00 2012";
            Date expiry = formatter.parse(dateString);


        Log.d("numver: ", String.valueOf(expiry));
      //  ParseObject newOrder = new ParseObject("Orders");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Area");

// Retrieve the object by id
            query.getInBackground("AsgTdDRyIW", new GetCallback<ParseObject>() {
                public void done(ParseObject gameScore, ParseException e) {
                    if (e == null && gameScore != null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to your Parse Server. playerName hasn't changed.
                        gameScore.put("date", expiry);


                        gameScore.saveInBackground();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }










    }
}
