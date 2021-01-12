package com.miki.justincase_v1.db.entity;



import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Clase intermedia entre "maletas" y "Items"
//Toda "maleta" tiene un "contenido"
//son "registros" de par "maleta-item"
@Entity(tableName = "content",
        foreignKeys = {
                @ForeignKey(entity = Item.class,
                        parentColumns = "itemID",
                        childColumns = "i_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Suitcase.class,
                        parentColumns = "suitcaseID",
                        childColumns = "s_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
                indices = {@Index(value = "s_id")
        })
public class SuitcaseContent implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int suitcaseContentID;

    public int i_id; //ID del viaje asociado
    public int s_id; //ID de la suitcase asociada

    public SuitcaseContent(int i_id, int s_id) {
        this.i_id = i_id;
        this.s_id = s_id;
    }


    public int getI_id() {
        return i_id;
    }

    public int getS_id() {
        return s_id;
    }
}
