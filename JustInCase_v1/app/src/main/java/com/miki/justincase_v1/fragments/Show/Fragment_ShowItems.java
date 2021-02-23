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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.Swipers.Item_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowItems extends BaseFragment implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Item adapter;
    ArrayList<Item> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);


        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);

        dataset = Presented.selectAllItems(getContext());
        setHasOptionsMenu(true);

        floatingButton.setOnClickListener(v -> {
            createNewItemDialog();
        });

        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
        ItemTouchHelper.SimpleCallback simpleCallback = new Item_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Item(dataset, getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setListener((View v) -> {
            Item item = dataset.get(recyclerView.getChildAdapterPosition(v));
            editItemDialog(v, item);
            return true;
        });


        return view;
    }

    private void editItemDialog(View v, Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.text_editItem));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        EditText editText = view.findViewById(R.id.alertdialog_editText);
        editText.setText(item.getItemName());

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.text_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_edit), ((dialog, which) -> {
            String itemName = editText.getText().toString();

            if (itemName.isEmpty()) {
                makeToast(v.getContext(), getString(R.string.warning_emptyName));
            } else {
                boolean update = Presented.updateItem(item, itemName, getContext());
                if (update) {
                    makeToast(v.getContext(), getString(R.string.text_haveBeenUpdated));
                    getNav().navigate(R.id.fragment_ShowItems);
                } else {
                    makeToast(v.getContext(), getString(R.string.warning_updateItem));
                }
            }
        }));
        builder.show();
    }

    private void createNewItemDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getString(R.string.text_newItem));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);
        builder.setView(view);

        EditText editText = view.findViewById(R.id.alertdialog_editText);
        editText.setHint(getString(R.string.fragment_createItem_hint));
        builder.setView(view);
        editText.setFocusable(true);
//        openKeyBoard();

        builder.setNegativeButton(getString(R.string.text_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_add), ((dialog, which) -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.warning_emptyName));
            } else {
                boolean create = Presented.createItem(itemName, getContext());
                if (!create) {
                    makeToast(getContext(), getString(R.string.warning_createItem));
                    getNav().navigate(R.id.fragment_ShowItems);
                } else {
                    makeToast(getContext(), getString(R.string.text_itemCreated));
                    anotherItemDialog();
                    getNav().navigate(R.id.fragment_ShowItems);
                }
            }
        }));
        builder.show();
    }

    private void anotherItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.text_Item));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(getString(R.string.text_ask_anotherItem));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.text_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_yes), ((dialog, which) -> {
            createNewItemDialog();
        }));
        builder.show();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Item.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            String name = dataset.get(deletedIndex).getItemName();
            Item deletedItem = dataset.get(deletedIndex);

            adapter.removeItem(viewHolder.getAdapterPosition());

            restoreDeletedElement(viewHolder, name, deletedItem, deletedIndex);
            //Note: if the item it's deleted and then restore, only restore in item. You must
            //yo add again in Baggages and Categorys.
            Presented.deleteItem(deletedItem, getContext());
            getNav().navigate(R.id.fragment_ShowItems);

        }
    }


    public void restoreDeletedElement(RecyclerView.ViewHolder viewHolder, String name, Item deletedItem, int deletedIndex) {
        String deleted = getString(R.string.text_hasBeenDeleted);
        String restore = getString(R.string.text_restore);
        Snackbar snackbar = Snackbar.make(((Adapter_Item.AdapterViewHolder) viewHolder).layout,
                name + " " + deleted,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(restore, v -> {
            adapter.restoreItem(deletedItem, deletedIndex);
            Presented.createItem(deletedItem.getItemName(), getContext());
            getNav().navigate(R.id.fragment_ShowItems);
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
            //true if the query has been handled by the listener, false to let the SearchView perform the default action.
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //false if the SearchView should perform the default action of showing any suggestions if available, true if the action was handled by the listener.
            @Override
            public boolean onQueryTextChange(String newText) {
//                floatingButton.setVisibility(View.GONE);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                floatingButton.setVisibility(View.VISIBLE);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }
}
