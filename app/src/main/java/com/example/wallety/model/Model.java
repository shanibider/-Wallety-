package com.example.wallety.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance() {
        return _instance;
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();

    private User loggedUser = null;
    private HashMap<String, User> usersByIds = new HashMap<>();

    private Model() {
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    public void fetchLoggedUser(Listener<Void> listener) {
        firebaseModel.fetchLoggedInUser(user -> {
            setCurrentUser(user);
            if (user != null) {
                listener.onComplete(null);
            }
        });
    }

    public User getCurrentUser() {
        return loggedUser;
    }

    public void setCurrentUser(User user) {
        loggedUser = user;
    }

    public void createUser(User user, Listener<Void> listener) {
        firebaseModel.createUser(user, (Void) -> {
            usersByIds.put(user.getId(), user);
            setCurrentUser(user);
            listener.onComplete(null);
        });
    }

    public void updateUser(User user, Listener<Void> listener) {
        usersByIds.put(user.getId(), user);
        firebaseModel.updateUser(user, (Void) -> {
            listener.onComplete(null);
        });
    }

    public void loginUser(String email, String password, Listener<Void> onSuccess, Listener<Void> onError) {
        firebaseModel.loginUser(email, password,
                (Void) -> {
                    onSuccess.onComplete(null);
                },
                (Void) -> {
                    onError.onComplete(null);
                });
    }

    public void setUsersByIds(HashMap<String, User> hashMap) {
        usersByIds = hashMap;
    }

    public void fetchUsers(Listener<HashMap<String, User>> callback) {
        firebaseModel.getUsers(callback);
    }

    public String getUsernameById(String id) {
        return Objects.requireNonNull(usersByIds.get(id)).getUsername();
    }

    public Boolean isUsernameNotExist(String username) {
        return usersByIds.values()
                .stream().noneMatch(user -> username.equals(user.getUsername()));
    }

    public String areUsernameOrEmailNotExist(String username, String email) {
        AtomicReference<String> existingDetail = new AtomicReference<>("");
        usersByIds.values().forEach(user -> {
                    if (username.equals(user.getUsername())) {
                        existingDetail.set("Username");
                    }
                    if (email.equals(user.getEmail())) {
                        existingDetail.set("Email");
                    }
                }
        );
        return existingDetail.get();
    }
}