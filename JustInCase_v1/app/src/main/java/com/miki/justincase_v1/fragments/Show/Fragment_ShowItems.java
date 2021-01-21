package com.miki.justincase_v1.fragments.Show;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_item;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Create.Fragment_CreateItem;

import java.util.ArrayList;

public class Fragment_ShowItems extends BaseFragment {

    AppDatabase db;

    Adapter_item adapter_item;
    ArrayList<Item> itemArrayList;
    RecyclerView itemRecyclerView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Activity activity;
    Binding_Entity_focusEntity bindingItemfocusItem;

    FloatingActionButton itemFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_items, container, false);

        itemFloatingActionButton = view.findViewById(R.id.fragment_showItem_btn_add);

        itemFloatingActionButton.setOnClickListener(v -> {

            doFragmentTransaction(new Fragment_CreateItem());
        });

        //obtenemos los datos de la tabla
        db = AppDatabase.getInstance(getContext());
        itemArrayList = (ArrayList<Item>) db.itemDAO().getAll();

        //vinculo con el recycler view
        itemRecyclerView = view.findViewById(R.id.fragment_showItems_recyclerview);

        //mostramos los datos
        mostrarDatos();

        return view;
    }

    private void mostrarDatos() {
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
