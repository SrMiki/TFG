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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Binding_Suitcase_focusSuitcase;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapter.Adapter_suitcases;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Suitcase;

import java.util.ArrayList;

/*
Fragmento que se corresponde al apartado "Mis maletas" al pulsar
en el drawer menu
 */
public class Fragment_ShowSuitcases extends Fragment {

    AppDatabase db;
    Adapter_suitcases adapter_suitcases;
    RecyclerView suitcaseRecyclerView;
    ArrayList<Suitcase> listOfSuitcases;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //referencia para la comunicacion de fragment
    Activity activity;
    Binding_Suitcase_focusSuitcase bindingSuitcasefocusSuitcase;

    FloatingActionButton suitcasefloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_suitcases, container, false);

        suitcasefloatingActionButton = view.findViewById(R.id.fragment_showSuitcase_btn_add);

        suitcasefloatingActionButton.setOnClickListener(view1 -> {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main_layout,new Fragment_CreateSuitcase());
            fragmentTransaction.commit();
        });

        //obtenemos los datos de la tabla
        db = AppDatabase.getInstance(getContext());
        listOfSuitcases = (ArrayList<Suitcase>) db.suitcaseDAO().getAll();

        //vinculo con el recycler view
        suitcaseRecyclerView = view.findViewById(R.id.fragment_showSuitcase_recyclerview);
        //mostramos los datos
        mostrarDatos();


        return view;
    }

    private void mostrarDatos() {
        suitcaseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_suitcases = new Adapter_suitcases(getContext(), listOfSuitcases);
        suitcaseRecyclerView.setAdapter(adapter_suitcases);

        adapter_suitcases.setListener(view -> {
            bindingSuitcasefocusSuitcase.sendSuitcase(listOfSuitcases.get(suitcaseRecyclerView.getChildAdapterPosition(view)));

        });

    }

    //Comunicacion entre el fragment Maleta y Detalles de la maleta
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingSuitcasefocusSuitcase = (Binding_Suitcase_focusSuitcase) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
