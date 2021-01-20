package com.miki.justincase_v1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Trip;

public class Fragment_CreateTrip extends Fragment {

    AppDatabase db;
    EditText CAMPOdestino, CAMPOfecha;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Button btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);

        CAMPOdestino = view.findViewById(R.id.activity_createTrip_input_tripDestino);
        CAMPOfecha = view.findViewById(R.id.activity_createTrip_input_travelDate);

        btn = view.findViewById(R.id.fragment_createTrip_btn_add);

        btn.setOnClickListener(view1 -> {


            String destination = CAMPOdestino.getText().toString();
            String travelDate = CAMPOfecha.getText().toString();

            //TODO add editText to this fields

            String returnDate = "";
            String travelTransport ="";
            String returnTransport ="";

//            if (destination == null | destination.isEmpty() | travelDate == null || travelDate.isEmpty()) {
//                Toast.makeText(getActivity(), "error al crear el nuevo viaje", Toast.LENGTH_SHORT).show();
//            }

            //TODO checkDate method + change getAllTrips

            /*if ( checkDate(travelDate)){

            }*/

            Trip trip = new Trip(destination, travelDate, returnDate,travelTransport,returnTransport);
            db = AppDatabase.getInstance(getActivity());
            db.tripDao().addViaje(trip);

            closeKeyBoard();
            fragmentTransaction();


        });
        return view;
    }

    private void fragmentTransaction() {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ShowTrips());
        fragmentTransaction.commit();
    }

    private void closeKeyBoard() {
        InputMethodManager inputManager =
                (InputMethodManager) getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * check format date
     * @param date
     * @return
     */
    private boolean checkDate(String date) {
        return true;
    }
}
