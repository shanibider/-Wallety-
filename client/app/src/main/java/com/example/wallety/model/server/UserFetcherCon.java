package com.example.wallety.model.server;

import com.example.wallety.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFetcherCon {
    private static final String BASE_URL = "http://192.168.1.21:3000/users/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final WalletyAPI api = retrofit.create(WalletyAPI.class);

    public static void getLoggedInUser(Callback<User> callback) {
        Call<User> call = api.getLoggedInUser();
        call.enqueue(callback);
    }

    public static void loginUser(UserLoginRequest userLoginRequest, Callback<User> callback) {
        Call<User> call = api.loginUser(userLoginRequest);
        call.enqueue(callback);
    }

    public static void signUpUser(User user, Callback<UserSignUpResponse> callback) {
        Call<UserSignUpResponse> call = api.signUpUser(user);
        call.enqueue(callback);
    }
}
