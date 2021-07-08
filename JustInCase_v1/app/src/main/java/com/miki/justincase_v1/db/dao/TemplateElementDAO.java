package com.miki.justincase_v1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.TemplateElement;

import java.util.List;

@Dao
public interface TemplateElementDAO {


    @Query("SELECT * FROM templateElement WHERE FKTemplateID IS :templateID")
    List<TemplateElement> getAllItemsFromThisTemplate(int templateID);

    @Query("SELECT * FROM items WHERE itemID IN " +
            "(SELECT templateElement.FKitemID from templateElement" +
            " WHERE FKTemplateID IS :templateID) ORDER BY itemName")
    List<Item> selectItemFromThisTemplate(int templateID);

    @Query("SELECT * FROM items WHERE NOT itemID IN " +
            "(SELECT templateElement.FKitemID from templateElement" +
            " WHERE FKTemplateID IS :templateID) ORDER BY itemName")
    List<Item> selectItemNOTinThisTemplate(int templateID);

    @Query("SELECT * FROM templateElement WHERE FKitemID IS :itemID")
    TemplateElement getTemplateElementByItemID(int itemID);


    @Insert
    void insert(TemplateElement templateElement);

    @Delete
    void deleteAll(List<TemplateElement> allItemsFromThisTemplate);

    @Delete
    void delete(TemplateElement templateElement);
}
