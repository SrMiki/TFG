package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.DetallesViajeBinding;
import com.miki.justincase_v1.adapter.Adaptador_viajes;
import com.miki.justincase_v1.CrearViaje;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Viaje;
import com.miki.justincase_v1.R;

import java.util.ArrayList;

/*
Fragmento que se corresponde al apartado "Mis viajes" al pulsar
en el drawer menu
 */
public class FragmentViajes extends Fragment {

    AppDatabase db;
    Adaptador_viajes adaptador_viajes;
    RecyclerView recyclerView;
    ArrayList<Viaje> listOfViajes;

    //referencia para la comunicacion de fragment
    Activity activity;
    DetallesViajeBinding detallesViajeBinding;

    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viajes, container, false);

        floatingActionButton = view.findViewById(R.id.floatingbtn_newViaje);
        /**
         * (new View.OnClickListener() {
         *             @Override
         *             public void onClick(View view) {
         */
        floatingActionButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CrearViaje.class);
            getActivity().startActivity(intent);
        });

        //obtenemos los datos de la tabla
        db = AppDatabase.getInstance(getContext());
        listOfViajes = (ArrayList<Viaje>) db.viajesDao().getAll();
        //vinculo con el recycler view

        recyclerView = view.findViewById(R.id.recyclerID);
        //mostramos los datos
        mostrarDatos();


        return view;
    }

    private void llenar() {
        listOfViajes.add(new Viaje("gc", "5"));
    }

    private void mostrarDatos() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador_viajes = new Adaptador_viajes(getContext(), listOfViajes);
        recyclerView.setAdapter(adaptador_viajes);

        /**
         * new View.OnClickListener() {
         *             @Override
         *             public void onClick(View view) {
         */
        adaptador_viajes.setListener(view -> {
            //String nombre = listOfViajes.get(recyclerView.getChildAdapterPosition(view)).getDestino();
            //Toast.makeText(getContext(), "has seleccionado: " + nombre, Toast.LENGTH_SHORT).show();
            detallesViajeBinding.sendViaje(listOfViajes.get(recyclerView.getChildAdapterPosition(view)));


        });

    }

    //Comunicacion entre el fragment Viaje y Detalle del viaje
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            detallesViajeBinding = (DetallesViajeBinding) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
