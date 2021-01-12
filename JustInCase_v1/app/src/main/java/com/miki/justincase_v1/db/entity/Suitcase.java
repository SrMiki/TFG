package com.miki.justincase_v1.db.entity;

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

    @ColumnInfo(name = "suitcaseName")
    public String suitcaseName;

    public Suitcase(String suitcaseName) {
        this.suitcaseName = suitcaseName;
    }

    public int getSuitcaseID(){ return suitcaseID; }

    public String getSuitcaseName() {
        return suitcaseName;
    }

    public void setSuitcaseName(String suitcaseName) {
        this.suitcaseName = suitcaseName;
    }

    @Override
    public String toString() {
        return "Maleta " + suitcaseName;

    }

}
