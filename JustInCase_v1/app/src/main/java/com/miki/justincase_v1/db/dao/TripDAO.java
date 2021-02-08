package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Trip;

import java.util.List;

//Data access object de la tabla "viajes"
//Entity Viaje.class

@Dao
public interface TripDAO {
    //LiveData
    @Query("SELECT * FROM trips")
    List<Trip> getAllEntity();

    @Query("SELECT * FROM trips WHERE tripID IS :tripID")
    Trip getTrip(int tripID);

    //date "YYYY-MM-DD" time "HH:MM:SS" datetime "YYYY-MM-DD HH:MM:SS"
    //TODO checkDateMethod!

    /**
     * required >> checkDateMethod
     * @return the list of all trips order by Date
     */
    @Query("SELECT * FROM trips ORDER BY date(travelDate) DESC LIMIT 1")
    List<Trip> getAllTripsOrderByTravelDate();

    @Update
    void update(Trip trip);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addViaje(Trip trip);

    @Delete
    void delete(Trip trip);
}

