package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Suitcase;

import java.util.List;

//Data access object de "suitcase"

@Dao
public interface SuitcaseDAO {
    //LiveData
    @Query("SELECT * FROM suitcases ORDER BY suitcaseName")
    List<Suitcase> getAll();

    @Query("SELECT * FROM suitcases WHERE suitcaseID IS :suitcaseID")
    Suitcase getSuitcase(int suitcaseID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewSuitcase(Suitcase suitcase);

    @Delete
    void delete(Suitcase suitcase);
}

