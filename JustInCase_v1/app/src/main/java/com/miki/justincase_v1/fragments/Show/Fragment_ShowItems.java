package com.miki.justincase_v1.fragments.Show;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_item;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Create.Fragment_CreateItem;

import java.util.ArrayList;

public class Fragment_ShowItems extends BaseFragment {

    Adapter_item adapter_item;
    ArrayList<Item> itemArrayList;
    RecyclerView itemRecyclerView;

    Activity activity;
    Binding_Entity_focusEntity bindingItemfocusItem;

    Button btnShowCategories;

    FloatingActionButton itemFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_items, container, false);

        itemFloatingActionButton = view.findViewById(R.id.fragment_showItem_btn_add);

        itemFloatingActionButton.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_CreateItem);
        });

        btnShowCategories = view.findViewById(R.id.fragment_itemManager_btn_categories);
        btnShowCategories.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_ShowCategories);
        });

        itemArrayList = Presented.getAllItems(view);

        itemRecyclerView = view.findViewById(R.id.fragment_showItems_recyclerview);
        loadRecyclerView();
        return view;
    }

    private void loadRecyclerView() {
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_item = new Adapter_item(getContext(), itemArrayList);
        itemRecyclerView.setAdapter(adapter_item);

        adapter_item.setListener(view ->{
            bindingItemfocusItem.sendItem(itemArrayList.get(itemRecyclerView.getChildAdapterPosition(view)));
        });
    }

    //Comunicacion entre el fragment Viaje y Detalle del viaje
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingItemfocusItem = (Binding_Entity_focusEntity) this.activity;

        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
