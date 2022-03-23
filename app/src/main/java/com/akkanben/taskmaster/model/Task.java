package com.akkanben.taskmaster.model;

public class Task {
    String title;
    String body;
    TaskStatus status;

    public Task(String title, String body, TaskStatus status) {
        this.title = title;
        this.body = body;
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
