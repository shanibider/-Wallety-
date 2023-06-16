package com.example.wallety.model.server;

import com.example.wallety.model.User;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFetcherCon {
    private static final Dotenv dotenv = Dotenv.configure().directory("./assets").filename("env").load();
    private static final String BASE_URL = String.format("%s/users/", dotenv.get("SERVER_URL"));
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final WalletyAPI api = retrofit.create(WalletyAPI.class);

    public static void getLoggedInUser(Callback<LoggedInUserResponse> callback) {
        Call<LoggedInUserResponse> call = api.getLoggedInUser();
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

    public static void makeTransaction(TransactionRequest transactionRequest, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = api.makeTransaction(transactionRequest);
        call.enqueue(callback);
    }

    public static void linkCard(LinkCardRequest request, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = api.linkCard(request);
        call.enqueue(callback);
    }
}
