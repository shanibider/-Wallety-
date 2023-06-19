package com.example.wallety.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @SerializedName("zScore")
    private double zScore;


    public Transaction(String id, int amount, String receiver, Boolean isUnusual) {
        this.id = id;
        this.amount = amount;
        this.receiver = receiver;
        this.isUnusual = isUnusual;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        String currentDate = formatter.format(new Date());
        this.date = currentDate;
    }

    static final String ID = "id";
    static final String DATE = "date";
    static final String AMOUNT = "amount";
    static final String RECEIVER = "receiver";
    static final String IS_UNUSUAL = "isUnusual";
    static final String Z_SCORE = "zScore";


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

    public void setUnusual(Boolean unusual) {
        isUnusual = unusual;
    }

    public void setZScore(double zScore) {
        this.zScore = zScore;
    }

    public double getZScore() {
        return zScore;
    }

    public static Transaction fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        int amount = Integer.parseInt((String) json.get(AMOUNT));
        String receiver = (String) json.get(RECEIVER);
        boolean isUnusual = Boolean.parseBoolean((String) json.get(IS_UNUSUAL));
        Transaction transaction = new Transaction(id, amount, receiver, isUnusual);

        return transaction;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(DATE, getDate());
        json.put(AMOUNT, getAmount());
        json.put(RECEIVER, getReceiver());
        json.put(IS_UNUSUAL, getIsUnusual());
        json.put(Z_SCORE, getZScore());

        return json;
    }

}