package com.miki.justincase_v1.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.miki.justincase_v1.db.dao.BaggageDAO;
import com.miki.justincase_v1.db.dao.HandLuggageDAO;
import com.miki.justincase_v1.db.dao.CategoryContentDAO;
import com.miki.justincase_v1.db.dao.CategoryDAO;
import com.miki.justincase_v1.db.dao.ItemDAO;
import com.miki.justincase_v1.db.dao.SuitcaseDAO;
import com.miki.justincase_v1.db.dao.TemplateDAO;
import com.miki.justincase_v1.db.dao.TemplateElementDAO;
import com.miki.justincase_v1.db.dao.TripDAO;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Template;
import com.miki.justincase_v1.db.entity.TemplateElement;
import com.miki.justincase_v1.db.entity.Trip;

@Database(entities = {Trip.class, Suitcase.class, Item.class, HandLuggage.class, Baggage.class,
        Category.class, CategoryContent.class, Template.class, TemplateElement.class}, version = 2 )
public abstract class AppDatabase extends RoomDatabase {

    //singleton!
    private static AppDatabase DBinstance;

    //Trip
    public abstract TripDAO tripDao();

    //Suitcase
    public abstract SuitcaseDAO suitcaseDAO();

    //Item
    public abstract ItemDAO itemDAO();

    //Baggage
    public abstract HandLuggageDAO handLuggageDAO();

    //Register item - baggage
    public abstract BaggageDAO baggageDAO();

    //Category
    public abstract CategoryDAO categoryDAO();

    //Category's Items
    public abstract CategoryContentDAO categoryContentDAO();

    //Template
    public abstract TemplateDAO templateDAO();

    //Template's Elements
    public abstract TemplateElementDAO templateElementDAO();

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

