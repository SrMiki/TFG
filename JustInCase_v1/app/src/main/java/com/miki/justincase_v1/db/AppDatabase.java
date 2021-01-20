package com.miki.justincase_v1.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.miki.justincase_v1.db.dao.BaggageContentDAO;
import com.miki.justincase_v1.db.dao.BaggageDAO;
import com.miki.justincase_v1.db.dao.CategoryContentDAO;
import com.miki.justincase_v1.db.dao.CategoryDAO;
import com.miki.justincase_v1.db.dao.ItemDAO;
import com.miki.justincase_v1.db.dao.SuitcaseDAO;
import com.miki.justincase_v1.db.dao.TripDAO;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Trip;


//App database
//Content all objects we would save; start version at 1 but, for every change we must upgrade it
//Just for the device who access
@Database(entities = {Trip.class, Suitcase.class, Item.class, Baggage.class, BaggageContent.class, Category.class, CategoryContent.class}, version = 15)

public abstract class AppDatabase extends RoomDatabase {

    //singleton!
    private static AppDatabase DBinstance;

    //Trips
    public abstract TripDAO tripDao();

    //Suitcase
    public abstract SuitcaseDAO suitcaseDAO();

    //Items
    public abstract ItemDAO itemDAO();

    //Baggage
    public abstract BaggageDAO baggageDAO();

    //Register item - baggage
    public abstract BaggageContentDAO baggageContentDAO();

    //Categories
    public abstract CategoryDAO categoryDAO();

    public abstract CategoryContentDAO categoryContentDAO();

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

