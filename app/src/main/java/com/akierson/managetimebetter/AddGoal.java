package com.akierson.managetimebetter;

import android.app.DatePickerDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddGoal extends AppCompatActivity {

    private static final String TAG = "Add Goal";
    EditText gName;
    EditText gDescr;
    Spinner gSpinner;
    Spinner gLevel;
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
        gRecurring = findViewById(R.id.addGoal_goalRecur);
        gReminder = findViewById(R.id.addGoal_addReminder);
        gReminderDay = findViewById(R.id.addGoal_reminderDay);

        // Instantiate data models
        mCal = new CalendarDataModel(this);
        gReminderDay.setText(mCal.getStartDay());
        // Get localized date format from phone
        Format dateFormat = DateFormat.getDateFormat(getApplicationContext());
        String pattern = ((SimpleDateFormat)dateFormat).toLocalizedPattern();
        dateParse = new SimpleDateFormat(pattern);
        reminderDate = Calendar.getInstance();
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
        String goalLevel = gLevel.getSelectedItem().toString();
        // will always have a value
        String goalArea = gSpinner.getSelectedItem().toString();
        // will always have a value
        boolean recurring = gRecurring.isEnabled();
        Goal mGoal = new Goal(name, description, goalArea, goalLevel, recurring);

        // Add Goal to Room Table
        mCal.addGoal(mGoal);
        // TODO: add reminder event
        finish();
        return true;
    }

    public void addReminder(boolean isChecked) {
        if (isChecked) {
            int year = Calendar.YEAR;
            int month = Calendar.MONTH;
            int day = Calendar.DAY_OF_MONTH;

            DatePickerDialog dialog = new DatePickerDialog(this, onDateChange, year, month, day);
            dialog.show();
        }
    }
}
