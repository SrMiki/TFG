package com.miki.justincase_v1.fragments.Create;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Fragment_ItemManager;

public class Fragment_CreateItem extends BaseFragment {

    AppDatabase db;
    EditText nameItemTV;

    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_item, container, false);

        nameItemTV = view.findViewById(R.id.fragment_createItem_input_ItemName);

        btn = view.findViewById(R.id.fragment_createItem_btn_add);

        btn.setOnClickListener(v -> {

            String nombreItem = nameItemTV.getText().toString();

            if (nombreItem == null | nombreItem.isEmpty()) {
                Toast.makeText(getActivity(), "error al crear el nuevo viaje", Toast.LENGTH_SHORT).show();
            }

            Item item = new Item(nombreItem);
            db = AppDatabase.getInstance(getActivity());
            db.itemDAO().addItem(item);


            closeKeyBoard();
            doFragmentTransaction(new Fragment_ItemManager());
        });
        return view;
    }

}
