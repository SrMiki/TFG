package com.miki.justincase_v1.fragments.Create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Show.Fragment_ItemManager;

public class Fragment_CreateItem extends BaseFragment {

    EditText nameItemTV;

    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_item, container, false);

        nameItemTV = view.findViewById(R.id.fragment_createItem_input_ItemName);

        btn = view.findViewById(R.id.fragment_createItem_btn_add);
        btn.setOnClickListener(v -> {

            String itemName = nameItemTV.getText().toString();

            Presented.createItem(itemName, view);

            closeKeyBoard();
           getNav().navigate(R.id.fragment_ItemManager);
        });
        return view;
    }
}