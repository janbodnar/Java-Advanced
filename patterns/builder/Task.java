package com.zetcode;

import java.time.LocalDate;

public class Task {

    private String name;
    private String description;
    private boolean isFinished;
    private LocalDate dueDate;

    public Task(String name, String description, boolean isFinished,
                LocalDate dueDate) {

        this.name = name;
        this.description = description;
        this.isFinished = isFinished;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {

        var sb = new StringBuilder("Task{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", isFinished=").append(isFinished);
        sb.append(", dueDate=").append(dueDate);
        sb.append('}');
        return sb.toString();
    }
}
