package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.CategoryContent;

import java.util.List;

//Data access object de "SuitcaseContent"

@Dao
public interface BaggageContentDAO {
    //LiveData
    @Query("SELECT * FROM baggageContent ORDER BY itemName")
    List<BaggageContent> getAll();

    @Query("SELECT * FROM baggageContent WHERE baggageContentID IS :baggageContentID")
   BaggageContent getBaggageContent(int baggageContentID);



    @Query("SELECT * FROM baggageContent WHERE FKbaggageID IS :baggageID ORDER BY itemName ")
    List<BaggageContent> getItemsFromThisBaggage(int baggageID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addANewItemForThisSuitcase(BaggageContent baggageContent);

    @Delete
    void deleteSuitcaseContent(BaggageContent baggageContent);

    @Update
    void updateBaggageContent(BaggageContent baggageContent);
}

