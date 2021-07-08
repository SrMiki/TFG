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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.adapters.Adapter_Suitcase;
import com.miki.justincase_v1.Swipers.Item_RecyclerItemTouchHelper;
import com.miki.justincase_v1.Swipers.Suitcase_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowSuitcases extends BaseFragment implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Suitcase adapter;
    ArrayList<Suitcase> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;
    ItemTouchHelper.SimpleCallback simpleCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

        dataset = Presenter.selectAllSuitcase(getContext());

        if (!dataset.isEmpty()) {
           LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
            linearLayout.setVisibility(View.VISIBLE);
        }

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
        setHasOptionsMenu(true);

        floatingButton.setOnClickListener(v -> {
            createNewSuitcaseDialog();
        });


        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
        simpleCallback = new Suitcase_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Suitcase(dataset);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), 1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setListener(v -> {

            Suitcase suitcase = dataset.get(recyclerView.getChildAdapterPosition(v));
            editSuitcase(suitcase, view, v);
        });
        return view;
    }

    private void createNewSuitcaseDialog() {
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

        builder.setNegativeButton(getString(R.string.dialog_cancel), ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(getString(R.string.dialog_add), ((dialog, which) -> {
            String name = nameET.getText().toString();
            String color = colorET.getText().toString();
            double weigth = Double.parseDouble(weigthET.getText().toString());

            double heigth = Double.parseDouble(heigthET.getText().toString());
            double width = Double.parseDouble(widthET.getText().toString());
            double depth = Double.parseDouble(depthET.getText().toString());

            if (name.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_emptyName));
                createNewSuitcaseDialog();
            } else {
                Suitcase suitcase = new Suitcase(name, color, weigth, heigth, width, depth);
                boolean haveBeenAdded = Presenter.createSuitcase(suitcase, getContext());
                if (haveBeenAdded) {
                    makeToast(getContext(), getString(R.string.toast_suitcaseCreated));
                } else {
                    makeToast(getContext(), getString(R.string.toast_error));
                }
                getNav().navigate(R.id.fragment_ShowSuitcases);
            }
        }));
        builder.show();
    }

    private void editSuitcase(Suitcase suitcase, View w, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        String cancel = getString(R.string.dialog_cancel);
        String add = getString(R.string.dialog_add);
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

        nameET.setText(suitcase.getName());
        colorET.setText(suitcase.getColor());

        weigthET.setText(String.valueOf(suitcase.getWeight()));

        heigthET.setText(String.valueOf(suitcase.getHeigth()));
        widthET.setText(String.valueOf(suitcase.getWidth()));
        depthET.setText(String.valueOf(suitcase.getDepth()));

        builder.setNegativeButton(cancel, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(add, ((dialog, which) -> {
            String name = nameET.getText().toString();
            String color = colorET.getText().toString();
            double weigth = Double.parseDouble(weigthET.getText().toString());

            double heigth = Double.parseDouble(heigthET.getText().toString());
            double width = Double.parseDouble(widthET.getText().toString());
            double depth = Double.parseDouble(depthET.getText().toString());
            suitcase.setSuticase(name, color, weigth, heigth, width, depth);
            boolean haveBeenUpdated = Presenter.updateSuitcase(suitcase, getContext());
            if (haveBeenUpdated) {
                makeToast(v.getContext(), getString(R.string.toast_suitcaseUpdated));
            } else {
                makeToast(v.getContext(), getString(R.string.toast_error));
            }
            getNav().navigate(R.id.fragment_ShowSuitcases);
        }));
        builder.show();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Suitcase.AdapterViewHolder) {
            int deletedIndex = viewHolder.getAdapterPosition();
//            String name = dataset.get(deletedIndex).getSuitcaseName();
            Suitcase suitcase = dataset.get(deletedIndex);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String title = getString(R.string.dialog_title_warning);
            builder.setTitle(title);

            builder.setCancelable(true);


            String text = getString(R.string.dialog_warning_deleteSuitcase);
            TextView textView = new TextView(getContext());
            textView.setText(text);

            builder.setView(textView);
            builder.setNegativeButton(getString(R.string.dialog_no), ((dialog, which) -> {
                dialog.dismiss();
                getNav().navigate(R.id.fragment_ShowSuitcases);
            }));
            builder.setPositiveButton(getString(R.string.dialog_yes), ((dialog, which) -> {
                adapter.removeItem(viewHolder.getAdapterPosition());

//            restoreDeletedElement(viewHolder, name, suitcase, deletedIndex);
                //Note: if the item it's deleted and then restore, only restore in item. You must
                //yo add again in Baggages and Categorys.
                Presenter.deleteSuitcase(suitcase, getContext());
                getNav().navigate(R.id.fragment_ShowSuitcases);
            }));
            builder.show();
        }

    }


    public void restoreDeletedElement(RecyclerView.ViewHolder viewHolder, String name, Suitcase deletedItem, int deletedIndex) {
        String deleted = getString(R.string.toast_hasBeenDeleted);
        String restore = getString(R.string.snackbar_restore);
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
