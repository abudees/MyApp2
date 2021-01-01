package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.time.Month;

public class MobileVerificationActivity extends AppCompatActivity {




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
