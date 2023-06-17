package com.example.wallety.model.server;

import com.example.wallety.model.CreditCard;
import com.example.wallety.model.User;
import com.google.gson.annotations.SerializedName;

public class GetCreditCardResponse {

    @SerializedName("creditCard")
    private CreditCard creditCard;

    public CreditCard getCreditCard() {
        return creditCard;
    }
}