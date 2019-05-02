package com.akierson.managetimebetter;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * A fragment showing a CardView
 */
public class DashboardFragment extends Fragment {

    // Constants for intents, tags and permission numeration
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    // Params
    Calendar startDate;
    Calendar endDate;
    CalendarDataModel mCal;

    SurfaceView goalsByArea;
    SurfaceView goalsByDay;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashboardFragment() {

    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mCal = new CalendarDataModel(getContext());

        goalsByArea = view.findViewById(R.id.dashboard_goals_area_graph);
        goalsByDay = view.findViewById(R.id.dashboard_goals_day_graph);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
