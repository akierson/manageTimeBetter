package com.akierson.managetimebetter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final String STARTDAY = "start-day";
    private static final String ENDDAY = "end-day";
    String mStartDay;
    String mEndDay;

    @SuppressLint("ValidFragment")
    public DashboardFragment() {
        // Required empty public constructor
    }
    // TODO: 4/12/2019 Load Dashboard Graphs 
    // TODO: 4/12/2019 Time spent on each goal area 
    // TODO: 4/12/2019 Goals Accomplished 
    // TODO: 4/12/2019 Time procrastinated 

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param startDay first day to start reporting - will default to first day from calendar.
     * @param endDay Last day to end reporting - will default to last day on calendar.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String startDay, String endDay) {
        DashboardFragment fragment = new DashboardFragment();
        // TODO: 3/20/2019 research this
        Bundle args = new Bundle();
        args.putString(STARTDAY, startDay);
        args.putString(ENDDAY, endDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStartDay = getArguments().getString(STARTDAY);
            mEndDay = getArguments().getString(ENDDAY);
        }
        // TODO: 3/20/2019 Get data from DashboardModel
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // TODO: 3/20/2019 Save dashboard end day to tempData
        mListener = null;
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
