package com.miki.justincase_v1.fragments.Focus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Edit.Fragment_Edit_Item;
import com.miki.justincase_v1.fragments.Show.Fragment_ItemManager;

public class Fragment_FocusItem extends BaseFragment {

    TextView itemNameTV;
    Button btn_focusItem_delete, btn_focusItem_edit;

    Binding_Entity_focusEntity bindingItemfocusItem;
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_item, container, false);

        itemNameTV = view.findViewById(R.id.fragment_focusItem_itemName);

        Bundle bundle = getArguments();
        Item item;

        if (bundle != null) {
            item = (Item) bundle.getSerializable("item");

            itemNameTV.setText(item.getItemName());

            btn_focusItem_delete = view.findViewById(R.id.fragment_focusItem_btn_delete);
            btn_focusItem_delete.setOnClickListener(v -> {
                Presented.deleteItem(item, view);
                getNav().navigate(R.id.fragment_ShowItems);
            });
            btn_focusItem_edit = view.findViewById(R.id.fragment_focusItem_btn_edit);
            btn_focusItem_edit.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisItem", item);
                getNav().navigate(R.id.fragment_Edit_Item, obundle);
            });
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingItemfocusItem = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
