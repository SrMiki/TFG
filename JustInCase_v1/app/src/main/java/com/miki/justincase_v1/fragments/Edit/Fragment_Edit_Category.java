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
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Show.Fragment_ItemManager;

public class Fragment_Edit_Category extends BaseFragment {

    EditText nameCategoryTV;

    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_category, container, false);

        nameCategoryTV = view.findViewById(R.id.fragment_createCategory_input_CategoryName);

        btn = view.findViewById(R.id.fragment_createCategory_btn_add);

        Bundle bundle = getArguments();

        if (bundle != null) {

            Category category = (Category) bundle.getSerializable("ThisCategory");

            nameCategoryTV.setText(category.getCategoryName());

            btn.setOnClickListener(v -> {

                String nameCategory = nameCategoryTV.getText().toString();
                Presented.updateCategory(category, nameCategory, view);

                closeKeyBoard();

                Bundle obundle = new Bundle();
                obundle.putSerializable("category", category);
                getNav().navigate(R.id.fragment_FocusCategory, obundle);
            });
        }
        return view;
    }
}
