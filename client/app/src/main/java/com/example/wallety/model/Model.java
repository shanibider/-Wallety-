package com.example.wallety.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wallety.R;
import com.example.wallety.model.server.LoggedInUserResponse;
import com.example.wallety.model.server.TransactionRequest;
import com.example.wallety.model.server.UserFetcherCon;
import com.example.wallety.model.server.UserLoginRequest;
import com.example.wallety.model.server.UserSignUpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

    public void fetchLoggedUser(Listener<Void> onSuccess, Listener<Void> onFailure) {
        UserFetcherCon.getLoggedInUser(new Callback<LoggedInUserResponse>() {
            @Override
            public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {
                if (response.isSuccessful() && response.body().getLoggedInUser() != null) {
                    User loggedInUser = response.body().getLoggedInUser();
                    setCurrentUser(loggedInUser);
                    onSuccess.onComplete(null);
                } else {
                    onFailure.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<LoggedInUserResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                onFailure.onComplete(null);
            }
        });
    }

    public User getCurrentUser() {
        return loggedUser;
    }

    public void setCurrentUser(User user) {
        loggedUser = user;
    }

    public void createUser(User user, Listener<Void> onSuccess, Listener<String> onFailure) {
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

    public void updateUser(User user, Listener<Void> listener) {


//        usersByIds.put(user.getId(), user);
        firebaseModel.updateUser(user, (Void) -> {
            listener.onComplete(null);
        });
    }

    public void loginUser(String email, String password, Listener<Void> onSuccess, Listener<Void> onError) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            onError.onComplete(null);
                            return;
                        }
//                      Get new FCM registration token
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

    public Boolean isNameNotExist(String name) {
        return usersByIds.values()
                .stream().noneMatch(user -> name.equals(user.getName()));
    }

    public List<Transaction> getParentUnusualExpenses(String parentId) {
        Transaction transaction1 = new Transaction("12gh", "21.05.2023", 300, "Supermarket", false);
        Transaction transaction2 = new Transaction("ffff", "20.05.2023", 350, "KSP", false);
        Transaction transaction3 = new Transaction("mmm", "18.05.2023", 420, "Shopping", false);
        List<Transaction> unusualExpenses = Arrays.asList(transaction1, transaction2, transaction3);

        return unusualExpenses;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "wallety")
                .setSmallIcon(R.drawable.wallety_logo)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
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

    public void makeTransaction(TransactionRequest transactionRequest, Listener<Void> onSuccess, Listener<Void> onFailure) {
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
}