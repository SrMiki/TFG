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

    private boolean selectedState;

    public Item(String itemName) {
        this.itemName = itemName;
        selectedState = false;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemID() {
        return itemID;
    }



    @Override
    public boolean equals(Object o) {
        Item item = (Item) o;
        return getItemName().equals(item.getItemName());
    }

    public void setItem(String itemName) {
        this.itemName = itemName;
    }

    public boolean isSelectedState() {
        return selectedState;
    }

    public void setSelectedState(boolean selectedState) {
        this.selectedState = selectedState;
    }

}

