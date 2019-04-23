package com.akierson.managetimebetter;

import java.util.Date;

public class Event {

    int _id;
    String title;
    String description;
    boolean allDay;
    String calendar;
    Date begin;
    Date end;

    public Event(int _id, String title, String description, boolean allDay, String calendar, Date begin, Date end) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
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

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
