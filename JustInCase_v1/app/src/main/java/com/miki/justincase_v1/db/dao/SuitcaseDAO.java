package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    //@Query("SELECT * FROM items WHERE NOT itemID IN (SELECT baggageContent.FKitemID from baggageContent WHERE baggageContent.FKbaggageID IS :baggageID) ORDER BY itemName")
    @Query("SELECT * FROM suitcases WHERE NOT suitcaseID IN (SELECT FKsuitcaseID FROM baggage WHERE FKtripID IS :tripID  )ORDER BY suitcaseName")
    List<Suitcase> getAllSuitcaseThatItNotInThisTrip(int tripID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewSuitcase(Suitcase suitcase);

    @Delete
    void delete(Suitcase suitcase);

    @Update
    void update(Suitcase suitcase);

}

