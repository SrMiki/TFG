package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;

import java.util.List;

@Dao
public interface CategoryContentDAO {

    @Query("SELECT * FROM categoryContent ORDER BY itemName")
    List<CategoryContent> getAll();

    @Query("SELECT * FROM categoryContent WHERE FKcategoryID IS :categoryID ORDER BY itemName")
    List<CategoryContent> getAllItemsFromThisCategory(int categoryID);

    @Query("SELECT * FROM categoryContent WHERE categoryContentID IS :categoryContentID")
    CategoryContent getCategoryContent(int categoryContentID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewItemForThisCategory(CategoryContent categoryContent);

    @Update
    void updateCategoryContent(CategoryContent categoryContent);

    @Delete
    void deleteCategoryContent(CategoryContent categoryContent);
}

