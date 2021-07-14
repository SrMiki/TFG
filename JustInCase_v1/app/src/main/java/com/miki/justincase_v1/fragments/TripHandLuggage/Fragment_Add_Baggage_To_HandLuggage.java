package com.miki.justincase_v1.fragments.TripHandLuggage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_list_of_Categories_to_select;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_list_of_Items_to_select;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_Baggage_To_HandLuggage extends BaseFragment {

    Adapter_list_of_Items_to_select adapter;
    RecyclerView recyclerView;
    ArrayList<Item> dataset;

    SharedPreferences sp;

    Bundle bundle;

    TextView suitcaseNameTV;

    FloatingActionButton floatingActionButton;
    ArrayList<Item> arrayList;
    ArrayList<Category> arrayList_Category;


    HandLuggage handLuggage;

    Button button;
    private ArrayList<Category> datasetCategory;
    private Adapter_list_of_Categories_to_select adapterCategory;
    private Category focusCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_baggage, container, false);
        suitcaseNameTV = view.findViewById(R.id.suitcaseNameTV);

        floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Baggage_finish);
        floatingActionButton.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.fragment_Add_Item_To_Baggage_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();

            handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");
            suitcaseNameTV.setText(handLuggage.getHandLuggageName());

            sp = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            boolean showCategories = sp.getBoolean("showCategories", false);

            if (showCategories) {
                showByCategory(view);
            } else {
                showByItem(view);
            }
        }
        return view;
    }

    private void showByItem(View view) {
        dataset = Presenter.selectItemNOTFromThisBaggage(handLuggage, getContext());
        adapter = new Adapter_list_of_Items_to_select(dataset);
        recyclerView.setAdapter(adapter);

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

        if (dataset.isEmpty()) {
            noItemsDialog();
        } else {
            Presenter.removeItemSelectedState(dataset, getContext());

            button = view.findViewById(R.id.fragment_Add_Item_To_Baggage_btn_add);
            button.setOnClickListener(v -> {
                createNewItemDialog();
            });


            floatingActionButton.setOnClickListener(v -> {
                Presenter.createBaggageByItems(arrayList, handLuggage, getContext());
                Presenter.removeItemSelectedState(arrayList, getContext());
                Bundle obundle = new Bundle();
                obundle.putSerializable("handluggage", handLuggage);
                getNav().navigate(R.id.action_fragment_Add_item_to_Baggage_to_fragment_ShowBaggageByItem, bundle);

            });
        }
    }

    private void showByCategory(View view) {
        datasetCategory = Presenter.selectCategoriesNOTFromThisBaggage(handLuggage, getContext());

        arrayList_Category = new ArrayList<>();


        adapterCategory = new Adapter_list_of_Categories_to_select(datasetCategory, getActivity(), handLuggage);
        recyclerView.setAdapter(adapterCategory);

        adapterCategory.setOnClickListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            focusCategory = datasetCategory.get(position);
            adapterCategory.setSelectedState(!adapterCategory.getSelectedState());
            adapterCategory.notifyItemChanged(position);
            if (arrayList_Category.contains(focusCategory)) {
                arrayList_Category.remove(focusCategory);
                adapterCategory.setSelectedState(false);

            } else {
                arrayList_Category.add(focusCategory);
                adapterCategory.setSelectedState(true);
                adapterCategory.notifyItemChanged(position);
            }

            adapterCategory.notifyItemChanged(position);
            if (arrayList_Category.isEmpty()) {
                floatingActionButton.setVisibility(View.GONE);
            } else {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });

        floatingActionButton.setOnClickListener(v -> {
            for (Category category : arrayList_Category) {
                arrayList.addAll(Presenter.selectItemFromThisCategory(category, getContext()));
            }
            Presenter.createBaggageByItems(arrayList, handLuggage, getContext());
//            Presenter.removeItemSelectedState(arrayList, getContext());
            Bundle obundle = new Bundle();
            obundle.putSerializable("handluggage", handLuggage);
            getNav().navigate(R.id.action_fragment_Add_item_to_Baggage_to_fragment_ShowBaggageByItem, bundle);

        });
    }

    private void noItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(getString(R.string.dialog_title_warning));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_ask_createNewItem));

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_notNow), ((dialog, which) -> {
            getNav().navigate(R.id.action_fragment_Add_item_to_Baggage_to_fragment_ShowBaggageByItem, bundle);
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
        dialogTitle.setText(getString(R.string.dialog_title_newItem));

        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setHint(getString(R.string.hint_itemName));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.fragment_ShowBaggage, bundle);
        }));

        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String itemName = editText.getText().toString();
            if (!Presenter.createItem(itemName, getContext())) {
                makeToast(getContext(), getString(R.string.toast_warning_item));
            } else {
                dialog.dismiss();
                adapter.notifyItemInserted(dataset.size()-1);
                anotherItemDialog();
            }
        });
    }

    private void anotherItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(getString(R.string.dialog_title_newItem));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_ask_anotherItem));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_yes), ((dialog, which) -> {
            dialog.dismiss();
            createNewItemDialog();
        }));
        builder.show();
    }
}