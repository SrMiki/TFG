package com.miki.justincase_v1.fragments.Add;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_item;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_item_to_Baggage extends BaseFragment {

    Adapter_item adapter_item;
    RecyclerView itemRecyclerView;
    ArrayList<Item> listaDeItems;

    Activity activity;
    Binding_Entity_focusEntity bindingBaggageFocusBaggage;

    ArrayList<BaggageContent> baggageContentArrayList;
    FloatingActionButton floatingActionButton;

    Button btn_category;

    Baggage thisBagagge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_baggagecontent, container, false);

        Bundle bundle = getArguments();

        baggageContentArrayList = new ArrayList<>();

        if (bundle != null) {
            btn_category = view.findViewById(R.id.fragment_itemManager_btn_categories);
            btn_category.setOnClickListener(v -> {
                getNav().navigate(R.id.fragment_Add_Category_To_Baggage, bundle);
            });
            thisBagagge = (Baggage) bundle.getSerializable("ThisBaggage");

            floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Baggage_btn_finish);
            floatingActionButton.setOnClickListener(v ->{
                Presented.addThosesItemsForThisBaggage(baggageContentArrayList, view);
                bindingBaggageFocusBaggage.sendBaggage(thisBagagge);

            });

            listaDeItems = Presented.getAllItemsThatItNotInThisBaggage(thisBagagge, view);

            itemRecyclerView = view.findViewById(R.id.fragment_addBaggageContent_recyclerview);
            loadRecyclerView();
        }
        return view;
    }

    private void loadRecyclerView() {
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_item = new Adapter_item(getContext(), listaDeItems);
        itemRecyclerView.setAdapter(adapter_item);

        adapter_item.setListener(view -> {

            Item item = listaDeItems.get(itemRecyclerView.getChildAdapterPosition(view));
            BaggageContent baggageContent = new BaggageContent(item.itemID, thisBagagge.baggageID, item.itemName);
            if(!baggageContentArrayList.contains(baggageContent)){
                baggageContentArrayList.add(baggageContent);
            }
        });
    }


    //Comunicacion entre el fragment Maleta y Detalles de la maleta
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingBaggageFocusBaggage = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

