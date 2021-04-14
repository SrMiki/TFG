package com.miki.justincase_v1.fragments.Baggage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_AddCategoryToBaggage;
import com.miki.justincase_v1.adapters.Adapter_Category;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_Category_To_Baggage extends BaseFragment {

    Adapter_AddCategoryToBaggage adapter;
    RecyclerView recyclerView;
    ArrayList<Category> dataset;

    ArrayList<Category> arrayList;
    FloatingActionButton floatingActionButton;

    TextView suitcaseNameTV;

    Category focusCategory;

    HandLuggage handLuggage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_baggage, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {

            arrayList = new ArrayList<>();
            handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");
            dataset = Presented.selectCategoriesNOTFromThisBaggage(handLuggage, getContext());

            suitcaseNameTV = view.findViewById(R.id.suitcaseNameTV);
            suitcaseNameTV.setText(handLuggage.getHandLuggageName());

            if (dataset.isEmpty()) {
//                noItemsDialog();
            } else {


                recyclerView = view.findViewById(R.id.fragment_Add_Item_To_Baggage_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new Adapter_AddCategoryToBaggage(dataset, getActivity(), handLuggage);
                recyclerView.setAdapter(adapter);

//            adapter.setOnClickListener(v -> {
//                int position = recyclerView.getChildAdapterPosition(v);
//                focusCategory = dataset.get(position);
//                adapter.setSelectedState(!adapter.getSelectedState());
//                adapter.notifyItemChanged(position);
//
////                if (arrayList.contains(focusCategory)) {
////                    arrayList.remove(focusCategory);
////                    adapter.setSelectedState(false);
////                } else {
////                    arrayList.add(focusCategory);
////                    adapter.setSelectedState(true);
////                    adapter.notifyItemChanged(position);
////                }
//            });

                floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Baggage_finish);
                floatingActionButton.setOnClickListener(v -> {
                    ArrayList<Item> itemArrayList = adapter.itemArrayList;
                    Presented.createBaggageByItems(itemArrayList, handLuggage, getContext());
                    getNav().navigate(R.id.action_fragment_Add_Category_To_Baggage_to_fragment_ShowBaggageByCategory, bundle);
                });

            }

        }
        return view;
    }

}

