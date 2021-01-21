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
import com.miki.justincase_v1.adapters.Adapter_suitcases;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Create.Fragment_CreateSuitcase;

import java.util.ArrayList;

public class Fragment_ShowSuitcases extends BaseFragment {

    AppDatabase db;
    Adapter_suitcases adapter_suitcases;
    RecyclerView suitcaseRecyclerView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ArrayList<Suitcase> listOfSuitcases;

    //referencia para la comunicacion de fragment
    Activity activity;
    Binding_Entity_focusEntity bindingSuitcasefocusSuitcase;

    FloatingActionButton suitcasefloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_suitcases, container, false);

        suitcasefloatingActionButton = view.findViewById(R.id.fragment_showSuitcase_btn_add);

        suitcasefloatingActionButton.setOnClickListener(v -> {
           doFragmentTransaction(new Fragment_CreateSuitcase());
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
            bindingSuitcasefocusSuitcase = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
