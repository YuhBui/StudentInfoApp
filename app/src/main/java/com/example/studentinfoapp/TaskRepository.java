package com.example.studentinfoapp;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private List<Task> data = new ArrayList<>();

    public TaskRepository() {
        data.add(new Task("Bài tập Toán cao cấp", "Làm bài tập chương 3", "10/04/2026", "Homework", 2));
        data.add(new Task("Dự án lập trình Java", "Hoàn thành code app", "20/04/2026", "Project", 1));
    }

    public List<Task> getAllTasks() {
        return data;
    }

    public void addTask(Task task) {
        data.add(task);
    }
}