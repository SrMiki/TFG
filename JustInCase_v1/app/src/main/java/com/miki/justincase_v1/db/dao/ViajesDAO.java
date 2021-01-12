package com.miki.justincase_v1.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Viaje;

import java.util.List;

//Data access object de la tabla "viajes"
//Entity Viaje.class

@Dao
public interface ViajesDAO {
    //LiveData
    @Query("SELECT * FROM viajes")
    List<Viaje> getAll();

    @Query("SELECT * FROM viajes WHERE viajeID IN (:userIds)")
    List<Viaje> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM viajes WHERE destino LIKE :first AND " +
            "fecha LIKE :last LIMIT 1")
    Viaje findByName(String first, String last);

    @Insert
    void insertAll(Viaje[] viajes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addViaje(Viaje viaje);

    @Delete
    void delete(Viaje viaje);
}

