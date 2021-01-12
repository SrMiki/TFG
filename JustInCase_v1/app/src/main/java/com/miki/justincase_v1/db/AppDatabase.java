package com.miki.justincase_v1.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.miki.justincase_v1.db.dao.BaggageDAO;
import com.miki.justincase_v1.db.dao.ItemDAO;
import com.miki.justincase_v1.db.dao.SuitcaseContentDAO;
import com.miki.justincase_v1.db.dao.SuitcaseDAO;
import com.miki.justincase_v1.db.dao.ViajesDAO;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.SuitcaseContent;
import com.miki.justincase_v1.db.entity.Viaje;


//se trata de la clase cuyos objetos vas a guardar
// en la base de datos; el número de versión empieza a contar por 1
// y tienes que cambiarlo cada vez que modifiques la base de datos.
@Database(entities = {Viaje.class, Suitcase.class, Item.class, Baggage.class, SuitcaseContent.class}, version = 3)

public abstract class AppDatabase extends RoomDatabase {

    //singleton
    private static AppDatabase DBinstance;

    public abstract ViajesDAO viajesDao();

    //Maletas indp.
    public abstract SuitcaseDAO suitcaseDAO();

    //Items indp.
    public abstract ItemDAO itemDAO();

    //Equipaje de un viaje
    public abstract BaggageDAO baggageDAO();

    //Contenido de maletas
    public abstract SuitcaseContentDAO suitcaseContentDAO();

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

