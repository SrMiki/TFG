package com.miki.justincase_v1.fragments.Show;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Category;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowCategories extends BaseFragment {

    Adapter_Category adapter;
    ArrayList<Category> dataset;
    RecyclerView recyclerView;

    Button btnShowItems;

    FloatingActionButton floatingButton;
    Category focusCategory;

    private boolean showOptionMenu = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_category, container, false);

        dataset = Presented.getAllCategories(view);

        setHasOptionsMenu(true);

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
        floatingButton.setOnClickListener(v -> newCategory(view, v));

        btnShowItems = view.findViewById(R.id.fragment_btn_items);
        btnShowItems.setOnClickListener(v -> getNav().navigate(R.id.fragment_ShowItems));


        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Category(dataset);
        recyclerView.setAdapter(adapter);

        adapter.setListener(v -> {
            focusCategory = dataset.get(recyclerView.getChildAdapterPosition(v));
            adapter.setCardSelected(recyclerView.getChildAdapterPosition(v));
            if (!adapter.isSelected()) {
                getActivity().invalidateOptionsMenu();
                showOptionMenu = !showOptionMenu;
                adapter.selected(true);
            }
            if (adapter.isNoMre()) {
                getActivity().invalidateOptionsMenu();
                showOptionMenu = !showOptionMenu;
                adapter.selected(false);
            }

        });
        return view;
    }

    private void newCategory(View view, View v) {
        AlertDialog.Builder builder = getCreateBuilder(getString(R.string.text_category));

        EditText editText = new EditText(getContext());
        builder.setView(editText);

        String add = getString(R.string.text_add);

        builder.setPositiveButton(add, ((dialog, which) -> {
            String name = editText.getText().toString();
            Presented.createCategory(name, view);
            makeToast(v.getContext(), getString(R.string.text_haveBeenAdded));
            getNav().navigate(R.id.fragment_ShowCategories);
        }));
        builder.show();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) search.getActionView();

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

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem menuItem;
        menuItem = menu.findItem(R.id.action_addItem);
        menuItem.setVisible(showOptionMenu);

        menuItem = menu.findItem(R.id.action_edit);
        menuItem.setVisible(showOptionMenu);

        menuItem = menu.findItem(R.id.action_delete);
        menuItem.setVisible(showOptionMenu);
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteThisCategory();
                return true;
            case R.id.action_edit:
                editThisCategory();
                return true;
            case R.id.action_addItem:
                addItems();
                return true;
            default:
                return true;
//                return super.onOptionsItemSelected(item);
        }

    }

    private void addItems() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ThisCategory", focusCategory);
        getNav().navigate(R.id.fragment_Add_Item_To_Category, bundle);
    }

    private void editThisCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String title = getString(R.string.text_edit);
        builder.setTitle(title);
        builder.setCancelable(true);
        EditText editText = new EditText(getContext());
        editText.setText(focusCategory.getCategoryName());
        builder.setView(editText);
        String haveBeenAdded = getString(R.string.text_haveBeenUpdated);
        String NegativeButton = getString(R.string.text_cancel);
        String positiveButton = getString(R.string.text_edit);
        builder.setNegativeButton(NegativeButton, ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.setPositiveButton(positiveButton, ((dialog, which) -> {
            String itemName = editText.getText().toString();
            Presented.updateCategory(focusCategory, itemName, getView());
//            makeToast(v.getContext(), haveBeenAdded);
            getActivity().invalidateOptionsMenu();
            showOptionMenu = !showOptionMenu;
            getNav().navigate(R.id.fragment_ShowCategories);
        }));
        builder.show();
    }

    private void deleteThisCategory() {
        Presented.deleteCategory(focusCategory, getView());
        focusCategory = null;
        getNav().navigate(R.id.fragment_ShowCategories);
    }


}
