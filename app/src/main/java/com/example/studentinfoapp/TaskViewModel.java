package com.example.studentinfoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class TaskViewModel extends ViewModel {
    private MutableLiveData<List<Task>> taskListLiveData;
    private TaskRepository repository;

    public TaskViewModel() {
        repository = new TaskRepository();
        taskListLiveData = new MutableLiveData<>();
        loadTasks();
    }

    public LiveData<List<Task>> getTaskList() {
        return taskListLiveData;
    }

    public void loadTasks() {
        taskListLiveData.setValue(repository.getAllTasks());
    }

    public void addTask(Task task) {
        repository.addTask(task);
        loadTasks();
    }
}