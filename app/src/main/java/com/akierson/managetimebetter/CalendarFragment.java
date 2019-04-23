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
import java.util.Date;
import java.util.Objects;


public class CalendarFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CalendarFragment";

    private Date mStartDate;
    private Date mEndDate;
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
            mStartDate = (Date) args.getSerializable("startDate");
            mEndDate = (Date) args.getSerializable("endDate");
        } else {
            mStartDate = new Date();
            mEndDate = new Date();
        }

        nextDay = mView.findViewById(R.id.nextDay);
        prevDay = mView.findViewById(R.id.prevDay);

        dayOne = mView.findViewById(R.id.left_event_column_day_one);
        dayTwo = mView.findViewById(R.id.left_event_column_day_two);
        dayThree = mView.findViewById(R.id.left_event_column_day_three);

        nextDay.setOnClickListener(this);
        prevDay.setOnClickListener(this);

        //TODO Load events from calendar model

        loadEvents();

        return mView;

    }

    private void loadEvents() {
        ArrayList<Event> events = mCalModel.getCalendarEvents();
        for (int i = 0; i < events.size(); i++) {

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
        switch (v.getId()) {
            case R.id.nextDay:
                // TODO: 4/3/2019 Go to next day
                Log.d(TAG, "onClick: Next Day Clicked");
                break;
            case R.id.prevDay:
                // TODO: 4/3/2019 Go to prev day
                Log.d(TAG, "onClick: Prev Day Clicked");
                break;
        }
    }
}
