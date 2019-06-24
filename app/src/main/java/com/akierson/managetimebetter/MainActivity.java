package com.akierson.managetimebetter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Set;

// TODO: 4/28/2019 Add launch screen of icon
// Loads Calendars from phone and stores usage events from recent times
public class MainActivity extends AppCompatActivity {

    // Constants for intents, tags and permission numeration
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    // Permission status
    private final int REQUEST_PERMISSION_READ_CALENDAR=1;
    private final int REQUEST_PERMISSION_WRITE_CALENDAR=2;

    // Tag for debugging
    private static final String TAG = "MainActivity";

    // Views
    TextView toolbarTitle;

    FloatingActionButton fabEvent;
    FloatingActionButton fabGoals;

    Animation showFabEvent;
    Animation showFabGoals;
    Animation hideFabEvent;
    Animation hideFabGoals;

    // Fab menu bool
    boolean fabMenuOpen = false;

    // Data Instances
    private Calendar startDateCal;
    private Calendar endDateCal;

    // Fragments
    CalendarFragment calFrag;
    DashboardFragment dashFrag;
    GoalsFragment goalFrag;

    // On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(R.layout.activity_main);

        //Set Bottom Nav
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        
        // Get Layout items
        toolbarTitle = findViewById(R.id.toolbar_title);
        fabEvent = findViewById(R.id.fab_event);
        fabGoals = findViewById(R.id.fab_goal);

