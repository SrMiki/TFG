package com.miki.justincase_v1.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.SuitcaseContent;

import java.util.List;

//Data access object de "SuitcaseContent"

@Dao
public interface SuitcaseContentDAO {
    //LiveData
    @Query("SELECT * FROM content")
    List<SuitcaseContent> getAll();

    @Query("SELECT * FROM content WHERE s_id IS :suitcaseID ")
    List<SuitcaseContent> getTheSuitContentOfThisSuitcase(int suitcaseID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewItemForThisSuitcase(SuitcaseContent suitcaseContent);

    @Delete
    void deleteSuitcaseContent(SuitcaseContent suitcaseContent);
}

