package com.miki.justincase_v1.fragments.Trip;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_HandLuggage;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class  Fragment_ShowHandLuggage extends BaseFragment {

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
        View view = inflater.inflate(R.layout.fragment_show_handluggage, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.fragment_focusTrip_LayoutReturnDate);

        Bundle bundle = getArguments();

        if (bundle != null) {

            fActionButton = view.findViewById(R.id.actionButton);
            fActionButton.setOnClickListener(v -> {
               getNav().navigate(R.id.fragment_startTrip, bundle);
            });

            setHasOptionsMenu(true);

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

            recyclerView = view.findViewById(R.id.handLuggageRecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_HandLuggage(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                HandLuggage handLuggage = dataset.get(recyclerView.getChildAdapterPosition(v));
                binding.sendHandLuggage(handLuggage);
            });
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
//        //remove searchOption
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(false);

//        MenuItem menuItem = menu.findItem(R.id.action_addItem);
//        menuItem.setVisible(true);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteTrip();
                return true;
            case R.id.action_edit:
                editTrip();
                return true;
            case R.id.action_addItem:
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisTrip", trip);
                getNav().navigate(R.id.fragment_Add_Baggage, obundle);
            default:
//                return super.onOptionsItemSelected(item);
                return false;
        }

    }

    private void editTrip() {
        Bundle obundle = new Bundle();
        obundle.putSerializable("ThisTrip", trip);
        getNav().navigate(R.id.fragment_Edit_Trip, obundle);
    }

    private void deleteTrip() {
        Presented.deleteTrip(trip, getView());
        getNav().navigate(R.id.fragment_ShowTrips);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            binding = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}