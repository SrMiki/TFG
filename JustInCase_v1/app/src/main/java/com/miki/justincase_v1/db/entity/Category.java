package com.miki.justincase_v1.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "categories")
public class Category implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int categoryID;

    @ColumnInfo(name = "categoryName")
    public String categoryName;

    private boolean selectedState;

    public Category(String categoryName) {
        this.categoryName = categoryName;
        selectedState=false;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isSelectedState() {
        return selectedState;
    }

    public void setSelectedState(boolean selectedState) {
        this.selectedState = selectedState;
    }

    @Override
    public boolean equals(Object o) {
        Category other = (Category) o;
        return this.getCategoryName().equals(other.getCategoryName());
    }

}
