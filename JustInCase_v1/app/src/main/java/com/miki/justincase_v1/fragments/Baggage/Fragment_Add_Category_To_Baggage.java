package com.miki.justincase_v1.fragments.Baggage;

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
import com.miki.justincase_v1.adapters.Adapter_Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_Category_To_Baggage extends BaseFragment {

    Adapter_Category adapter;
    RecyclerView recyclerView;
    ArrayList<Category> dataset;

    Button btn_items;

    ArrayList<Baggage> baggageArrayList;
    FloatingActionButton floatingActionButton;

    HandLuggage thisBagagge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_category, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {

            baggageArrayList = new ArrayList<>();

            btn_items = view.findViewById(R.id.fragment_btn_items);
            btn_items.setOnClickListener(v -> {
                getNav().navigate(R.id.fragment_Add_item_to_Baggage, bundle);
            });
            thisBagagge = (HandLuggage) bundle.getSerializable("ThisBaggage");

            floatingActionButton = view.findViewById(R.id.fragment_show_entity_btn_add);
            floatingActionButton.setOnClickListener(v -> {
//                Presented.addBaggage(baggageArrayList, view);
//                Bundle obundle = new Bundle();
//                obundle.putSerializable("baggage", thisBagagge);
//                getNav().navigate(R.id.fragment_ShowBaggage, obundle);
            });

            dataset = Presented.getAllCategories(view);

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            loadRecyclerView();
        }
        return view;
    }

    private void loadRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Category(dataset);
        recyclerView.setAdapter(adapter);

//        adapterCategory.setListener(view -> {
//            Category category = categoryArrayList.get(categoryRecyclerView.getChildAdapterPosition(view));
//            ArrayList<CategoryContent> allItemsFromThisCategory = Presented.getAllItemsFromThisCategory(category, view);
//            for (CategoryContent categoryContent : allItemsFromThisCategory) {
//                BaggageContent baggageContent = new BaggageContent(categoryContent.FKitemID, thisBagagge.baggageID, categoryContent.categoryContentName);
//                if (!baggageContentArrayList.contains(baggageContent)) {
//                    baggageContentArrayList.add(baggageContent);
//                }
//            }
//        });
    }
}

