package com.miki.justincase_v1.fragments.Trip;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_CreateTrip extends BaseFragment {

    EditText travelDestinationTV, travelDateTV, returnDateTV;

    Switch dateSwitch;

    Button btn;
    Trip newTrip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);
        LinearLayout inputReturnDate = view.findViewById(R.id.fragment_createTrip_LayoutInput_tripReturnDate);


        travelDestinationTV = view.findViewById(R.id.fragment_createTrip_input_tripDestino);
        travelDateTV = view.findViewById(R.id.activity_createTrip_input_travelDate);
        returnDateTV = view.findViewById(R.id.fragment_createTrip_input_tripReturnDate);

        //SWITCH BUTTON
        dateSwitch = view.findViewById(R.id.fragment_createTrip_switch);
        dateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inputReturnDate.setVisibility(view.GONE);
            } else {
                inputReturnDate.setVisibility(view.VISIBLE);
                travelDateTV.setText("");
            }
            returnDateTV.setText("");
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            String destination = (String) bundle.getSerializable("destination");
            travelDestinationTV.setText(destination);
        }
        travelDestinationTV.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_CountryList);
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

            newTrip = new Trip(destination, travelDate, returnDate, travelTransport, returnTransport);
            if (!Presented.createTrip(newTrip, getContext())) {
                printWarningToast(getContext(), getString(R.string.warning_createTrip));
            } else {
//                closeKeyBoard();
                ask_newHandluggageDialog();
            }
        });
        return view;
    }


    private void ask_newHandluggageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(getString(R.string.text_ask_newHandluggage));

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.text_no), ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.fragment_ShowTrips);
        }));
        builder.setPositiveButton(getString(R.string.text_yes), ((dialog, which) -> {
            Bundle bundle = new Bundle();
            // We can't use the new Trip cause we need the trip ID
            // The new Trip ID is the last register on the DataBase (size()-1 of all trips)
            ArrayList<Trip> allTrips = Presented.getAllTrips(getContext());
            Trip trip = allTrips.get(allTrips.size() - 1);
            bundle.putSerializable("trip", trip);
            getNav().navigate(R.id.fragment_Add_HandLuggage, bundle);
        }));
        builder.show();
    }
}
