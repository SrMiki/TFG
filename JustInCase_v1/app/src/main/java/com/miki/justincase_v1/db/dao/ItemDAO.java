package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.List;

//Item Data Acces Objet

@Dao
public interface ItemDAO {

    @Query("SELECT * FROM items WHERE itemName IS :itemName")
    Item getItemByItemName(String itemName);

    @Query("SELECT * FROM items WHERE itemID IS :itemID")
    Item getItem(int itemID);

    @Query("SELECT * FROM items")
    List<Item> getAll();

    @Query("SELECT * FROM items WHERE itemID IN (SELECT categoryContent.FKitemID from categoryContent WHERE FKcategoryID IS :categoryID)")
    List<Item> getItemFromThisCategory(int categoryID);

    @Query("SELECT * FROM items ORDER BY itemName")
    List<Item> selectAll();

    @Query("SELECT * FROM items WHERE NOT itemID IN (SELECT categoryContent.FKitemID from categoryContent) ORDER BY itemName")
    List<Item> selectItemWhitNoCategory();

    @Query("SELECT * FROM items WHERE itemID IN (SELECT categoryContent.FKitemID from categoryContent WHERE FKcategoryID IS :categoryID) ORDER BY itemName")
    List<Item> selectItemFromThisCategory(int categoryID);


    @Query("SELECT * FROM items WHERE itemID IN (SELECT Baggage.FKitemID from Baggage WHERE Baggage.FKHandLuggageID IS :handLuggageID) ORDER BY itemName")
    List<Item> selectItemFromThisBaggage(int handLuggageID);

    @Query("SELECT * FROM items WHERE NOT itemID IN (SELECT Baggage.FKitemID from Baggage WHERE Baggage.FKHandLuggageID IS :handLuggageID) ORDER BY itemName")
    List<Item> selectItemNOTFromThisBaggage(int handLuggageID);

//
//    @Query("SELECT * FROM items WHERE itemID IN ((SELECT categoryContent.FKitemID from categoryContent WHERE FKcategoryID IS :categoryID) AND (SELECT Baggage.FKitemID from Baggage WHERE Baggage.FKHandLuggageID IS :handLuggageID))")
//    List<Item> theMethod(int categoryID, int handLuggageID);

    @Update
    void update(Item item);

    @Update
    void updateListOfItem(List<Item> arrayList);

    //(onConflict = OnConflictStrategy.REPLACE)
    //default >> ABORT
    @Insert
    void insert(Item item);

    @Insert
    void insertAll(List<Item> item);

    @Delete
    void delete(Item item);

    @Delete
    void deleteAll(List<Item> itemList);



}

