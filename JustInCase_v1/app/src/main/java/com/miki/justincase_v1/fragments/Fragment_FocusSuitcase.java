package com.miki.justincase_v1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.MainActivity;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapter.Adaptador_suitcaseContent;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.SuitcaseContent;

import java.util.ArrayList;

public class Fragment_FocusSuitcase extends Fragment {

    TextView nombreMaleta;
    Button btn_detallesMaleta_eliminar, btn_focusSuitcase_addNewItem;
    RecyclerView suticaseContentrecyclerView;
    Adaptador_suitcaseContent adaptador_suitcaseContent;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    AppDatabase db;
    ArrayList<SuitcaseContent> contenidoDeEstaMaleta;
    Fragment_Add_item_to_suitcase fragmentAddItemToSuitcase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_focus_suitcase, container, false);

        nombreMaleta = view.findViewById(R.id.fragment_focusSuitcase_suitcaseName);

        //bundle para recibir la informacion

        Bundle bundle = getArguments();
        Suitcase suitcase;
        //validacion
        if (bundle != null) {
            //pillamos la maleta que nos han pasado a traves del bundle
            suitcase = (Suitcase) bundle.getSerializable("suitcase");

            nombreMaleta.setText(suitcase.getSuitcaseName());

            db = AppDatabase.getInstance(view.getContext());
            //ahora vamos a (si tiene) mostrar el contenido de la maleta
            contenidoDeEstaMaleta = (ArrayList<SuitcaseContent>) db.suitcaseContentDAO().getTheSuitContentOfThisSuitcase(suitcase.getSuitcaseID());
            //contenidoDeEstaMaleta = (ArrayList<SuitcaseContent>) db.suitcaseContentDAO().getAll();

            //vinculo con el recycler view
            suticaseContentrecyclerView = view.findViewById(R.id.fragment_focusSuitcase_recyclerView);
            //mostramos los datos
            mostrarDatos();

            //Boton para eliminar un viaje
            btn_detallesMaleta_eliminar = view.findViewById(R.id.fragment_focusSuitcase_btn_delete);
            btn_detallesMaleta_eliminar.setOnClickListener(v -> {
                db.suitcaseDAO().delete(suitcase);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ShowSuitcases());
                fragmentTransaction.commit();
            });

            //Boton para añadir items a la maleta
            btn_focusSuitcase_addNewItem = view.findViewById(R.id.fragment_focusSuitcase_btn_add);
            btn_focusSuitcase_addNewItem.setOnClickListener(v -> {

                Bundle obundle = new Bundle();
                //enviamos el objeto (en serial)
                obundle.putSerializable("ThisSuitcase", suitcase);
                fragmentAddItemToSuitcase = new Fragment_Add_item_to_suitcase();
                fragmentAddItemToSuitcase.setArguments(obundle);

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, fragmentAddItemToSuitcase);
                fragmentTransaction.commit();

            });


        }
        return view;
    }

    private void mostrarDatos() {
        //Esto sería los items que tiene la maleta!
        suticaseContentrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador_suitcaseContent = new Adaptador_suitcaseContent(getContext(), contenidoDeEstaMaleta);
        suticaseContentrecyclerView.setAdapter(adaptador_suitcaseContent);

//        adaptador_suitcaseContent.setListener(view -> {

    }
}
