package com.example.wallety.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class User {
    @SerializedName("id")
    private String id = "";

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("isParent")
    private boolean isParent;

    @SerializedName("registrationToken")
    public String registrationToken;

    @SerializedName("lastUpdated")
    public Long lastUpdated;

    public User(String name, String phone, String email, String password, boolean isParent) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.isParent = isParent;
    }

    public User(String id, String name, String phone, String email, String password, boolean isParent) {
        this(name, email, password, phone, isParent);
        this.id = id;
    }

    public static final String COLLECTION = "users";
    static final String ID = "id";
    static final String USER = "name";
    static final String PHONE = "phone";
    static final String EMAIL = "email";
    static final String PASSWORD = "password";
    static final String IS_PARENT = "isParent";
    static final String LAST_UPDATED = "lastUpdated";

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

//
    public static User fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String name = (String) json.get(USER);
        String phone = (String) json.get(PHONE);
        String email = (String) json.get(EMAIL);
        String password = (String) json.get(PASSWORD);
        boolean isParent = Boolean.parseBoolean((String) json.get(IS_PARENT));
        User user = new User(id, name, phone, email, password, isParent);
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
        json.put(USER, getName());
        json.put(PHONE, getPhone());
        json.put(IS_PARENT, getIsParent());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }
}
