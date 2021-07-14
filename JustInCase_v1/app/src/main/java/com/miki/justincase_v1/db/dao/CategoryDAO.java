package com.miki.justincase_v1.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM categories")
    List<Category> getAll();


    @Query("SELECT * FROM categories ORDER BY categoryName")
    List<Category> getAllOrdened();

    @Query("SELECT * FROM categories WHERE categoryID IS :thisCategoryID")
    Category getCategory(int thisCategoryID);

    @Query("SELECT * FROM categories WHERE categoryID IN" +
            "(SELECT FKcategoryID FROM categoryContent WHERE FKitemID IN" +
            "(SELECT FKitemID FROM baggage WHERE NOT FKHandLuggageID IS :handLuggageID))")
    List<Category> selectCategoriesNOTFromThisBaggage(int handLuggageID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category newCategory);

    @Insert
    void insertAll(ArrayList<Category> newCategory);

    @Delete
    void delete(Category categoryToDelete);

    @Delete
    void deleteSelected(List<Category> categories);

    @Update
    void update(Category category);

    @Update
    void updateListOfCategory(ArrayList<Category> arrayList);

    @Query("SELECT * FROM categories WHERE categoryName IS :newCategory")
    Category getCategoryByName(String newCategory);


}
