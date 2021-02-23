package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;

import java.util.List;

//Data access object of "Baggage"

@Dao
public interface HandLuggageDAO {
    @Query("SELECT * FROM HandLuggage")
    List<HandLuggage> getAll();

    @Query("SELECT * FROM HandLuggage WHERE handLuggageID IS :handLuggageID")
    HandLuggage getHandLuggage(int handLuggageID);

    @Query("SELECT * FROM HandLuggage WHERE FKtripID IS :tripID ")
    List<HandLuggage> getTheHandLuggageOfThisTrip(int tripID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HandLuggage handLuggage);

    @Delete
    void delete(HandLuggage handLuggage);

    @Update
    void update(HandLuggage handLuggage);

}

