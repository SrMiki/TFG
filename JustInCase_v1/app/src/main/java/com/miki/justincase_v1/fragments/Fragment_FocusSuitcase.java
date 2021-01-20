package com.miki.justincase_v1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Suitcase;

public class Fragment_FocusSuitcase extends Fragment {

    TextView suitcaseName, suitcaseColor, suticaseWeight, suitcaseDimns;
    Button btn_focusSuitcase_delete;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_suitcase, container, false);

        suitcaseName = view.findViewById(R.id.fragment_focusSuitcase_suitcaseName);
        suitcaseColor = view.findViewById(R.id.fragment_focusSuitcase_suitcaseColor);
        suticaseWeight = view.findViewById(R.id.fragment_focusSuitcase_suitcaseWeight);
        suitcaseDimns = view.findViewById(R.id.fragment_focusSuitcase_suitcaseDimns);

        Bundle bundle = getArguments();
        Suitcase suitcase;
        //validacion
        if (bundle != null) {
            //pillamos la maleta que nos han pasado a traves del bundle
            suitcase = (Suitcase) bundle.getSerializable("suitcase");

            suitcaseName.setText(suitcase.getSuitcaseName());
            suitcaseColor.setText(suitcase.getSuitcaseColor());
            suticaseWeight.setText(suitcase.getSuitcaseWeight());
            suitcaseDimns.setText(suitcase.getSuitcaseDims());

            db = AppDatabase.getInstance(view.getContext());

            //Boton para eliminar un viaje
            btn_focusSuitcase_delete = view.findViewById(R.id.fragment_focusSuitcase_btn_delete);
            btn_focusSuitcase_delete.setOnClickListener(v -> {
                db.suitcaseDAO().delete(suitcase);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ShowSuitcases());
                fragmentTransaction.commit();
            });

        }
        return view;
    }
}
