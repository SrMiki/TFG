package com.miki.justincase_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Viaje;

public class CrearViaje extends AppCompatActivity {

    AppDatabase db;
    EditText CAMPOdestino, CAMPOfecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_viaje);

        CAMPOdestino = findViewById(R.id.inputText_destino);
        CAMPOfecha = findViewById(R.id.inputText_fecha);
    }

    public void onClick(View view) {

        String destino = CAMPOdestino.getText().toString();
        String fecha = CAMPOfecha.getText().toString();

        if(destino == null | destino.isEmpty() | fecha == null || fecha.isEmpty()){
            Toast.makeText(this, "error al crear el nuevo viaje", Toast.LENGTH_SHORT).show();
            finish();
        }

        Viaje viaje = new Viaje(destino, fecha);
        db = AppDatabase.getInstance(this);
        db.viajesDao().addViaje(viaje);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
