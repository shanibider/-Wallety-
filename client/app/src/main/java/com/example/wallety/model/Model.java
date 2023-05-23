package com.example.wallety.model;

import android.util.Log;

import com.example.wallety.model.server.LoggedInUserResponse;
import com.example.wallety.model.server.UserFetcherCon;
import com.example.wallety.model.server.UserLoginRequest;
import com.example.wallety.model.server.UserSignUpResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance() {
        return _instance;
    }

    private FirebaseModel firebaseModel = new FirebaseModel();
    private User loggedUser = null;
    private HashMap<String, User> usersByIds = new HashMap<>();

    private Model() {
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    public void fetchLoggedUser(Listener<Void> osSuccess, Listener<Void> onFailure) {
        UserFetcherCon.getLoggedInUser(new Callback<LoggedInUserResponse>() {
            @Override
            public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {
                if (response.isSuccessful() && response.body().getLoggedInUser() != null) {
                    User loggedInUser = response.body().getLoggedInUser();
                    setCurrentUser(loggedInUser);
                    osSuccess.onComplete(null);
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
        usersByIds.put(user.getId(), user);
        firebaseModel.updateUser(user, (Void) -> {
            listener.onComplete(null);
        });
    }

    public void loginUser(String email, String password, Listener<Void> onSuccess, Listener<Void> onError) {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
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
        Transaction transaction1 = new Transaction("12gh", "Supermarket", 300, "21.05.2023", "John");
        Transaction transaction2 = new Transaction("ffff", "KSP", 350, "20.05.2023", "Adam Cohen");
        Transaction transaction3 = new Transaction("mmm", "Shopping", 420, "18.05.2023", "Shir Choki");
        List<Transaction> unusualExpenses = Arrays.asList(transaction1, transaction2, transaction3);

        return unusualExpenses;
    }
}