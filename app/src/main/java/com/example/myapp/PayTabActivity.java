package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class PayTabActivity extends AppCompatActivity  implements CallbackPaymentInterface {


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_tab);


        String profileId = "67603";
        String serverKey = "SSJJNRLZLRK-JBMHH6KLHK-KLGDKW9RGL";
        String clientKey = "C2KM6M-GQGD62-NHHGKB-BTR6BD";
        PaymentSdkLanguageCode locale = PaymentSdkLanguageCode.EN;
        String screenTitle = "Test SDK";
        String cartId = "123456";
        String cartDesc = "cart description";
        String currency = "AED";
        double amount = 20.0;

        PaymentSdkTokenise tokeniseType = PaymentSdkTokenise.NONE; // tokenise is off


        PaymentSdkTransactionType transType = PaymentSdkTransactionType.SALE;



        PaymentSdkTokenFormat tokenFormat = new PaymentSdkTokenFormat.Hex32Format();


        PaymentSdkBillingDetails billingData = new PaymentSdkBillingDetails(
                "Dammam",
                "SA",
                "abudees@gmail.com",
                "ahmed ali",
                "00966547414030", "eastern",
                "street 6", "31432"
        );

        PaymentSdkShippingDetails shippingData = new PaymentSdkShippingDetails(
                "Dammam",
                "SA",
                "abudees@gmail.com",
                "ahmed ali",
                "00966547414030", "eastern",
                "street 6", "31432"
        );
        PaymentSdkConfigurationDetails configData = new PaymentSdkConfigBuilder(profileId, serverKey, clientKey, amount, currency)
                .setCartDescription(cartDesc)
                .setLanguageCode(locale)
                .setBillingData(billingData)
                .setMerchantCountryCode("AE") // ISO alpha 2
                .setShippingData(shippingData)
                .setCartId(cartId)
                .setTransactionType(transType)
                .showBillingInfo(true)
                .showShippingInfo(true)
                .forceShippingInfo(true)
                .setScreenTitle(screenTitle)
                .build();
        PaymentSdkActivity.startCardPayment(this, configData, PayTabActivity.this);





    }

    @Override
    public void onError(@NonNull PaymentSdkError paymentSdkError) {


        Log.d("here", paymentSdkError.toString());
    }

    @Override
    public void onPaymentCancel() {


    }

    @Override
    public void onPaymentFinish(@NonNull PaymentSdkTransactionDetails paymentSdkTransactionDetails) {

        Log.d("here", "alhamdullilah");

    }
}