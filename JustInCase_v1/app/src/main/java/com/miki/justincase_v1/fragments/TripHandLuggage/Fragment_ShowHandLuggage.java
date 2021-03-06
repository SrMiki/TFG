package com.miki.justincase_v1.fragments.TripHandLuggage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Baggage_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.Adapter_Baggage;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_Categories_in_Baggage;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowHandLuggage extends BaseFragment implements Baggage_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Bundle bundle;
    Switch switchShowCategories;

    TextView suitcaseName;

    FloatingActionButton actionButton;
    Button btn_generateBaggage;

    Suitcase suitcase;
    HandLuggage handLuggage;
    Baggage focusBaggage;

    SharedPreferences sp;
    NumberPicker numberPicker;

    RecyclerView recyclerView;
    ArrayList<Baggage> dataset;
    ArrayList<Category> datasetCategory;

    Adapter_Baggage adapter;
    Adapter_Categories_in_Baggage adapterCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_baggage, container, false);
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle != null) {
            handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");

            setHasOptionsMenu(true);

            suitcaseName = view.findViewById(R.id.suitcaseNameTV);
            suitcaseName.setText(handLuggage.getHandLuggageName());

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            btn_generateBaggage = view.findViewById(R.id.btn_generateBaggage);
            btn_generateBaggage.setOnClickListener(v -> {
                Trip trip = Presenter.getTrip(handLuggage, getContext());

                Bundle obundle = new Bundle();
                obundle.putSerializable("handLuggage", handLuggage);
                obundle.putSerializable("trip", trip);
                getNav().navigate(R.id.suitcase_generator, obundle);
            });


            switchShowCategories = view.findViewById(R.id.switch_ShowCategory);
            sp = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            boolean showCategories = sp.getBoolean("showCategories", false);
            switchShowCategories.setChecked(showCategories);
            editor.putBoolean("showCategories", switchShowCategories.isChecked());

            if (showCategories) {
                switchShowCategories.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    editor.putBoolean("showCategories", false);
                    editor.apply();
                    getNav().navigate(R.id.action_fragment_ShowBaggage_self, bundle);
                });
                showByCategory(view);
            } else {
                switchShowCategories.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    editor.putBoolean("showCategories", true);
                    editor.apply();
                    getNav().navigate(R.id.action_fragment_ShowBaggage_self, bundle);
                });
                showByItem(view);
            }

            actionButton = view.findViewById(R.id.fragment_show_entity_btn_add);
            actionButton.setOnClickListener(v -> {
                getNav().navigate(R.id.fragment_Add_Baggage, bundle);
            });

        }
        return view;
    }

    private void showByItem(View view) {
        dataset = Presenter.getBaggageOfThisHandLuggage(handLuggage, getContext());

        ItemTouchHelper.SimpleCallback simpleCallback = new Baggage_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        adapter = new Adapter_Baggage(dataset, handLuggage);
        recyclerView.setAdapter(adapter);

        adapter.setListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            focusBaggage = dataset.get(position);
            changeItemCount(v);
        });
    }

    private void showByCategory(View view) {
        datasetCategory = Presenter.selectAllCategoriesOfThisHandLuggage(handLuggage, getContext());

        if (!datasetCategory.isEmpty()) {

            adapterCategory = new Adapter_Categories_in_Baggage(datasetCategory, getActivity(), handLuggage);
            recyclerView.setAdapter(adapterCategory);
        }
    }

    private void deleteHandLuggageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getString(R.string.dialog_warning));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_textview, null);
        builder.setView(view);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(getString(R.string.dialog_ask_deleteHandLuggage));

        builder.setNegativeButton(getString(R.string.dialog_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_yes), ((dialog, which) -> {
            Presenter.deleteHandluggage(handLuggage, getContext());
            getNav().navigate(R.id.fragment_ShowTrips, bundle);
        }));
        builder.show();

    }

    private void changeItemCount(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getString(R.string.text_itemCount));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_count, null);
        builder.setView(view);

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);
        numberPicker.setValue(focusBaggage.getBaggageCount());

        builder.setNegativeButton(getString(R.string.dialog_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_edit), ((dialog, which) -> {
            Presenter.updateBaggage(focusBaggage, numberPicker.getValue(), getContext());
//            makeToast(v.getContext(), getString(R.string.text_haveBeenUpdated));
            getNav().navigate(R.id.fragment_ShowBaggage, bundle);
        }));
        builder.show();
    }


    private void editHandLuggageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setTitle(getString(R.string.text_edit));
        View view = inflater.inflate(R.layout.alertdialog_suitcase, null);
        builder.setView(view);

        EditText nameET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText colorET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);

        EditText weigthET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);

        EditText heigthET = view.findViewById(R.id.card_view_suitcase_heigth);
        EditText widthET = view.findViewById(R.id.card_view_suitcase_width);
        EditText depthET = view.findViewById(R.id.card_view_suitcase_depth);

        suitcase = Presenter.getSuitcase(handLuggage, getContext());

        nameET.setText(suitcase.getName());
        colorET.setText(suitcase.getColor());

        weigthET.setText(String.valueOf(suitcase.getWeight()));

        heigthET.setText(String.valueOf(suitcase.getHeigth()));
        widthET.setText(String.valueOf(suitcase.getWidth()));
        depthET.setText(String.valueOf(suitcase.getDepth()));


        builder.setNegativeButton(getString(R.string.dialog_cancel), ((dialog, which) -> dialog.dismiss()));
        builder.setPositiveButton(getString(R.string.dialog_add), (dialog, which) -> {

            String name = nameET.getText().toString();
            String color = colorET.getText().toString();
            double weigth = Double.parseDouble(weigthET.getText().toString());

            double heigth = Double.parseDouble(heigthET.getText().toString());
            double width = Double.parseDouble(widthET.getText().toString());
            double depth = Double.parseDouble(depthET.getText().toString());

            suitcase.setSuticase(name, color, weigth, heigth, width, depth);
            handLuggage.setHandLuggage(suitcase.getName());

            Presenter.updateSuitcase(suitcase, getContext());
            Presenter.updateHandLuggage(handLuggage, getContext());
            makeToast(getView().getContext(), getString(R.string.toast_suitcaseUpdated));
            Bundle obundle = new Bundle();
            obundle.putSerializable("handLuggage", handLuggage);
            getNav().navigate(R.id.fragment_ShowBaggage, obundle);
        });
        builder.show();
    }

    private void importHandLuggageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getString(R.string.DialogTitle_importHandLuggage));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_photo, null);
        builder.setView(view);

    }

    private void shareHandLuggageDialog() {
        ArrayList<Baggage> baggageOfThisHandLuggage = Presenter.getBaggageOfThisHandLuggage(handLuggage, getContext());
        String baggage = makeString(baggageOfThisHandLuggage);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, baggage);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "hola");
        startActivity(shareIntent);

//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//        builder.setTitle(getString(R.string.DialogTitle_shareHandLuggage));
//        builder.setMessage(getString(R.string.DialogMessage_shareHandLuggage));
//
//        builder.setNegativeButton(getString(R.string.text_cancel), ((dialog, which) -> dialog.dismiss()));
//        builder.setPositiveButton(getString(R.string.DialogText_shareHandLuggage), (dialog, which) -> {
//
//
//
//        });

    }

    private String makeString(ArrayList<Baggage> baggageOfThisHandLuggage) {
        String msg = handLuggage.handLuggageName + "\n";
        for (Baggage baggage : baggageOfThisHandLuggage
        ) {
            msg += baggage.baggageName + " " + baggage.baggageCount + "\n";
        }
        return msg;
    }


    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        adapter.onSwipe(viewHolder, direction, position);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.handluggage_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                editHandLuggageDialog();
                return true;
            case R.id.action_delete:
                deleteHandLuggageDialog();
                return true;
            case R.id.action_import:
                importHandLuggageDialog();
                return true;
            case R.id.action_share:
                shareHandLuggageDialog();
                return true;
            case R.id.action_template:
                getNav().navigate(R.id.fragment_Use_Templates_For_HandLuggage, bundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
