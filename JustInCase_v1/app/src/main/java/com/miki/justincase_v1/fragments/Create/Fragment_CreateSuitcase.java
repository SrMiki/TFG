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

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowSuitcases;

public class Fragment_CreateSuitcase extends BaseFragment {

    EditText suitcaseNameTV, suitcaseColorTV, suitcaseWeightTV, suitcaseDimnsTV;

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

            String suticaseName = suitcaseNameTV.getText().toString();
            String color = suitcaseColorTV.getText().toString();
            String weight = suitcaseWeightTV.getText().toString();
            String dimns = suitcaseDimnsTV.getText().toString();

            Presented.createSuitcase(suticaseName, color, weight, dimns, view);

            closeKeyBoard();
            getNav().navigate(R.id.fragment_ShowSuitcases);
        });
        return view;
    }
}