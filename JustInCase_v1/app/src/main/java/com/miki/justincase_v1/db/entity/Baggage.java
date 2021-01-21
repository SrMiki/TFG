package com.miki.justincase_v1.db.entity;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Baggage of a especific trip
@Entity(tableName = "baggage",
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
public class Baggage implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int baggageID;

    public int FKtripID; //trip ID foreign key
    public int FKsuitcaseID; //suitcase ID foreign key

    public Baggage(int FKtripID, int FKsuitcaseID) {
        this.FKtripID = FKtripID;
        this.FKsuitcaseID = FKsuitcaseID;
    }

    public int getBaggageID() {
        return baggageID;
    }

    public int getFKtripID() {
        return FKtripID;
    }

    public int getFKsuitcaseID() {
        return FKsuitcaseID;
    }
}
