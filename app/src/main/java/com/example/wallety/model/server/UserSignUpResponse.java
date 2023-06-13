package com.example.wallety.model.server;

import com.example.wallety.model.User;
import com.google.gson.annotations.SerializedName;

public class UserSignUpResponse {
    @SerializedName("existingDetail")
    private String existingDetail;

    @SerializedName("user")
    private User user;

    public UserSignUpResponse(String existingDetail, User user) {
        this.existingDetail = existingDetail;
        this.user = user;
    }

    public String getExistingDetail() {
        return existingDetail;
    }

    public User getUser() {
        return user;
    }
}
