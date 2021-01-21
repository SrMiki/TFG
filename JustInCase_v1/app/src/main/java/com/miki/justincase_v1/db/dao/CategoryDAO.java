package com.miki.justincase_v1.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM categories ORDER BY categoryName")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE categoryID IS :thisCategoryID")
    Category getCategory(int thisCategoryID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewCategory(Category newCategory);

    @Delete
    void delete(Category categoryToDelete);

    @Update
    void update(Category category);
}