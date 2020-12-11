package com.miki.justincase_v1.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.miki.justincase_v1.db.dao.ViajesDAO;
import com.miki.justincase_v1.db.entity.Viaje;


//se trata de la clase cuyos objetos vas a guardar
// en la base de datos; el número de versión empieza a contar por 1
// y tienes que cambiarlo cada vez que modifiques la base de datos.
@Database(entities = {Viaje.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    //singleton
    private static AppDatabase DBinstance;

    public abstract ViajesDAO viajesDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (DBinstance == null) {
            DBinstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return DBinstance;
    }
}

