package com.miki.justincase_v1.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Item;

import java.util.List;

//Data access object de "items"

@Dao
public interface ItemDAO {
    //LiveData
    @Query("SELECT * FROM items")
    List<Item> getAll();

    @Query("SELECT * FROM items WHERE itemID IN (:itemIds)")
    List<Item> loadAllByIds(int[] itemIds);

    @Query("SELECT * FROM items WHERE itemName LIKE :name ")
    Item findByName(String name);

    @Query("SELECT * FROM items WHERE itemID IS :itemID")
    Item getItem(int itemID);

    @Insert
    void insertAll(Item[] item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItem(Item item);

    @Delete
    void delete(Item item);
}

