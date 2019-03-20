package com.akierson.managetimebetter;

import android.Manifest;
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
public class MainActivity extends AppCompatActivity {
    // Permission status
    private final int REQUEST_PERMISSION_READ_CALENDAR=1;
    private final int REQUEST_PERMISSION_WRITE_CALENDAR=2;

    // Tag for debugging
    private static final String TAG = "MainActivity";

    // For stopping time usage events

    // Views
    private ImageView previousDay;
    private ImageView nextDay;
    private TextView currentDate;
    private Calendar cal = Calendar.getInstance();
    private RelativeLayout mLayout;
    private int eventIndex;

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
        FloatingActionButton fab = findViewById(R.id.fab);
        mLayout = findViewById(R.id.left_event_column);

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

            // Get Events from Calendar
            getDataFromCalendarTable();
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
        Intent intent = new Intent(this, AddEvent.class);
        startActivity(intent);
    }

    public void getDataFromCalendarTable() {
        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.CALENDAR_LOCATION,
                        CalendarContract.Calendars.CALENDAR_TIME_ZONE
                };

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"*"};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            Log.d(TAG, "onCreate: Item added");
            String displayName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
            String accountName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));

            TextView tv1 =  new TextView(this);
            tv1.setText(displayName);
            mLayout.addView(tv1);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override

        // TODO: 3/13/2019 change over to ViewPager and have load calls in selectors 
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    // Remove Other Fragments first
                    // Then Load Current Fragment
                    Log.d(TAG, "Adding Calendar Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, calFrag).commit();
                    return true;
                case R.id.navigation_goals:
                    Log.d(TAG, "Adding Goal Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, goalFrag).commit();
                    return true;
                case R.id.navigation_dashboard:
                    Log.d(TAG, "Adding Dashboard Fragment");
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, dashFrag).commit();
                    return true;

            }
            return false;
        }
    };
}
