package com.miki.justincase_v1.fragments.Trip;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adaptador_Baggage;
import com.miki.justincase_v1.adapters.Adaptador_CheckList;
import com.miki.justincase_v1.adapters.Adapter_HandLuggage;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_CheckList extends BaseFragment {

    TextView suitcaseName, suitcaseColor, suticaseWeight, suitcaseDimns;
    Button btnCategories;

    RecyclerView recyclerView;
    Adaptador_CheckList adapter;

    Suitcase suitcase;
    HandLuggage handLuggage;

    ArrayList<Baggage> contenidoDeEsteBaggage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {

//            setHasOptionsMenu(true);
            handLuggage = (HandLuggage) bundle.getSerializable("handluggage");

//            btnCategories = view.findViewById(R.id.fragment_btn_categories);
//            btnCategories.setOnClickListener(v -> {
//                getNav().navigate(R.id.fragment_ShowBaggageByCategory, bundle);
//            });

            suitcase = Presented.getSuitcase(handLuggage, view);
            //focusBaggage
            setSuitcase(view);

            contenidoDeEsteBaggage = Presented.getBaggage(handLuggage, view);

            recyclerView = view.findViewById(R.id.fragment_checlist_recyclerview);
            //Esto sería los items que tiene la maleta!
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adaptador_CheckList(getContext(), contenidoDeEsteBaggage);
            recyclerView.setAdapter(adapter);

//        adaptador_suitcaseContent.setListener(view -> {
        }
        return view;
    }

    private void setSuitcase(View view) {
        suitcaseName = view.findViewById(R.id.card_view_suitcase_suitcaseName);
        suitcaseColor = view.findViewById(R.id.card_view_suitcase_suitcaseColor);
        suticaseWeight = view.findViewById(R.id.card_view_suitcase_suitcaseWeight);
        suitcaseDimns = view.findViewById(R.id.card_view_suitcase_suitcaseDimns);

        suitcaseName.setText(suitcase.getSuitcaseName());
        suitcaseColor.setText(suitcase.getSuitcaseColor());
        suticaseWeight.setText(suitcase.getSuitcaseWeight());
        suitcaseDimns.setText(suitcase.getSuitcaseDims());
    }

}
