package com.miki.justincase_v1.fragments.Show;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_Item_To_Category extends BaseFragment {

    Adapter_Item adapter;
    RecyclerView recyclerView;
    ArrayList<Item> dataset;

    FloatingActionButton floatingActionButton;
    ArrayList<CategoryContent> arrayList;

    Category category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_categorycontent, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            arrayList = new ArrayList<>();

            category = (Category) bundle.getSerializable("ThisCategory");

            dataset = Presented.getAllItemsThatItNotInThisCategory(view);

            recyclerView = view.findViewById(R.id.fragment_addCategoryContent_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Item(dataset);
            adapter.setSelectionMode();
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                Item item = dataset.get(recyclerView.getChildAdapterPosition(v));
                adapter.notifyItemChanged(recyclerView.getChildAdapterPosition(v));
                CategoryContent categoryContent = new CategoryContent(item.itemID, category.categoryID, category.categoryName);
                if (!arrayList.contains(categoryContent)) {
                    arrayList.add(categoryContent);
                }
            });

            floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Category_btn_finish);
            floatingActionButton.setOnClickListener(v -> {
                Presented.addItemsToThisCateogory(arrayList, category, view);
                getNav().navigate(R.id.fragment_ShowCategories);
            });
        }
        return view;
    }
}