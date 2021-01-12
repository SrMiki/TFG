package com.miki.justincase_v1.db.entity;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Clase intermedia entre "viajes" y "maletas"
//Todo "viaje" tiene un "equipaje"
//Un "equipaje" está formado por "maletas"
//el viaje es independiente de las maletas, no del equipaje
//Todo equipaje está asoaciado a un viaje;
//son "registros" de par "viaje-maleta"
@Entity(tableName = "baggage",
        foreignKeys = {
                @ForeignKey(entity = Viaje.class,
                        parentColumns = "viajeID",
                        childColumns = "v_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Suitcase.class,
                        parentColumns = "suitcaseID",
                        childColumns = "s_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )},
                indices = {@Index(value = "v_id")
        })
public class Baggage implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int baggageID;

    public int v_id; //ID del viaje asociado
    public int s_id; //ID de la suitcase asociada

    public Baggage(int v_id, int s_id) {
        this.v_id = v_id;
        this.s_id = s_id;
    }

}
