package com.example.wallety.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallety.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.net.ApiResource;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class StripeActivity extends AppCompatActivity {

    Button payment;
    String PublishableKey = "pk_test_51N4RlUKjV4uSO0PohSW6fr6ZYyl3fhFkomCz5Ggr1U9WFoeR9K9LzWUX0Tk6wI1zuKcsJJsVmOaLuZpCiahSoXSM00icmIQKmh";
    String Secretkey = "sk_test_51N4RlUKjV4uSO0PouNXJrTAX3sTTRIvxnkTK98cOaAy4P4UpoJ0WOOWF5fYZA7ffoxuCm6NinzhFEJEFo2Gfrn4o008Bjto8Oy";
    String CustomerId;
    String EphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_stripe);

        payment = findViewById(R.id.btn);

        // Initialize PaymentConfiguration with PublishableKey
        PaymentConfiguration.init(this, PublishableKey);

        // Initialize PaymentSheet
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        // Set OnClickListener for payment button
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentFlow();
            }
        });


        // Request to create a customer in the Stripe API
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            CustomerId = object.getString("id");
                            Toast.makeText(StripeActivity.this, CustomerId, Toast.LENGTH_SHORT).show();
                            getEphericalKey();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StripeActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                // Set Authorization header with Secretkey
                Map<String,String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + Secretkey);
                return header;
            }
        };

        // Make the request to create a customer
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);



    }

    //paymentFlow method
    private void paymentFlow() {
        // Present PaymentSheet with the provided ClientSecret and configuration
        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Wallety", new PaymentSheet.CustomerConfiguration(
                CustomerId,
                EphericalKey
        )));
    }

    // onPaymentResult method
    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        // Check if the payment was completed successfully
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        }
    }







    // getEphericalKey method
    private void getEphericalKey() {
        // Request to create an ephemeral key in the Stripe API
        // (This key used to authenticate and authorize the client's interaction with the API)
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);

                            EphericalKey = object.getString("id");

                            Toast.makeText(StripeActivity.this, CustomerId, Toast.LENGTH_SHORT).show();

                            getClientSecret (CustomerId, EphericalKey);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StripeActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{

                Map<String,String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + Secretkey);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }







    //getClientSecret
    private void getClientSecret(String customerId, String ephericalKey) {

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);

                            ClientSecret = object.getString("client_secret");

                            Toast.makeText(StripeActivity.this, ClientSecret, Toast.LENGTH_SHORT).show();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StripeActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{

                Map<String,String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + Secretkey);
                return header;
            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                params.put("amount", "500"+"00");
                params.put("currency", "USD");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}