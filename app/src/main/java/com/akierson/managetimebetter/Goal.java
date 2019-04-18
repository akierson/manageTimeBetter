package com.akierson.managetimebetter;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "goal")
public class Goal {
    // TODO: 4/12/2019 Reasearch Room Persistance 
    @PrimaryKey (autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "level")
    private String level;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "area")
    private String area;
    @ColumnInfo(name = "recursion")
    private boolean recursion;
    @ColumnInfo(name = "attached_event")
    public int attached_event_id;

    public Goal(String name, String description, String area, String level, boolean recursion) {
        this.name = name;
        this.description = description;
        this.area = area;
        this.level = level;
        this.recursion = recursion;
    }

    public Goal(String name, String description, String area, String level, boolean recursion, int eventid) {
        this.name = name;
        this.description = description;
        this.area = area;
        this.level = level;
        this.recursion = recursion;
        this.attached_event_id = eventid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
