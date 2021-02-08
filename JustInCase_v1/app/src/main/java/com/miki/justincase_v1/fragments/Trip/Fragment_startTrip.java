package com.miki.justincase_v1.fragments.Trip;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_HandLuggage;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_startTrip extends BaseFragment {

    Activity activity;
    TextView tripDestinationTV, tripTravelDateTV, returnDateTV;

    Adapter_HandLuggage adapter;
    ArrayList<HandLuggage> dataset;
    RecyclerView recyclerView;

    FloatingActionButton fActionButton;

    Binding_Entity_focusEntity binding;
    Trip trip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_trip, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.fragment_focusTrip_LayoutReturnDate);

        Bundle bundle = getArguments();

        if (bundle != null) {

//            fActionButton = view.findViewById(R.id.fragment_show_entity_btn_add);
//            fActionButton.setOnClickListener(v -> {
//                Bundle obundle = new Bundle();
//                obundle.putSerializable("ThisTrip", trip);
//                getNav().navigate(R.id.fragment_Add_Baggage, obundle);
//            });

//            setHasOptionsMenu(true);

            tripDestinationTV = view.findViewById(R.id.fragment_focusTrip_tripDestino);
            tripTravelDateTV = view.findViewById(R.id.fragment_focusTrip_tripDate);
            returnDateTV = view.findViewById(R.id.fragment_focusTrip_returnDate);

            trip = (Trip) bundle.getSerializable("trip");

            tripDestinationTV.setText(trip.getDestination());
            tripTravelDateTV.setText(trip.getTravelDate());

            if (trip.getReturnDate().isEmpty()) {
                linearLayout.setVisibility(view.GONE);
            } else {
                returnDateTV.setText(trip.getReturnDate());
            }

            dataset = Presented.getHandLuggage(trip, view);

            recyclerView = view.findViewById(R.id.startTrip_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_HandLuggage(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                HandLuggage handLuggage = dataset.get(recyclerView.getChildAdapterPosition(v));
                Bundle obundle = new Bundle();
                obundle.putSerializable("handluggage", handLuggage);
//                getNav().navigate(R.id.fragment_CheckList, obundle);
            });
        }
        return view;
    }
}
