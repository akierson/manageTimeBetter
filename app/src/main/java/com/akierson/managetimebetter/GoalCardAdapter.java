package com.akierson.managetimebetter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GoalCardAdapter extends RecyclerView.Adapter<GoalCardAdapter.GoalCardHolder> {
    // TODO: 4/12/2019 Get Data from table 
    private Context mCtx;
    private List<Goal> goalList;

    public GoalCardAdapter(Context mCtx, List<Goal> goalList, View itemView) {
        this.mCtx = mCtx;
        this.goalList = goalList;
    }

    @NonNull
    @Override
    public GoalCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.content_add_goal, null);
        return new GoalCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalCardHolder goalCardHolder, int i) {
        Goal goal = goalList.get(i);
        goalCardHolder.level.setText(goal.getLevel());
        goalCardHolder.description.setText(goal.getDescription());
        goalCardHolder.recursion.setText(String.valueOf(goal.isRecursion()));
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    class GoalCardHolder extends RecyclerView.ViewHolder {

        TextView level, description, recursion;

        public GoalCardHolder(View itemView) {
            super(itemView);

            level = itemView.findViewById(R.id.goalLevel);
            description = itemView.findViewById(R.id.goalDescription);
            recursion = itemView.findViewById(R.id.goalRecursion);
        }
    }
}
