package com.akierson.managetimebetter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CalendarFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CalendarFragment";

    private Calendar mStartDate;
    private Calendar mEndDate;
    private CalendarDataModel mCalModel;
    SimpleDateFormat dateParse;

    AsyncTaskRunner eventLoader;

    // Layout items
    ImageButton nextDay;
    ImageButton prevDay;

    RelativeLayout dayOne;
    RelativeLayout dayTwo;
    RelativeLayout dayThree;
    private Fragment mcContent;

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
        // Restore Calendar Things
//        mcContent = getFragmentManager().getFragment(savedInstanceState, "calendarFragment");
        // TODO: 4/13/2019 Create view based and date range /Later
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

        nextDay = mView.findViewById(R.id.nextDay);
        prevDay = mView.findViewById(R.id.prevDay);

        dayOne = mView.findViewById(R.id.left_event_column_day_one);
        dayTwo = mView.findViewById(R.id.left_event_column_day_two);
        dayThree = mView.findViewById(R.id.left_event_column_day_three);

        nextDay.setOnClickListener(this);
        prevDay.setOnClickListener(this);

        // TODO: 4/24/2019 not loading
        AsyncTaskRunner eventLoader = new AsyncTaskRunner();
        eventLoader.execute();

        return mView;
    }

    private void removeEvents() {
        dayOne.removeAllViews();
        dayTwo.removeAllViews();
        dayThree.removeAllViews();
    }


    @Override
    // Day Changer
    public void onClick(View v) {
        Calendar newDay;
        switch (v.getId()) {
            case R.id.nextDay:
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
                eventLoader.execute();
                break;
            case R.id.prevDay:
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
                eventLoader.execute();
                break;
        }
        Log.d(TAG, "onClick: New dates: " + mCalModel.getStartDay().getTime().toString() + " - " + mCalModel.getEndDay().getTime().toString());
    }

    private class AsyncTaskRunner extends AsyncTask<ArrayList<Event>, String, Void> {

        ProgressDialog progressDialog;
        ArrayList<RelativeLayout> containers = new ArrayList<>();

        protected Void doInBackground(ArrayList<Event>... event) {

            // Calls onProgressUpdate()
            publishProgress("Loading...");

            //Load events from calendar model
            Log.d(TAG, "loadEvents: " + event.getTitle() + " Retrieved");

            // Create Markers, as longs b/s millis
            long dayOneStart = mCalModel.getStartDay().getTimeInMillis();
            long dayTwoStart = dayOneStart + 86400000;
            long dayThreeStart = dayTwoStart + 86400000;
            long dayThreeEnd = dayThreeStart + 86400000;

            // Add Containers for events
            RelativeLayout dayOneContainer = (RelativeLayout) RelativeLayout.inflate(getActivity(), R.layout.fragment_calendar_container, null);
            RelativeLayout dayTwoContainer = (RelativeLayout) RelativeLayout.inflate(getActivity(), R.layout.fragment_calendar_container, null);
            RelativeLayout dayThreeContainer = (RelativeLayout) RelativeLayout.inflate(getActivity(), R.layout.fragment_calendar_container, null);

               Log.d(TAG, "loadEvents: " + newEvent.getTitle() + " Time: " + newEvent.getBegin().getTime().toString() + " - " + newEvent.getEnd().getTime().toString());

                    // Create event view
                    LayoutInflater eventInflator = LayoutInflater.from(getActivity());
                    View eventLayout = (View) eventInflator.inflate(R.layout.fragment_calendar_event, null, true);

                    // Get items in view
                    TextView eventid = eventLayout.findViewById(R.id.event_id);
                    TextView eventName = eventLayout.findViewById(R.id.event_name);
                    TextView eventLocation = eventLayout.findViewById(R.id.event_location);

                    if (!newEvent.isAllDay()) {

                        // Set items text
                        // Add try except: Holidays have no event id
                        try {
                            eventName.setText(newEvent.getTitle());
                            eventLocation.setText(newEvent.getLocation());

                            // Set EventLayout Params
                            // TODO: 4/24/2019 Create optimizer for events
                            long distFromTop = (dayOne.getHeight() * newEvent.getBegin().getTimeInMillis()) / 86400000;
                            Log.d(TAG, "doInBackground: top Margin" + distFromTop);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)eventLayout.getLayoutParams();
                            params.setMargins(0, (int) distFromTop, 0, 0);
                            eventLayout.setLayoutParams(params);

                            int startTime = (int) newEvent.getBegin().getTimeInMillis();
                            if (startTime > dayOneStart && startTime < dayTwoStart ){
                                // Add Event to column 1
                                dayOneContainer.addView(eventLayout, i);

                            } else if (startTime < dayThreeStart){
                                // Add Event to column 2
                                dayTwoContainer.addView(eventLayout, i);

                            } else if (startTime < dayThreeEnd){
                                // Add Event to column 3
                                dayThreeContainer.addView(eventLayout, i);

                            }
                        } catch (Exception e){
                            Log.d(TAG, "loadEvents: No ID for " + newEvent.getTitle() + " " + e );
                        }
                    } else {
                        // add Event to place under date
                    }

                }
            containers.add(dayOneContainer);
            containers.add(dayOneContainer);
            containers.add(dayOneContainer);
            return containers;
        }

        @Override
        protected void onPostExecute(ArrayList<RelativeLayout> containers) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            dayOne.addView(containers.get(0));
            dayTwo.addView(containers.get(1));
            dayThree.addView(containers.get(2));

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "Adding Events",
                    "Loading");
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}
