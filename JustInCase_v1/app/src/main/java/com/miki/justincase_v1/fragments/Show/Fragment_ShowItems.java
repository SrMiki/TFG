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
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.adapters.Item_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowItems extends BaseFragment implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Item adapter;
    ArrayList<Item> dataset;
    RecyclerView recyclerView;

    Button btnShowCategories;

    FloatingActionButton floatingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_item, container, false);


        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);

        dataset = Presented.getAllItems(view);
        setHasOptionsMenu(true); //Pido que se muestre el menu

        floatingButton.setOnClickListener(v -> {
            createNewItemDialog(view, v);
        });

        btnShowCategories = view.findViewById(R.id.fragment_btn_categories);
        btnShowCategories.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_ShowCategories);
        });

        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
        ItemTouchHelper.SimpleCallback simpleCallback = new Item_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
//        simpleCallback = new Item_RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Item(dataset);
        recyclerView.setAdapter(adapter);

        adapter.setListener(v -> {
            Item item = dataset.get(recyclerView.getChildAdapterPosition(v));
            editItemDialog(view, v, item);
        });

        return view;
    }

    private void editItemDialog(View view, View v, Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String title = getString(R.string.text_edit);
        builder.setTitle(title);
        builder.setCancelable(true);
        EditText editText = new EditText(getContext());
        editText.setText(item.getItemName());
        builder.setView(editText);
        String haveBeenAdded = getString(R.string.text_haveBeenUpdated);
        String NegativeButton = getString(R.string.text_cancel);
        String positiveButton = getString(R.string.text_edit);
        builder.setNegativeButton(NegativeButton, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(positiveButton, ((dialog, which) -> {
            String itemName = editText.getText().toString();
            Presented.updateItem(item, itemName, view);
            makeToast(v.getContext(), haveBeenAdded);
            getNav().navigate(R.id.fragment_ShowItems);
        }));
        builder.show();
    }

    private void createNewItemDialog(View view, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String title = getString(R.string.text_Item);
        builder.setTitle(title);
        builder.setCancelable(true);
        EditText editText = new EditText(getContext());
        editText.setHint(getString(R.string.fragment_createItem_hint));
        builder.setView(editText);
        String cancel = getString(R.string.text_cancel);
        String haveBeenAdded = getString(R.string.text_haveBeenAdded);
        String add = getString(R.string.text_add);
        builder.setNegativeButton(cancel, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(add, ((dialog, which) -> {
            String itemName = editText.getText().toString();
            Presented.createItem(itemName, view);
            makeToast(v.getContext(), haveBeenAdded);
            getNav().navigate(R.id.fragment_ShowItems);
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
            Presented.deleteItem(deletedItem, viewHolder.itemView);
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
            Presented.createItem(deletedItem.getItemName(), viewHolder.itemView);
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
        super.onCreateOptionsMenu(menu, inflater);
    }
}
