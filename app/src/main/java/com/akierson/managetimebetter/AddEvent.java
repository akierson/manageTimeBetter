package com.akierson.managetimebetter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddEvent extends Activity {

    private static final String TAG = "Add Event";

    private DatePickerDialog.OnDateSetListener startDateChange;
    private DatePickerDialog.OnDateSetListener endDateChange;
    private TimePickerDialog.OnTimeSetListener startTimeChange;
    private TimePickerDialog.OnTimeSetListener endTimeChange;

    private TextView title;
    private TextView startDate;
    private TextView startTime;
    private TextView endDate;
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

        Calendar.getInstance();
        String today = Calendar.YEAR + "/" + Calendar.MONTH + "/" + Calendar.DAY_OF_MONTH;

        title = findViewById(R.id.addEvent_title);
        startDate = findViewById(R.id.addEvent_startDate);
        startDate.setText(today);
        startTime = findViewById(R.id.addEvent_startTime);
        endDate = findViewById(R.id.addEvent_endDate);
        endDate.setText(today);
        endTime = findViewById(R.id.addEvent_endTime);
        location = findViewById(R.id.addEvent_location);
        notes = findViewById(R.id.addEvent_notes);
        allDay = findViewById(R.id.addEvent_allDay);
        busy = findViewById(R.id.addEvent_busy);

        startDateChange = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                startDate.setText(date);
            }
        };
        endDateChange = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                endDate.setText(date);
            }
        };

        startTimeChange = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                startTime.setText(time);
            }
        };

        endTimeChange = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                endTime.setText(time);
            }
        };
    }

    public void addEvent(View view) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date mdate = sdf.parse(startDate.getText().toString() + " " + startTime.getText().toString());
        long startDateMillis = mdate.getTime();

        mdate = sdf.parse(endDate.getText().toString() + " " + endTime.getText().toString());
        long endDateMillis = mdate.getTime();


        // Finish adding parts to event handling
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, title.getText());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startDateMillis);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                endDateMillis);
        intent.putExtra(CalendarContract.Events.ALL_DAY, allDay.isChecked());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, notes.getText());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText());
        if (busy.isChecked()) {
            intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        }

        startActivity(intent);
    }


    public void setStartDate(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                startDateChange,
                year, month, day);
        dialog.show();
    }
    public void setStartTime(View view) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);


        TimePickerDialog dialog = new TimePickerDialog(
                this,
                startTimeChange,
                hour, minute, DateFormat.is24HourFormat(this));
        dialog.show();
    }

    public void setEndDate(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                endDateChange,
                year, month, day);
        dialog.show();
    }
    public void setEndTime(View view) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);


        TimePickerDialog dialog = new TimePickerDialog(
                this,
                endTimeChange,
                hour, minute, DateFormat.is24HourFormat(this));
        dialog.show();
    }
}
