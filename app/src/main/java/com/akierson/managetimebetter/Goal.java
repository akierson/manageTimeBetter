package com.akierson.managetimebetter;

public class Goal {
    private int id;
    private int level;
    private String description;
    private boolean recursion;

    public Goal(int level, String description, boolean recursion) {
        this.level = level;
        this.description = description;
        this.recursion = recursion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecursion() {
        return recursion;
    }

    public void setRecursion(boolean recursion) {
        this.recursion = recursion;
    }
}
