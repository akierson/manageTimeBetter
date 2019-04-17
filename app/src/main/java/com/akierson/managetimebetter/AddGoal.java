package com.akierson.managetimebetter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddGoal extends AppCompatActivity {

    TextView gDescr;
    Spinner gSpinner;
    Spinner gLevel;
    Switch gRecurring;
    Switch gReminder;
    TextView gReminderDay;
    CalendarDataModel mCal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create layout
        setContentView(R.layout.activity_add_goal);

        // Get views
        gDescr = findViewById(R.id.addGoal_goalDescription);
        gSpinner = findViewById(R.id.addGoal_goalArea);
        gLevel = findViewById(R.id.addGoal_goalLevel);
        gRecurring = findViewById(R.id.addGoal_goalRecur);
        gReminder = findViewById(R.id.addGoal_addReminder);
        gReminderDay = findViewById(R.id.addGoal_reminderDay);

        // Instantiate data models
        mCal = new CalendarDataModel(this);
        // TODO: 4/13/2019 Set day of reminder
        gReminderDay.setText(mCal.getStartDay());
        // TODO: 4/13/2019 add onclick listener to change time of reminder 

    }

    public boolean addGoal(View view) {

        // Get values of item
        String description = "";
        if (gDescr.getText() != null) {
            description = (String) gDescr.getText();
        } else {
            Toast.makeText(AddGoal.this, String.valueOf(R.string.err_addGoal_noDescr), Toast.LENGTH_LONG).show();
            return false;
        }

        String goalLevel = String.valueOf(gLevelButton.getText());
        String goalArea = (String) gSpinner.getSelectedItem();                          // will always have a value
        boolean recurring = gRecurring.isEnabled();                                     // will always have a value

        Goal mGoal = new Goal(goalLevel, description, recurring);

        // Add Goal to Room Table
        mCal.addGoal(mGoal);
        // TODO: add reminder event
        // TODO: 4/12/2019 send to add event for activity for result 
        finish();
        return true;
    }
}
