package com.miki.justincase_v1.fragments.TripHandLuggage;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Template;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Template;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Use_Templates_For_HandLuggage extends BaseFragment {

    Adapter_Template adapter;
    ArrayList<Template> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;
    Template focusTemplate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_category, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {

            dataset = Presenter.selectAllTemplates(getContext());

//            if (!dataset.isEmpty()) {
//                LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
//                linearLayout.setVisibility(View.VISIBLE);
//                setHasOptionsMenu(true);
//            }


            recyclerView = view.findViewById(R.id.fragment_Add_Item_To_Category_recyclerview);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Template(dataset, getActivity());
            recyclerView.setAdapter(adapter);

//        floatingButton = view.findViewById(R.id.fragment_Add_Item_To_Category_finish);
//
//        floatingButton.setOnClickListener(v ->
//                );

            adapter.setOnClickListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                focusTemplate = dataset.get(position);
                ArrayList<Item> arrayList = Presenter.selectItemFromThisTemplate(focusTemplate, getContext());

                HandLuggage handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");

                Presenter.createBaggageByItems(arrayList, handLuggage, getContext());
//                getNav().navigate(R.id.action_fragment_Use_Templates_For_HandLuggage_to_fragment_ShowBaggageByItem, bundle);
            });

        }

        return view;
    }


    private void newTemplateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.text_newTemplate));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);
        builder.setView(view);

        EditText editText = view.findViewById(R.id.alertdialog_viewEditText);
        editText.setHint(getString(R.string.fragment_createTemplate_hint));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_create), ((dialog, which) -> {
            if (editText.getText().toString().isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_emptyName));
                newTemplateDialog();
            } else {
                String name = editText.getText().toString();
                boolean category = Presenter.createTemplate(name, getContext());
                if (category) {
                    makeToast(getContext(), getString(R.string.text_templateCreated));

                    addItemsDialog();

                    getNav().navigate(R.id.fragment_ShowTemplates);
                } else {
                    makeToast(getContext(), getString(R.string.toast_error_TemplateExisted));
                }
            }
        }));
        builder.show();
    }

    private void addItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_title_newCategory));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(getString(R.string.dialog_ask_addItemsToCategory));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_yes), ((dialog, which) -> {
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

        SearchView searchView = (SearchView) search.getActionView();

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

    private void deleteItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(getString(R.string.dialog_ask_deleteItemsToo));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_no), ((dialog, which) -> {
            dialog.dismiss();
            Presenter.deleteTemplate(focusTemplate, getContext());
            getNav().navigate(R.id.fragment_ShowCategories);
        }));

//        builder.setPositiveButton(getString(R.string.text_yes), ((dialog, which) -> {
//            Presenter.deleteItemFromTrips(focusTemplate, getContext());
//            Presenter.deleteItemOfThisCategory(focusTemplate, getContext());
//            Presenter.deleteCategory(focusTemplate, getContext());
//            getNav().navigate(R.id.fragment_ShowCategories);
//        }));
        builder.show();

    }
}
