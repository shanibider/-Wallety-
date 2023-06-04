package com.example.wallety.model.server;

import com.google.gson.annotations.SerializedName;

public class UserLoginRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("registrationToken")
    private String registrationToken;

    public UserLoginRequest(String email, String password, String registrationToken) {
        this.email = email;
        this.password = password;
        this.registrationToken = registrationToken;
    }
}
