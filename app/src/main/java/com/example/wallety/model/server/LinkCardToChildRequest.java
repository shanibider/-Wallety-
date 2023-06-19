package com.example.wallety.model.server;

import com.example.wallety.model.CreditCard;
import com.google.gson.annotations.SerializedName;

public class LinkCardToChildRequest {
    @SerializedName("creditCard")
    private CreditCard creditCard;

    @SerializedName("childName")
    private String childName;

    @SerializedName("amount")
    private int amount;
    private String accessToken;

    public LinkCardToChildRequest(CreditCard creditCard, String accessToken, String childName, int amount) {
        this.creditCard = creditCard;
        this.accessToken = accessToken;
        this.childName = childName;
        this.amount = amount;
    }
}
