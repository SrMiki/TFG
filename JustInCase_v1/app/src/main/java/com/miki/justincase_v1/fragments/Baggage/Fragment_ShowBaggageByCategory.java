package com.miki.justincase_v1.fragments.Baggage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_CategoryBaggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowBaggageByCategory extends BaseFragment {

    //SUITCASE OF THE BAGGAGE!
    TextView suitcaseName, suitcaseColor, suticaseWeight, suitcaseDimns;

    FloatingActionButton actionButton;

    RecyclerView recyclerView;
    Adapter_CategoryBaggage adapter;

    Suitcase suitcase;
    HandLuggage handLuggage;

    ArrayList<Category> dataset;

    Bundle bundle;
    Switch switchShowCategories;
    SharedPreferences sp;
    Button btn_delete, btn_edit, btn_generateBaggage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_baggage, container, false);


        bundle = getArguments();
        if (bundle != null) {

            switchShowCategories = view.findViewById(R.id.switch_ShowCategory);
            sp = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            boolean showCategories = sp.getBoolean("showCategories", false);

            switchShowCategories.setChecked(showCategories);
            editor.putBoolean("showCategories", switchShowCategories.isChecked());

            switchShowCategories.setOnCheckedChangeListener((buttonView, isChecked) -> {
                editor.putBoolean("showCategories", false);
                editor.apply();
                getNav().navigate(R.id.fragment_ShowBaggageByItem, bundle);
            });

            setHasOptionsMenu(true);
            handLuggage = (HandLuggage) bundle.getSerializable("handluggage");

            btn_edit = view.findViewById(R.id.showBaggage_button_edit);
            btn_edit.setOnClickListener(v -> {
                editHandLuggageDialog();
            });

            btn_delete = view.findViewById(R.id.showBaggage_button_delete);
            btn_delete.setOnClickListener(v -> {
                deleteHandLuggageDialog(v);
            });

            btn_generateBaggage = view.findViewById(R.id.btn_generateBaggage);
            btn_generateBaggage.setOnClickListener(v -> {
                Trip trip = Presented.getTrip(handLuggage, getContext());

                Bundle obundle = new Bundle();
                obundle.putSerializable("handLuggage", handLuggage);
                obundle.putSerializable("trip", trip);
                getNav().navigate(R.id.algoritm, obundle);
            });

            suitcaseName = view.findViewById(R.id.suitcaseNameTV);
            suitcaseName.setText(handLuggage.getHandLuggageName());

            dataset = Presented.selectAllCategoriesOfThisHandLuggage(handLuggage, getContext());

            if (!dataset.isEmpty()) {
                recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new Adapter_CategoryBaggage(dataset, getActivity(), handLuggage);
                recyclerView.setAdapter(adapter);
            }

            actionButton = view.findViewById(R.id.fragment_show_entity_btn_add);
            actionButton.setOnClickListener(v -> {
                getNav().navigate(R.id.fragment_Add_Category_To_Baggage, bundle);
            });

        }
        return view;
    }


//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_delete:
//                deleteThisBaggage();
//                return true;
//            case R.id.action_edit:
//                editThisBaggage();
//                return true;
//            case R.id.action_addItem:
//                Bundle obundle = new Bundle();
//                obundle.putSerializable("ThisBaggage", handLuggage);
//                getNav().navigate(R.id.fragment_Add_item_to_Baggage, obundle);
//                return true;
//            default:
//                return false;
////                return super.onOptionsItemSelected(item);
//        }
//
//    }

    private void editHandLuggageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        String cancel = getString(R.string.text_cancel);
        String haveBeenAdded = getString(R.string.text_haveBeenAdded);
        String add = getString(R.string.text_add);

        String title = getString(R.string.text_edit);

        builder.setTitle(title);
        View view = inflater.inflate(R.layout.alertdialog_suitcase, null);
        builder.setView(view);

        EditText nameET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText colorET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);

        EditText weigthET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);

        EditText heigthET = view.findViewById(R.id.card_view_suitcase_heigth);
        EditText widthET = view.findViewById(R.id.card_view_suitcase_width);
        EditText depthET = view.findViewById(R.id.card_view_suitcase_depth);

        suitcase = Presented.getSuitcase(handLuggage, getContext());

        nameET.setText(suitcase.getName());
        colorET.setText(suitcase.getColor());

        weigthET.setText(String.valueOf(suitcase.getWeight()));

        heigthET.setText(String.valueOf(suitcase.getHeigth()));
        widthET.setText(String.valueOf(suitcase.getWidth()));
        depthET.setText(String.valueOf(suitcase.getDepth()));

        builder.setNegativeButton(cancel, ((dialog, which) -> dialog.dismiss()));
        builder.setPositiveButton(add, (dialog, which) -> {
            String name = nameET.getText().toString();
            String color = colorET.getText().toString();
            double weigth = Double.parseDouble(weigthET.getText().toString());

            double heigth = Double.parseDouble(heigthET.getText().toString());
            double width = Double.parseDouble(widthET.getText().toString());
            double depth = Double.parseDouble(depthET.getText().toString());

            suitcase.setSuticase(name, color, weigth, heigth, width, depth);
            handLuggage.setHandLuggage(suitcase.getName());


            Presented.updateSuitcase(suitcase, getContext());
            Presented.updateHandLuggage(handLuggage, getContext());
            makeToast(getView().getContext(), haveBeenAdded);
            Bundle obundle = new Bundle();
            obundle.putSerializable("handluggage", handLuggage);
            getNav().navigate(R.id.fragment_ShowBaggageByItem, obundle);
        });
        builder.show();

    }

    private void deleteHandLuggageDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getString(R.string.text_warning));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_textview, null);
        builder.setView(view);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(getString(R.string.ask_deleteHandLuggage));

        builder.setNegativeButton(getString(R.string.text_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_yes), ((dialog, which) -> {
            Presented.deleteHandluggage(handLuggage, getContext());
            getNav().navigate(R.id.fragment_ShowTrips);
        }));
        builder.show();

    }
}
