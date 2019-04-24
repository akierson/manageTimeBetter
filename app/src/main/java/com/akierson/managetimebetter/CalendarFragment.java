package com.akierson.managetimebetter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class CalendarFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CalendarFragment";

    private Calendar mStartDate;
    private Calendar mEndDate;
    private CalendarDataModel mCalModel;

    // Layout items
    ImageButton nextDay;
    ImageButton prevDay;

    RelativeLayout dayOne;
    RelativeLayout dayTwo;
    RelativeLayout dayThree;

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

        // TODO: 4/13/2019 Create view based and date range /Later
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Using calendar model
        mCalModel = new CalendarDataModel(this.getActivity());

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
        mCalModel.setStartDay(mStartDate);
        mCalModel.setEndDay(mEndDate);

        nextDay = mView.findViewById(R.id.nextDay);
        prevDay = mView.findViewById(R.id.prevDay);

        dayOne = mView.findViewById(R.id.left_event_column_day_one);
        dayTwo = mView.findViewById(R.id.left_event_column_day_two);
        dayThree = mView.findViewById(R.id.left_event_column_day_three);

        nextDay.setOnClickListener(this);
        prevDay.setOnClickListener(this);


        loadEvents();

        return mView;

    }

    private void loadEvents() {
        //Load events from calendar model
        ArrayList<Event> events = mCalModel.getCalendarEvents();

        for (int i = 0; i < events.size(); i++) {
            Log.d(TAG, "loadEvents: " + events.get(i).getTitle() + " Time: " + events.get(i).getBegin() + " - " + events.get(i).getEnd());
            // TODO: 4/23/2019 Load event into calendar

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        //TODO: save start and end days.
        super.onDetach();
    }

    @Override
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
                mCalModel.setStartDay(newDay);
                // TODO: 4/23/2019 Reload mCal and events
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
                mCalModel.setStartDay(newDay);
                break;
        }
    }
}
