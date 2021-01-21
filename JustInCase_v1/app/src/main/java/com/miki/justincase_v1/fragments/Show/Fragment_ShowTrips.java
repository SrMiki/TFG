package com.miki.justincase_v1.fragments.Show;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.adapters.Adapter_trip;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Create.Fragment_CreateTrip;

import java.util.ArrayList;

public class Fragment_ShowTrips extends BaseFragment {

    AppDatabase db;

    Adapter_trip adaptertrip;
    ArrayList<Trip> listOfTrips;
    Binding_Entity_focusEntity bindingTripfocusTrip;
    RecyclerView recyclerView;

    Activity activity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_trips, container, false);

        floatingActionButton = view.findViewById(R.id.fragment_showTrips_btn_add);
        /**
         * (new View.OnClickListener() {
         *             @Override
         *             public void onClick(View view) {
         */
        floatingActionButton.setOnClickListener(v -> {
           doFragmentTransaction(new Fragment_CreateTrip());
        });

        db = AppDatabase.getInstance(getContext());
        listOfTrips = (ArrayList<Trip>) db.tripDao().getAllTrips();

        recyclerView = view.findViewById(R.id.fragment_showTrips_recyclerview);
        mostrarDatos();
        return view;
    }

    private void mostrarDatos() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptertrip = new Adapter_trip(getContext(), listOfTrips);
        recyclerView.setAdapter(adaptertrip);

        adaptertrip.setListener(view -> {
            bindingTripfocusTrip.sendTrip(listOfTrips.get(recyclerView.getChildAdapterPosition(view)));
        });
    }

    //Comunicacion entre el fragment Viaje y Detalle del viaje
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingTripfocusTrip = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
