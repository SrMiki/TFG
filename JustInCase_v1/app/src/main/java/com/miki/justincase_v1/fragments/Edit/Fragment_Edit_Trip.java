package com.miki.justincase_v1.fragments.Edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowTrips;

public class Fragment_Edit_Trip extends BaseFragment {

    EditText travelDestinationTV, travelDateTV;

    Button btn;
    Trip trip;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);

        travelDestinationTV = view.findViewById(R.id.activity_createTrip_input_tripDestino);
        travelDateTV = view.findViewById(R.id.activity_createTrip_input_travelDate);

        btn = view.findViewById(R.id.fragment_createTrip_btn_add);

        Bundle bundle = getArguments();

        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("ThisTrip");

            travelDestinationTV.setText(trip.destination);
            travelDateTV.setText(trip.travelDate);

            btn.setOnClickListener(v -> {

                String destination = travelDestinationTV.getText().toString();
                String travelDate = travelDateTV.getText().toString();

                //TODO add editText to this fields
                String returnDate = "";
                String travelTransport = "";
                String returnTransport = "";

                Presented.updateTrip(trip, destination, travelDate, returnDate, travelTransport, returnTransport, view);

                closeKeyBoard();
                Bundle obundle = new Bundle();
                obundle.putSerializable("trip", trip);
                getNav().navigate(R.id.fragment_FocusTrip2, obundle);
            });
        }
        return view;
    }
}