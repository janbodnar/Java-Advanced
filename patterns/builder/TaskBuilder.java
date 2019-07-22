package com.zetcode;

import java.time.LocalDate;

public class TaskBuilder {

    private String name;
    private String description;
    private boolean isFinished = false;
    private LocalDate dueDate;

    public TaskBuilder() {

    }

    public TaskBuilder(String name, String description, boolean isFinished,
                       LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.isFinished = isFinished;
        this.dueDate = dueDate;
    }

    public TaskBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder setFinished(boolean isFinished) {
        this.isFinished = isFinished;
        return this;
    }

    public TaskBuilder setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Task build() {
        return new Task(name, description, isFinished, dueDate);
    }
}
