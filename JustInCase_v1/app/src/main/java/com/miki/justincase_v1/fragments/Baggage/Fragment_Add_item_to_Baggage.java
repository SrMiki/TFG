package com.miki.justincase_v1.fragments.Baggage;

import android.app.AlertDialog;
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
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_ItemSeleccionados;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_item_to_Baggage extends BaseFragment {

    Adapter_ItemSeleccionados adapter;
    RecyclerView recyclerView;
    ArrayList<Item> dataset;

    Bundle bundle;

    TextView suitcaseNameTV;

    FloatingActionButton floatingActionButton;
    ArrayList<Item> arrayList;

    HandLuggage handLuggage;

    Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_baggage, container, false);
        suitcaseNameTV = view.findViewById(R.id.suitcaseNameTV);


        bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();

            handLuggage = (HandLuggage) bundle.getSerializable("handluggage");
            suitcaseNameTV.setText(handLuggage.getHandLuggageName());

            dataset = Presented.selectItemNOTFromThisBaggage(handLuggage, getContext());
            if (dataset.isEmpty()) {
                noItemsDialog();
            } else {
                Presented.removeItemSelectedState(dataset, getContext());

                button = view.findViewById(R.id.fragment_Add_Item_To_Baggage_btn_add);
                button.setOnClickListener(v -> {
                    createNewItemDialog();
                });


                recyclerView = view.findViewById(R.id.fragment_Add_Item_To_Baggage_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new Adapter_ItemSeleccionados(dataset);
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
                    Presented.updateItemState(item, getContext());
                    adapter.notifyItemChanged(position);
                });

                floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Baggage_finish);
                floatingActionButton.setOnClickListener(v -> {
                    Presented.createBaggageByItems(arrayList, handLuggage, getContext());
                    Presented.removeItemSelectedState(arrayList, getContext());

//                    Bundle obundle = new Bundle();
//                    obundle.putSerializable("handluggage", handLuggage);
                    getNav().navigate(R.id.fragment_ShowBaggageByItem, bundle);

                });
            }
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
            getNav().navigate(R.id.fragment_ShowBaggageByItem, bundle);
        }));


        builder.setPositiveButton(getString(R.string.text_yes), ((dialog, which) -> {
            createNewItemDialog();
        }));
        builder.show();
    }

    private void createNewItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

//        openKeyBoard();

        builder.setTitle(getString(R.string.text_newItem));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);
        builder.setView(view);

        EditText editText = view.findViewById(R.id.alertdialog_editText);
        editText.setHint(getString(R.string.fragment_createItem_hint));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.text_cancel), ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.fragment_ShowBaggageByItem, bundle);
        }));

        builder.setPositiveButton(getString(R.string.text_create), ((dialog, which) -> {
            String itemName = editText.getText().toString();
            boolean create = Presented.createItem(itemName, getContext());
            if (create) {
                getNav().navigate(R.id.fragment_Add_item_to_Baggage, bundle);
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

