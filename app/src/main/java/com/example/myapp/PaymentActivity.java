package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.payment.paymentsdk.PaymentSdkActivity;
import com.payment.paymentsdk.PaymentSdkConfigBuilder;
import com.payment.paymentsdk.integrationmodels.PaymentSdkBillingDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkConfigurationDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkError;
import com.payment.paymentsdk.integrationmodels.PaymentSdkLanguageCode;
import com.payment.paymentsdk.integrationmodels.PaymentSdkShippingDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenFormat;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenise;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionType;
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface;

import org.apache.http.auth.AUTH;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaymentActivity extends AppCompatActivity  implements CallbackPaymentInterface {


    String profileId = "PROFILE_ID";
    String serverKey = "SERVER_KEY";
    String clientKey = "CLIENT_KEY";
    PaymentSdkLanguageCode locale = PaymentSdkLanguageCode.EN;
    String screenTitle = "Test SDK";
    String cartId = "123456";
    String cartDesc = "cart description";
    String currency = "AED";
    double amount = 20.0;

    PaymentSdkTokenise tokeniseType = PaymentSdkTokenise.NONE; // tokenise is off
  //  or PaymentSdkTokenise.USER_OPTIONAL // tokenise if optional as per user approval
   // or PaymentSdkTokenise.USER_MANDATORY // tokenise is forced as per user approval
  //  or PaymentSdkTokenise.MERCHANT_MANDATORY // tokenise is forced without user approval

    PaymentSdkTransactionType transType = PaymentSdkTransactionType.SALE;
   // or PaymentSdkTransactionType.AUTH


    PaymentSdkTokenFormat tokenFormat = new PaymentSdkTokenFormat.Hex32Format();
  //  or new PaymentSdkTokenFormat.NoneFormat()
  //  or new PaymentSdkTokenFormat.AlphaNum20Format()
  //  or new PaymentSdkTokenFormat.Digit22Format()
  //  or new PaymentSdkTokenFormat.Digit16Format()
  //  or new PaymentSdkTokenFormat.AlphaNum32Format()

    PaymentSdkBillingDetails billingData = new PaymentSdkBillingDetails(
            "City",
            "2 digit iso Country code",
            "email1@domain.com",
            "name ",
            "phone", "state",
            "address street", "zip"
    );

    PaymentSdkShippingDetails shippingData = new PaymentSdkShippingDetails(
            "City",
            "2 digit iso Country code",
            "email1@domain.com",
            "name ",
            "phone", "state",
            "address street", "zip"
    );
    PaymentSdkConfigurationDetails configData = new PaymentSdkConfigBuilder(profileId, serverKey, clientKey, amount, currency)
            .setCartDescription(cartDesc)
            .setLanguageCode(locale)
            .setBillingData(billingData)
            .setMerchantCountryCode("AE") // ISO alpha 2
            .setShippingData(shippingData)
            .setCartId(cartId)
            .setTransactionType(transType)
            .showBillingInfo(false)
            .showShippingInfo(true)
            .forceShippingInfo(true)
            .setScreenTitle(screenTitle)
            .build();

    public void pay (View view){


        PaymentSdkActivity.startCardPayment(this, configData, this);
    }



    @Override
    public void onError(@NotNull PaymentSdkError paymentSdkError) {

    }

    @Override
    public void onPaymentCancel() {

    }

    @Override
    public void onPaymentFinish(@NotNull PaymentSdkTransactionDetails paymentSdkTransactionDetails) {

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        ParseQuery<ParseObject> queryAPIs = new ParseQuery<>("APIs");

        queryAPIs.whereEqualTo("name", "paytabsTest");

        queryAPIs.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        profileId = object.getString("url");

                        serverKey = object.getString("accountSID");
                        clientKey = object.getString("authToken");

                    }
                }
            }
        });

        /*
        ParseQuery<ParseObject> querySDKs = new ParseQuery<>("APIs");


        querySDKs.whereEqualTo("name", "patabsSDKtest");
        querySDKs.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject object : objects) {

                        profileId = object.getString("url");

                        serverKey = object.getString("accountSID");
                        clientKey = object.getString("authToken");
                    }
                }
            }
        });
*/




        Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
        in.putExtra("pt_merchant_email", "merchant@myapp.com");
        //this a demo account for testing the sdk
        in.putExtra("pt_secret_key",
                "oIUhj8mssa9rTWRAqHg4P9ECOcfs35lsOgJ7p6ARgJjaFbK6S1aIbOlZ1As5GNxu4hCtnclEWEOCPzIIBSrMGMMImeN22kx6C9zZ");
                        //Add your Secret Key Here
                in.putExtra("pt_transaction_title", "Mr. John Doe");
        in.putExtra("pt_amount", "1");
        in.putExtra("pt_currency_code", "USD"); //Use Standard 3 character ISO
        in.putExtra("pt_shared_prefs_name", "myapp_shared"); // write a name of the shared folder between your App and PayTabs SDK
        in.putExtra("pt_customer_email", "test@example.com");
        in.putExtra("pt_customer_phone_number", "0097300001");
        in.putExtra("pt_order_id", "1234567");
        in.putExtra("pt_product_name", "Samsung Galaxy S6");
        in.putExtra("pt_timeout_in_seconds", "300"); //Optional

        // Billing Address
        in.putExtra("pt_address_billing", "Flat 1,Building 123, Road 2345");
        in.putExtra("pt_city_billing", "Juffair");
        in.putExtra("pt_state_billing", "Manama");
        in.putExtra("pt_country_billing", "Bahrain");
        in.putExtra("pt_postal_code_billing", "00973"); //Put Country Phone code if Postal code not available '00973'//

        // Shipping Address
        in.putExtra("pt_address_shipping", "Flat 1,Building 123, Road 2345");
        in.putExtra("pt_city_shipping", "Juffair");
        in.putExtra("pt_state_shipping", "Manama");
        in.putExtra("pt_country_shipping", "Bahrain");
        in.putExtra("pt_postal_code_shipping", "00973"); //Put Country Phone code if Postal code not available '00973'




        startActivity(in);


    }
}