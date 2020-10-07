package com.example.myapp;

import java.io.IOException;

public class CheckConnection {

    public boolean isNetworkAvailable() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}
