package com.miki.justincase_v1.fragments.Trip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.fragments.BaseFragment;

public class Fragment_CreateTrip extends BaseFragment {

    EditText travelDestinationTV, travelDateTV, returnDateTV;

    Switch dateSwitch;

    Button btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.fragment_createTrip_LayoutInput_tripReturnDate);

        travelDestinationTV = view.findViewById(R.id.fragment_createTrip_input_tripDestino);
        travelDateTV = view.findViewById(R.id.activity_createTrip_input_travelDate);
        returnDateTV = view.findViewById(R.id.fragment_createTrip_input_tripReturnDate);

        //SWITCH BUTTON
        dateSwitch = view.findViewById(R.id.fragment_createTrip_switch);
        dateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayout.setVisibility(view.GONE);
            } else {
                linearLayout.setVisibility(view.VISIBLE);
                travelDateTV.setText("");
            }
            returnDateTV.setText("");
        });


        travelDateTV.setOnClickListener(v -> {
            showDatePickerDialog(travelDateTV);
            returnDateTV.setText("");
        });

        returnDateTV.setOnClickListener(v -> {
            //cannot used until insert TravelDate
            if (!dateSwitch.isChecked())
                if (!travelDateTV.getText().toString().isEmpty()) {
                    showDatePickerDialog(travelDateTV, returnDateTV);
                }
        });


        btn = view.findViewById(R.id.fragment_createTrip_btn_add);
        btn.setOnClickListener(v -> {

            String destination = travelDestinationTV.getText().toString();
            String travelDate = travelDateTV.getText().toString();
            String returnDate = returnDateTV.getText().toString();

            //TODO add editText to this fields

            String travelTransport = "";
            String returnTransport = "";

            Presented.createTrip(destination, travelDate, returnDate, travelTransport, returnTransport, view);

            closeKeyBoard();
            getNav().navigate(R.id.fragment_ShowTrips);
        });
        return view;
    }
}
