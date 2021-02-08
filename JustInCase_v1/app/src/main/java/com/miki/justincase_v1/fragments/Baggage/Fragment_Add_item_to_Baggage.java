package com.miki.justincase_v1.fragments.Baggage;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_item_to_Baggage extends BaseFragment {

    Adapter_Item adapter;
    RecyclerView recyclerView;
    ArrayList<Item> dataset;

    Activity activity;
    Binding_Entity_focusEntity binding;

    ArrayList<Baggage> arrayList;
    FloatingActionButton floatingActionButton;

    Button btn_category;

    HandLuggage thisBagagge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_item, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {

            thisBagagge = (HandLuggage) bundle.getSerializable("ThisBaggage");
            arrayList = new ArrayList<>();

            btn_category = view.findViewById(R.id.fragment_btn_categories);
            btn_category.setOnClickListener(v -> {
                getNav().navigate(R.id.fragment_Add_Category_To_Baggage, bundle);
            });

            floatingActionButton = view.findViewById(R.id.fragment_show_entity_btn_add);
            floatingActionButton.setOnClickListener(v -> {
                Presented.addBaggage(arrayList, view);
                Bundle obundle = new Bundle();
                obundle.putSerializable("baggage", thisBagagge);
                getNav().navigate(R.id.fragment_ShowBaggage, obundle);

            });

            dataset = Presented.getAllItemsThatItNotInThisBaggage(thisBagagge, view);

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Item(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {

                Item item = dataset.get(recyclerView.getChildAdapterPosition(v));
                Baggage baggage = new Baggage(item.itemID, thisBagagge.handLuggageID, item.itemName);
                if (!arrayList.contains(baggage)) {
                    arrayList.add(baggage);
                }
            });
        }
        return view;
    }
}

