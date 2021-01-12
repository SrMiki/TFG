package com.miki.justincase_v1.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.List;

//Data access object de "equipaje"

@Dao
public interface BaggageDAO {
    //LiveData
    @Query("SELECT * FROM baggage")
    List<Baggage> getAll();

    @Query("SELECT * FROM baggage WHERE s_id LIKE :vid AND v_id LIKE :sid ")
    Baggage getBaggageOfThisTripAndThisSuitcase(int vid, int sid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBaggage(Baggage baggage);

    @Delete
    void deleteBaggage(Baggage baggage);
}

