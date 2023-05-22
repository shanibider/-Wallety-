package com.example.wallety.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
<<<<<<< HEAD

=======
>>>>>>> stripe-payment
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
<<<<<<< HEAD

=======
>>>>>>> stripe-payment
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
<<<<<<< HEAD

import org.json.JSONException;
import org.json.JSONObject;

=======
import com.stripe.net.ApiResource;

import org.json.JSONException;
import org.json.JSONObject;
>>>>>>> stripe-payment
import java.util.HashMap;
import java.util.Map;

public class StripeActivity extends AppCompatActivity {

<<<<<<< HEAD
    AppCompatButton button;
    String SECRET_KEY="sk_test_51N4RlUKjV4uSO0PouNXJrTAX3sTTRIvxnkTK98cOaAy4P4UpoJ0WOOWF5fYZA7ffoxuCm6NinzhFEJEFo2Gfrn4o008Bjto8Oy";
    String PUBLISH_KEY="pk_live_51N4RlUKjV4uSO0PohHEC00ZSa1pgoZJnEF1Baer9fZdVAcMDUWNoSxNHmoWe6Xut6WHJElMONHwXvO9M7JYghXTL00B8IJ50xL";
    PaymentSheet paymentSheet;

    String customerID;
    String EphericalKey;
    String ClientSecret;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe);

        button = findViewById(R.id.btn);

        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, this::onPaymentResult);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentFlow();
=======
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
>>>>>>> stripe-payment
            }
        });


<<<<<<< HEAD


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
=======
        // Request to create a customer in the Stripe API
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
>>>>>>> stripe-payment
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

<<<<<<< HEAD
                        try {
                            JSONObject object = new JSONObject(response);
                            customerID= object.getString("id");
                            Toast.makeText(StripeActivity.this, customerID, Toast.LENGTH_SHORT).show();

                            getEphericalKey(customerID);
=======
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            CustomerId = object.getString("id");
                            Toast.makeText(StripeActivity.this, CustomerId, Toast.LENGTH_SHORT).show();
                            getEphericalKey();

>>>>>>> stripe-payment

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
<<<<<<< HEAD

=======
>>>>>>> stripe-payment
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
<<<<<<< HEAD

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StripeActivity.this);
        requestQueue.add(stringRequest);
    }






    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
    if (paymentSheetResult instanceof PaymentSheetResult.Completed){
        Toast.makeText(this, "payment success", Toast.LENGTH_SHORT).show();
    }

    }
=======
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

>>>>>>> stripe-payment





<<<<<<< HEAD
    //getEphericalKey
    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
=======

    // getEphericalKey method
    private void getEphericalKey() {
        // Request to create an ephemeral key in the Stripe API
        // (This key used to authenticate and authorize the client's interaction with the API)
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
>>>>>>> stripe-payment
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

<<<<<<< HEAD
                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey= object.getString("id");
                            Toast.makeText(StripeActivity.this, EphericalKey, Toast.LENGTH_SHORT).show();

                            getClientSecret(customerID,EphericalKey);
=======
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);

                            EphericalKey = object.getString("id");

                            Toast.makeText(StripeActivity.this, CustomerId, Toast.LENGTH_SHORT).show();

                            getClientSecret (CustomerId, EphericalKey);


>>>>>>> stripe-payment

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
<<<<<<< HEAD

=======
>>>>>>> stripe-payment
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
<<<<<<< HEAD

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+SECRET_KEY);
=======
                Toast.makeText(StripeActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{

                Map<String,String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + Secretkey);
>>>>>>> stripe-payment
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

<<<<<<< HEAD
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
=======

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
>>>>>>> stripe-payment
                return params;
            }
        };

<<<<<<< HEAD
        RequestQueue requestQueue = Volley.newRequestQueue(StripeActivity.this);
        requestQueue.add(stringRequest);
=======

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
>>>>>>> stripe-payment
    }





<<<<<<< HEAD
//getClientSecret
    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
=======


    //getClientSecret
    private void getClientSecret(String customerId, String ephericalKey) {

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
>>>>>>> stripe-payment
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

<<<<<<< HEAD
                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret= object.getString("client_secret");
                            Toast.makeText(StripeActivity.this, ClientSecret, Toast.LENGTH_SHORT).show();

                            //after clicking the button
                            PaymentFlow();
=======
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);

                            ClientSecret = object.getString("client_secret");

                            Toast.makeText(StripeActivity.this, ClientSecret, Toast.LENGTH_SHORT).show();


>>>>>>> stripe-payment


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
<<<<<<< HEAD

=======
>>>>>>> stripe-payment
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
<<<<<<< HEAD

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+SECRET_KEY);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", "1000"+"00");
                params.put("currency", "usd");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StripeActivity.this);
        requestQueue.add(stringRequest);

    }




    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(ClientSecret,
                new PaymentSheet.Configuration("wallety"
                        ,new PaymentSheet.CustomerConfiguration(customerID, EphericalKey))
        );
    }



=======
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
>>>>>>> stripe-payment
}