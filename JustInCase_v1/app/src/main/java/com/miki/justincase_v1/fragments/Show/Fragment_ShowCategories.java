package com.miki.justincase_v1.fragments.Show;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_category;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowCategories extends BaseFragment {

    Adapter_category adapterCategory;
    ArrayList<Category> categories;
    RecyclerView categoryRecyclerView;

    Activity activity;
    Binding_Entity_focusEntity bindingCategoryFocusCategory;
    Button btnShowItems;

    FloatingActionButton categoryFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_categories, container, false);

        categoryFloatingActionButton = view.findViewById(R.id.fragment_showCategories_btn_add);

        categoryFloatingActionButton.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_CreateCategory);
        });

        btnShowItems = view.findViewById(R.id.fragment_itemManager_btn_items);
        btnShowItems.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_ShowItems);
        });

        categories = Presented.getAllCategories(view);

        categoryRecyclerView = view.findViewById(R.id.fragment_showCategories_recyclerview);

        loadRecyclerView();
        return view;
    }

    private void loadRecyclerView() {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCategory = new Adapter_category(getContext(), categories);
        categoryRecyclerView.setAdapter(adapterCategory);

        adapterCategory.setListener(v -> {
            bindingCategoryFocusCategory.sendCategory(categories.get(categoryRecyclerView.getChildAdapterPosition(v)));
        });

    }

    //Comunicacion entre el fragment Viaje y Detalle del viaje
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
