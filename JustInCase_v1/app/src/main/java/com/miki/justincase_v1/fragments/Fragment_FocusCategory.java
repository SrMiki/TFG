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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_CategoryContent;
import com.miki.justincase_v1.bindings.Binding_Category_focusCategory;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;

import java.util.ArrayList;

public class Fragment_FocusCategory extends Fragment {

    AppDatabase db;

    TextView categoryNameTV, categorySizeTV;
    Button btn_focusCategory_delete, btn_focusCategory_add;
    RecyclerView categoryContentRecyclerView;
    Adapter_CategoryContent adapterCategoryContent;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Binding_Category_focusCategory bindingCategoryFocusCategory;
    Activity activity;
    ArrayList<CategoryContent> categoryContent;
    Fragment_Add_Item_To_Category fragmentAddItemToCategory;
    private Adapter_CategoryContent adapter_categoryContent;
    private Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_category, container, false);

        categoryNameTV = view.findViewById(R.id.fragment_focusCategory_categoryName);
        categorySizeTV = view.findViewById(R.id.fragment_focusCategory_categorySize);

        Bundle bundle = getArguments();
        if (bundle != null) {

            category = (Category) bundle.getSerializable("category");

            categoryNameTV.setText(category.getCategoryName());


            db = AppDatabase.getInstance(view.getContext());

            categoryContent = (ArrayList<CategoryContent>) db.categoryContentDAO().getAllItemsFromThisCategory(category.categoryID);
            categoryContentRecyclerView = view.findViewById(R.id.fragment_focusCategory_recyclerView);

            categorySizeTV.setText(String.valueOf(categoryContent.size()));
            mostrarDatos();

            //Baggage add items to THIS Baggage button
            btn_focusCategory_add = view.findViewById(R.id.fragment_focusCategory_btn_add);
            btn_focusCategory_add.setOnClickListener(v -> {

                Bundle obundle = new Bundle();
                //enviamos el objeto (en serial)
                obundle.putSerializable("ThisCategory", category);
                fragmentAddItemToCategory = new Fragment_Add_Item_To_Category();
                fragmentAddItemToCategory.setArguments(obundle);

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, fragmentAddItemToCategory);
                fragmentTransaction.commit();

            });


            btn_focusCategory_delete = view.findViewById(R.id.fragment_focusCategory_btn_delete);
            btn_focusCategory_delete.setOnClickListener(v -> {

                db.categoryDAO().delete(category);

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ItemManager());
                fragmentTransaction.commit();
            });
        }
        return view;
    }

    private void mostrarDatos() {
        categoryContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCategoryContent = new Adapter_CategoryContent(getContext(), categoryContent);
        categoryContentRecyclerView.setAdapter(adapterCategoryContent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingCategoryFocusCategory = (Binding_Category_focusCategory) this.activity;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
