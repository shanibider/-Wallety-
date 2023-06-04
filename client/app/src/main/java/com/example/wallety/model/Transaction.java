package com.example.wallety.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Transaction {
    @SerializedName("id")
    private String id;

    @SerializedName("date")
    private String date;

    @SerializedName("amount")
    private int amount;

    @SerializedName("receiver")
    private String receiver;

    @SerializedName("isUnusual")
    private Boolean isUnusual;

    public Transaction(String id, String date, int amount, String receiver, Boolean isUnusual) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.receiver = receiver;
        this.isUnusual = isUnusual;
    }

    static final String ID = "id";
    static final String DATE = "date";
    static final String AMOUNT = "amount";
    static final String RECEIVER = "receiver";
    static final String IS_UNUSUAL = "isUnusual";

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public Boolean getIsUnusual() {
        return isUnusual;
    }

    public static Transaction fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String date = (String) json.get(DATE);
        int amount = Integer.parseInt((String) json.get(AMOUNT));
        String receiver = (String) json.get(RECEIVER);
        boolean isUnusual = Boolean.parseBoolean((String) json.get(IS_UNUSUAL));
        Transaction transaction = new Transaction(id, date, amount, receiver, isUnusual);

        return transaction;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(DATE, getDate());
        json.put(AMOUNT, getAmount());
        json.put(RECEIVER, getReceiver());
        json.put(IS_UNUSUAL, getIsUnusual());

        return json;
    }
}
