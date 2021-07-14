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

    @ColumnInfo(name = "handLuggageCompleted")
    public boolean handLuggageCompleted;

    @ColumnInfo(name = "handLuggageSize")
    public int handLuggageSize;

    @ColumnInfo(name = "owners")
    public String owners;

    public int FKtripID; //trip ID foreign key
    public int FKsuitcaseID; //suitcase ID foreign key

    public HandLuggage(int FKtripID, int FKsuitcaseID, String handLuggageName, String owners) {
        this.FKtripID = FKtripID;
        this.FKsuitcaseID = FKsuitcaseID;
        this.handLuggageName = handLuggageName;
        this.owners = owners;
        handLuggageCompleted = false;
        handLuggageSize = 0;
    }

    public boolean isHandLuggageCompleted() {
        return handLuggageCompleted;
    }

    public void setHandLuggageCompleted(boolean handLuggageCompleted) {
        this.handLuggageCompleted = handLuggageCompleted;
    }

    public void increaseSize() {
        handLuggageSize++;
    }

    public void decreaseSize() {
        if (handLuggageSize > 0) {
            handLuggageSize--;
        }
    }

    public int getHandLuggageSize() {
        return handLuggageSize;
    }

    public void setHandLuggageSize(int handLuggageSize) {
        this.handLuggageSize = handLuggageSize;
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

    public void setHandLuggage(String suitcaseName) {
        this.handLuggageName = suitcaseName;
    }

    public String getOwners() {
        return this.owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

}
