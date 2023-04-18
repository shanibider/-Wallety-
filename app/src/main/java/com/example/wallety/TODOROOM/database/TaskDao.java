package com.example.wallety.TODOROOM.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wallety.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insert(Task task);

    @Update
    public void update(Task task);

    @Delete
    public void delete(Task task);

    @Query("SELECT * FROM my_tasks")
    public LiveData<List<Task>> getAllData();


}
