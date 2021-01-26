package com.miki.justincase_v1.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


//Clase "maleta" (suitcase)
//esta entidad es independiente y agrupa un conjuto de items también independientes
//es decir la relacion es "1 maleta puede pertenecer a 1 o varios viajes" y "un item puede pertenecer a
//1 o varias maletas"
//la foreing key por tanto tiene que ir en "viajes"
@Entity(tableName = "suitcases")
public class Suitcase implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int suitcaseID;

    @NonNull
    @ColumnInfo(name = "suitcaseName")
    public String suitcaseName;

    @ColumnInfo(name = "suitcaseColor")
    public String suitcaseColor;

    @ColumnInfo(name = "suitcaseWeight")
    public String suitcaseWeight;

    @ColumnInfo(name = "suitcaseDims")
    public String suitcaseDims; // heigth x width x depth

    public Suitcase(@NonNull String suitcaseName, String suitcaseColor, String suitcaseWeight, String suitcaseDims) {
        this.suitcaseName = suitcaseName;
        this.suitcaseColor = suitcaseColor;
        this.suitcaseWeight = suitcaseWeight;
        this.suitcaseDims = suitcaseDims;
    }

    public int getSuitcaseID() {
        return suitcaseID;
    }

    public String getSuitcaseName() {
        return suitcaseName;
    }

    public void setSuitcaseName(String suitcaseName) {
        this.suitcaseName = suitcaseName;
    }

    public String getSuitcaseColor() {
        return suitcaseColor;
    }

    public String getSuitcaseWeight() {
        return suitcaseWeight;
    }

    public String getSuitcaseDims() {
        return suitcaseDims;
    }

    public void setSuticase(String suitcaseName, String suitcaseColor, String suitcaseWeight, String suitcaseDims) {
        this.suitcaseName = suitcaseName;
        this.suitcaseColor = suitcaseColor;
        this.suitcaseWeight = suitcaseWeight;
        this.suitcaseDims = suitcaseDims;
    }


}
