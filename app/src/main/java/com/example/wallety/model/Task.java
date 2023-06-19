package com.example.wallety.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

public class Task {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("desc")
    private String desc;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("amount")
    private String amount;

    @SerializedName("isChecked")
    private boolean isChecked;

    @SerializedName("targetChild")
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

    public static final String COLLECTION = "tasks";
    static final String ID = "id";
    static final String NAME = "name";
    static final String DESC = "desc";
    static final String DATE = "date";
    static final String TIME = "time";
    static final String AMOUNT = "amount";
    static final String TARGET_CHILD = "targetChild";

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


//    this.id = id;
//        this.name = name;
//        this.desc = desc;
//        this.date = date;
//        this.time = time;
//        this.amount = amount;
//        this.targetChild = targetChild;

    public static Task fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String name = (String) json.get(NAME);
        String desc = (String) json.get(DESC);
        String date = (String) json.get(DATE);
        String time = (String) json.get(TIME);
        String amount = (String) json.get(AMOUNT);
        String targetChild = (String) json.get(TARGET_CHILD);


        Task task = new Task(id, name, desc, date, time, amount, targetChild);
        return task;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(DESC, getDesc());
        json.put(DATE, getDate());
        json.put(TIME, getTime());
        json.put(AMOUNT, getAmount());
        json.put(TARGET_CHILD, getTargetChild());

        return json;
    }


}
