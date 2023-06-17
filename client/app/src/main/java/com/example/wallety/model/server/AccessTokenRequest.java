package com.example.wallety.model.server;

import com.example.wallety.model.CreditCard;
import com.google.gson.annotations.SerializedName;

public class AccessTokenRequest {

    @SerializedName("accessToken")
    private String accessToken;

    public AccessTokenRequest(String accessToken) {
        this.accessToken = accessToken;
    }
}
