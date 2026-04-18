package com.example.studentinfoapp;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> taskListLiveData;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);

        taskListLiveData = repository.getTasks();
    }

    public LiveData<List<Task>> getTaskList() {
        return taskListLiveData;
    }

    public void addTask(Task task) {
        repository.insertTask(task);
    }

    public void deleteTask(int taskId) {
        repository.deleteTask(taskId);
    }
}