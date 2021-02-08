package com.miki.justincase_v1.fragments.Show;


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miki.justincase_v1.adapters.Item_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.Suitcase_RecyclerItemTouchHelper;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.adapters.Adapter_Suitcase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowSuitcases extends BaseFragment implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Suitcase adapter;
    ArrayList<Suitcase> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_entity, container, false);

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
        dataset = Presented.getAllSuitcase(view);
        setHasOptionsMenu(true);

        floatingButton.setOnClickListener(v -> {
            createNewSuitcase(view, v);
        });


        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
        ItemTouchHelper.SimpleCallback simpleCallback = new Suitcase_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Suitcase(dataset);
        recyclerView.setAdapter(adapter);

        adapter.setListener(v -> {
            Suitcase suitcase = dataset.get(recyclerView.getChildAdapterPosition(v));
            editSuitcase(suitcase, view, v);
        });
        return view;
    }

    private void createNewSuitcase(View vista, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        String cancel = getString(R.string.text_cancel);
        String haveBeenAdded = getString(R.string.text_haveBeenAdded);
        String add = getString(R.string.text_add);
        String title = getString(R.string.text_suitcase);

        builder.setTitle(title);
        View view = inflater.inflate(R.layout.form_suitcase, null);
        builder.setView(view);

        EditText name = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText color = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);
        EditText weigth = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);
        EditText dimns = view.findViewById(R.id.activity_createSuitcase_input_suitcaseDims);
        name.setText("");
        color.setText("");
        weigth.setText("");
        dimns.setText("");

        builder.setNegativeButton(cancel, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(add, ((dialog, which) -> {
            String itemName = name.getText().toString();
            String colorS = color.getText().toString();
            String weigthS = weigth.getText().toString();
            String dimnS = dimns.getText().toString();
            Presented.createSuitcase(itemName, colorS, weigthS, dimnS, view);
            makeToast(v.getContext(), haveBeenAdded);
            getNav().navigate(R.id.fragment_ShowSuitcases);
        }));
        builder.show();
    }

    private void editSuitcase(Suitcase suitcase, View w, View v) {
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

        builder.setNegativeButton(cancel, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(add, ((dialog, which) -> {
            String itemName = name.getText().toString();
            String colorS = color.getText().toString();
            String weigthS = weigth.getText().toString();
            String dimnS = dimns.getText().toString();
            Presented.updateSuitcase(suitcase, itemName, colorS, weigthS, dimnS, view);
            makeToast(v.getContext(), haveBeenAdded);
            getNav().navigate(R.id.fragment_ShowSuitcases);
        }));
        builder.show();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Suitcase.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
//            String name = dataset.get(deletedIndex).getSuitcaseName();
            Suitcase deletedItem = dataset.get(deletedIndex);

            adapter.removeItem(viewHolder.getAdapterPosition());

//            restoreDeletedElement(viewHolder, name, deletedItem, deletedIndex);
            //Note: if the item it's deleted and then restore, only restore in item. You must
            //yo add again in Baggages and Categorys.
            Presented.deleteSuitcase(deletedItem, viewHolder.itemView);

        }
    }


    public void restoreDeletedElement(RecyclerView.ViewHolder viewHolder, String name, Suitcase deletedItem, int deletedIndex) {
        String deleted = getString(R.string.text_hasBeenDeleted);
        String restore = getString(R.string.text_restore);
        Snackbar snackbar = Snackbar.make(((Adapter_Item.AdapterViewHolder) viewHolder).layout,
                name + " " + deleted,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(restore, v -> {
            adapter.restoreItem(deletedItem, deletedIndex);
//            Presented.createItem(deletedItem.getSuitcaseName(), viewHolder.itemView);
        });

        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
//        super.onCreateOptionsMenu(menu, inflater);
    }
}
