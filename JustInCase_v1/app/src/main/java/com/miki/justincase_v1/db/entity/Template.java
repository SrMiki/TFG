package com.miki.justincase_v1.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Baggage Template
@Entity(tableName = "template")
public class Template implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int templateID;

    @ColumnInfo(name = "templateName")
    public String templateName;

    public Template (String templateName){
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getTemplateID() {
        return templateID;
    }
}
