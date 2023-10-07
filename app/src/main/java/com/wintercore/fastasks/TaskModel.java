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

    public int getMinutes() {
        return minutes;
    }

    public String getTaskName() {
        return taskName;
    }
}
