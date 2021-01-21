package com.miki.justincase_v1.fragments.Focus;

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

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adaptador_baggageContent;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Fragment_Add_item_to_Baggage;

import java.util.ArrayList;

public class Fragment_FocusBaggage extends BaseFragment {

    //SUITCASE OF THE BAGGAGE!
    TextView suitcaseName, suitcaseColor, suticaseWeight, suitcaseDimns;

    Button btn_focusBaggage_add, btn_focusBaggage_delete;
    RecyclerView BaggageContentrecyclerView;
    Adaptador_baggageContent adaptador_baggageContent;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Binding_Entity_focusEntity bindingTripFocusTrip;

    AppDatabase db;
    ArrayList<BaggageContent> contenidoDeEsteBaggage;
    Fragment_Add_item_to_Baggage fragmentAddItemToBaggage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_baggage, container, false);

        //focusBaggage
        suitcaseName = view.findViewById(R.id.fragment_focusBaggage_suitcaseName);
        suitcaseColor = view.findViewById(R.id.fragment_focusBaggage_suitcaseColor);
        suticaseWeight = view.findViewById(R.id.fragment_focusBaggage_suitcaseWeight);
        suitcaseDimns = view.findViewById(R.id.fragment_focusBaggage_suitcaseDimns);

        Bundle bundle = getArguments();
        Suitcase suitcase;
        Baggage baggage;

        if (bundle != null) {
            //pillamos la maleta que nos han pasado a traves del bundle
            baggage = (Baggage) bundle.getSerializable("baggage");

            db = AppDatabase.getInstance(view.getContext());

            suitcase = db.suitcaseDAO().getSuitcase(baggage.getFKsuitcaseID());

            suitcaseName.setText(suitcase.getSuitcaseName());
            suitcaseColor.setText(suitcase.getSuitcaseColor());
            suticaseWeight.setText(suitcase.getSuitcaseWeight());
            suitcaseDimns.setText(suitcase.getSuitcaseDims());

            //ahora vamos a (si tiene) mostrar el contenido de la maleta
            contenidoDeEsteBaggage = (ArrayList<BaggageContent>) db.baggageContentDAO().getItemsFromThisBaggage(baggage.baggageID);

            BaggageContentrecyclerView = view.findViewById(R.id.fragment_focusBaggage_recyclerView);
            mostrarDatos();

            //Baggage delete button
            btn_focusBaggage_delete = view.findViewById(R.id.fragment_focusBaggage_btn_delete);
            btn_focusBaggage_delete.setOnClickListener(v -> {
                Trip trip;
                trip = db.tripDao().getTrip(baggage.getFKtripID());

                db.baggageDAO().deleteBaggage(baggage);

                bindingTripFocusTrip.sendTrip(trip);


            });

            //Baggage add items to THIS Baggage button
            btn_focusBaggage_add = view.findViewById(R.id.fragment_focusBaggage_btn_add);
            btn_focusBaggage_add.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisBaggage", baggage);
                doFragmentTransaction(new Fragment_Add_item_to_Baggage());

            });


        }
        return view;
    }

    private void mostrarDatos() {
        //Esto sería los items que tiene la maleta!
        BaggageContentrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador_baggageContent = new Adaptador_baggageContent(getContext(), contenidoDeEsteBaggage);
        BaggageContentrecyclerView.setAdapter(adaptador_baggageContent);

//        adaptador_suitcaseContent.setListener(view -> {

    }
}
