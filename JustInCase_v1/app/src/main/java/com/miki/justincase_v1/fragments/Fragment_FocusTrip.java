package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miki.justincase_v1.bindings.Binding_Baggage_focusBaggage;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_baggage;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;

public class Fragment_FocusTrip extends Fragment {

    AppDatabase db;

    TextView tripDestination, tripTravelDate;
    Button btn_focusTrip_delete, btn_focusTrip_addNewBaggage;

    Activity activity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment_Add_Baggage fragmentAddBaggage;

    Adapter_baggage adapter_baggage;
    ArrayList<Baggage> maletasDeEsteViaje;
    Binding_Baggage_focusBaggage binding_baggage_focusBaggage;
    RecyclerView baggageRecyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_trip, container, false);

        tripDestination = view.findViewById(R.id.fragment_focusTrip_tripDestino);
        tripTravelDate = view.findViewById(R.id.fragment_focusTrip_tripDate);

        //bundle for information
        Bundle bundle = getArguments();
        Trip trip;

        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("trip");

            tripDestination.setText(trip.getDestination());
            tripTravelDate.setText(trip.getTravelDate());

            db = AppDatabase.getInstance(view.getContext());

            maletasDeEsteViaje = (ArrayList<Baggage>) db.baggageDAO().getTheBaggageOfThisTrip(trip.tripID);

            baggageRecyclerview = view.findViewById(R.id.fragment_focusTrip_recyclerView);
            mostrarDatos();

            //trip delete btn
            btn_focusTrip_delete = view.findViewById(R.id.fragment_focusTrip_btn_delete);
            btn_focusTrip_delete.setOnClickListener(view1 -> {
                db = AppDatabase.getInstance(view.getContext());
                db.tripDao().delete(trip);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ShowTrips());
                fragmentTransaction.commit();
            });

            //trip add baggage btn
            btn_focusTrip_addNewBaggage = view.findViewById(R.id.fragment_focusTrip_btn_add);
            btn_focusTrip_addNewBaggage.setOnClickListener(v -> {

                Bundle obundle = new Bundle();

                obundle.putSerializable("ThisTrip", trip);
                fragmentAddBaggage = new Fragment_Add_Baggage();
                fragmentAddBaggage.setArguments(obundle);

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, fragmentAddBaggage);
                fragmentTransaction.commit();
            });
        }
        return view;
    }


    private void mostrarDatos() {
        baggageRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_baggage = new Adapter_baggage(getContext(), maletasDeEsteViaje);
        baggageRecyclerview.setAdapter(adapter_baggage);

        adapter_baggage.setListener(view -> {
            Baggage baggage = maletasDeEsteViaje.get(baggageRecyclerview.getChildAdapterPosition(view));
            binding_baggage_focusBaggage.sendBaggage(baggage);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            binding_baggage_focusBaggage = (Binding_Baggage_focusBaggage) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}