package com.miki.justincase_v1.fragments;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;

public class Fragment_CreateItem extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    AppDatabase db;
    EditText CAMPOnombre;

    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_item, container, false);

        CAMPOnombre = view.findViewById(R.id.fragment_createItem_input_ItemName);

        btn = view.findViewById(R.id.fragment_createItem_btn_add);

        btn.setOnClickListener(view1 -> {

            String nombreItem = CAMPOnombre.getText().toString();

            if (nombreItem == null | nombreItem.isEmpty()) {
                Toast.makeText(getActivity(), "error al crear el nuevo viaje", Toast.LENGTH_SHORT).show();
            }

            Item item = new Item(nombreItem);
            db = AppDatabase.getInstance(getActivity());
            db.itemDAO().addItem(item);


            //Cerrar el teclado al pulsar
            InputMethodManager inputManager =
                    (InputMethodManager) getContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ItemManager());
            fragmentTransaction.commit();
        });
        return view;
    }

}
