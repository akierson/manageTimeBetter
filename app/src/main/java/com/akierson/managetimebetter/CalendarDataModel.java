package com.akierson.managetimebetter;

import android.Manifest;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// TODO: 4/13/2019 make async 
public class CalendarDataModel {
    public static final String TAG = "CalendarDataModel";

    // store Calendar Names
    Set<String> mCals;

    // store goals
    final GoalAppDatabase gdb;

    // get user calendar ids
    public static final String[] FIELDS = {
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE
    };
    public static final String[] EVENTFIELDS = {
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.CALENDAR_COLOR,
            CalendarContract.Events.CALENDAR_DISPLAY_NAME,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            String.valueOf(CalendarContract.Events.CONTENT_URI),

    };
    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    Set<String> calendars = new HashSet<String>();

    String startDay;
    String endDay;


    public CalendarDataModel(Context ctx) {
        contentResolver = ctx.getContentResolver();
        gdb = new GoalAppDatabase() {
            @NonNull
            @Override
            protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
                return null;
            }

            @NonNull
            @Override
            protected InvalidationTracker createInvalidationTracker() {
                return null;
            }

            @Override
            public void clearAllTables() {

            }

            @Override
            public GoalDAO goalDAO() {
                return null;
            }
        };
        if (ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Log.d(TAG, "onCreate: Calendar Permissions Granted");
            // Run query
            mCals = getCalendars();
        }
    }

    public Set<String> getCalendars() {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor = contentResolver.query(CALENDAR_URI, FIELDS, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean selected = !cursor.getString(3).equals("0");
                    calendars.add(displayName);
                }
            }
        } catch (AssertionError ex) {
            Log.e(TAG, "getCalendars: ", ex );
        }

        Log.d(TAG, "getCalendars: " + calendars);
        return calendars;
    }

    public Set<String> getCalendarEvents() {
        // Fetch a list of all events and associated calendars
        Cursor cursor = contentResolver.query(ContentUris., EVENTFIELDS, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean selected = !cursor.getString(3).equals("0");
                    calendars.add(displayName);
                }
            }
        } catch (AssertionError ex) {
            Log.e(TAG, "getCalendars: ", ex );
        }

        Log.d(TAG, "getCalendars: " + calendars);
        return calendars;
    }

    public void addGoal(Goal goal) {
        // TODO: 4/18/2019 move to separate thread
        gdb.goalDAO().insertAll(goal);
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }
}
