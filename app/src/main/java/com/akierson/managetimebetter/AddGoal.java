package com.akierson.managetimebetter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

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

    public void addGoal(View view) {
        // TODO: 3/14/2019 Add Goal to SQLite Table, Check is filled out
        if (gDescr.getText() != null) {
            String description = (String) gDescr.getText();
        }
        String goalLevel;
        String goalArea = (String) gSpinner.getSelectedItem();
        boolean recurring = gRecurring.isEnabled();
    }
}
