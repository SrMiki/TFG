package com.miki.justincase_v1.fragments.Focus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_baggage;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_FocusTrip extends BaseFragment {

    Activity activity;
    TextView tripDestinationTV, tripTravelDateTV, returnDateTV;
    Button btn_focusTrip_delete, btn_focusTrip_addNewBaggage, btn_focusTrip_edit;

    Adapter_baggage adapter_baggage;
    ArrayList<Baggage> maletasDeEsteViaje;
    Binding_Entity_focusEntity binding_baggage_focusBaggage;
    RecyclerView baggageRecyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_trip, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.fragment_focusTrip_LayoutReturnDate);


        tripDestinationTV = view.findViewById(R.id.fragment_focusTrip_tripDestino);
        tripTravelDateTV = view.findViewById(R.id.fragment_focusTrip_tripDate);
        returnDateTV = view.findViewById(R.id.fragment_focusTrip_returnDate);

        Bundle bundle = getArguments();
        Trip trip;

        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("trip");

            tripDestinationTV.setText(trip.getDestination());
            tripTravelDateTV.setText(trip.getTravelDate());

            if (trip.getReturnDate().isEmpty()) {
                linearLayout.setVisibility(view.GONE);
            } else {
                returnDateTV.setText(trip.getReturnDate());
            }

            maletasDeEsteViaje = Presented.getTheBaggageOfThisTrip(trip, view);

            baggageRecyclerview = view.findViewById(R.id.fragment_focusTrip_recyclerView);
            loadRecyclerView();

            btn_focusTrip_delete = view.findViewById(R.id.fragment_focusTrip_btn_delete);
            btn_focusTrip_delete.setOnClickListener(v -> {
                Presented.deleteTrip(trip, view);
                getNav().navigate(R.id.fragment_ShowTrips);
            });

            btn_focusTrip_addNewBaggage = view.findViewById(R.id.fragment_focusTrip_btn_add);
            btn_focusTrip_addNewBaggage.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisTrip", trip);
                getNav().navigate(R.id.fragment_Add_Baggage, obundle);
            });

            btn_focusTrip_edit = view.findViewById(R.id.fragment_focusTrip_btn_edit);
            btn_focusTrip_edit.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisTrip", trip);
                getNav().navigate(R.id.fragment_Edit_Trip, obundle);
            });
        }
        return view;
    }

    private void loadRecyclerView() {
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