package com.example.wallety.model;

import java.io.Serializable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "my_tasks" )
public class Task {
    private String taskTitle;
    private String taskDetail;

    @PrimaryKey(autoGenerate = true)
    private int taskId;

    public Task() {
    }

    public Task(String taskTitle, String taskDetail) {
        this.taskTitle = taskTitle;
        this.taskDetail = taskDetail;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getTaskTitle() {
            return taskTitle;
        }

    public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

}