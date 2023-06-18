package com.example.wallety.model.server;

import com.example.wallety.model.CreditCard;
import com.google.gson.annotations.SerializedName;

public class LinkCardRequest {
    @SerializedName("creditCard")
    private CreditCard creditCard;
    private String accessToken;

    public LinkCardRequest(CreditCard creditCard, String accessToken) {
        this.creditCard = creditCard;
        this.accessToken = accessToken;
    }
}
