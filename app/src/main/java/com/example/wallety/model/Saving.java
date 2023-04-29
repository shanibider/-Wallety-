package com.example.wallety.model;

import android.widget.ProgressBar;

public class Saving {

    private String id;
    private String goal;
    private String detail;
    private String amount;
    private String currentAmount;

    public Saving(String id, String goal, String detail, String amount, String currentAmount) {
        this.id = id;
        this.goal = goal;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String progressBar) {
        this.currentAmount = currentAmount;
    }
}
