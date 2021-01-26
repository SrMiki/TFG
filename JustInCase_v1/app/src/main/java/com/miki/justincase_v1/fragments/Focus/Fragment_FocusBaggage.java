package com.miki.justincase_v1.fragments.Focus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adaptador_baggageContent;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Edit.Fragment_Edit_Baggage;
import com.miki.justincase_v1.fragments.Add.Fragment_Add_item_to_Baggage;

import java.util.ArrayList;

public class Fragment_FocusBaggage extends BaseFragment {

    //SUITCASE OF THE BAGGAGE!
    TextView suitcaseName, suitcaseColor, suticaseWeight, suitcaseDimns;

    Button btn_focusBaggage_add, btn_focusBaggage_delete, btn_focusBaggage_edit;
    RecyclerView BaggageContentrecyclerView;
    Adaptador_baggageContent adaptador_baggageContent;

    Binding_Entity_focusEntity bindingTripFocusTrip;

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
            baggage = (Baggage) bundle.getSerializable("baggage");

            suitcase = Presented.getSuitcaseAsociatedWithThisBaggage(baggage, view);

            suitcaseName.setText(suitcase.getSuitcaseName());
            suitcaseColor.setText(suitcase.getSuitcaseColor());
            suticaseWeight.setText(suitcase.getSuitcaseWeight());
            suitcaseDimns.setText(suitcase.getSuitcaseDims());

            contenidoDeEsteBaggage = Presented.getItemsFromThisBaggage(baggage, view);

            BaggageContentrecyclerView = view.findViewById(R.id.fragment_focusBaggage_recyclerView);
            loadRecyclerView();

            btn_focusBaggage_delete = view.findViewById(R.id.fragment_focusBaggage_btn_delete);
            btn_focusBaggage_delete.setOnClickListener(v -> {
                Presented.deleteBaggage(baggage, view);

                Trip trip = Presented.getTheTripAsociatedWithThisBaggage(baggage, view);
                bindingTripFocusTrip.sendTrip(trip);
            });

            btn_focusBaggage_add = view.findViewById(R.id.fragment_focusBaggage_btn_add);
            btn_focusBaggage_add.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisBaggage", baggage);
                getNav().navigate(R.id.fragment_Add_item_to_Baggage, obundle);
            });

            btn_focusBaggage_edit = view.findViewById(R.id.fragment_focusBaggage_btn_edit);
            btn_focusBaggage_edit.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisSuitcase", suitcase);
                getNav().navigate(R.id.fragment_Edit_Baggage, obundle);
            });


        }
        return view;
    }

    private void loadRecyclerView() {
        //Esto sería los items que tiene la maleta!
        BaggageContentrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador_baggageContent = new Adaptador_baggageContent(getContext(), contenidoDeEsteBaggage);
        BaggageContentrecyclerView.setAdapter(adaptador_baggageContent);

//        adaptador_suitcaseContent.setListener(view -> {

    }
}
