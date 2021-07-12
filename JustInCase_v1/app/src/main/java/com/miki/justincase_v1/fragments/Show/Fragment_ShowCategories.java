package com.miki.justincase_v1.fragments.Show;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Category;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowCategories extends BaseFragment {

    Adapter_Category adapter;
    ArrayList<Category> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;
    Category focusCategory;

    private boolean showOptionMenu = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

        dataset = Presenter.selectAllCategories(getContext());

        if (!dataset.isEmpty()) {
            LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
            linearLayout.setVisibility(View.VISIBLE);

            setHasOptionsMenu(true);

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Category(dataset, getActivity());
            recyclerView.setAdapter(adapter);

            adapter.setOnClickListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                focusCategory = dataset.get(position);
                updateFocusCategory(position);
            });

            Bundle bundle = getArguments();
            if (bundle != null) {
                if (bundle.getSerializable("categoryPosition") != null) {
                    int categoryPosition = (int) bundle.getSerializable("categoryPosition");
                    adapter.setCardSelected(categoryPosition);
                }
            }
        }

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);

        floatingButton.setOnClickListener(v -> newCategoryDialog());
        return view;
    }

    private void updateFocusCategory(int position) {
        if (!adapter.isSelected()) {
            changeFocusState(position);
        } else if (adapter.getCardSelected() == position) {
            changeFocusState(-1);// all closed
        } else {
            adapter.setCardSelected(position);
        }
    }

    private void changeFocusState(int position) {
        adapter.setCardSelected(position);
        showOptionMenu = !showOptionMenu;
        adapter.setSelectedState(showOptionMenu);
    }

    private void newCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);
        builder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(getString(R.string.dialog_title_newCategory));

        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setHint(getString(R.string.hint_createCategory));

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            if (editText.getText().toString().isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                String name = editText.getText().toString();
                if (!Presenter.createCategory(name, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_category));
                } else {
//                    makeToast(getContext(), getString(R.string.toast_created_category));
                    dialog.dismiss();
                    addItemsDialog();
                }
            }
        });
    }

    private void addItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(getString(R.string.dialog_title_addCategoryContent));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_ask_addItemsToCategory));

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_yes), ((dialog, which) -> {
            ArrayList<Category> allCategories = Presenter.getAllCategories(getContext());
            Category category = allCategories.get(allCategories.size() - 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable("category", category);
            getNav().navigate(R.id.fragment_Add_Item_To_Category, bundle);
        }));
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
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
}
