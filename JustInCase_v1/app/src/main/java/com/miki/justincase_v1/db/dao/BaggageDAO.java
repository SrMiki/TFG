package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Baggage;

import java.util.List;

//Data access object de "SuitcaseContent"

@Dao
public interface BaggageDAO {
    //LiveData
    @Query("SELECT * FROM Baggage ORDER BY itemName")
    List<Baggage> getAll();

    @Query("SELECT * FROM Baggage WHERE baggageID IS :baggageID")
    Baggage getBaggageContent(int baggageID);

    @Query("SELECT * FROM Baggage WHERE FKbaggageID IS :baggageID ORDER BY itemName ")
    List<Baggage> getItemsFromThisBaggage(int baggageID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewBaggage(Baggage baggage);

    @Delete
    void delete(Baggage baggage);

    @Update
    void update(Baggage baggage);
}

