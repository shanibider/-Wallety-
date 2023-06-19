package com.example.wallety.model.server;

import com.example.wallety.model.CreditCard;
import com.google.gson.annotations.SerializedName;

// the response structure for the API call to retrieve the credit card details
public class GetCreditCardResponse {
    @SerializedName("creditCard")
    private CreditCard creditCard;

    public CreditCard getCreditCard() {
        return creditCard;
    }
}
