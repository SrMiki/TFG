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

    @ColumnInfo(name = "travels")
    public int travels;

    @ColumnInfo(name = "members")
    public String members;

    @ColumnInfo(name = "memberSize")
    public int memberSize;

    public Trip(@NonNull String destination, @NonNull String travelDate, String returnDate, String travelTransport, String returnTransport, String members, int memberSize) {
        this.destination = destination;
        this.travelDate = travelDate;
        this.returnDate = returnDate;
        this.travelTransport = travelTransport;
        this.returnTransport = returnTransport;
        this.members = members;
        this.memberSize = memberSize;
        travelling = 0;
        travels = 0;
    }

    //aux Constructor
    public Trip() {}

    public void setTrip(String destination, String travelDate, String returnDate, String travelTransport, String returnTransport, String members, int memberSize) {
        this.destination = destination;
        this.travelDate = travelDate;
        this.returnDate = returnDate;
        this.travelTransport = travelTransport;
        this.returnTransport = returnTransport;
        this.members = members;
        this.memberSize = memberSize;
        if (!returnDate.isEmpty()) {
            travels = 1;
        }
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

    public int getTravels() {
        return travels;
    }

    public void setTravels(int travels) {
        this.travels = travels;
    }

    public void setTravelling(int travelling) {
        this.travelling = travelling;
    }

    public String getMembers() {
        return this.members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public int getMemberSize() {
        return this.memberSize;
    }

    public void setMemberSize(int memberSize) {
        this.memberSize = memberSize;
    }

    public int getTripID() {
        return this.tripID;
    }
}
