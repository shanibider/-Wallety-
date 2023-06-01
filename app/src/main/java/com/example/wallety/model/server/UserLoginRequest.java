package com.example.wallety.model.server;

import com.google.gson.annotations.SerializedName;

public class UserLoginRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
