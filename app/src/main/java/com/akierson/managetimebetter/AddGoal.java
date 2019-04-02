package com.akierson.managetimebetter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddGoal extends AppCompatActivity {

    TextView gDescr;
    Spinner gSpinner;
    RadioGroup gLevel;
    Switch gRecurring;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        gDescr = findViewById(R.id.addGoal_goalDescription);
        gSpinner = findViewById(R.id.addGoal_spinner);
        gLevel = findViewById(R.id.addGoal_radioGroup);
        gRecurring = findViewById(R.id.addGoal_switch);


    }

    public boolean addGoal(View view) {
//        TODO: Make multiple levels of goal setting

        // Get values of item
        String description = "";
        if (gDescr.getText() != null) {
            description = (String) gDescr.getText();
        } else {
            Toast.makeText(AddGoal.this, String.valueOf(R.string.err_addGoal_noDescr), Toast.LENGTH_SHORT).show();
            return false;
        }
        //TODO: cast to int
        RadioButton gLevelButton= findViewById(gLevel.getCheckedRadioButtonId());         // will always have a value
        String goalLevel = String.valueOf(gLevelButton.getText());
        String goalArea = (String) gSpinner.getSelectedItem();                          // will always have a value
        boolean recurring = gRecurring.isEnabled();                                     // will always have a value

        Goal mGoal = new Goal(Integer.parseInt(goalLevel), description, recurring);


        // TODO: 3/14/2019 Add Goal to SQLite Table
        return true;
    }
}
