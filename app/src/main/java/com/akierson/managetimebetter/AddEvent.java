package com.akierson.managetimebetter;

import android.R.layout;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.DataSetObserver;
import android.net.Uri;
import android.provider.CalendarContract;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

public class AddEvent extends Activity {

    private static final String TAG = "Add Event";

    Calendar currentDate;
    Calendar startDate;
    Calendar endDate;
    SimpleDateFormat dateParse;

    CalendarDataModel mCal;

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

    private Spinner userCalendar;

    Button addEventButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Load Data
        mCal = new CalendarDataModel(this);


        // Get localized date format from phone
        Format dateFormat = DateFormat.getDateFormat(getApplicationContext());
        String pattern = ((SimpleDateFormat)dateFormat).toLocalizedPattern();
        dateParse = new SimpleDateFormat(pattern);

        // Dates
        currentDate = Calendar.getInstance();
        startDate = currentDate;
        endDate = currentDate;

        // Get layout items
        title = findViewById(R.id.addEvent_title);
        startDateTv = findViewById(R.id.addEvent_startDate);
        startTime = findViewById(R.id.addEvent_startTime);
        endDateTv = findViewById(R.id.addEvent_endDate);
        endTime = findViewById(R.id.addEvent_endTime);
        location = findViewById(R.id.addEvent_location);
        notes = findViewById(R.id.addEvent_notes);
        allDay = findViewById(R.id.addEvent_allDay);
        busy = findViewById(R.id.addEvent_busy);
        userCalendar = findViewById(R.id.addEvent_userCalendar);

        // Populate fields
        ArrayList<String> userCals =  mCal.getCalendars();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userCals);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Check if there are calendars on phone
        if (userCals != null) {
            userCalendar.setAdapter(arrayAdapter);
        } else {
            userCalendar.setVisibility(View.GONE);
            Log.d(TAG, "onCreate: " + getString(R.string.addEvent_error_noCals));
        }
        startDateTv.setText(dateParse.format(startDate.getTime()));
        endDateTv.setText(dateParse.format(startDate.getTime()));

        addEventButton = findViewById(R.id.addEvent_addEvent);
        cancelButton = findViewById(R.id.addEvent_cancel);

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

    public void addEvent(View view) {
        // Insert Event
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, startDate.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endDate.getTimeInMillis());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        Log.d(TAG, "addEvent: " + title.getText());
        if (title.getText().length() > 0) {
            values.put(CalendarContract.Events.TITLE, String.valueOf(title.getText()));
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.addEvent_error), Toast.LENGTH_LONG).show();
            return;
        }
        if (notes.getText().length() > 0) {
            values.put(CalendarContract.Events.DESCRIPTION, String.valueOf(notes.getText()));
        }
        // all day
        values.put(CalendarContract.Events.ALL_DAY, allDay.isChecked());
        //location
        if (location.getText().length() > 0) {
            values.put(CalendarContract.Events.EVENT_LOCATION, String.valueOf(location.getText()));
        }
        //Busy?
        if (busy.isChecked()) {
            values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        }
        // TODO: 4/23/2019 Change which calendar it goes to
        long userCalId = userCalendar.getSelectedItemId();
        values.put(CalendarContract.Events.CALENDAR_ID, userCalId);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // Retrieve ID for new event
        String eventID = uri.getLastPathSegment();
        Log.d(TAG, "addEvent: Event " + eventID + " added");

        // return to previous screen
        Toast.makeText(getApplicationContext(), getString(R.string.addEvent_eventAdded), Toast.LENGTH_LONG).show();

        finish();
    }

    public void cancelAddEvent(View view) {
        finish();
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
}
