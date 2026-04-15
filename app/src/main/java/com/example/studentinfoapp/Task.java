package com.example.studentinfoapp;

import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable {
    private String id;
    private String title;
    private String description;
    private String dueDate;
    private String category;
    private int priority;
    private boolean isCompleted;

    public Task(String title, String description, String dueDate, String category, int priority) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public String getCategory() { return category; }
    public int getPriority() { return priority; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}