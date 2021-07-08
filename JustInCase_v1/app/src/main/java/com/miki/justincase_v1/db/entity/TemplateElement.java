package com.miki.justincase_v1.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "templateElement",
        foreignKeys = {
                @ForeignKey(entity = Item.class,
                        parentColumns = "itemID",
                        childColumns = "FKitemID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Template.class,
                        parentColumns = "templateID",
                        childColumns = "FKTemplateID",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
        indices = {@Index(value = "FKTemplateID"), @Index(value = "FKitemID")}
)
public class TemplateElement implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int templateElementID;

    @ColumnInfo(name = "templateElementCount")
    public int templateElementCount;

    @ColumnInfo(name = "templateElementName")
    public String templateElementName;

    public int FKitemID;
    public int FKTemplateID;

    public TemplateElement(int FKitemID, int FKTemplateID, String templateElementName) {
        this.FKitemID = FKitemID;
        this.FKTemplateID = FKTemplateID;
        this.templateElementCount = 1;
        this.templateElementName = templateElementName;
    }

    public int getFKitemID() {
        return FKitemID;
    }

}
