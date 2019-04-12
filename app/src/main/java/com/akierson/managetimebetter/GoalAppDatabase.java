package com.akierson.managetimebetter;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Goal.class}, version = 1, exportSchema = false)
public abstract class GoalAppDatabase extends RoomDatabase {
    public abstract GoalDAO userDAO();
}
