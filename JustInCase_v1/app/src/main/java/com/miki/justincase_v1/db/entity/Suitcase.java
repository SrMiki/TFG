package com.miki.justincase_v1.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "suitcases")
public class Suitcase implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int suitcaseID;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color")
    public String color;


    @ColumnInfo(name = "weight")
    public double weight;

    @ColumnInfo(name = "heigth")
    public double heigth;

    @ColumnInfo(name = "width")
    public double width;

    @ColumnInfo(name = "depth")
    public double depth;

    public Suitcase(@NonNull String name, String color, double weight, double heigth, double width, double depth) {
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.heigth = heigth;
        this.width = width;
        this.depth = depth;
    }

    public int getSuitcaseID() {
        return suitcaseID;
    }

    public String getName() {
        return name;
    }


    public String getColor() {
        return color;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeigth() {
        return heigth;
    }
    public double getWidth() {
        return width;
    }
    public double getDepth() {
        return depth;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSuticase(String name, String color,  double weight, double heigth, double width, double depth) {
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.heigth = heigth;
        this.width = width;
        this.depth = depth;
    }

    public void setSuitcaseID(int suitcaseID) {
        this.suitcaseID = suitcaseID;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDimns(double heigth, double width, double depth) {
        this.heigth = heigth;
        this.width = width;
        this.depth = depth;
    }
}
