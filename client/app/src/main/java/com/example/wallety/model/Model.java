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

    public void fetchLoggedUser(Listener<Void> osSuccess, Listener<Void> onFailure) {
        firebaseModel.fetchLoggedInUser(user -> {
            setCurrentUser(user);
            if (user != null) {
                osSuccess.onComplete(null);
            } else {
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

    public void createUser(User user, Listener<Void> listener, Listener<String> onFailure) {
        firebaseModel.handleUserCreation(user,
                (Void) -> {
                    usersByIds.put(user.getId(), user);
                    setCurrentUser(user);
                    listener.onComplete(null);
                },
                onFailure);
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

    public String getNameById(String id) {
        return Objects.requireNonNull(usersByIds.get(id)).getName();
    }

    public Boolean isNameNotExist(String name) {
        return usersByIds.values()
                .stream().noneMatch(user -> name.equals(user.getName()));
    }

    public String areNameOrEmailNotExist(String name, String email) {
        AtomicReference<String> existingDetail = new AtomicReference<>("");
        usersByIds.values().forEach(user -> {
                    if (name.equals(user.getName())) {
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