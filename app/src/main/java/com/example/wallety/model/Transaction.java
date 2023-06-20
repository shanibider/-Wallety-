package com.example.wallety.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @SerializedName("saving")
    private Saving saving;

    @SerializedName("childReceiver")
    private User childReceiver;


    public Transaction(int amount, String receiver, Boolean isUnusual) {
        this.amount = amount;
        this.receiver = receiver;
        this.isUnusual = isUnusual;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        String currentDate = formatter.format(new Date());
        this.date = currentDate;
    }

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

    public Saving getSaving() {
        return saving;
    }

    public User getChildReceiver() {
        return childReceiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSaving(Saving saving) {
        this.saving = saving;
    }

    public void setChildReceiver(User childReceiver) {
        this.childReceiver = childReceiver;
    }

    public void setUnusual(Boolean unusual) {
        isUnusual = unusual;
    }
}