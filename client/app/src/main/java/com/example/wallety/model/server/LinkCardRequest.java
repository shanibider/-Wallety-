package com.example.wallety.model.server;

import com.example.wallety.model.CreditCard;
import com.example.wallety.model.Transaction;
import com.google.gson.annotations.SerializedName;

public class LinkCardRequest {

    @SerializedName("creditCard")
    private CreditCard creditCard;

    public LinkCardRequest(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
