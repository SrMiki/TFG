package com.miki.justincase_v1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Category;

public class Fragment_CreateCategory extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment_ItemManager fragment_itemManager;

    AppDatabase db;
    EditText nameCategoryTV;

    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_category, container, false);

        nameCategoryTV = view.findViewById(R.id.fragment_createCategory_input_CategoryName);

        btn = view.findViewById(R.id.fragment_createCategory_btn_add);

        btn.setOnClickListener(v -> {

            String nameCategory = nameCategoryTV.getText().toString();

            //If()
            db = AppDatabase.getInstance(getActivity());

            Category category = new Category(nameCategory);
            db.categoryDAO().addANewCategory(category);

            closeKeyboard();
            fragmentTransaction();
        });
        return view;
    }

    private void fragmentTransaction() {
        //aqui va toda la logica para el paso de datos entre fragments
        fragment_itemManager = new Fragment_ItemManager();
        //Bundle para transportar la informacion
        Bundle bundle = new Bundle();
        //enviamos el objeto (en serial)
        bundle.putSerializable("showCategory", true);
        fragment_itemManager.setArguments(bundle);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragment_itemManager);
        fragmentTransaction.commit();
    }

    private void closeKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
