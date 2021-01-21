package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Baggage;

import java.util.List;

//Data access object of "Baggage"

@Dao
public interface BaggageDAO {
    @Query("SELECT * FROM baggage")
    List<Baggage> getAll();

    @Query("SELECT * FROM baggage WHERE baggageID IS :baggageID")
    Baggage getThisBagagge(int baggageID);

    @Query("SELECT * FROM baggage WHERE FKtripID IS :tripID ")
    List<Baggage> getTheBaggageOfThisTrip(int tripID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewBaggageForThisTrip(Baggage baggage);

    @Delete
    void deleteBaggage(Baggage baggage);
}

