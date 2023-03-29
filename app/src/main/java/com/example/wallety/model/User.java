package com.example.wallety.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id = "";
    private String username;
    private String email;
    private String password;
    public Long lastUpdated;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String id, String username, String email, String password) {
        this(username, email, password);
        this.id = id;
    }

    static final String COLLECTION = "users";
    static final String ID = "id";
    static final String USERNAME = "username";
    static final String EMAIL = "email";
    static final String PASSWORD = "password";
    static final String LAST_UPDATED = "lastUpdated";

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static User fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String username = (String) json.get(USERNAME);
        String email = (String) json.get(EMAIL);
        String password = (String) json.get(PASSWORD);
        User user = new User(id, username, email, password);
        try {
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            user.setLastUpdated(time.getSeconds());
        } catch (Exception e) {

        }

        return user;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(USERNAME, getUsername());
        json.put(EMAIL, getEmail());
        json.put(PASSWORD, getPassword());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }
}
