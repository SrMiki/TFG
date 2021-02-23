package com.miki.justincase_v1.fragments.Travel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_CategoryCheckList;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_DoCheckListByCategory extends BaseFragment {


    RecyclerView recyclerView;
    Adapter_CategoryCheckList adapter;

    HandLuggage handLuggage;

    ArrayList<Category> dataset;
    FloatingActionButton actionButton;
    TextView suitcaseNameTV;
    private SharedPreferences sp;
    Switch switchShowCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);
        suitcaseNameTV = view.findViewById(R.id.suitcaseNameTV);
        switchShowCategories = view.findViewById(R.id.switch_ShowCategory);

        Bundle bundle = getArguments();
        if (bundle != null) {
            sp = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            boolean showCategories = sp.getBoolean("showCategories", false);

            switchShowCategories.setChecked(showCategories);
            editor.putBoolean("showCategories", switchShowCategories.isChecked());

            switchShowCategories.setOnCheckedChangeListener((buttonView, isChecked) -> {
                editor.putBoolean("showCategories", false);
                editor.apply();
                getNav().navigate(R.id.fragment_DoCheckListByItem, bundle);
            });

//            setHasOptionsMenu(true);
            handLuggage = (HandLuggage) bundle.getSerializable("handluggage");
            suitcaseNameTV.setText(handLuggage.getHandLuggageName());


            recyclerView = view.findViewById(R.id.fragment_checklist_recyclerview);

            ArrayList<Baggage> itemsFromThisBaggage = Presented.getBaggageOfThisHandLuggage(handLuggage, getContext());
            dataset = Presented.selectAllCategoriesOfThisHandLuggage(handLuggage, getContext());

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_CategoryCheckList(dataset, handLuggage);
            recyclerView.setAdapter(adapter);

            actionButton = view.findViewById(R.id.fragment_checklist_btn_finish);
            actionButton.setOnClickListener(v -> {
//                Presented.checkBaggage(handLuggage, dataset, getContext());
                Bundle obundle = new Bundle();
                Trip trip = Presented.getTrip(handLuggage, getContext());
                obundle.putSerializable("trip", trip);
                getNav().navigate(R.id.fragment_CheckIn, obundle);
            });
        }
        return view;
    }
}
