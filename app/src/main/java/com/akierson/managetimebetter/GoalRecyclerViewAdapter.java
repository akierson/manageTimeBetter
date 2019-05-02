package com.akierson.managetimebetter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class GoalRecyclerViewAdapter extends RecyclerView.Adapter<GoalRecyclerViewAdapter.ViewHolder> {

    List<Goal> mValues;

    public GoalRecyclerViewAdapter(List<Goal> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_goals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Set values
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getName());
        switch (mValues.get(position).getLevel()) {
            case 0:
                holder.mLevel.setText("TODAY");
                break;
            case 1:
                holder.mLevel.setText("THIS WEEK");
                break;
            case 2:
                holder.mLevel.setText("THIS MONTH");
                break;
            case 3:
                holder.mLevel.setText("THIS YEAR");
                break;
            case 4:
                holder.mLevel.setText("NEXT 5 YEARS");
                break;
        }

        if (mValues.get(position).isRecursion()) {
            holder.mRecursion.setText(R.string.goal_isRecursive);
        } else {
            holder.mRecursion.setText(R.string.goal_notRecursive);
        }
    }

    public Goal deleteItem(int position) {
        Goal recentlyDeceased = mValues.get(position);
        mValues.remove(position);
        notifyItemRemoved(position);
        // TODO: Add Snackbar for undoing
        return recentlyDeceased;

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mRecursion;
        public final TextView mLevel;
        public Goal mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = view.findViewById(R.id.goalDescription);
            mRecursion = view.findViewById(R.id.goalRecursion);
            mLevel = view.findViewById(R.id.goalLevel);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
