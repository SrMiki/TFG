package com.miki.justincase_v1.fragments.Add;

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

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_suitcases;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;

public class Fragment_Add_Baggage extends Fragment {

    Adapter_suitcases adapter_suitcases;
    RecyclerView suitcaseRecyclerView;
    ArrayList<Suitcase> listaDeMaletas;

    Binding_Entity_focusEntity binding_trip_focusTrip;

    Activity activity;
    Trip trip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_baggage, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("ThisTrip");

            listaDeMaletas = Presented.getAllSuitcaseThatItNotInThisTrip(trip, view);
            suitcaseRecyclerView = view.findViewById(R.id.fragment_addBaggage_recyclerview);
            loadRecyclerView();
        }
        return view;
    }

    private void loadRecyclerView() {
        suitcaseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_suitcases = new Adapter_suitcases(getContext(), listaDeMaletas);
        suitcaseRecyclerView.setAdapter(adapter_suitcases);

        adapter_suitcases.setListener(view -> {

            Suitcase suitcase = listaDeMaletas.get(suitcaseRecyclerView.getChildAdapterPosition(view));

            Presented.addANewBaggageForThisTrip(suitcase, trip, view);

            binding_trip_focusTrip.sendTrip(trip);

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