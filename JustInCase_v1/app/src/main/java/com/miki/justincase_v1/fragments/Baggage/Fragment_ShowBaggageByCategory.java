package com.miki.justincase_v1.fragments.Baggage;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Category;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowBaggageByCategory extends BaseFragment {

    //SUITCASE OF THE BAGGAGE!
    TextView suitcaseName, suitcaseColor, suticaseWeight, suitcaseDimns;
    Button btnItems;

    RecyclerView recyclerView;
    Adapter_Category adapter;

    Suitcase suitcase;
    HandLuggage handLuggage;

    ArrayList<Category> dataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_baggage_bycategory, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {

            setHasOptionsMenu(true);
            handLuggage = (HandLuggage) bundle.getSerializable("baggage");

            btnItems = view.findViewById(R.id.fragment_btn_items);
            btnItems.setOnClickListener(v->{
                getNav().navigate(R.id.fragment_ShowBaggage, bundle);
            });

            suitcase = Presented.getSuitcase(handLuggage, view);
            //focusBaggage
            setSuitcase(view);

            ArrayList<Baggage> itemsFromThisBaggage = Presented.getBaggage(handLuggage, view);
           dataset = Presented.getAllCategoriesAItem(itemsFromThisBaggage, view);

            recyclerView = view.findViewById(R.id.fragment_BaggageByCategory_recyclerView);
            //Esto sería los items que tiene la maleta!
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Category(dataset);
            recyclerView.setAdapter(adapter);

//        adaptador_suitcaseContent.setListener(view -> {
        }
        return view;
    }

    private void setSuitcase(View view) {
        suitcaseName = view.findViewById(R.id.card_view_suitcase_suitcaseName);
        suitcaseColor = view.findViewById(R.id.card_view_suitcase_suitcaseColor);
        suticaseWeight = view.findViewById(R.id.card_view_suitcase_suitcaseWeight);
        suitcaseDimns = view.findViewById(R.id.card_view_suitcase_suitcaseDimns);

        suitcaseName.setText(suitcase.getSuitcaseName());
        suitcaseColor.setText(suitcase.getSuitcaseColor());
        suticaseWeight.setText(suitcase.getSuitcaseWeight());
        suitcaseDimns.setText(suitcase.getSuitcaseDims());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteThisBaggage();
                return true;
            case R.id.action_edit:
                editThisBaggage();
                return true;
            case R.id.action_addItem:
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisBaggage", handLuggage);
                getNav().navigate(R.id.fragment_Add_item_to_Baggage, obundle);
                return true;
            default:
                return false;
//                return super.onOptionsItemSelected(item);
        }

    }

    private void editThisBaggage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        String cancel = getString(R.string.text_cancel);
        String haveBeenAdded = getString(R.string.text_haveBeenAdded);
        String add = getString(R.string.text_add);

        String title = getString(R.string.text_edit);

        builder.setTitle(title);
        View view = inflater.inflate(R.layout.form_suitcase, null);
        builder.setView(view);
        EditText name = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText color = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);
        EditText weigth = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);
        EditText dimns = view.findViewById(R.id.activity_createSuitcase_input_suitcaseDims);
        name.setText(suitcase.getSuitcaseName());
        color.setText(suitcase.getSuitcaseColor());
        weigth.setText(suitcase.getSuitcaseWeight());
        dimns.setText(suitcase.getSuitcaseDims());

        builder.setNegativeButton(cancel, ((dialog, which) -> dialog.dismiss()));
        builder.setPositiveButton(add, (dialog, which) -> {
            String itemName = name.getText().toString();
            String colorS = color.getText().toString();
            String weigthS = weigth.getText().toString();
            String dimnS = dimns.getText().toString();
            Presented.updateSuitcase(suitcase, itemName, colorS, weigthS, dimnS, view);
            Presented.updateHandLuggage(handLuggage, itemName, colorS, weigthS, dimnS, view);
            makeToast(getView().getContext(), haveBeenAdded);
            Bundle obundle = new Bundle();
            obundle.putSerializable("baggage", handLuggage);
            getNav().navigate(R.id.fragment_ShowBaggage, obundle);
        });
        builder.show();

    }

    private void deleteThisBaggage() {
        Presented.deleteBaggage(handLuggage, getView());

        Trip trip = Presented.getTrip(handLuggage, getView());
        Bundle obundle = new Bundle();
        obundle.putSerializable("trip", trip);
        getNav().navigate(R.id.fragment_showHandLaggage, obundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem;

        menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(false);

    }
}
