package com.example.wallety.model.server;

import com.example.wallety.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

// API endpoints for making different HTTP requests
// @POST/@GET to specify the HTTP method, @Body to indicate the request body
public interface WalletyAPI {

    @POST("loginUser")
    Call<User> loginUser(@Body UserLoginRequest userLoginRequest);

    @POST("signUpUser")
    Call<UserSignUpResponse> signUpUser(@Body User user);

    @GET("childrenWithoutParent")
    Call<List<User>> getChildrenWithoutParent();

    @POST("makeTransaction")
    Call<ResponseBody> makeTransaction(@Body TransactionRequest transactionRequest);

    @POST("linkCard")
    Call<ResponseBody> linkCard(@Body LinkCardRequest request);
<<<<<<< HEAD

    @POST("creditCards")
    Call<GetCreditCardResponse> getCreditCard(@Body AccessTokenRequest request);

    @POST("linkCardToChild")
    Call<ResponseBody> linkCardToChild(@Body LinkCardToChildRequest request);

=======
>>>>>>> master
}