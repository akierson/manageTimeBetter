package com.akierson.managetimebetter;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


// Loads Calendars from phone and stores usage events from recent times
public class MainActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener{
    // Permission status
    private final int REQUEST_PERMISSION_READ_CALENDAR=1;
    private final int REQUEST_PERMISSION_WRITE_CALENDAR=2;

    // Tag for debugging
    private static final String TAG = "MainActivity";

    // For stopping time usage events

    // Views
    FloatingActionButton fabEvent;
    FloatingActionButton fabGoals;

    Animation showFabEvent;
    Animation showFabGoals;

    // Data Instances
    private Calendar cal = Calendar.getInstance();
    private int eventIndex;
    // Public in order to be accessed from frags
    //TODO: Add DashboardModel, GoalModel at astart for easier access
    public CalendarModel calModel = new CalendarModel();

    // Fragments
    CalendarFragment calFrag;
    DashboardFragment dashFrag;
    GoalsFragment goalFrag;

    //  From Documentation on Calendar Provider
    public static final String[] FIELDS = new String[] {
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE
    };

    // On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get Layout items
        fabEvent = findViewById(R.id.fab_event);
        fabGoals = findViewById(R.id.fab_goal);

        // Add Fab Animations
        showFabEvent = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab_event);
        showFabGoals = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab_goals);

        // Initialise Fragments
        calFrag = new CalendarFragment();
        dashFrag = new DashboardFragment();
        goalFrag = new GoalsFragment();

        // Add Calendar Fragment to home screen
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, calFrag).commit();

        // Check for calendar permissions
        showReadCalendarPermission();
        showWriteCalendarPermission();


        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            Log.d(TAG, "onCreate: Calendar Permissions Granted");

            // Run query
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
    public void onFabPress(View fab){
        // open new window with event adding
        // TODO: 3/20/2019 Pass current date to addEvent for better workage
        // TODO: 4/3/2019 get current fragment
        // Add Two fabs
        // Move FAB Event
        FrameLayout.LayoutParams layoutParamsEvent = (FrameLayout.LayoutParams) fabEvent.getLayoutParams();
        FrameLayout.LayoutParams layoutParamsGoals = (FrameLayout.LayoutParams) fabGoals.getLayoutParams();

        layoutParamsEvent.rightMargin += (int) (fabEvent.getWidth() * 1.7);
        layoutParamsGoals.rightMargin += (int) (fabGoals.getWidth() * 1.7);
        layoutParamsEvent.leftMargin += (int) (fabEvent.getHeight() * 0.25);
        layoutParamsGoals.leftMargin += (int) (fabGoals.getHeight() * 0.25);
        fabEvent.setLayoutParams(layoutParamsEvent);
        fabGoals.setLayoutParams(layoutParamsGoals);
        fabEvent.startAnimation(showFabEvent);
        fabGoals.startAnimation(showFabGoals);
        fabEvent.setClickable(true);
        fabGoals.setClickable(true);

//        Intent intent = new Intent(this, AddEvent.class);
//        startActivity(intent);
    }
    // Fab Event
    // Fab Goal

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    // Remove Other Fragments first
                    // Then Load Current Fragment
                    Log.d(TAG, "Adding Calendar Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, calFrag, "CalFragAttached").commit();
                    return true;
                case R.id.navigation_goals:
                    Log.d(TAG, "Adding Goal Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, goalFrag, "GoalFragAttached").commit();
                    return true;
                case R.id.navigation_dashboard:
                    Log.d(TAG, "Adding Dashboard Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, dashFrag, "DashFragAttached").commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: 4/3/2019 display event selected 
    }
}
