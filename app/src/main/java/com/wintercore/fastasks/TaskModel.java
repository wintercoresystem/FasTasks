package com.wintercore.fastasks;

public class TaskModel {
    int hours;
    int minutes;
    String taskName;

    public TaskModel(int hours, int minutes, String taskName) {
        this.hours = hours;
        this.minutes = minutes;
        this.taskName = taskName;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
