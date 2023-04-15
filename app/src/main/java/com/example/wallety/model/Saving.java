package com.example.wallety.model;

import android.widget.ProgressBar;

public class Saving {

    String user;
    private String goal;
    private String detail;
    private String sum;
    private String progressBar;

    public Saving(String user, String goal, String detail, String sum, String progressBar) {
        this.user = user;
        this.goal = goal;
        this.detail = detail;
        this.sum = sum;
        this.progressBar = progressBar;
    }

 public String getUser() {
        return user;
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

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(String progressBar) {
        this.progressBar = progressBar;
    }
}
