package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BaggageDAO {
    //LiveData

    //getters

    @Query("SELECT * FROM baggage " +
            "WHERE FKHandLuggageID IS :handLuggageID " +
            "AND FKitemID IN (SELECT FKitemID FROM CATEGORYCONTENT WHERE FKcategoryID IS :categoryID) ")
//    @Query("SELECT * FROM baggage INNER JOIN categoryContent ON categoryContent.FKitemID IS baggage.FKitemID WHERE (baggage.FKHandLuggageID IS :handLuggageID) AND (categoryContent.FKcategoryID IS :categoryID)")
    List<Baggage> theMethod(int categoryID, int handLuggageID);

    @Query("SELECT * FROM Baggage ORDER BY itemName")
    List<Baggage> getAll();

    @Query("SELECT * FROM Baggage WHERE baggageID IS :baggageID")
    Baggage getBaggage(int baggageID);

    @Query("SELECT * FROM baggage WHERE FKitemID IS :itemID")
    Baggage getBaggageByItem(int itemID);
    // DB

    @Query("SELECT * FROM Baggage WHERE FKHandLuggageID IS :handluggageID")
    List<Baggage> selectBaggageOfThisHandLuggage(int handluggageID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Baggage baggage);

    @Insert
    void insertAll(List<Baggage> baggage);

    @Delete
    void delete(Baggage baggage);

    @Update
    void update(Baggage baggage);

    @Delete
    void deleteAll(List<Baggage> baggages);

    @Query("SELECT * FROM categories " +
            "WHERE categoryID IN (SELECT FKcategoryID FROM categoryContent " +
            "WHERE FKitemID IN (SELECT FKitemID FROM baggage WHERE FKHandLuggageID IS :handLuggageID)) ORDER BY categoryName")
    List<Category> selectCategoriesOfThisHandLuggage(int handLuggageID);
//
//    @Query("SELECT * FROM categories " +
//            "WHERE categoryID NOT IN (SELECT FKcategoryID FROM categoryContent " +
//            "WHERE FKitemID IN (SELECT FKitemID FROM baggage WHERE FKHandLuggageID IS :handLuggageID)) ORDER BY categoryName")
//    List<Category> selectCategoriesNOTOfThisHandLuggage(int handLuggageID);


//    @Query("SELECT * FROM items WHERE itemID IN " +
//            "(SELECT categoryContent.FKitemID FROM categoryContent INNER JOIN baggage ON categoryContent.FKitemID IS baggage.FKitemID WHERE (categoryContent.FKcategoryID IS :categoryID AND NOT baggage.FKHandLuggageID IS  :handLuggageID))")
//    List<Item> selectItemFromThisCategoryButNotInThisBaggage(int categoryID, int handLuggageID);

    @Query("SELECT * FROM items WHERE itemID IN (SELECT FKitemID FROM categoryContent WHERE FKcategoryID IS :categoryID)")
    List<Item> selectItemFromThisCategory(int categoryID);

    @Query("SELECT * FROM items WHERE itemID IN (SELECT FKitemID FROM baggage WHERE baggage.FKHandLuggageID IS :handLuggageID)")
    List<Item> selectItemFromThisHandLuggage(int handLuggageID);




}

