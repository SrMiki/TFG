package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Item;

import java.util.List;

//Item Data Acces Objet

@Dao
public interface ItemDAO {
    @Query("SELECT * FROM items ORDER BY itemName")
    List<Item> getAll();

    @Query("SELECT * FROM items WHERE NOT itemID IN (SELECT Baggage.FKitemID from Baggage WHERE Baggage.FKbaggageID IS :baggageID) ORDER BY itemName")
    List<Item> getAllItemsThatItNotInThisBaggage(int baggageID);

    @Query("SELECT * FROM items WHERE NOT itemID IN (SELECT categoryContent.FKitemID from categoryContent) ORDER BY itemName")
    List<Item> getAllItemsThatItNotInThisCategory();

    @Query("SELECT * FROM items WHERE itemID IN (SELECT categoryContent.FKitemID from categoryContent WHERE FKcategoryID IS :categoryID) ORDER BY itemName")
    List<Item> getAllItemsFromThisCategory(int categoryID);

    @Query("SELECT * FROM items WHERE itemID IS :itemID")
    Item getItem(int itemID);

    @Update
    void updateItem(Item item);

    //(onConflict = OnConflictStrategy.REPLACE)
    //default >> ABORT
    @Insert
    void addItem(Item item);

    @Insert
    void addListItem(List<Item> item);

    @Delete
    void delete(Item item);
}

