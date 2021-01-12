package com.miki.justincase_v1.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "items")
public class Item implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int itemID;

    @ColumnInfo(name = "itemName")
    public String itemName;

    public Item(String nombreItem) {
        this.itemName = nombreItem;
    }

    public Item(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String nombreItem) {
        this.itemName = nombreItem;
    }

    @Override
    public String toString() {
        return "Maleta " + itemName;

    }

}
