package com.akierson.managetimebetter;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GoalDAO {
    @Query("SELECT * FROM goal")
    List<Goal> getAll();

    @Query("SELECT * FROM goal WHERE _id IN (:goalIds)")
    List<Goal> loadAllByIds(int[] goalIds);

    // Set complete
    // Set Due Date

    @Query("SELECT * FROM goal WHERE _id IS :id")
    Goal findById(int id);

    @Query("SELECT * FROM goal WHERE name LIKE :name")
    Goal findByName(String name);

    @Insert
    void insertAll(Goal... goal);

    @Delete
    void delete(Goal goal);
}
