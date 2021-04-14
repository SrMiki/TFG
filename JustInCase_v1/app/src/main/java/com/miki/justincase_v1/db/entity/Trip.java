package com.miki.justincase_v1.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "trips")
public class Trip implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int tripID;

    @NonNull
    @ColumnInfo(name = "destination")
    public String destination;

    @NonNull
    @ColumnInfo(name = "travelDate")
    public String travelDate;

    @ColumnInfo(name = "returnDate")
    public String returnDate;

    @ColumnInfo(name = "travelTransport")
    public String travelTransport;

    @ColumnInfo(name = "returnTransport")
    public String returnTransport;


    @ColumnInfo(name = "travelling")
    public int travelling;

    public Trip(@NonNull String destination, @NonNull String travelDate, String returnDate, String travelTransport, String returnTransport) {
        this.destination = destination;
        this.travelDate = travelDate;
        this.returnDate = returnDate;
        this.travelTransport = travelTransport;
        this.returnTransport = returnTransport;
        travelling = 0;
    }

    public void setTrip(String destination, String travelDate, String returnDate, String travelTransport, String returnTransport) {
        this.destination = destination;
        this.travelDate = travelDate;
        this.returnDate = returnDate;
        this.travelTransport = travelTransport;
        this.returnTransport = returnTransport;
    }



    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public int isTravelling() {
        return travelling;
    }

    public void setTravelling(int travelling) {
        this.travelling = travelling;
    }


}
