package com.akierson.managetimebetter;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Goal {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "name")
    private int name;
    @ColumnInfo(name = "level")
    private int level;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "recursion")
    private boolean recursion;
    @ColumnInfo(name = "attached_event")
    public int attached_event_id;

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

    public int getAttached_event_id() {
        return attached_event_id;
    }

    public void setAttached_event_id(int attached_event_id) {
        this.attached_event_id = attached_event_id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
    // TODO: 4/3/2019 Add methods to add goal to table 

}
