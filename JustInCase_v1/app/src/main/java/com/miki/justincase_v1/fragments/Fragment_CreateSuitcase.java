package com.miki.justincase_v1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Suitcase;

public class Fragment_CreateSuitcase extends Fragment {

    AppDatabase db;
    EditText suitcaseNameTV, suitcaseColorTV, suitcaseWeightTV, suitcaseDimnsTV;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Button bnt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_suitcase, container, false);

        suitcaseNameTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        suitcaseColorTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);
        suitcaseWeightTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);
        suitcaseDimnsTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseDims);

        bnt = view.findViewById(R.id.fragment_createSuitcase_btn_add);

        bnt.setOnClickListener(view1 -> {

            String nombreMaleta = suitcaseNameTV.getText().toString();
            String color = suitcaseColorTV.getText().toString();
            String weight = suitcaseWeightTV.getText().toString();
            String dimns = suitcaseDimnsTV.getText().toString();

            /*if (nombreMaleta == null | nombreMaleta.isEmpty()) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                return;
            }*/

            //añadimos la nueva maleta
            Suitcase newSuitcase = new Suitcase(nombreMaleta, color, weight, dimns);
            db = AppDatabase.getInstance(getActivity());
            db.suitcaseDAO().addANewSuitcase(newSuitcase);

            //Cerrar el teclado al pulsar
            InputMethodManager inputManager =
                    (InputMethodManager) getContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ShowSuitcases());
            fragmentTransaction.commit();

        });

        return view;


    }


}
