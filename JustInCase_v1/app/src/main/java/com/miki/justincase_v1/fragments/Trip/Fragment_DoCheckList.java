package com.miki.justincase_v1.fragments.Trip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_Check_Category;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_Check_Items;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_DoCheckList extends BaseFragment {

    Switch switchShowCategories;
    private SharedPreferences sp;

    ArrayList<Baggage> dataset;
    Adapter_Check_Items adapter;
    RecyclerView recyclerView;

    HandLuggage handLuggage;

    FloatingActionButton actionButton;
    TextView suitcaseNameTV;
    private ArrayList<Category> datasetCategory;
    private Adapter_Check_Category adapterCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);


        suitcaseNameTV = view.findViewById(R.id.suitcaseNameTV);

        Bundle bundle = getArguments();
        if (bundle != null) {

            setHasOptionsMenu(true);

            handLuggage = (HandLuggage) bundle.getSerializable("handluggage");
            suitcaseNameTV.setText(handLuggage.getHandLuggageName());

            recyclerView = view.findViewById(R.id.fragment_checklist_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            switchShowCategories = view.findViewById(R.id.switch_ShowCategory);
            sp = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            boolean showCategories = sp.getBoolean("showCategories", false);
            switchShowCategories.setChecked(showCategories);
            editor.putBoolean("showCategories", switchShowCategories.isChecked());

            //always iniciate
            dataset = Presenter.getBaggageOfThisHandLuggage(handLuggage, getContext());

            if (showCategories) {
                switchShowCategories.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    editor.putBoolean("showCategories", false);
                    editor.apply();
                    getNav().navigate(R.id.action_fragment_DoCheckListByItem_self, bundle);
                });
                showByCategory();
            } else {
                switchShowCategories.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    editor.putBoolean("showCategories", true);
                    editor.apply();
                    getNav().navigate(R.id.action_fragment_DoCheckListByItem_self, bundle);
                });
                showByItem();
            }

            CheckBox checkBoxAll = view.findViewById(R.id.checkbox_checkAll);
            checkBoxAll.setChecked(handLuggage.isHandLuggageCompleted());
            checkBoxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Presenter.checkAllBaggage(dataset, isChecked, getContext());
                Bundle obundle = new Bundle();
                Trip trip = Presenter.getTrip(handLuggage, getContext());
                obundle.putSerializable("trip", trip);
                getNav().navigate(R.id.action_fragment_DoCheckList_to_fragment_CheckIn, obundle);
//                adapter.notifyDataSetChanged();
            });

            actionButton = view.findViewById(R.id.fragment_checklist_btn_finish);
            actionButton.setOnClickListener(v -> {
                Presenter.checkBaggage(handLuggage, dataset, getContext());
                Bundle obundle = new Bundle();
                Trip trip = Presenter.getTrip(handLuggage, getContext());
                obundle.putSerializable("trip", trip);
                getNav().navigate(R.id.action_fragment_DoCheckList_to_fragment_CheckIn, obundle);
            });
        }
        return view;
    }

    private void showByItem() {
        adapter = new Adapter_Check_Items(dataset);
        recyclerView.setAdapter(adapter);

//        adapter.setListener(v -> {
//            int position = recyclerView.getChildAdapterPosition(v);
//            focusBaggage = dataset.get(position);
//            changeItemCount();
//        });
    }

    private void showByCategory() {
        datasetCategory = Presenter.selectAllCategoriesOfThisHandLuggage(handLuggage, getContext());

        if (!datasetCategory.isEmpty()) {

            adapterCategory = new Adapter_Check_Category(datasetCategory, handLuggage);
            recyclerView.setAdapter(adapterCategory);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        menuItem.setOnMenuItemClickListener(item -> {
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
