package com.akierson.managetimebetter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Goals.
 */
// TODO: 5/1/2019 Add Sorting
//    5/1/2019 Add Reload when coming from add goal/event
//    have detailed view
public class GoalsFragment extends Fragment {

    private CalendarDataModel mCal;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GoalsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GoalsFragment newInstance() {
        GoalsFragment fragment = new GoalsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: 5/1/2019 color by area
        View view = inflater.inflate(R.layout.fragment_goals_list, container, false);

        mCal = new CalendarDataModel(getContext());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
            Comparator<Goal> goalCompare = new Comparator<Goal>() {
                @Override
                public int compare(Goal o1, Goal o2) {

                    return o1.getLevel() - o2.getLevel();
                }
            };
            List<Goal> goals = mCal.getGoals();
            goals.sort(goalCompare);
            GoalRecyclerViewAdapter mAdapter = new GoalRecyclerViewAdapter(goals);
            recyclerView.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Custome Classes for deleting etc. from https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
    public  class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        private GoalRecyclerViewAdapter mAdapter;

        private Drawable icon;
        private final ColorDrawable background;


        public SwipeToDeleteCallback(GoalRecyclerViewAdapter adapter) {
            super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            mAdapter = adapter;
            icon = getContext().getDrawable(R.drawable.baseline_delete_white_24dp);
            background = new ColorDrawable(Color.RED);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // used for up and down movements
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // TODO: Add Goal completion on left swipe
            int position = viewHolder.getAdapterPosition();
            mCal.gdb.goalDAO().delete(mAdapter.deleteItem(position));
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                int iconRight = itemView.getLeft() + iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            } else if (dX < 0) { // Swiping to the left
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }

            background.draw(c);
            icon.draw(c);
        }
    }
}
