package com.akierson.managetimebetter;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


// TODO: add goal editor
public class AddGoal extends AppCompatActivity {

    private static final String TAG = "Add Goal";
    EditText gName;
    EditText gDescr;
    Spinner gSpinner;
    Spinner gLevel;
    Button gLevelAdder;
    Switch gRecurring;
    Switch gReminder;
    TextView gReminderDay;

    CalendarDataModel mCal;
    DatePickerDialog.OnDateSetListener onDateChange;
    SimpleDateFormat dateParse;
    Calendar reminderDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create layout
        setContentView(R.layout.activity_add_goal);

        // Get views
        gName = findViewById(R.id.addGoal_goalName);
        gDescr = findViewById(R.id.addGoal_goalDescription);
        gSpinner = findViewById(R.id.addGoal_goalArea);
        gLevel = findViewById(R.id.addGoal_goalLevel);
        gLevelAdder = findViewById(R.id.addGoal_addArea);
        gRecurring = findViewById(R.id.addGoal_goalRecur);
        gReminder = findViewById(R.id.addGoal_addReminder);
        gReminderDay = findViewById(R.id.addGoal_reminderDay);

        // Instantiate data models
        mCal = new CalendarDataModel(this);

        // Get localized date format from phone
        Format dateFormat = DateFormat.getDateFormat(getApplicationContext());
        String pattern = ((SimpleDateFormat)dateFormat).toLocalizedPattern();
        dateParse = new SimpleDateFormat(pattern);

        reminderDate = Calendar.getInstance();
        gReminderDay.setText(dateParse.format(reminderDate.getTime()));

        onDateChange = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                reminderDate.set(Calendar.DAY_OF_MONTH, day);
                reminderDate.set(Calendar.MONTH, month);
                reminderDate.set(Calendar.YEAR, year);

                Log.d(TAG, "onDateSet: " + dateParse.format(reminderDate.getTime()));
                gReminderDay.setText(dateParse.format(reminderDate.getTime()));
            }
        };

        gReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addReminder(isChecked);
            }
        });

        gLevelAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            // TODO: 5/1/2019 Add Table to database 
            public void onClick(View v) {
                
            }
        });

    }

    public boolean addGoal(View view) {

        // Get values of item
        String name = "";
        if (gName.getText() != null) {
            name = gName.getText().toString();
        } else {
            Toast.makeText(AddGoal.this, String.valueOf(R.string.err_addGoal_noName), Toast.LENGTH_LONG).show();
            return false;
        }

        String description = "";
        if (gDescr.getText() != null) {
            description = gDescr.getText().toString();
        } else {
            return false;
        }

        // will always have a value
        int goalLevel = gLevel.getSelectedItemPosition();
        // will always have a value
        String goalArea = gSpinner.getSelectedItem().toString();
        // will always have a value
        boolean recurring = gRecurring.isChecked();
        Goal mGoal = new Goal(name, description, goalArea, goalLevel, recurring);

        // Add Goal to Room Table
        mCal.addGoal(mGoal);

        if (gReminder.isChecked()) {
            // Add reminder event
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.DTSTART, reminderDate.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, reminderDate.getTimeInMillis());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            values.put(CalendarContract.Events.TITLE, name);
            values.put(CalendarContract.Events.DESCRIPTION, String.valueOf(R.string.addGoal_addGoalDescr));
            values.put(CalendarContract.Events.ALL_DAY, Boolean.TRUE);
            // TODO: Have each area assigned to a calendar etc.
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            String eventID = uri.getLastPathSegment();
            Log.d(TAG, "addEvent: Event " + eventID + " added");
        }

        Toast.makeText(getApplicationContext(), getString(R.string.addGoal_goalAdded), Toast.LENGTH_LONG).show();

        finish();
        return true;
    }

    public void addReminder(boolean isChecked) {
        if (isChecked) {
            int year = reminderDate.get(Calendar.YEAR);
            int month = reminderDate.get(Calendar.MONTH);
            int day = reminderDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, onDateChange, year, month, day);
            dialog.show();
        }
    }

    public void cancel(View view) {
        finish();
    }
}
