package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.miki.justincase_v1.Binding_Item_focusItem;
import com.miki.justincase_v1.MainActivity;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapter.Adapter_item;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.SuitcaseContent;

import java.util.ArrayList;

public class Fragment_Add_item_to_suitcase extends Fragment {

    AppDatabase db;
    Adapter_item adapter_item;
    RecyclerView itemRecyclerView;
    ArrayList<Item> listaDeItems;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Activity activity;
    Binding_Item_focusItem bindingItemfocusItem;

    Fragment parentFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_suitcasecontent, container, false);
        parentFragment = getParentFragment();
        //obtenemos los datos de la tabla
        db = AppDatabase.getInstance(getContext());
        listaDeItems = (ArrayList<Item>) db.itemDAO().getAll();
        //vinculo con el recycler view
        itemRecyclerView = view.findViewById(R.id.fragment_addSuitcaseContent_recyclerview);
        mostrarDatos();
        return view;
    }

    private void mostrarDatos() {
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_item = new Adapter_item(getContext(), listaDeItems);
        itemRecyclerView.setAdapter(adapter_item);



        //Cuando pulse en un dato
        adapter_item.setListener(view -> {
            Bundle bundle = getArguments();
            Suitcase suitcase;
            //validacion
            if (bundle != null) {
                //pillamos la maleta que nos han pasado a traves del bundle
                suitcase = (Suitcase) bundle.getSerializable("ThisSuitcase");

                int itemID = listaDeItems.get(itemRecyclerView.getChildAdapterPosition(view)).itemID;

                SuitcaseContent suitcaseContent = new SuitcaseContent(itemID, suitcase.suitcaseID);

                db.suitcaseContentDAO().addANewItemForThisSuitcase(suitcaseContent);

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout,  new Fragment_ShowSuitcases());
                fragmentTransaction.commit();

            }
        });
    }

    //Comunicacion entre el fragment Viaje y Detalle del viaje
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingItemfocusItem = (Binding_Item_focusItem) this.activity;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

