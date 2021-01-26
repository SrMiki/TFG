package com.miki.justincase_v1.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "categoryContent",
        foreignKeys = {
                @ForeignKey(entity = Item.class,
                        parentColumns = "itemID",
                        childColumns = "FKitemID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Category.class,
                        parentColumns = "categoryID",
                        childColumns = "FKcategoryID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
        indices = {@Index(value = "FKcategoryID"), @Index(value = "FKitemID")}
)
public class CategoryContent {
    @PrimaryKey(autoGenerate = true)
    public int categoryContentID;

    @ColumnInfo(name = "itemName")
    public String categoryContentName;

    @ColumnInfo(name = "itemCount")
    public int categoryCount;

    public int FKitemID; // item ID foreign key
    public int FKcategoryID; // category ID foreign key

    public CategoryContent(int FKitemID, int FKcategoryID, String categoryContentName) {
        this.FKitemID = FKitemID;
        this.FKcategoryID = FKcategoryID;
        this.categoryContentName = categoryContentName;
        this.categoryCount = 0;
    }

    public int getFKitemID() {
        return FKitemID;
    }

    public int getFKcategoryID() {
        return FKcategoryID;
    }

    public String getCategoryCount() {
        return String.valueOf(categoryCount);
    }

    public void increaseThisItem() {
        this.categoryCount++;
    }

    public void decreaseThisItem() {
        if (categoryCount > 0) {
            categoryCount--;
        }
    }
}
