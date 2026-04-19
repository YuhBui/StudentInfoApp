package com.example.studentinfoapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    // Truy vấn lấy toàn bộ danh sách
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();
}