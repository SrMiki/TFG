package com.miki.justincase_v1.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Viaje;

import java.util.List;

//Data access object de "maletas"

@Dao
public interface SuitcaseDAO {
    //LiveData
    @Query("SELECT * FROM suitcases")
    List<Suitcase> getAll();

    @Query("SELECT * FROM suitcases WHERE suitcaseName LIKE :name ")
    Suitcase findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewSuitcase(Suitcase suitcase);

    @Delete
    void delete(Suitcase suitcase);
}

