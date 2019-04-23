package com.akierson.managetimebetter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

// TODO: 4/13/2019 make async 
public class CalendarDataModel {
    public static final String TAG = "CalendarDataModel";

    // store Calendar Names
    ArrayList<String> mCals;

    // store goals
    final GoalAppDatabase gdb;

    // get user calendar ids
    public static final String[] CALENDAR_FIELDS = {
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE
    };
    public static final String[] INSTANCE_FIELDS = {
            CalendarContract.Instances._ID,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.DESCRIPTION,
            CalendarContract.Instances.ALL_DAY,
            CalendarContract.Instances.CALENDAR_COLOR,
            CalendarContract.Instances.CALENDAR_DISPLAY_NAME,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END,
    };
    //
    private static final int CALENDAR_ID = 0;
    private static final int CALENDAR_NAME = 1;
    private static final int CALENDAR_DISPLAY_NAME = 2;
    private static final int CALENDAR_COLOR = 3;
    private static final int CALENDAR_VISIBLE = 4;

    private static final int INSTANCE_ID = 0;
    private static final int INSTANCE_TITLE = 1;
    private static final int INSTANCE_DESCRIPTION= 2;
    private static final int INSTANCE_ALL_DAY= 3;
    private static final int INSTANCE_COLOR = 4;
    private static final int INSTANCE_DISPLAY_NAME = 5;
    private static final int INSTANCE_BEGIN= 6;
    private static final int INSTANCE_END = 7;

    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    ArrayList<String> calendars;

    Calendar startDay;
    Calendar endDay;
    private ArrayList<Event> calEvents;
    private String title;
    private int eventID;
    private String descrVal;
    private int allDayVal;
    private long colorVal;
    private String calendarVal;
    private long beginVal;
    private long endVal;


    public CalendarDataModel(Context ctx) {

        calendars = new ArrayList<String>();
        calEvents = new ArrayList<Event>();
        startDay = Calendar.getInstance();
        endDay = Calendar.getInstance();
        // Get Content getter
        contentResolver = ctx.getContentResolver();
        // Get AppDatabase
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

        // Check for permissions
        if (ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Log.d(TAG, "onCreate: Calendar Permissions Granted");
        }
    }

    public ArrayList<String> getCalendars() {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor = contentResolver.query(CALENDAR_URI, CALENDAR_FIELDS, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(CALENDAR_NAME);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean selected = !cursor.getString(3).equals("0");
                    // TODO: 4/22/2019 Check if calendar already exists 
                    calendars.add(displayName);
                }
            }
        } catch (AssertionError ex) {
            Log.e(TAG, "getCalendars: ", ex );
        }
        cursor.close();
        Log.d(TAG, "getCalendars: " + calendars);
        return calendars;
    }

    @SuppressLint("NewApi")
    public ArrayList<Event> getCalendarEvents() {
        // Specify the date range you want to search for recurring
        // event instances
        // TODO: 4/22/2019 Set start time
        long startMillis = startDay.getTimeInMillis();
        long endMillis = endDay.getTimeInMillis();

        Cursor cur = null;

        // The ID of the recurring event whose instances you are searching
        // for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {"207"};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        cur = (Cursor) contentResolver.query(builder.build(),
        INSTANCE_FIELDS, null, null);

        while (cur.moveToNext()) {
            // Get the field values

            eventID = cur.getInt(INSTANCE_ID);
            
            title = cur.getString(INSTANCE_TITLE);
            descrVal = cur.getString(INSTANCE_DESCRIPTION);
            allDayVal = cur.getInt(INSTANCE_ALL_DAY);
            colorVal = cur.getLong(INSTANCE_COLOR);
            calendarVal = cur.getString(INSTANCE_DISPLAY_NAME);
            beginVal = cur.getLong(INSTANCE_BEGIN);
            endVal = cur.getLong(INSTANCE_END);

            Calendar eventStart = Calendar.getInstance();
            eventStart.setTimeInMillis(beginVal);

            Calendar eventEnd = Calendar.getInstance();
            eventEnd.setTimeInMillis(endVal);

            // Do something with the values.
            Log.i(TAG, "Event:  " + title);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Log.i(TAG, "Date: " + formatter.format(eventStart.getTime()));

            Event mEvent = new Event(eventID, title, descrVal, allDayVal, calendarVal, eventStart, eventEnd);
            // TODO: 4/23/2019 Don't dupe events
            calEvents.add(mEvent);
        }
        
        return calEvents;
    }

    public void addGoal(Goal goal) {
        // TODO: 4/18/2019 move to separate thread
        gdb.goalDAO().insertAll(goal);
    }

    public Calendar getStartDay() {
        return startDay;
    }

    public void setStartDay(Calendar startDay) {
        this.startDay = startDay;
    }

    public Calendar getEndDay() {
        return endDay;
    }

    public void setEndDay(Calendar endDay) {
        this.endDay = endDay;
    }
}
