package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.time.Month;

public class MobileVerificationActivity extends  AppCompatActivity {





    public void verify (View view) {

        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);


        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();


        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });


    }




    public  String getCountryDialCode(){
        String countryID = null;
        String contryDialCode = null;
        Context context;

        TelephonyManager telephonyMngr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        countryID = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode = this.getResources().getStringArray(R.array.DialingCountryCode);
        for(int i=0; i<arrContryCode.length; i++){
            String[] arrDial = arrContryCode[i].split(",");
            if ( arrDial[1].trim().equals ( countryID.trim())){
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);


        Log.d(" this: ", getCountryDialCode());
    }
}
