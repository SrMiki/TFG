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

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Fragment_ItemManager;

public class Fragment_FocusItem extends BaseFragment {

    TextView itemNameTV;
    Button btn_detalleItem_eliminar, btn_detalleItem_edit;
    AppDatabase db;

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

            btn_detalleItem_eliminar = view.findViewById(R.id.fragment_focusItem_btn_delete);
            btn_detalleItem_eliminar.setOnClickListener(view1 -> {
                db = AppDatabase.getInstance(view.getContext());
                db.itemDAO().delete(item);

               doFragmentTransaction(new Fragment_ItemManager());
            });
            btn_detalleItem_edit = view.findViewById(R.id.fragment_focusItem_btn_edit);
            /*btn_detalleItem_edit.setOnClickListener(view1 -> {
                db = AppDatabase.getInstance(view.getContext());

                db.itemDAO().updateItem(item);

                bindingItemfocusItem.sendItem(item);
            });*/


        }
        return view;
    }


    //Comunicacion entre el fragment Viaje y Detalle del viaje
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
