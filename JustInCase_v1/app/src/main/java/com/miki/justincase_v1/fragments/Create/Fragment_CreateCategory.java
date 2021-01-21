package com.miki.justincase_v1.fragments.Create;

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
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Fragment_ItemManager;

public class Fragment_CreateCategory extends BaseFragment {

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

            closeKeyBoard();

            Bundle bundle = new Bundle();
            bundle.putSerializable("showCategory", true);
           doFragmentTransactionWithBundle(new Fragment_ItemManager(), bundle);
        });
        return view;
    }
}
