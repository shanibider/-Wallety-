package com.example.wallety.TODOROOM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wallety.TODOROOM.database.TaskRepo;
import com.example.wallety.model.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepo taskRepo;
    private LiveData<List<Task>> taskList;
    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepo = new TaskRepo((application));
        taskList=taskRepo.getAllData();

    }

    public void insert(Task task){
        taskRepo.insertData(task);
    }
    public void delete(Task task){
        taskRepo.deleteData(task);
    }
    public void update(Task task){
        taskRepo.updateData(task);
    }

    public LiveData<List<Task>> getTaskList() {
        return taskList;
    }
}
