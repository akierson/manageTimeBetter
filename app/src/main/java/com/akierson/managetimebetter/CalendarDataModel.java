package com.akierson.managetimebetter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CalendarDataModel {
    public static final String TAG = "CalendarDataModel";

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
            CalendarContract.Instances.EVENT_LOCATION,
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
    private static final int INSTANCE_LOCATION= 3;
    private static final int INSTANCE_ALL_DAY= 4;
    private static final int INSTANCE_COLOR = 5;
    private static final int INSTANCE_DISPLAY_NAME = 6;
    private static final int INSTANCE_BEGIN= 7;
    private static final int INSTANCE_END = 8;

    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    HashMap<String, String> calendars;

    Calendar startDay;
    Calendar endDay;
    private ArrayList<Event> calEvents;
    private String title;
    private int eventID;
    private String descrVal;
    private String locVal;
    private int allDayVal;
    private String calendarVal;
    private long beginVal;
    private long endVal;


    public CalendarDataModel(Context ctx) {

        calendars = new HashMap<String, String>();
        calEvents = new ArrayList<Event>();
        startDay = Calendar.getInstance();
        endDay = Calendar.getInstance();
        // Get Content getter
        contentResolver = ctx.getContentResolver();
        // Get AppDatabase
        gdb = GoalAppDatabase.getAppDatabase(ctx);

        // Check for permissions
        if (ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                ctx.getApplicationContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Log.d(TAG, "onCreate: Calendar Permissions Granted");
        }
    }

    public HashMap<String, String> getCalendars() {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor = contentResolver.query(CALENDAR_URI, CALENDAR_FIELDS, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
                    Log.d(TAG, "getCalendars: " + displayName);
                    String calendarID = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                    Log.d(TAG, "getCalendars: " + calendarID);
                    // Check if calendar already exists
                    if (!calendars.keySet().contains(displayName)) {
                        calendars.put(displayName, calendarID);
                    }
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
        // Reset
        calEvents = new ArrayList<Event>();
        Log.d(TAG, "getCalendarEvents: Getting Events" );
        // Specify the date range you want to search for recurring
        // event instances
        long startMillis = startDay.getTimeInMillis();
        long endMillis = endDay.getTimeInMillis();

        Cursor cur = null;

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        cur = (Cursor) contentResolver.query(builder.build(),
        INSTANCE_FIELDS, null, null);

        while (cur.moveToNext()) {
            // Get the field values

            eventID = Integer.parseInt(cur.getString(INSTANCE_ID));
            
            title = cur.getString(INSTANCE_TITLE);
            descrVal = cur.getString(INSTANCE_DESCRIPTION);
            locVal = cur.getString(INSTANCE_LOCATION);
            allDayVal = cur.getInt(INSTANCE_ALL_DAY);
            // Not used
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

            Event mEvent = new Event(eventID, title, descrVal, locVal, allDayVal, calendarVal, eventStart, eventEnd);
            if (!calEvents.contains(mEvent)) {
                calEvents.add(mEvent);
            }
        }
        
        return calEvents;
    }

    // Goal Methods
    public void addGoal(Goal goal) {
        AsyncTask loader = new AsyncTask<Goal, String, String>(){

            @Override
            protected String doInBackground(Goal... goals) {
                gdb.goalDAO().insertAll(goals);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // TODO: 4/30/2019 Make Error reporting
            }
        };
        loader.execute(goal);

    }

    public List<Goal> getGoals () {
        return gdb.goalDAO().getAll();
    }

    // Getters and Setters

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
