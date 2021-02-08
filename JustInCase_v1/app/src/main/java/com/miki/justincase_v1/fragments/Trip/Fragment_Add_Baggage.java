package com.miki.justincase_v1.fragments.Trip;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Suitcase;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;

public class Fragment_Add_Baggage extends Fragment {

    Adapter_Suitcase adapter;
    RecyclerView recyclerView;
    ArrayList<Suitcase> arrayList;

    Binding_Entity_focusEntity binding_trip_focusTrip;

    Activity activity;
    Trip trip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_baggage, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("ThisTrip");

            arrayList = Presented.getAllSuitcaseThatItNotInThisTrip(trip, view);
            recyclerView = view.findViewById(R.id.fragment_addBaggage_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Suitcase(arrayList);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                Suitcase suitcase = arrayList.get(recyclerView.getChildAdapterPosition(v));
                Presented.addANewBaggageForThisTrip(suitcase, trip, v);
                binding_trip_focusTrip.sendTrip(trip);

            });
        }
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            binding_trip_focusTrip = (Binding_Entity_focusEntity) this.activity;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}