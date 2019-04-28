package com.akierson.managetimebetter;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Goal.class}, version = 1, exportSchema = false)
public abstract class GoalAppDatabase extends RoomDatabase {
    public abstract GoalDAO goalDAO();
    private static GoalAppDatabase INSTANCE;

    public static GoalAppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), GoalAppDatabase.class, "goal-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


}
