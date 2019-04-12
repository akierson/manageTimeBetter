package com.akierson.managetimebetter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddEvent extends Activity {

    private static final String TAG = "Add Event";

    Calendar currentDate;
    Calendar startDate;
    Calendar endDate;
    SimpleDateFormat dateParse;

    private DatePickerDialog.OnDateSetListener startDateChange;
    private DatePickerDialog.OnDateSetListener endDateChange;
    private TimePickerDialog.OnTimeSetListener startTimeChange;
    private TimePickerDialog.OnTimeSetListener endTimeChange;

    private TextView title;
    private TextView startDateTv;
    private TextView startTime;
    private TextView endDateTv;
    private TextView endTime;
    private TextView location;
    private TextView notes;

    private Switch allDay;
    private Switch busy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Add Toolbar

        // Get localized date format from phone
        Format dateFormat = DateFormat.getDateFormat(getApplicationContext());
        String pattern = ((SimpleDateFormat)dateFormat).toLocalizedPattern();
        dateParse = new SimpleDateFormat(pattern);

        // Dates
        currentDate = Calendar.getInstance();
        startDate = currentDate;
        endDate = currentDate;

        title = findViewById(R.id.addEvent_title);
        startDateTv = findViewById(R.id.addEvent_startDate);
        startTime = findViewById(R.id.addEvent_startTime);
        endDateTv = findViewById(R.id.addEvent_endDate);
        endTime = findViewById(R.id.addEvent_endTime);
        location = findViewById(R.id.addEvent_location);
        notes = findViewById(R.id.addEvent_notes);
        allDay = findViewById(R.id.addEvent_allDay);
        busy = findViewById(R.id.addEvent_busy);

        startDateTv.setText(dateParse.format(startDate.getTime()));
        endDateTv.setText(dateParse.format(startDate.getTime()));


        startDateChange = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startDate.set(Calendar.DAY_OF_MONTH, day);
                startDate.set(Calendar.MONTH, month);
                startDate.set(Calendar.YEAR, year);

                // If startDate is more than endDate, change endDate = startDate
                if (startDate.compareTo(endDate) < 1 ) {
                    endDate = startDate;
                }

                Log.d(TAG, "onDateSet: " + dateParse.format(startDate.getTime()));
                startDateTv.setText(dateParse.format(startDate.getTime()));
            }
        };
        endDateChange = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                endDate.set(Calendar.DAY_OF_MONTH, day);
                endDate.set(Calendar.MONTH, month);
                endDate.set(Calendar.YEAR, year);

                // If startDate is more than endDate, change endDate = startDate
                if (startDate.compareTo(endDate) < 1 ) {
                    startDate = endDate;
                }

                Log.d(TAG, "onDateSet: " + dateParse.format(endDate.getTime()));
                endDateTv.setText(dateParse.format(endDate.getTime()));
            }
        };

        startTimeChange = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                startTime.setText(time);
            }
        };

        endTimeChange = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                endTime.setText(time);
            }
        };
    }

    public void addEvent(View view) throws ParseException {

        // TODO: 4/11/2019 add Checks
        // Insert Event
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, startDate.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endDate.getTimeInMillis());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        if (String.valueOf(title.getText()) != null) {
            values.put(CalendarContract.Events.TITLE, String.valueOf(title.getText()));
        }
        if (String.valueOf(notes.getText()) != null) {
            values.put(CalendarContract.Events.DESCRIPTION, String.valueOf(notes.getText()));
        }
        // all day
        values.put(CalendarContract.Events.ALL_DAY, allDay.isChecked());
        //location
        if (String.valueOf(location.getText()) != null) {
            values.put(CalendarContract.Events.EVENT_LOCATION, String.valueOf(location.getText()));
        }
        //Busy?
        if (busy.isChecked()) {
            values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        }
        // TODO: have user pick Calendar ID
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // Retrieve ID for new event
        String eventID = uri.getLastPathSegment();

        // TODO: 4/11/2019 add Event to attached goal area
    }


    public void setStartDate(View view) {
        int year = startDate.get(Calendar.YEAR);
        int month = startDate.get(Calendar.MONTH);
        int day = startDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                startDateChange,
                year, month, day);
        dialog.show();
    }
    public void setStartTime(View view) {
        int hour = startDate.get(Calendar.HOUR_OF_DAY);
        int minute = startDate.get(Calendar.MINUTE);


        TimePickerDialog dialog = new TimePickerDialog(
                this,
                startTimeChange,
                hour, minute, DateFormat.is24HourFormat(this));
        dialog.show();
    }

    public void setEndDate(View view) {
        int year = endDate.get(Calendar.YEAR);
        int month = endDate.get(Calendar.MONTH);
        int day = endDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                endDateChange,
                year, month, day);
        dialog.show();
    }
    public void setEndTime(View view) {
        int hour = endDate.get(Calendar.HOUR_OF_DAY);
        int minute = endDate.get(Calendar.MINUTE);


        TimePickerDialog dialog = new TimePickerDialog(
                this,
                endTimeChange,
                hour, minute, DateFormat.is24HourFormat(this));
        dialog.show();
    }

    // TODO: 4/11/2019 Add location validation

    //TODO: cancel not working
    public void cancel(View view) {
        finish();
    }
}
