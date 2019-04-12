package com.akierson.managetimebetter;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.Date;

import static com.akierson.managetimebetter.R.id.nextDay;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CalendarFragment";

    private OnFragmentInteractionListener mListener;
    private Date mStartDate = new Date();
    private Date mEndDate = new Date();

    // Layout items
    ImageButton nextDay;
    ImageButton prevDay;

    public CalendarFragment() {
        // Required empty public constructor
    }
    
    public static CalendarFragment newInstance() {
        // TODO: Add params for storing current day
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //TODO: Load calendar from current day


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        
        //TODO Load events from calendar model
        nextDay = mView.findViewById(R.id.nextDay);
        prevDay = mView.findViewById(R.id.prevDay);

        nextDay.setOnClickListener(this);
        prevDay.setOnClickListener(this);

        return mView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // TODO: 3/13/2019 Requires listener if passing information between
        // TODO: 3/20/2019 pass currently selected day(s) 
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        //TODO: save start and end days.
        super.onDetach();
        mListener = null;

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
