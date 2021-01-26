package com.miki.justincase_v1.fragments.Edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Show.Fragment_ItemManager;

public class Fragment_Edit_Item extends BaseFragment {

    EditText nameItemTV;

    Button btn;
    Item item;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_item, container, false);

        nameItemTV = view.findViewById(R.id.fragment_createItem_input_ItemName);

        Bundle bundle = getArguments();

        if (bundle != null) {
            item = (Item) bundle.getSerializable("ThisItem");

            nameItemTV.setText(item.getItemName());

            btn = view.findViewById(R.id.fragment_createItem_btn_add);
            btn.setOnClickListener(v -> {

                String nombreItem = nameItemTV.getText().toString();
                Presented.updateItem(item, nombreItem, view);

                closeKeyBoard();
                Bundle obundle = new Bundle();
                obundle.putSerializable("item", item);
                getNav().navigate(R.id.fragment_FocusItem, obundle);
            });
        }
        return view;
    }
}
