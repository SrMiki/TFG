package com.miki.justincase_v1.fragments.Show;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_list_of_Items_to_select;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_Item_To_Category extends BaseFragment {

    Adapter_list_of_Items_to_select adapter;
    RecyclerView recyclerView;
    ArrayList<Item> dataset;

    FloatingActionButton floatingActionButton;
    ArrayList<Item> arrayList;

    TextView title;

    Category category;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_category, container, false);

        bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();

            category = (Category) bundle.getSerializable("category");

            title = view.findViewById(R.id.Name);
            title.setText(category.getCategoryName());

            dataset = Presenter.selectItemWhitNoCategory(getContext());
            if (dataset.isEmpty()) {
                noItemsDialog();
            }
            Presenter.removeItemSelectedState(dataset, getContext());

            recyclerView = view.findViewById(R.id.fragment_Add_Item_To_Category_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_list_of_Items_to_select(dataset);
            recyclerView.setAdapter(adapter);

            floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Category_finish);
            floatingActionButton.setVisibility(View.GONE);

            adapter.setListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                Item item = dataset.get(position);

                if (!arrayList.contains(item)) {
                    arrayList.add(item);
                    item.setSelectedState(true);
                } else {
                    arrayList.remove(item);
                    item.setSelectedState(false);
                }
                Presenter.updateItemState(item, getContext());

                adapter.notifyItemChanged(position);
                if (arrayList.isEmpty()) {
                    floatingActionButton.setVisibility(View.GONE);
                } else {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }

            });


            floatingActionButton.setOnClickListener(v -> {
                Presenter.addItemToThisCategory(arrayList, category, getContext());
                Presenter.removeItemSelectedState(arrayList, getContext());
                getNav().navigate(R.id.action_fragment_Add_Item_To_Category_to_fragment_ShowCategories, bundle);
            });

        }
        return view;
    }

    private void noItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_itemTextview);
        dialogTitle.setText(getString(R.string.dialog_warning));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_ask_createNewItem));

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_no), ((dialog, which) -> {
            getNav().navigate(R.id.fragment_ShowCategories);
        }));


        builder.setPositiveButton(getString(R.string.dialog_button_yes), ((dialog, which) -> {
            createNewItemDialog();
        }));
        builder.show();
    }

    private void createNewItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(R.string.dialog_title_newItem);

        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setHint(getString(R.string.hint_itemName));

        builder.setView(view);
        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_create), ((dialog, which) -> {
            String itemName = editText.getText().toString();
            boolean create = Presenter.createItem(itemName, getContext());
            dataset = Presenter.selectItemWhitNoCategory(getContext());
            if (create) {
                adapter.notifyItemInserted(dataset.size()-1);
                anotherItemDialog();
            } else {
                makeToast(getContext(), getString(R.string.toast_warning_item));
                dialog.dismiss();
                createNewItemDialog();
            }
        }));
        builder.show();
    }

    private void anotherItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);


        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(R.string.text_item);


        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_ask_anotherItem));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_yes), ((dialog, which) -> {
            createNewItemDialog();
        }));
        builder.show();
    }
}