package com.akierson.managetimebetter;

import java.util.Calendar;
import java.util.Date;

public class Event {

    int _id;
    String title;
    String description;
    String location;
    boolean allDay;
    String calendar;
    Calendar begin;
    Calendar end;

    public Event(int _id, String title, String description, String location, int allDay, String calendar, Calendar begin, Calendar end) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.location = location;
        if (allDay == 1){
            this.allDay = true;
        } else {
            this.allDay = false;
        }
        this.calendar = calendar;
        this.begin = begin;
        this.end = end;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocation() {
        return location;
    }

    public Event setLocation(String location) {
        this.location = location;
        return this;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public Calendar getBegin() {
        return begin;
    }

    public void setBegin(Calendar begin) {
        this.begin = begin;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }
}
