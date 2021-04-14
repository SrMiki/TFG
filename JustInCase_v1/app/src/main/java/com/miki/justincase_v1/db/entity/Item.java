package com.miki.justincase_v1.db.entity;

import android.widget.ImageView;

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

    public int count;

    private String itemPhotoURI;

    public Item(String itemName, String itemPhotoURI) {
        this.itemName = itemName;
        selectedState = false;
        count=1;
        this.itemPhotoURI=itemPhotoURI;
    }



    public String getItemName() {
        return itemName;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemPhotoURI(String itemPhotoURI) {
        this.itemPhotoURI = itemPhotoURI;
    }

    public String getItemPhotoURI() {
        return itemPhotoURI;
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

    public int getItemCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

