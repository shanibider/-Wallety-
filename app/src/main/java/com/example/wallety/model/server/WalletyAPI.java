package com.example.wallety.model.server;

import com.example.wallety.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WalletyAPI {
    @GET("loggedInUser")
    Call<LoggedInUserResponse> getLoggedInUser();

    @POST("loginUser")
    Call<User> loginUser(@Body UserLoginRequest userLoginRequest);

    @POST("signUpUser")
    Call<UserSignUpResponse> signUpUser(@Body User user);

    @POST("makeTransaction")
    Call<ResponseBody> makeTransaction(@Body TransactionRequest transactionRequest);
}