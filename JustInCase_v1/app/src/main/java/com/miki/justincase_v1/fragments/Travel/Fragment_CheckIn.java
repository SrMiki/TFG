package com.miki.justincase_v1.fragments.Travel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_CheckList_Suitcase;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_CheckIn extends BaseFragment {

    TextView tripDestinationTV, tripTravelDateTV, returnDateTV;

    Adapter_CheckList_Suitcase adapter;
    ArrayList<HandLuggage> dataset;
    RecyclerView recyclerView;

    Button button;

    Trip trip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_in_out_trip, container, false);
//        LinearLayout linearLayout = view.findViewById(R.id.fragment_focusTrip_LayoutReturnDate);

        button = view.findViewById(R.id.startTrip_button);

        Bundle bundle = getArguments();
        if (bundle != null) {

            trip = (Trip) bundle.getSerializable("trip");

            dataset = Presenter.getHandLuggage(trip, getContext());

//            setTrip(view);

//            if (trip.getReturnDate().isEmpty()) {
//                linearLayout.setVisibility(View.GONE);
//            } else {
//                returnDateTV.setText(trip.getReturnDate());
//            }

            recyclerView = view.findViewById(R.id.startTrip_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_CheckList_Suitcase(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                HandLuggage handLuggage = dataset.get(recyclerView.getChildAdapterPosition(v));
                Bundle obundle = new Bundle();
                obundle.putSerializable("handluggage", handLuggage);
                SharedPreferences sp;
                sp = v.getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
                boolean showCategoires = sp.getBoolean("showCategories", false);
                if (showCategoires) {
                    getNav().navigate(R.id.fragment_DoCheckListByCategory, obundle);
                } else {
                    getNav().navigate(R.id.fragment_DoCheckListByItem, obundle);
                }
            });


            ArrayList<HandLuggage> handLuggageArrayList = Presenter.getHandLuggage(trip, getContext());


            if (allSelected(handLuggageArrayList)) {
                button.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.GONE);
            }

            setCheckTripStatus();
            button.setOnClickListener(v -> {
                Presenter.clearCheckHandLuggage(trip, getContext());
                Presenter.updateTrip(trip, getContext());
                getNav().navigate(R.id.mainFragment);
            });


        }
        return view;
    }

    /**
     * update chek-in / check-out status
     * 0 == planning
     * 1 == start
     * 2 == check-out (1st) (for travel trip if there is a return Trip)
     * 3 == check-in (2nd) (for return Trip)
     * 4 == finished
     * <p>
     * 2 & 3 for go and return travel
     */
    private void setCheckTripStatus() {
        if (trip.isTravelling() == 0) {
            trip.setTravelling(1); //start
        } else if(trip.isTravelling()==2) {
            trip.setTravelling(3);
        }
    }

    private void setTrip(View view) {
        tripDestinationTV = view.findViewById(R.id.fragment_focusTrip_tripDestino);
        tripTravelDateTV = view.findViewById(R.id.fragment_focusTrip_tripDate);
        returnDateTV = view.findViewById(R.id.fragment_focusTrip_returnDate);


        tripDestinationTV.setText(trip.getDestination());
        tripTravelDateTV.setText(trip.getTravelDate());
    }

    private boolean allSelected(ArrayList<HandLuggage> handLuggageArrayList) {
        for (HandLuggage handLuggage : handLuggageArrayList) {
            if (!handLuggage.isHandLuggageCompleted()) {
                return false;
            }
        }
        return true;
    }
}
