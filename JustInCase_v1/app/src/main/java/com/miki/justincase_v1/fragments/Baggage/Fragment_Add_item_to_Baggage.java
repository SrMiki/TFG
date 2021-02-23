package com.miki.justincase_v1.fragments.Baggage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_ItemSeleccionados;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_item_to_Baggage extends BaseFragment {

    Adapter_ItemSeleccionados adapter;
    RecyclerView recyclerView;
    ArrayList<Item> dataset;

    TextView suitcaseNameTV;

    FloatingActionButton floatingActionButton;
    ArrayList<Item> arrayList;

    HandLuggage handLuggage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_baggage, container, false);
        suitcaseNameTV = view.findViewById(R.id.suitcaseNameTV);

        Bundle bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();

            handLuggage = (HandLuggage) bundle.getSerializable("handluggage");
            suitcaseNameTV.setText(handLuggage.getHandLuggageName());

            dataset = Presented.selectItemNOTFromThisBaggage(handLuggage, getContext());

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_ItemSeleccionados(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                Item item = dataset.get(position);

                if (!arrayList.contains(item)) {
                    arrayList.add(item);
                    adapter.setSelectedState(true);
                } else {
                    arrayList.remove(item);
                    adapter.setSelectedState(false);
                }
                adapter.notifyItemChanged(position);
            });

            floatingActionButton = view.findViewById(R.id.fragment_show_entity_btn_add);
            floatingActionButton.setOnClickListener(v -> {
                Presented.createBaggageByItems(arrayList, handLuggage, getContext());
                Bundle obundle = new Bundle();
                obundle.putSerializable("handluggage", handLuggage);
                getNav().navigate(R.id.fragment_ShowBaggageByItem, obundle);

            });
        }
        return view;
    }
}

