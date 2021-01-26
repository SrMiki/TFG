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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_CategoryContent;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Edit.Fragment_Edit_Category;
import com.miki.justincase_v1.fragments.Add.Fragment_Add_Item_To_Category;
import com.miki.justincase_v1.fragments.Show.Fragment_ItemManager;

import java.util.ArrayList;

public class Fragment_FocusCategory extends BaseFragment {

    TextView categoryNameTV, categorySizeTV;
    Button btn_focusCategory_delete, btn_focusCategory_add, btn_focusCategory_edit;
    RecyclerView categoryContentRecyclerView;
    Adapter_CategoryContent adapterCategoryContent;

    Binding_Entity_focusEntity bindingCategoryFocusCategory;
    Activity activity;
    ArrayList<CategoryContent> categoryContent;
    Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_category, container, false);

        categoryNameTV = view.findViewById(R.id.fragment_focusCategory_categoryName);
        categorySizeTV = view.findViewById(R.id.fragment_focusCategory_categorySize);

        Bundle bundle = getArguments();
        if (bundle != null) {

            category = (Category) bundle.getSerializable("category");

            categoryNameTV.setText(category.getCategoryName());

            categoryContent = Presented.getAllItemsFromThisCategory(category, view);
            categoryContentRecyclerView = view.findViewById(R.id.fragment_focusCategory_recyclerView);

            categorySizeTV.setText(String.valueOf(categoryContent.size()));
            loadRecyclerView();

            btn_focusCategory_add = view.findViewById(R.id.fragment_focusCategory_btn_add);
            btn_focusCategory_add.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisCategory", category);
                getNav().navigate(R.id.fragment_Add_Item_To_Category, obundle);
            });

            btn_focusCategory_delete = view.findViewById(R.id.fragment_focusCategory_btn_delete);
            btn_focusCategory_delete.setOnClickListener(v -> {
                Presented.deleteCategory(category, view);
                getNav().navigate(R.id.fragment_ShowCategories);

            });

            btn_focusCategory_edit = view.findViewById(R.id.fragment_focusCategory_btn_edit);
            btn_focusCategory_edit.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisCategory", category);
                getNav().navigate(R.id.fragment_Edit_Category, obundle);
            });
        }
        return view;
    }

    private void loadRecyclerView() {
        categoryContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCategoryContent = new Adapter_CategoryContent(getContext(), categoryContent);
        categoryContentRecyclerView.setAdapter(adapterCategoryContent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingCategoryFocusCategory = (Binding_Entity_focusEntity) this.activity;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
