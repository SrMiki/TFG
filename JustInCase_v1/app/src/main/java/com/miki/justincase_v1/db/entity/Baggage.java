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
                        childColumns = "FKHandLuggageID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
        indices = {@Index(value = "FKHandLuggageID"), @Index(value = "FKitemID")}
)
public class Baggage implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int baggageID;

    @ColumnInfo(name = "itemCount")
    public int baggageCount;

    @ColumnInfo(name = "itemName")
    public String baggageName;

    @ColumnInfo(name = "check")
    public boolean check;

    public int FKitemID;
    public int FKHandLuggageID;

    public Baggage(int FKitemID, int FKHandLuggageID, String baggageName) {
        this.FKitemID = FKitemID;
        this.FKHandLuggageID = FKHandLuggageID;
        this.baggageCount = 1;
        this.baggageName = baggageName;
        check = false;
    }

    public int getFKitemID() {
        return FKitemID;
    }

    public int getFKHandLuggageID() {
        return FKHandLuggageID;
    }

    public int getBaggageCount() {
        return baggageCount;
    }

    public void setCount(int baggageCount) {
        this.baggageCount = baggageCount;
    }
    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }


}