        // Add Fab Animations
        showFabEvent = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab_event);
        showFabGoals = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab_goals);
        hideFabEvent = AnimationUtils.loadAnimation(getApplication(), R.anim.hide_fab_event);
        hideFabGoals = AnimationUtils.loadAnimation(getApplication(), R.anim.hide_fab_goals);

        // Create localized date variables
        startDateCal = Calendar.getInstance();
        endDateCal = Calendar.getInstance();
        endDateCal.add(Calendar.DATE, 3);

        // Check for calendar permissions
        showReadCalendarPermission();
        showWriteCalendarPermission();

        // TODO: 4/13/2019 add on permission change to reload activity
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Log.d(TAG, "onCreate: Calendar Permissions Granted");

            // Initialise Fragments
            Bundle bundle = new Bundle();
            // Variables to pass
            bundle.putSerializable(START_DATE, startDateCal);
            bundle.putSerializable(END_DATE, endDateCal);
            //Instantiate fragments
            calFrag = new CalendarFragment();
            dashFrag = new DashboardFragment();
            goalFrag = new GoalsFragment();
            // Put args
            calFrag.setArguments(bundle);
            dashFrag.setArguments(bundle);
            goalFrag.setArguments(bundle);

            // Add Calendar Fragment to home screen
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, calFrag).commit();
            toolbarTitle.setText(R.string.title_calendar);
        } else {
            showReadCalendarPermission();
            showWriteCalendarPermission();
        }
    }

    private void showReadCalendarPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CALENDAR);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR)) {
                showExplanation("Permission Needed", "Calendar Access needed for app to work", Manifest.permission.READ_PHONE_STATE, REQUEST_PERMISSION_READ_CALENDAR);
            } else {
                requestPermission(Manifest.permission.READ_CALENDAR, REQUEST_PERMISSION_READ_CALENDAR);
            }
        } else {
            Log.d(TAG, "showReadCalendarPermission: Read Calendar Permission Already Granted");
            //Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }
    private void showWriteCalendarPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {
                showExplanation("Permission Needed", "Calendar Access needed for app to work", Manifest.permission.WRITE_CALENDAR, REQUEST_PERMISSION_WRITE_CALENDAR);
            } else {
                requestPermission(Manifest.permission.WRITE_CALENDAR, REQUEST_PERMISSION_WRITE_CALENDAR);
            }
        } else {
            Log.d(TAG, "showWriteCalendarPermission: Write Calendar Permission Already Granted");
            // Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_CALENDAR:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Read Permission Granted");
                    //Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Read Permission Denied");
                    // Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_WRITE_CALENDAR:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Write Permission Granted");
                    // Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Write Permission Denied");
                    // Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    // On Fab Press
    @SuppressLint("RestrictedApi")
    // TODO: 5/2/2019 fix animations 
    public void onFabPress(View fab){
        // open new window with event adding
        // Add Two fabs
        // Move FAB Event
        FrameLayout.LayoutParams layoutParamsEvent = (FrameLayout.LayoutParams) fabEvent.getLayoutParams();
        FrameLayout.LayoutParams layoutParamsGoals = (FrameLayout.LayoutParams) fabGoals.getLayoutParams();

        if (!fabMenuOpen) {
            fabEvent.setLayoutParams(layoutParamsEvent);
            fabGoals.setLayoutParams(layoutParamsGoals);
            fabEvent.startAnimation(showFabEvent);
            fabGoals.startAnimation(showFabGoals);
            layoutParamsEvent.bottomMargin += (int) (fabEvent.getHeight() * 1.7);
            layoutParamsGoals.bottomMargin += (int) (fabGoals.getHeight() * 3.0);
            fabEvent.setVisibility(View.VISIBLE);
            fabGoals.setVisibility(View.VISIBLE);
            fabEvent.setClickable(true);
            fabGoals.setClickable(true);

            fabMenuOpen = true;
        } else {
            fabEvent.setLayoutParams(layoutParamsEvent);
            fabGoals.setLayoutParams(layoutParamsGoals);
            fabEvent.startAnimation(hideFabEvent);
            fabGoals.startAnimation(hideFabGoals);
            layoutParamsEvent.bottomMargin -= (int) (fabEvent.getHeight() * 1.7);
            layoutParamsGoals.bottomMargin -= (int) (fabGoals.getHeight() * 3.0);
            fabEvent.setVisibility(View.INVISIBLE);
            fabGoals.setVisibility(View.INVISIBLE);
            fabEvent.setClickable(false);
            fabGoals.setClickable(false);

            fabMenuOpen = false;
        }
    }

    // Fab Event
    public void onFabEventPress (View fab) {
        Intent intent = new Intent(this, AddEvent.class);
        intent.putExtra(START_DATE, startDateCal);
        intent.putExtra(END_DATE, endDateCal);
        onFabPress(fab);
        startActivity(intent);
    }

    // Fab Goal
    public void onFabGoalPress (View fab) {
        Intent intent = new Intent(this, AddGoal.class);
        intent.putExtra(START_DATE, startDateCal);
        intent.putExtra(END_DATE, endDateCal);
        onFabPress(fab);
        startActivity(intent);
    }

    // TODO: 5/16/2019 Need to add a Null reference check on Fragment Manager
    // TODO: 5/16/2019 Fix one off day 
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (getSupportFragmentManager().getFragments() != null) {
            if (calFrag.isAdded()) {
                // TODO: Reload Events
            } else if (goalFrag.isAdded()) {
                // TODO: Reload Goals
            } else if (dashFrag.isAdded()) {
                //TODO: Reload Dashboard
            }
        }
    }

    // TODO: 5/22/2019 Get Dates from Fragments
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Have check to stop app from crashing if permissions not granted
            showReadCalendarPermission();
            showWriteCalendarPermission();

            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    // Remove Other Fragments first
                    // Then Load Current Fragment
                    Log.d(TAG, "Adding Calendar Fragment");
                    toolbarTitle.setText(getString(R.string.title_calendar));
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, calFrag, "CalFragAttached").commit();
                    return true;
                case R.id.navigation_goals:
                    toolbarTitle.setText(getString(R.string.title_goals));
                    Log.d(TAG, "Adding Goal Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, goalFrag, "GoalFragAttached").commit();
                    return true;
                case R.id.navigation_dashboard:
                    toolbarTitle.setText(getString(R.string.title_dashboard));

                    Log.d(TAG, "Adding Dashboard Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, dashFrag, "DashFragAttached").commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        if (calFrag.isAdded()) {
            handled = calFrag.gestDetector.onTouchEvent(ev);
        } if (goalFrag.isAdded()) {
            return true;
        } if (goalFrag.isAdded()) {
            return true;
        }
        return handled;
    }
}
