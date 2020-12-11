package com.miki.justincase_v1.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miki.justincase_v1.CrearViaje;
import com.miki.justincase_v1.MainActivity;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Viaje;

public class FragmentDetallesViajes extends Fragment {

    TextView destinoDetalle, fechaDetalle;
    Button btn_detallesViaje_eliminar;
    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalles_viaje, container, false);

        destinoDetalle = view.findViewById(R.id.destinoDetalle);
        fechaDetalle = view.findViewById(R.id.fechaDetalle);

        //bundle para recibir la informacion

        Bundle bundle = getArguments();
        Viaje viaje;

        //validacion
        if (bundle != null) {
            viaje = (Viaje) bundle.getSerializable("objeto");

            destinoDetalle.setText(viaje.getDestino());
            fechaDetalle.setText(viaje.getFecha());


            //Boton para eliminar un viaje
            btn_detallesViaje_eliminar = view.findViewById(R.id.btn_detallesViaje_eliminar);
            btn_detallesViaje_eliminar.setOnClickListener(view1 -> {
                db = AppDatabase.getInstance(view.getContext());
                db.viajesDao().delete(viaje);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
            });

        }


        return view;
    }


}