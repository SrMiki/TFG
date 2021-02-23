package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;
import java.util.List;

//Data access object de la tabla "viajes"
//Entity Viaje.class

@Dao
public interface TripDAO {
    //LiveData
    @Query("SELECT * FROM trips")
    List<Trip> getAll();

    @Query("SELECT * FROM trips WHERE tripID IS :tripID")
    Trip getTrip(int tripID);

    @Query("SELECT * FROM trips WHERE travelling IS 0 ORDER BY date(travelDate)")
    List<Trip> selectPlanningTrip();

    @Query("SELECT * FROM trips WHERE ((travelling IS 1) OR (travelling IS 3)) ORDER BY date(travelDate)")
    List<Trip> selectCheckOutTrip();

    @Query("SELECT * FROM trips WHERE travelling IS 2 ORDER BY date(travelDate)")
    List<Trip> selectCheckInBACKtrip();

    @Query("SELECT * FROM trips WHERE travelling IS 4 ORDER BY date(travelDate)")
    List<Trip> selectFinishedTrip();

    @Update
    void update(Trip trip);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Trip trip);

    @Insert
    void insertAll(ArrayList<Trip> trips);

    @Delete
    void delete(Trip trip);

}