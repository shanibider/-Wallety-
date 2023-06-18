package com.example.wallety.model.server;

import com.google.gson.annotations.SerializedName;

public class AccessTokenRequest {
    @SerializedName("accessToken")
    private String accessToken;

    public AccessTokenRequest(String accessToken) {
        this.accessToken = accessToken;
    }
}
