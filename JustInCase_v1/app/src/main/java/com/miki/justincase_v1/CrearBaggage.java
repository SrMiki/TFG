package com.miki.justincase_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Viaje;

import java.util.List;

public class CrearBaggage extends AppCompatActivity {

    AppDatabase db;
    EditText CAMPONombreMaleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baggage);

        CAMPONombreMaleta = findViewById(R.id.idmaleta);
    }

    public void onClick(View view) {

        String nombreMaleta = CAMPONombreMaleta.getText().toString();

        if (nombreMaleta == null || nombreMaleta.isEmpty()) {
            Toast.makeText(this, "error al crear el nuevo viaje", Toast.LENGTH_SHORT).show();
            finish();
        }

        Suitcase byName = db.suitcaseDAO().findByName(nombreMaleta);
        List<Viaje> lastViaje = db.viajesDao().getAll();

        Baggage baggage = new Baggage(lastViaje.size()-1,byName.getSuitcaseID());
        db = AppDatabase.getInstance(this);

        db.baggageDAO().addBaggage(baggage);


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}