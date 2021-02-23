package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CategoryContentDAO {

    @Query("SELECT * FROM categoryContent ORDER BY itemName")
    List<CategoryContent> getAll();

    @Query("SELECT * FROM categoryContent WHERE FKitemID IS :FKitemID")
    CategoryContent getCategoryContentByItemID(int FKitemID);

    @Query("SELECT * FROM categoryContent WHERE FKcategoryID IS :categoryID ORDER BY itemName")
    List<CategoryContent> getAllItemsFromThisCategory(int categoryID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryContent categoryContent);

    @Update
    void update(CategoryContent categoryContent);

    @Delete
    void delete(CategoryContent categoryContent);

    @Delete
    void deleteAll(List<CategoryContent> allItemsFromThisCategory);
}

