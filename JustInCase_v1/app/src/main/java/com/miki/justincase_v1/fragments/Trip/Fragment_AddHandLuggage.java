package com.miki.justincase_v1.fragments.Trip;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Suitcase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Fragment_AddHandLuggage extends BaseFragment {

    Adapter_Suitcase adapter;
    RecyclerView recyclerView;
    ArrayList<Suitcase> dataset;

    FloatingActionButton floatingActionButton;
    ArrayList<Suitcase> arrayList;
    Trip trip;
    Bundle bundle;

    Button btn_newSuitcase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_handluggage, container, false);

        floatingActionButton = view.findViewById(R.id.fragment_Add_HandLuggage_finish);
        floatingActionButton.setVisibility(View.GONE);

        btn_newSuitcase = view.findViewById(R.id.addHandluggage_btn_newSuitcase);
        btn_newSuitcase.setOnClickListener(v -> createNewSuitcaseDialog());

        recyclerView = view.findViewById(R.id.fragment_addBaggage_recyclerview);

        bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();
            trip = (Trip) bundle.getSerializable("trip");

            dataset = Presenter.selectSuitcaseNOTFromThisTrip(trip, getContext());

            if (dataset.isEmpty()) {
                warningDialog();
            } else {
                setRecyclerView();
            }
        }

        floatingActionButton.setOnClickListener(v -> {
            Presenter.addSomeSuitcaseToThisTrip(arrayList, trip, getContext());
            Presenter.removeSuitcaseSelectedState(arrayList, getContext());
            if (arrayList.size() == 1) {
                makeToast(getContext(), getString(R.string.toast_created_handLuggage));
            } else {
                makeToast(getContext(), getString(R.string.toast_created_someHandLuggage));
            }
            bundle.putSerializable("trip", trip);
            getNav().navigate(R.id.action_fragment_Add_HandLuggage_to_fragment_ShowTrips, bundle);
        });
        return view;
    }

    private void setRecyclerView() {
        Presenter.removeSuitcaseSelectedState(dataset, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Suitcase(dataset);
        recyclerView.setAdapter(adapter);

        adapter.setListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            Suitcase suitcase = dataset.get(position);
            if (!arrayList.contains(suitcase)) {
                arrayList.add(suitcase);
                suitcase.setSelectedState(true);
            } else {
                arrayList.remove(suitcase);
                suitcase.setSelectedState(false);
            }
            Presenter.updateSuitcaseState(suitcase, getContext());
            adapter.notifyItemChanged(position);

            if (arrayList.isEmpty()) {
                floatingActionButton.setVisibility(View.GONE);
            } else {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void warningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(getString(R.string.dialog_title_warning));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_warning_noSuitcase));

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_no), ((dialog, which) -> getNav().navigate(R.id.fragment_ShowTrips)));
        builder.setPositiveButton(getString(R.string.dialog_button_yes), ((dialog, which) -> createNewSuitcaseDialog()));

        builder.show();
    }

    private void createNewSuitcaseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_suitcase, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_suitcase);
        dialogTitle.setText(getString(R.string.dialog_title_newSuitcase));

        EditText nameET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText colorET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);

        EditText weigthET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);

        EditText heigthET = view.findViewById(R.id.card_view_suitcase_heigth);
        EditText widthET = view.findViewById(R.id.card_view_suitcase_width);
        EditText depthET = view.findViewById(R.id.card_view_suitcase_depth);

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_create), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            if ((nameET.getText()).toString().isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                Suitcase suitcase = createSuitcase(nameET, colorET, weigthET, heigthET, widthET, depthET);
                if (!Presenter.createSuitcase(suitcase, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_suitcase));
                } else {
                    makeToast(getContext(), getString(R.string.toast_created_suitcase));
                    dialog.dismiss();
                    dataset = Presenter.selectSuitcaseNOTFromThisTrip(trip, getContext());
                    setRecyclerView();
                }
            }

        });
    }

    @NotNull
    private Suitcase createSuitcase(EditText nameET, EditText colorET, EditText weigthET, EditText heigthET, EditText widthET, EditText depthET) {
        String name = nameET.getText().toString();
        String color = colorET.getText().toString();
        double weigth = Double.parseDouble(weigthET.getText().toString());

        double heigth = Double.parseDouble(heigthET.getText().toString());
        double width = Double.parseDouble(widthET.getText().toString());
        double depth = Double.parseDouble(depthET.getText().toString());

        return new Suitcase(name, color, weigth, heigth, width, depth);
    }
}