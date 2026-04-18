package com.example.studentinfoapp;

import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable {
    private String id;
    private String title;
    private String description;
    private String dueDate;
    private String category;

    // Đổi lại thành String nếu ở AddTaskActivity bạn đang dùng "Low", "Medium", "High"
    // Nếu bạn bắt buộc dùng int, hãy để lại int và tự map giá trị bên AddTaskActivity
    private String priority;

    private boolean isCompleted;

    // CONSTRUCTOR 1: Có đầy đủ 6 tham số (ĐỂ SỬA LỖI CỦA BẠN)
    public Task(String title, String description, String dueDate, String category, String priority, boolean isCompleted) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    // CONSTRUCTOR 2: Dùng cho database khi đã có sẵn ID
    public Task(String id, String title, String description, String dueDate, String category, String priority, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}