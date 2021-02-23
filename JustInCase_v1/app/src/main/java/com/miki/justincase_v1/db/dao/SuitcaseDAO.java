package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Suitcase;

import java.util.ArrayList;
import java.util.List;

//Data access object de "suitcase"

@Dao
public interface SuitcaseDAO {
    //LiveData
    @Query("SELECT * FROM suitcases ORDER BY name")
    List<Suitcase> selectAll();

    @Query("SELECT * FROM suitcases WHERE suitcaseID IS :suitcaseID")
    Suitcase getSuitcase(int suitcaseID);

    @Query("SELECT * FROM suitcases WHERE NOT suitcaseID IN (SELECT FKsuitcaseID FROM HandLuggage WHERE FKtripID IS :tripID  )ORDER BY name")
    List<Suitcase> selectSuitcaseNOTFromThisTrip(int tripID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Suitcase suitcase);

    @Insert
    void insertAll(ArrayList<Suitcase> suitcases);

    @Delete
    void delete(Suitcase suitcase);

    @Update
    void update(Suitcase suitcase);

}

