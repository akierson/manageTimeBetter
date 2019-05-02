package com.akierson.managetimebetter;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CalendarFragment extends Fragment{

    private static final String TAG = "CalendarFragment";
    private Calendar mStartDate;
    private Calendar mEndDate;
    private CalendarDataModel mCalModel;
    SimpleDateFormat dateParse;

    AsyncTaskRunner eventLoader;

    // Layout items
    ScrollView scrollView;
    RelativeLayout dayOne;
    RelativeLayout dayTwo;
    RelativeLayout dayThree;

    TextView dayOneDisplay;
    TextView dayTwoDisplay;
    TextView dayThreeDisplay;

    ProgressBar progressBar;
    GestureDetector gestDetector;

    // TODO: 4/28/2019 Add Reload on scroll down, Also Requires on Fragment interaction
    public CalendarFragment() {
        // Required empty public constructor
    }
    
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Using calendar model
        mCalModel = new CalendarDataModel(this.getActivity());
        Format dateFormat = DateFormat.getDateFormat(getActivity().getApplicationContext());
        String pattern = ((SimpleDateFormat)dateFormat).toLocalizedPattern();
        dateParse = new SimpleDateFormat(pattern);

        // Load Start Day and End Dat from previous instance
        Bundle args = this.getArguments();
        if (args != null){
            mStartDate = (Calendar) args.getSerializable("startDate");
            mEndDate = (Calendar) args.getSerializable("endDate");
        } else {
            mStartDate = Calendar.getInstance();
            mEndDate = Calendar.getInstance();
        }
        // Set Calendar Date model
        // Dates also need to be set to start of day
        mStartDate.set(Calendar.HOUR, 0);
        mStartDate.set(Calendar.MINUTE, 0);
        mStartDate.set(Calendar.SECOND, 0);
        mStartDate.set(Calendar.MILLISECOND, 0);
        mEndDate.set(Calendar.HOUR, 0);
        mEndDate.set(Calendar.MINUTE, 0);
        mEndDate.set(Calendar.SECOND, 0);
        mEndDate.set(Calendar.MILLISECOND, 0);

        mCalModel.setStartDay(mStartDate);
        mCalModel.setEndDay(mEndDate);

        dayOne = mView.findViewById(R.id.left_event_column_day_one);
        dayTwo = mView.findViewById(R.id.left_event_column_day_two);
        dayThree = mView.findViewById(R.id.left_event_column_day_three);

        dayOneDisplay = mView.findViewById(R.id.calendar_display_dayOne);
        dayTwoDisplay = mView.findViewById(R.id.calendar_display_dayTwo);
        dayThreeDisplay = mView.findViewById(R.id.calendar_display_dayThree);

        progressBar = mView.findViewById(R.id.calendar_loadEvent_progressBar);

        scrollView = mView.findViewById(R.id.calendar_scrollView);

        gestDetector = new GestureDetector(this.getContext(), mGestureListener);

        AsyncTaskRunner eventLoader = new AsyncTaskRunner();
        eventLoader.execute(mCalModel.getCalendarEvents());

        return mView;
    }

    private void removeEvents() {
        dayOne.removeAllViews();
        dayTwo.removeAllViews();
        dayThree.removeAllViews();
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                                float distanceY) {
            // TODO: 5/1/2019 Add Reload here
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Calendar newDay;
            float diff = Math.abs(e1.getX() - e2.getX());
            // TODO: 5/1/2019 Make diff screen independent
            // TODO: 5/1/2019 add animations 
            if (diff > 150) {
                if (e1.getX() > e2.getX()) {
                    Log.d(TAG, "onClick: Next Day Clicked");
                    //Set Start Day
                    newDay = mCalModel.getStartDay();
                    newDay.add(Calendar.DATE, 1);
                    mCalModel.setStartDay(newDay);

                    // Set End Day
                    newDay = mCalModel.getEndDay();
                    newDay.add(Calendar.DATE, 1);
                    mCalModel.setEndDay(newDay);
                    removeEvents();
                    eventLoader = new AsyncTaskRunner();
                    eventLoader.execute(mCalModel.getCalendarEvents());
                } else {
                    Log.d(TAG, "onClick: Prev Day Clicked");
                    //Set Start Day
                    newDay = mCalModel.getStartDay();
                    newDay.add(Calendar.DATE, -1);
                    mCalModel.setStartDay(newDay);

                    // Set End Day
                    newDay = mCalModel.getEndDay();
                    newDay.add(Calendar.DATE, -1);
                    mCalModel.setEndDay(newDay);
                    removeEvents();
                    eventLoader = new AsyncTaskRunner();
                    eventLoader.execute(mCalModel.getCalendarEvents());
                }
            }

            Log.d(TAG, "onClick: New dates: " + mCalModel.getStartDay().getTime().toString() + " - " + mCalModel.getEndDay().getTime().toString());
            return true;
        }
    };

    private class AsyncTaskRunner extends AsyncTask<ArrayList<Event>, String, ArrayList<RelativeLayout>> {

        protected ArrayList<RelativeLayout> doInBackground(ArrayList<Event>... arrayList) {

            // TODO: 4/26/2019 Load calendars and color values
            HashMap<String, String> userCals = mCalModel.getCalendars();
            // Calls onProgressUpdate()
            publishProgress("Loading...");
            // Get events from varargs
            ArrayList<Event> events;
            try {
                events = arrayList[0];

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Events empty: ", e);
                return null;
            }

            //Load events from calendar model
            Log.d(TAG, "doInBackground: " + events.size() + " Events Retrieved");

            // Create Markers, as longs b/c in millis
            Calendar dayOneStart = mCalModel.getStartDay();
            Calendar dayTwoStart = (Calendar) dayOneStart.clone();
            dayTwoStart.add(Calendar.DATE, 1);
            Calendar dayThreeStart = (Calendar) dayTwoStart.clone();
            dayThreeStart.add(Calendar.DATE, 1);
            Calendar dayThreeEnd = (Calendar) dayThreeStart.clone();
            dayThreeEnd.add(Calendar.DATE, 1);

            // TODO: 5/1/2019 make static fields 
            DisplayMetrics displayMetric = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetric);
            float scrollViewHeight = (getResources().getDisplayMetrics().density * 1500);

            // Add Containers for events
            ArrayList<RelativeLayout> containers = new ArrayList<>();

            RelativeLayout dayOneContainer = (RelativeLayout) RelativeLayout.inflate(getActivity(), R.layout.fragment_calendar_container, null);
            RelativeLayout dayTwoContainer = (RelativeLayout) RelativeLayout.inflate(getActivity(), R.layout.fragment_calendar_container, null);
            RelativeLayout dayThreeContainer = (RelativeLayout) RelativeLayout.inflate(getActivity(), R.layout.fragment_calendar_container, null);

            Date newDate = mCalModel.getStartDay().getTime();
            dayOneDisplay.setText(dateParse.format(newDate));
            newDate.setTime(newDate.getTime() + 86400000);
            dayTwoDisplay.setText(dateParse.format(newDate));
            newDate.setTime(newDate.getTime() + 86400000);
            dayThreeDisplay.setText(dateParse.format(newDate));

            // Set Colors by number of calendars
            //TODO: fix me
//            HashMap<String, Integer> colors = new HashMap<>();
//            int interval = (int) (16581375.0/userCals.size());
//            for (int i = 0; i < userCals.size(); i++) {
//                colors.put(userCals.get(i), interval * i);
//                Log.d(TAG, "doInBackground: Calendar: " + userCals.get(i) + " Color: " + userCals.get(i));
//            }

            for (int i = 0; i < events.size(); i++) {
                final Event newEvent = events.get(i);
                Log.d(TAG, "doInBackground: " + newEvent.getTitle() + ": " + newEvent.getBegin().getTime().toString() + " - " + newEvent.getEnd().getTime().toString());

                if (!newEvent.isAllDay()) {
                    // Create event view
                    View eventLayout = (View) LinearLayout.inflate(getActivity(), R.layout.fragment_calendar_event, null);
                    // TODO: 4/25/2019 set color of event
                    // Get Color from array
                    // TODO: Add on click event for viewing events
                    eventLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(newEvent.getDescription())
                                    .setTitle(newEvent.getTitle());
                            AlertDialog dialog = builder.create();
                            Log.d(TAG, "onClick: Clicked");
                            dialog.show();
                        }
                    });

                    // Get items in view
                    TextView eventName = eventLayout.findViewById(R.id.event_name);
                    TextView eventLocation = eventLayout.findViewById(R.id.event_location);

                    Calendar startTime = (Calendar) newEvent.getBegin().clone();
                    // Tag Item with _id to use for editing events later on
                    try {
                        eventLayout.setTag(newEvent.get_id());
                    } catch (Exception e) {
                        Log.e(TAG, "doInBackground: Event: " + newEvent.title + " has no Id: " + e);
                    }

                    try {
                        // Set items text
                        eventName.setText(newEvent.getTitle());
                        eventLocation.setText(newEvent.getLocation());

                        // TODO: 4/24/2019 Create optimizer for events
                        long height = newEvent.getEnd().getTimeInMillis() - startTime.getTimeInMillis();
                        height = (long) ((height * scrollViewHeight)/86400000);

                        // 1 hr = 60dp, 1min = 1dp
                        float distFromTop = ((startTime.get(Calendar.HOUR_OF_DAY) * 60) + startTime.get(Calendar.MINUTE)) * getResources().getDisplayMetrics().density;

                        if (startTime.after(dayOneStart) && startTime.before(dayTwoStart) ) {
                            // Set Params
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
                            params.setMargins(0, (int) distFromTop, 0, 0);
                            eventLayout.setLayoutParams(params);
                            // Add Event to column 1
                            dayOneContainer.addView(eventLayout);

                        } else if (startTime.before(dayThreeStart)){
                            // Set Params
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
                            params.setMargins(0, (int) distFromTop, 0, 0);
                            eventLayout.setLayoutParams(params);
                            // Add Event to column 2
                            dayTwoContainer.addView(eventLayout);

                        } else if (startTime.before(dayThreeEnd)){
                            // Set Params
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
                            params.setMargins(0, (int) distFromTop, 0, 0);
                            eventLayout.setLayoutParams(params);
                            // Add Event to column 3
                            dayThreeContainer.addView(eventLayout);
                        }


                    } catch (Exception e){
                        Log.e(TAG, "doInBackground: Error ", e );
                    }
                } else {
                    // add Event to place under date
                }

            }
            containers.add(dayOneContainer);
            containers.add(dayTwoContainer);
            containers.add(dayThreeContainer);

            return containers;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

        @Override
        protected void onPostExecute(ArrayList<RelativeLayout> containers) {
            // execution of result of Long time consuming operation
            progressBar.setVisibility(View.GONE);

            dayOne.addView(containers.get(0));
            dayTwo.addView(containers.get(1));
            dayThree.addView(containers.get(2));

        }
    }
}
