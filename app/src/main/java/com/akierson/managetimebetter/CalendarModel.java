package com.akierson.managetimebetter;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

@Entity
public class CalendarModel {

    public CalendarModel () {
        // Empty Constructor
    }

    //TODO: Load calendar from calendar
    //TODO: Load Events from Table
    Calendar cal = Calendar.getInstance();
    // User account
    @PrimaryKey
    public int userAccount;

    // Events from calendar

    // Events from UsageStats

    // Calendar accounts

    // private Methods

    // Methods
    public void getPlannedEvents (Date startDay, Date endDay) {
        if ( startDay == null ) {

        }
    }

    // getUsageEvents (startDay, endDay)
    // default  to today

    public void getDataFromCalendarTable() {

    }
}
