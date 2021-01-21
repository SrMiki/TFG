package com.miki.justincase_v1.fragments.Create;

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
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowSuitcases;

public class Fragment_CreateSuitcase extends BaseFragment {

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

        bnt.setOnClickListener(v -> {

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

            closeKeyBoard();
            doFragmentTransaction(new Fragment_ShowSuitcases());


        });

        return view;


    }


}
