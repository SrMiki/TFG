package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.bindings.Binding_Item_focusItem;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;

public class Fragment_FocusItem extends Fragment {

    TextView CAMPOItemDnombre;
    Button btn_detalleItem_eliminar, btn_detalleItem_edit;
    AppDatabase db;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Binding_Item_focusItem bindingItemfocusItem;
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_item, container, false);

        CAMPOItemDnombre = view.findViewById(R.id.fragment_focusItem_itemName);

        Bundle bundle = getArguments();
        Item item;

        if (bundle != null) {
            item = (Item) bundle.getSerializable("item");

            CAMPOItemDnombre.setText(item.getItemName());

            btn_detalleItem_eliminar = view.findViewById(R.id.fragment_focusItem_btn_delete);
            btn_detalleItem_eliminar.setOnClickListener(view1 -> {
                db = AppDatabase.getInstance(view.getContext());
                db.itemDAO().delete(item);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ItemManager());
                fragmentTransaction.commit();
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
            bindingItemfocusItem = (Binding_Item_focusItem) this.activity;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
