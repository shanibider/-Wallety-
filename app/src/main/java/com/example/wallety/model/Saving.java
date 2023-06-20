package com.example.wallety.model;

import com.google.gson.annotations.SerializedName;

public class Saving {

    @SerializedName("id")
    private String id;

    @SerializedName("goal")
    private String goal;

    @SerializedName("details")
    private String details;

    @SerializedName("amount")
    private int amount;

    @SerializedName("currentAmount")
    private int currentAmount;

    public Saving(String id, String goal, String detail, int amount, int currentAmount) {
        this.id = id;
        this.goal = goal;
        this.details = detail;
        this.amount = amount;
        this.currentAmount = currentAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void increaseCurrentAmount(int amount) {
        currentAmount += amount;
    }
}
