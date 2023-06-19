package com.example.wallety.model.server;

import com.example.wallety.model.User;

import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// connector to the server  by creating Retrofit instance and providing methods to make API requests
public class UserFetcherCon {
    // initializes the Retrofit instance with the base URL of the server and a Gson converter factory
    private static final Dotenv dotenv = Dotenv.configure().directory("./assets").filename("env").load();
    private static final String BASE_URL = String.format("%s/users/", dotenv.get("SERVER_URL"));
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final WalletyAPI api = retrofit.create(WalletyAPI.class);


    // The methods in this class enqueue the API calls using Retrofit and provide the callback for handling the response
    public static void loginUser(UserLoginRequest userLoginRequest, Callback<User> callback) {
        Call<User> call = api.loginUser(userLoginRequest);
        call.enqueue(callback);
    }

    public static void signUpUser(User user, Callback<UserSignUpResponse> callback) {
        Call<UserSignUpResponse> call = api.signUpUser(user);
        call.enqueue(callback);
    }

    public static void getChildrenWithoutParent(Callback<List<User>> callback) {
        Call<List<User>> call = api.getChildrenWithoutParent();
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

    public static void linkCardToChild(LinkCardToChildRequest request, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = api.linkCardToChild(request);
        call.enqueue(callback);
    }

}
