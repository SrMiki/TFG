package com.miki.justincase_v1.fragments.Travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_paraStartTrip;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_CheckOut extends BaseFragment {

    TextView tripDestinationTV, tripTravelDateTV, returnDateTV;

    Adapter_paraStartTrip adapter;
    ArrayList<HandLuggage> dataset;
    RecyclerView recyclerView;

    Button button;

    Trip trip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_in_out_trip, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.fragment_focusTrip_LayoutReturnDate);

        button = view.findViewById(R.id.startTrip_button);

        Bundle bundle = getArguments();
        if (bundle != null) {

            trip = (Trip) bundle.getSerializable("trip");

            dataset = Presented.getHandLuggage(trip, getContext());

//            setTrip(view);

//            if (trip.getReturnDate().isEmpty()) {
//                linearLayout.setVisibility(View.GONE);
//            } else {
//                returnDateTV.setText(trip.getReturnDate());
//            }

            recyclerView = view.findViewById(R.id.startTrip_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_paraStartTrip(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                HandLuggage handLuggage = dataset.get(recyclerView.getChildAdapterPosition(v));
                Bundle obundle = new Bundle();
                obundle.putSerializable("handluggage", handLuggage);
                getNav().navigate(R.id.fragment_DoCheckListByItem, obundle);
            });


            ArrayList<HandLuggage> handLuggageArrayList = Presented.getHandLuggage(trip, getContext());


            if (allSelected(handLuggageArrayList)) {
                button.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.GONE);
            }

            setCheckTripStatus();
            button.setOnClickListener(v -> {
                Presented.clearCheckHandLuggage(trip, getContext());
                Presented.updateTrip(trip, getContext());
                getNav().navigate(R.id.fragment_ShowTrips);
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
        if (trip.isTravelling() == 1) {
            if (!trip.getReturnDate().isEmpty()) {
                trip.setTravelling(2);
            } else {
                trip.setTravelling(4);
            }
            button.setText(getString(R.string.text_doCheckOut));

        } else if (trip.isTravelling() == 3) {
            trip.setTravelling(4);
            button.setText(getString(R.string.text_finish));
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
