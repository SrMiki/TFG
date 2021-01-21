package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_item;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Fragment_Add_item_to_Baggage extends Fragment {

    AppDatabase db;
    Adapter_item adapter_item;
    RecyclerView itemRecyclerView;
    ArrayList<Item> listaDeItems;

    Activity activity;
    Binding_Entity_focusEntity bindingBaggageFocusBaggage;

    Fragment parentFragment;
    Baggage thisBagagge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_baggagecontent, container, false);
        parentFragment = getParentFragment();

        Bundle bundle = getArguments();

        //validacion
        if (bundle != null) {
            //pillamos la maleta que nos han pasado a traves del bundle
            thisBagagge = (Baggage) bundle.getSerializable("ThisBaggage");
            //obtenemos los datos de la tabla
            db = AppDatabase.getInstance(getContext());
            listaDeItems = (ArrayList<Item>) db.itemDAO().getAllItemsThatItNotInThisBaggage(thisBagagge.baggageID);
            //vinculo con el recycler view
            itemRecyclerView = view.findViewById(R.id.fragment_addBaggageContent_recyclerview);
            mostrarDatos();
        }
        return view;
    }

    private void mostrarDatos() {
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_item = new Adapter_item(getContext(), listaDeItems);
        itemRecyclerView.setAdapter(adapter_item);

        //Cuando pulse en un dato
        adapter_item.setListener(view -> {


            Item item = listaDeItems.get(itemRecyclerView.getChildAdapterPosition(view));

            BaggageContent baggageContent = new BaggageContent(item.itemID, thisBagagge.baggageID, item.itemName);

            db.baggageContentDAO().addANewItemForThisSuitcase(baggageContent);

            bindingBaggageFocusBaggage.sendBaggage(thisBagagge);


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

