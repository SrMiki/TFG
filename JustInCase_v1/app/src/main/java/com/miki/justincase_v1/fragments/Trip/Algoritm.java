package com.miki.justincase_v1.fragments.Trip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

public class Algoritm extends BaseFragment {

    TextView colorTV, WeigthTV, HeighTV, WidthTV, Depth, nameTV;
    public TextView tripName_TextView, tripDate_TextView;
    public LinearLayout layout;

    Trip trip;
    HandLuggage handLuggage;
    Suitcase suitcase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_algoritm, container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {


            handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");

            suitcase = Presented.getSuitcase(handLuggage, getContext());

            setSuitcase(view);

            trip = (Trip) bundle.getSerializable("trip");

            setTrip(view);

        }


        return view;
    }

    private void setTrip(View view) {
        tripName_TextView = view.findViewById(R.id.fragment_focusTrip_tripDestino);
        tripDate_TextView = view.findViewById(R.id.fragment_focusTrip_tripDate);

        String destination = trip.getDestination();
        String travelDate = trip.getTravelDate();
        String returnDate = trip.getReturnDate();

        if (!returnDate.isEmpty()) {
            travelDate += " - " + returnDate;
        }
        tripName_TextView.setText(destination);
        tripDate_TextView.setText(travelDate);
    }

    private void setSuitcase(View view) {
        colorTV = view.findViewById(R.id.card_view_suitcase_suitcaseColor);
        WeigthTV = view.findViewById(R.id.card_view_suitcase_suitcaseWeight);
        HeighTV = view.findViewById(R.id.card_view_suitcase_heigth);
        WidthTV = view.findViewById(R.id.card_view_suitcase_width);
        Depth = view.findViewById(R.id.card_view_suitcase_depth);
        nameTV = view.findViewById(R.id.card_view_suitcase_suitcaseName);

        nameTV.setText(suitcase.getName());

        colorTV.setText(suitcase.getColor());

        WeigthTV.setText(String.valueOf(suitcase.getWeight()));

        HeighTV.setText(String.valueOf(suitcase.getHeigth()));
        WidthTV.setText(String.valueOf(suitcase.getWidth()));
        Depth.setText(String.valueOf(suitcase.getDepth()));
    }
}
