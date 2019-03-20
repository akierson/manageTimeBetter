package com.akierson.managetimebetter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class CalendarModel {

    //TODO: Load calendar from calendar
    //TODO: Load Events from Table
    Calendar cal = Calendar.getInstance();
    // User account

    // Events from calendar

    // Events from UsageStats

    // Calendar accounts

    // private Methods
    //

    // Methods
    public void getPlannedEvents (Date startDay, Date endDay) {
        if ( startDay == null ) {

        }
    }

    // getUsageEvents (startDay, endDay)
    // default  to today

    public void getDataFromCalendarTable() {
        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.CALENDAR_LOCATION,
                        CalendarContract.Calendars.CALENDAR_TIME_ZONE
                };

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"*"};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            Log.d(TAG, "onCreate: Item added");
            String displayName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
            String accountName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));

            TextView tv1 =  new TextView(this);
            tv1.setText(displayName);
            mLayout.addView(tv1);
        }

    }
}
