package com.example.wallety.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wallety.R;
import com.example.wallety.model.server.LinkCardRequest;
import com.example.wallety.model.server.TransactionRequest;
import com.example.wallety.model.server.UserFetcherCon;
import com.example.wallety.model.server.UserLoginRequest;
import com.example.wallety.model.server.UserSignUpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model extends FirebaseMessagingService {
    private static final Model _instance = new Model();

    public static Model instance() {
        return _instance;
    }

    private FirebaseModel firebaseModel = new FirebaseModel();
    private User loggedUser = null;

    private HashMap<String, User> usersByIds = new HashMap<>();

    public Model() {
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    // Get the currently logged-in user
    public User getCurrentUser() {
        return loggedUser;
    }

    // Set the currently logged-in user
    public void setCurrentUser(User user) {
        loggedUser = user;
    }

    // Create a new user on the server
    public void createUser(User user, Listener<Void> onSuccess, Listener<String> onFailure) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            onFailure.onComplete("");
                            return;
                        }
//                      Get new FCM registration token
                        String registrationToken = task.getResult();
                        user.setRegistrationToken(registrationToken);
                        UserFetcherCon.signUpUser(user, new Callback<UserSignUpResponse>() {
                            @Override
                            public void onResponse(Call<UserSignUpResponse> call, Response<UserSignUpResponse> response) {
                                if (response.isSuccessful()) {
                                    User user = response.body().getUser();
                                    if (user != null) {
                                        setCurrentUser(user);
                                        onSuccess.onComplete(null);
                                    } else {
                                        onFailure.onComplete(response.body().getExistingDetail());
                                    }
                                } else {
                                    onFailure.onComplete("");
                                }
                            }

                            @Override
                            public void onFailure(Call<UserSignUpResponse> call, Throwable t) {
                                Log.d("ERROR", t.getMessage());
                                onFailure.onComplete("");
                            }
                        });
                    }
                });
    }

    public void updateUser(User user, Listener<Void> listener) {


//        usersByIds.put(user.getId(), user);
        firebaseModel.updateUser(user, (Void) -> {
            listener.onComplete(null);
        });
    }

    // Log in a user with the provided email and password
    public void loginUser(String email, String password,
                          Listener<Void> onSuccess, Listener<Void> onError) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            onError.onComplete(null);
                            return;
                        }
                        // Get new FCM registration token
                        String registrationToken = task.getResult();

                        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password, registrationToken);
                        UserFetcherCon.loginUser(userLoginRequest, new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User loggedInUser = response.body();
                                    setCurrentUser(loggedInUser);
                                    onSuccess.onComplete(null);
                                } else {
                                    onError.onComplete(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.d("ERROR", t.getMessage());
                                onError.onComplete(null);
                            }
                        });
                    }
                });
    }

    public void setUsersByIds(HashMap<String, User> hashMap) {
        usersByIds = hashMap;
    }

    public void fetchUsers(Listener<HashMap<String, User>> callback) {
        firebaseModel.getUsers(callback);
    }

    public String getNameById(String id) {
        return Objects.requireNonNull(usersByIds.get(id)).getName();
    }

    // Check if a name does not exist in the usersByIds HashMap
    public Boolean isNameNotExist(String name) {
        return usersByIds.values()
                .stream().noneMatch(user -> name.equals(user.getName()));
    }

    // Unusual Expenses
    public List<Transaction> getParentUnusualExpenses(String parentId) {
        Transaction transaction1 = new Transaction("12gh",  300, "Supermarket", false);
        Transaction transaction2 = new Transaction("ffff", 350, "KSP", false);
        Transaction transaction3 = new Transaction("mmm", 420, "Shopping", false);
        List<Transaction> unusualExpenses = Arrays.asList(transaction1, transaction2, transaction3);

        return unusualExpenses;
    }

    public void getChildrenWithoutParent(Listener<List<User>> onSuccess, Listener<Void> onFailure) {
        UserFetcherCon.getChildrenWithoutParent(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> childrenWithoutParent = response.body();
                    onSuccess.onComplete(childrenWithoutParent);
                } else {
                    onFailure.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                onFailure.onComplete(null);
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // notification builder with the received data
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "wallety")
                .setSmallIcon(R.drawable.wallety_logo)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        // Notify the user with the created notification
        notificationManager.notify(123, builder.build());

        Log.d("TAG", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("TAG", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token);
    }

    public void makeTransaction(TransactionRequest transactionRequest,
                                Listener<Void> onSuccess, Listener<Void> onFailure) {
        // Make a transaction request to the server
        UserFetcherCon.makeTransaction(transactionRequest, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    onSuccess.onComplete(null);
                } else {
                    onFailure.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                onFailure.onComplete(null);
            }
        });
    }

    public void linkCard(LinkCardRequest request, Listener<Void> onSuccess, Listener<Void> onFailure) {
        UserFetcherCon.linkCard(request, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    onSuccess.onComplete(null);
                } else {
                    onFailure.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                onFailure.onComplete(null);
            }
        });
    }


    public void getUserCreditCard(AccessTokenRequest request, Listener<Void> onSuccess, Listener<Void> onFailure) {
        UserFetcherCon.getCreditCard(request, new Callback<GetCreditCardResponse>() {
            @Override
            public void onResponse(Call<GetCreditCardResponse> call, Response<GetCreditCardResponse> response) {
                if (response.isSuccessful() && response.body().getCreditCard() != null) {
                    CreditCard data = response.body().getCreditCard();
                    setCreditCard(data);
                    onSuccess.onComplete(null);
                } else {
                    onFailure.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<GetCreditCardResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                onFailure.onComplete(null);
            }
        });
    }


}