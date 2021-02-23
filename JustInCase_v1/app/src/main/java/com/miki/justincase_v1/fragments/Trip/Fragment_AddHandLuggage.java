package com.miki.justincase_v1.fragments.Trip;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Suitcase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_AddHandLuggage extends BaseFragment {

    Adapter_Suitcase adapter;
    RecyclerView recyclerView;
    ArrayList<Suitcase> dataset;

    FloatingActionButton floatingActionButton;
    ArrayList<Suitcase> arrayList;
    Trip trip;
    Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_handluggage, container, false);

        bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();
            trip = (Trip) bundle.getSerializable("trip");

            dataset = Presented.selectSuitcaseNOTFromThisTrip(trip, getContext());

            if (dataset.isEmpty()) {
                warningDialog(view);
            }

            floatingActionButton = view.findViewById(R.id.fragment_Add_HandLuggage_finish);
            floatingActionButton.setOnClickListener(v -> {
                Presented.addHandLuggageToThisTrip(arrayList, trip, getContext());
                bundle.putSerializable("trip", trip);
                getNav().navigate(R.id.fragment_ShowTrips, bundle);
            });

            recyclerView = view.findViewById(R.id.fragment_addBaggage_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Suitcase(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                Suitcase suitcase = dataset.get(position);

                if (!arrayList.contains(suitcase)) {
                    arrayList.add(suitcase);
                    adapter.setSelectedState(true);
                } else {
                    arrayList.remove(suitcase);
                    adapter.setSelectedState(false);
                }
                adapter.notifyItemChanged(position);
            });
        }

        return view;
    }

    private void warningDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String yes = getString(R.string.text_yes);
        String no = getString(R.string.text_no);
        String text = getString(R.string.warning_noSuitcase);

        EditText editText = new EditText(getContext());
        editText.setText(text);
        builder.setView(editText);

        builder.setNegativeButton(no, ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.fragment_ShowTrips);
        }));
        builder.setPositiveButton(yes, ((dialog, which) -> {
            dialog.dismiss();
            createNewSuitcaseDialog(v);

        }));
        builder.show();


    }

    private void createNewSuitcaseDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.text_newSuitcase));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_suitcase, null);
        builder.setView(view);

        EditText nameET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText colorET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);

        EditText weigthET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);

        EditText heigthET = view.findViewById(R.id.card_view_suitcase_heigth);
        EditText widthET = view.findViewById(R.id.card_view_suitcase_width);
        EditText depthET = view.findViewById(R.id.card_view_suitcase_depth);

        builder.setNegativeButton(getString(R.string.text_cancel), ((dialog, which) -> {
            dialog.dismiss();
        }));


        builder.setPositiveButton(getString(R.string.text_add), ((dialog, which) -> {
            String name = nameET.getText().toString();
            String color = colorET.getText().toString();
            double weigth = Double.parseDouble(weigthET.getText().toString());

            double heigth = Double.parseDouble(heigthET.getText().toString());
            double width = Double.parseDouble(widthET.getText().toString());
            double depth = Double.parseDouble(depthET.getText().toString());

            Suitcase suitcase = new Suitcase(name, color, weigth, heigth, width, depth);
            boolean haveBeenAdded = Presented.createSuitcase(suitcase, getContext());
            if (haveBeenAdded) {
                makeToast(v.getContext(), getString(R.string.text_haveBeenAdded));
            } else {
                makeToast(v.getContext(), getString(R.string.error));
            }
            createAnotherSuitcaseDialog(v);
        }));
        builder.show();
    }

    private void createAnotherSuitcaseDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String NegativeButton = getString(R.string.text_no);
        String positiveButton = getString(R.string.text_yes);
        String string = getString(R.string.text_ask_anotherSuitcase);

        builder.setCancelable(true);

        EditText editText = new EditText(getContext());
        editText.setText(string);
        builder.setView(editText);
        builder.setNegativeButton(NegativeButton, ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.fragment_Add_HandLuggage, bundle);
        }));
        builder.setPositiveButton(positiveButton, ((dialog, which) -> {
            dialog.dismiss();
            createNewSuitcaseDialog(v);
        }));
        builder.show();
    }
}