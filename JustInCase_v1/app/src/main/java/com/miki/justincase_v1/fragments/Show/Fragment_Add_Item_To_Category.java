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
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_ItemSeleccionados;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_Item_To_Category extends BaseFragment {

    Adapter_ItemSeleccionados adapter;
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
        View view = inflater.inflate(R.layout.fragment_add_categorycontent, container, false);

        bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();

            category = (Category) bundle.getSerializable("category");

            title = view.findViewById(R.id.Category_name);
            title.setText(category.getCategoryName());

            dataset = Presented.selectItemWhitNoCategory(getContext());
            if (dataset.isEmpty()) {
                noItemsDialog();
            }

            recyclerView = view.findViewById(R.id.fragment_Add_Item_To_Category_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_ItemSeleccionados(dataset);
            recyclerView.setAdapter(adapter);

            floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Category_finish);
            floatingActionButton.setOnClickListener(v -> {
                Presented.addItemToThisCategory(arrayList, category, getContext());
                getNav().navigate(R.id.fragment_ShowCategoryContent, bundle);
            });

            floatingActionButton.setVisibility(View.GONE);
            adapter.setListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                Item item = dataset.get(position);

                if (!arrayList.contains(item)) {
                    arrayList.add(item);
                    adapter.setSelectedState(true);
                    floatingActionButton.setVisibility(View.VISIBLE);
                } else {
                    arrayList.remove(item);
                    adapter.setSelectedState(false);
                }
                adapter.notifyItemChanged(position);
                if (arrayList.isEmpty()) {
                    floatingActionButton.setVisibility(View.GONE);
                }

            });


        }
        return view;
    }

    private void noItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.text_warning));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(getString(R.string.ask_createNewItem));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.text_no), ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.fragment_ShowCategories);
        }));


        builder.setPositiveButton(getString(R.string.text_yes), ((dialog, which) -> {
            createNewItemDialog();
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

        builder.setNegativeButton(getString(R.string.text_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_create), ((dialog, which) -> {
            String itemName = editText.getText().toString();
            boolean create = Presented.createItem(itemName, getContext());
            if (create) {
                getNav().navigate(R.id.fragment_Add_Item_To_Category, bundle);
                anotherItemDialog();
            } else {
                makeToast(getContext(), getString(R.string.warningItemRepeted));
                dialog.dismiss();
                createNewItemDialog();
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
}