package com.example.myapp;

import java.io.IOException;

public class IsNetworkAvailable {

    public boolean isNetwork() throws InterruptedException, IOException {

      //  final String command = "ping -c 1 google.com";
     //  return Runtime.getRuntime().exec(command).waitFor() == 0;

       return true;
    }


    public boolean isParseServerAvailable() throws InterruptedException, IOException {

        return true;

    }
}
