package com.akierson.managetimebetter;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// TODO: 4/13/2019 make async 
public class CalendarDataModel {
    public static final String TAG = "CalendarDataModel";

    // store events
    Set<String> mCal;
    // store goals
    RoomDatabase.Builder<GoalAppDatabase> gdb;
    // get user calendar ids
    public static final String[] FIELDS = {
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE
    };
    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    Set<String> calendars = new HashSet<String>();

    String startDay;


    public CalendarDataModel(Context ctx) {
        contentResolver = ctx.getContentResolver();
        gdb = Room.databaseBuilder(ctx.getApplicationContext(), GoalAppDatabase.class, "goal-database");
        if (ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Log.d(TAG, "onCreate: Calendar Permissions Granted");
            // Run query
            mCal = getCalendars();
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
            /*TODO: log exception and bail*/
            Log.e(TAG, "getCalendars: ", ex );
        }

        Log.d(TAG, "getCalendars: " + String.valueOf(calendars));
        return calendars;
    }

    public void addGoal(Goal goal) {
        // TODO: 4/13/2019 Figure out Room
        gdb.getGoalDao().insertAll(goal);
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }
}
