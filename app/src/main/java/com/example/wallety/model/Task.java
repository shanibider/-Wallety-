package com.example.wallety.model;

public class Task {
    private String id;
    private String name;
    private String desc;
    private String date;
    private String time;
    private String amount;
    private String targetChild;


    public Task() {
    }

    public Task(String id, String name, String desc, String date, String time, String amount, String targetChild) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.targetChild = targetChild;

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTargetChild() {
        return targetChild;
    }

    public void setTargetChild(String targetChild) {
        this.targetChild = targetChild;
    }
}
