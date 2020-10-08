package com.example.myapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);



        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

       // ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("49cd35a5b5f6610271ebdebb749464d7770ea2ad")
                // if defined
                .clientKey("36e7f81fcbe7caa26452001f6c6b31f6591f263c")
                .server("http://18.190.25.222:80/parse/")
                .build()
        );

        // ParseCrashReporting.enable(this);

    }
}
