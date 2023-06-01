package com.example.wallety.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Transaction {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("amount")
    private int amount;

    @SerializedName("date")
    private String date;

//    demo
    private String child;
//

    public Transaction(String id, String name, int amount, String date, String child) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.child = child;
    }

    static final String COLLECTION = "transactions";
    static final String ID = "id";
    static final String NAME = "name";
    static final String AMOUNT = "amount";
    static final String DATE = "date";

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getChild() {
        return child;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static Transaction fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String name = (String) json.get(NAME);
        int amount = Integer.parseInt((String) json.get(AMOUNT));
        String date = (String) json.get(DATE);
//
        String child = (String) json.get("");
//
        Transaction transaction = new Transaction(id, name, amount, date, child);
        try {
//            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
//            user.setLastUpdated(time.getSeconds());
        } catch (Exception e) {

        }

        return transaction;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(AMOUNT, getAmount());
        json.put(DATE, getDate());
//        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }
}
