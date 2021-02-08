package com.miki.justincase_v1.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "baggage",
        foreignKeys = {
                @ForeignKey(entity = Item.class,
                        parentColumns = "itemID",
                        childColumns = "FKitemID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = HandLuggage.class,
                        parentColumns = "handLuggageID",
                        childColumns = "FKbaggageID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
        indices = {@Index(value = "FKbaggageID"), @Index(value = "FKitemID")}
)
public class Baggage implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int baggageID;

    @ColumnInfo(name = "itemCount")
    public int baggageCount;

    @ColumnInfo(name = "itemName")
    public String baggageName;


    public int FKitemID; // trip ID foreign key
    public int FKbaggageID; // baggage ID foreign key

    public Baggage(int FKitemID, int FKbaggageID, String baggageName) {
        this.FKitemID = FKitemID;
        this.FKbaggageID = FKbaggageID;
        this.baggageCount = 0;
        this.baggageName = baggageName;
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
