package com.miki.justincase_v1.fragments.Focus;

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

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_baggage;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Fragment_Add_Baggage;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowTrips;

import java.util.ArrayList;

public class Fragment_FocusTrip extends BaseFragment {

    AppDatabase db;

    Activity activity;
    TextView tripDestination, tripTravelDate;
    Button btn_focusTrip_delete, btn_focusTrip_addNewBaggage;


    Adapter_baggage adapter_baggage;
    ArrayList<Baggage> maletasDeEsteViaje;
    Binding_Entity_focusEntity binding_baggage_focusBaggage;
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
            btn_focusTrip_delete.setOnClickListener(v -> {
                db = AppDatabase.getInstance(view.getContext());
                db.tripDao().delete(trip);

                doFragmentTransaction(new Fragment_ShowTrips());
            });

            //trip add baggage btn
            btn_focusTrip_addNewBaggage = view.findViewById(R.id.fragment_focusTrip_btn_add);
            btn_focusTrip_addNewBaggage.setOnClickListener(v -> {

                Bundle obundle = new Bundle();

                obundle.putSerializable("ThisTrip", trip);
               doFragmentTransactionWithBundle(new Fragment_Add_Baggage(), obundle);
            });
        }
        return view;
    }


    private void mostrarDatos() {
        baggageRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_baggage = new Adapter_baggage(getContext(), maletasDeEsteViaje);
        baggageRecyclerview.setAdapter(adapter_baggage);

        adapter_baggage.setListener(v -> {
            Baggage baggage = maletasDeEsteViaje.get(baggageRecyclerview.getChildAdapterPosition(v));
            binding_baggage_focusBaggage.sendBaggage(baggage);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            binding_baggage_focusBaggage = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}