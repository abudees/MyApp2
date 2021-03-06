package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.payment.paymentsdk.PaymentSdkActivity;
import com.payment.paymentsdk.PaymentSdkConfigBuilder;
import com.payment.paymentsdk.creditcard.view.customs.PaytabsEditText;
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

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class PayTabActivity extends AppCompatActivity  implements CallbackPaymentInterface {


    Intent intent;

    int mobile , orderNo;
    SqliteDatabase mDatabase ;
    ParseGeoPoint location;
    int[] cartProducts = new int[]{1,22};
    String myDate;
    int[] cartQty = new int[]{5654,545};

    String profileId , serverKey, clientKey , recipientMobile;
    PaymentSdkLanguageCode locale = PaymentSdkLanguageCode.EN;
    String screenTitle = "Test SDK";
    String cartId = "123456";
    String cartDesc = "cart description";
    String currency = "USD";
    double amount = 20.0;


    PaymentSdkTokenise tokeniseType;

    PaymentSdkTransactionType transType;

    PaymentSdkTokenFormat tokenFormat;

    PaymentSdkBillingDetails billingData;

    PaymentSdkShippingDetails shippingData;

    PaymentSdkConfigurationDetails configData;

    String username = ParseUser.getCurrentUser().getUsername();




    public void payee() {

}

    @Override
    public void onError(@NonNull PaymentSdkError paymentSdkError) {


        Log.d("here", paymentSdkError.toString());
    }

    @Override
    public void onPaymentCancel() {


        Log.d("here", "aah");

    }

    @Override
    public void onPaymentFinish(@NonNull PaymentSdkTransactionDetails paymentSdkTransactionDetails) {

        Log.d("here", "alhamdullilah");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Orders");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {





                    username = username.replaceAll("\\D", "");
                    mobile = 0;

                    mobile = Integer.parseInt(username) ;

                    orderNo = mobile + 11112222  + objects.size();
                    //  if (cart.size() > 0) {

                    ParseObject newOrder = new ParseObject("Orders");
                    newOrder.put("orderNo", orderNo);
                    newOrder.put("username", ParseUser.getCurrentUser().getUsername());
                    newOrder.put("deliveryDate", myDate);
                    //  newOrder.put("total", );
                    newOrder.put("exchangeRate", 1);
                    newOrder.put("deliveryLocation", location);
                    newOrder.put("totalQty", mDatabase.listAll().size());
                    newOrder.put("status", "n");
                    newOrder.put("recipientMobile", 655656565);
                    newOrder.put("voucher", 1337);

                    //  placeOrder.put("message", 1337);
                    //   placeOrder.put("voucher", 1337);

                    newOrder.saveInBackground();


                    for (int i = 0; i < cartProducts.length; i++) {

                        ParseObject newOrderItem = new ParseObject("OrdersDetails");

                        int a = cartProducts[i];
                        int b = cartQty[i];
                        newOrderItem.put("orderNo", orderNo);
                        newOrderItem.put("productNo", a);
                        newOrderItem.put("qty", b);

                        newOrderItem.saveInBackground();
                    }

                } else {

                    String username = ParseUser.getCurrentUser().getUsername();

                    username = username.replaceAll("\\D", "");
                    mobile = 0;

                    mobile = Integer.parseInt(username) + 11112222;

                    orderNo = mobile + 11112222;





                    ParseObject newOrder = new ParseObject("Orders");
                    newOrder.put("orderNo", orderNo);
                    newOrder.put("username", ParseUser.getCurrentUser().getUsername());
                    newOrder.put("deliveryDate", myDate);
                    //  newOrder.put("total", );
                    newOrder.put("exchangeRate", 1);
                    newOrder.put("deliveryLocation", location);
                    newOrder.put("totalQty", mDatabase.listAll().size());
                    newOrder.put("status", "n");
                    newOrder.put("recipientMobile", 655656565);




                    for (int i = 0; i < cartProducts.length; i++) {

                        ParseObject newOrderItem = new ParseObject("OrdersDetails");

                        int a = cartProducts[i];
                        int b = cartQty[i];
                        newOrderItem.put("orderNo", orderNo);
                        newOrderItem.put("productNo", a);
                        newOrderItem.put("qty", b);

                        newOrderItem.saveInBackground();

                        intent = new Intent(getApplicationContext(), CategoriesActivity.class);


                        intent.putExtra("orderNo", orderNo);

                        startActivity(intent);
                    }
                }
            }
        });
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_tab);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                myDate = "";

            } else {

                myDate = extras.getString("myDate");
            }
        }



        if (ParseUser.getCurrentUser() != null) {

            ParseQuery<ParseObject> query = new ParseQuery<>("APIs");

            query.whereEqualTo("name", "PayTabs");

            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {

                        for (ParseObject object : objects) {

                            profileId = object.getString("apiKey1");
                            serverKey = object.getString("apiKey2");
                            clientKey = object.getString("password");
                        }


                        tokeniseType = PaymentSdkTokenise.NONE; // tokenise is off

                        transType = PaymentSdkTransactionType.SALE;

                        tokenFormat = new PaymentSdkTokenFormat.Hex32Format();


                        billingData = new PaymentSdkBillingDetails(
                                "Dammam",
                                "SA",
                                "abudees@gmail.com",
                                "ahmed ali",
                                username, "eastern",
                                "street 6", "31432"
                        );

                        shippingData = new PaymentSdkShippingDetails(
                                "Khartoum",
                                "SD",
                                "abudees@gmail.com",
                                "ahmed ali",
                                recipientMobile, "eastern",
                                "street 6", "31432"
                        );
                        configData = new PaymentSdkConfigBuilder(profileId, serverKey, clientKey, amount, currency)
                                .setCartDescription(cartDesc)
                                .setLanguageCode(locale)
                                .setBillingData(billingData)
                                .setMerchantCountryCode("SD") // ISO alpha 2
                                .setShippingData(shippingData)
                                .setCartId(cartId)
                                .setTransactionType(transType)
                                .showBillingInfo(false)
                                .showShippingInfo(true)
                                .forceShippingInfo(true)
                                .setScreenTitle(screenTitle)
                                .build();
                        PaymentSdkActivity.startCardPayment(PayTabActivity.this, configData, PayTabActivity.this);


                    }
                }
            });
        }
    }
}