package com.miki.justincase_v1.fragments.Show;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_suitcases;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Create.Fragment_CreateSuitcase;

import java.util.ArrayList;

public class Fragment_ShowSuitcases extends BaseFragment {

    Adapter_suitcases adapter_suitcases;
    RecyclerView suitcaseRecyclerView;

    ArrayList<Suitcase> listOfSuitcases;

    Activity activity;
    Binding_Entity_focusEntity bindingSuitcasefocusSuitcase;

    FloatingActionButton suitcasefloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_suitcases, container, false);

        suitcasefloatingActionButton = view.findViewById(R.id.fragment_showSuitcase_btn_add);

        suitcasefloatingActionButton.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_CreateSuitcase);
        });

        listOfSuitcases = Presented.getAllSuitcase(view);

        suitcaseRecyclerView = view.findViewById(R.id.fragment_showSuitcase_recyclerview);
        loadRecyclerView();
        return view;
    }

    private void loadRecyclerView() {
        suitcaseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_suitcases = new Adapter_suitcases(getContext(), listOfSuitcases);
        suitcaseRecyclerView.setAdapter(adapter_suitcases);

        adapter_suitcases.setListener(view -> {
            bindingSuitcasefocusSuitcase.sendSuitcase(listOfSuitcases.get(suitcaseRecyclerView.getChildAdapterPosition(view)));
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingSuitcasefocusSuitcase = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
