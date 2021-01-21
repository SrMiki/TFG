package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_suitcases;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;

public class Fragment_Add_Baggage extends Fragment {

    AppDatabase db;
    Adapter_suitcases adapter_suitcases;
    RecyclerView suitcaseRecyclerView;
    ArrayList<Suitcase> listaDeMaletas;

    Binding_Entity_focusEntity binding_trip_focusTrip;

    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_baggage, container, false);
        //obtenemos los datos de la tabla
        db = AppDatabase.getInstance(getContext());
        listaDeMaletas = (ArrayList<Suitcase>) db.suitcaseDAO().getAll();
        //vinculo con el recycler view
        suitcaseRecyclerView = view.findViewById(R.id.fragment_addBaggage_recyclerview);
        mostrarDatos();
        return view;
    }

    private void mostrarDatos() {
        suitcaseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_suitcases = new Adapter_suitcases(getContext(), listaDeMaletas);
        suitcaseRecyclerView.setAdapter(adapter_suitcases);

        //Cuando pulse en un dato
        adapter_suitcases.setListener(view -> {
            Bundle bundle = getArguments();
            Trip trip;
            if (bundle != null) {
                //pillamos la maleta que nos han pasado a traves del bundle
                trip = (Trip) bundle.getSerializable("ThisTrip");

                int suitcaseID = listaDeMaletas.get(suitcaseRecyclerView.getChildAdapterPosition(view)).getSuitcaseID();

                Baggage newBaggage = new Baggage(trip.tripID, suitcaseID);

                db.baggageDAO().addANewBaggageForThisTrip(newBaggage);

               binding_trip_focusTrip.sendTrip(trip);

            }
        });
    }

    //Comunicacion entre el fragment Viaje y Detalle del viaje
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            binding_trip_focusTrip = (Binding_Entity_focusEntity) this.activity;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}