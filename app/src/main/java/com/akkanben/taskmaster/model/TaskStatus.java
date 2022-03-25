package com.akkanben.taskmaster.model;

import androidx.annotation.NonNull;

public enum TaskStatus {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private final String taskString;

    TaskStatus(String taskString) {
        this.taskString = taskString;
    }

    public static TaskStatus fromString(String string) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.taskString.equals(string))
                return status;
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return taskString == null ? "" : taskString;
    }

}
