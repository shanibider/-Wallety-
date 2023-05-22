package com.example.wallety.model.server;

import com.example.wallety.model.User;
import com.google.gson.annotations.SerializedName;

public class LoggedInUserResponse {

    @SerializedName("loggedInUser")
    private User loggedInUser;

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
