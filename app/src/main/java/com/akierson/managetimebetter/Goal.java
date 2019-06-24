package com.akierson.managetimebetter;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@Entity (tableName = "goal")
public class Goal {
    @PrimaryKey (autoGenerate = true)
    private int _id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "level")
    private int level;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "area")
    private String area;
    @ColumnInfo(name = "recursion")
    private boolean recursion;
    @ColumnInfo(name = "attached_event")
    private int attached_event_id;

    // These are set outside of constructor
    @ColumnInfo(name = "event_completed")
    private boolean event_completed;
    // Date Added
    @ColumnInfo(name = "date_added")
    private long date_added;
    // Date Deadline
    @ColumnInfo(name = "date_due")
    private long date_due;

    // Date Completed
    @ColumnInfo(name = "date_completed")
    private long date_completed;

    // Constructor without Event
    public Goal(String name, String description, String area, int level, boolean recursion) {
        this.name = name;
        this.description = description;
        this.area = area;
        this.level = level;
        this.recursion = recursion;
    }

    // Constructor with Event
    public Goal(String name, String description, String area, int level, boolean recursion, int eventid) {
        this.name = name;
        this.description = description;
        this.area = area;
        this.level = level;
        this.recursion = recursion;
        this.attached_event_id = eventid;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
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

    public boolean isEvent_completed() {
        return event_completed;
    }

    public void setEvent_completed(boolean event_completed) {
        this.event_completed = event_completed;
        if (this.isEvent_completed()) {
            this.setDate_completed(Calendar.getInstance().getTimeInMillis());
        } else {
            this.setDate_completed(0);
            this.event_completed = false;
        }
    }

    public long getDate_added() {
        return date_added;
    }

    public void setDate_added(long date_added) {
        this.date_added = date_added;
    }

    public long getDate_due() {
        return date_due;
    }

    public void setDate_due(long date_due) {
        this.date_due = date_due;
    }

    public long getDate_completed() {
        return date_completed;
    }

    public void setDate_completed(long date_completed) {
        this.date_completed = date_completed;
    }
}
