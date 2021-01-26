package com.miki.justincase_v1.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Clase intermedia entre "<Equipaje>" e "Items"
//registro del par <Equipaje>-Item
@Entity(tableName = "baggageContent",
        foreignKeys = {
                @ForeignKey(entity = Item.class,
                        parentColumns = "itemID",
                        childColumns = "FKitemID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Baggage.class,
                        parentColumns = "baggageID",
                        childColumns = "FKbaggageID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
        indices = {@Index(value = "FKbaggageID"), @Index(value = "FKitemID")}
)
public class BaggageContent implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int baggageContentID;

    @ColumnInfo(name = "itemCount")
    public int baggageCount;

    @ColumnInfo(name = "itemName")
    public String baggageContentName;


    public int FKitemID; // trip ID foreign key
    public int FKbaggageID; // baggage ID foreign key

    public BaggageContent(int FKitemID, int FKbaggageID, String baggageContentName) {
        this.FKitemID = FKitemID;
        this.FKbaggageID = FKbaggageID;
        this.baggageCount = 0;
        this.baggageContentName = baggageContentName;
    }

    public int getFKitemID() {
        return FKitemID;
    }

    public int getFKbaggageID() {
        return FKbaggageID;
    }

    public String getBaggageCount() {
        return String.valueOf(baggageCount);
    }

    public void increaseThisItem() {
        this.baggageCount++;
    }

    public void decreaseThisItem() {
        if (baggageCount > 0) {
            this.baggageCount--;
        }
    }
}
