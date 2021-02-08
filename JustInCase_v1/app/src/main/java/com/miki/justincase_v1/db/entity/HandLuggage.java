package com.miki.justincase_v1.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Baggage of a especific trip
@Entity(tableName = "handLuggage",
        foreignKeys = {
                @ForeignKey(entity = Trip.class,
                        parentColumns = "tripID",
                        childColumns = "FKtripID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Suitcase.class,
                        parentColumns = "suitcaseID",
                        childColumns = "FKsuitcaseID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
        indices = {@Index(value = "FKtripID"), @Index(value = "FKsuitcaseID")}
)
public class HandLuggage implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int handLuggageID;

    @ColumnInfo(name = "handLuggageName")
    public String handLuggageName;

    public int FKtripID; //trip ID foreign key
    public int FKsuitcaseID; //suitcase ID foreign key

    public HandLuggage(int FKtripID, int FKsuitcaseID) {
        this.FKtripID = FKtripID;
        this.FKsuitcaseID = FKsuitcaseID;
    }

    public int getHandLuggageID() {
        return handLuggageID;
    }

    public int getFKtripID() {
        return FKtripID;
    }

    public int getFKsuitcaseID() {
        return FKsuitcaseID;
    }

    public String getHandLuggageName() {
        return handLuggageName;
    }
}
