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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StripeActivity extends AppCompatActivity {

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
            }
        });




        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            customerID= object.getString("id");
                            Toast.makeText(StripeActivity.this, customerID, Toast.LENGTH_SHORT).show();

                            getEphericalKey(customerID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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





    //getEphericalKey
    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey= object.getString("id");
                            Toast.makeText(StripeActivity.this, EphericalKey, Toast.LENGTH_SHORT).show();

                            getClientSecret(customerID,EphericalKey);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer "+SECRET_KEY);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(StripeActivity.this);
        requestQueue.add(stringRequest);
    }





//getClientSecret
    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret= object.getString("client_secret");
                            Toast.makeText(StripeActivity.this, ClientSecret, Toast.LENGTH_SHORT).show();

                            //after clicking the button
                            PaymentFlow();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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



}