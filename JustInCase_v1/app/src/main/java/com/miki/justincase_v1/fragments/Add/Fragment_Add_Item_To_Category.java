package com.miki.justincase_v1.fragments.Add;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_item;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Fragment_Add_Item_To_Category extends Fragment {


    Adapter_item adapter_item;
    RecyclerView itemRecyclerView;
    ArrayList<Item> listaDeItems;

    Activity activity;
    Binding_Entity_focusEntity bindingCategoryFocusCategory;

    FloatingActionButton floatingActionButton;
    ArrayList<CategoryContent> categoryContentArrayList;


    Category thisCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_categorycontent, container, false);

        Bundle bundle = getArguments();
        categoryContentArrayList = new ArrayList<>();

        floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_Category_btn_finish);
        floatingActionButton.setOnClickListener(v -> {
            Presented.addThesesItemsForThisCategory(categoryContentArrayList, thisCategory, view);

            bindingCategoryFocusCategory.sendCategory(thisCategory);
        });

        if (bundle != null) {
            thisCategory = (Category) bundle.getSerializable("ThisCategory");

            listaDeItems = Presented.getAllItemsThatItNotInThisCategory(thisCategory, view);

            itemRecyclerView = view.findViewById(R.id.fragment_addCategoryContent_recyclerview);
            loadRecyclerView();
        }
        return view;
    }

    private void loadRecyclerView() {
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_item = new Adapter_item(getContext(), listaDeItems);
        itemRecyclerView.setAdapter(adapter_item);

        adapter_item.setListener(view -> {
            Item item = listaDeItems.get(itemRecyclerView.getChildAdapterPosition(view));
            CategoryContent categoryContent = new CategoryContent(item.itemID, thisCategory.categoryID, thisCategory.categoryName);
            if (!categoryContentArrayList.contains(categoryContent)) {
                categoryContentArrayList.add(categoryContent);
            }
        });
    }


    //Comunicacion entre el fragment Maleta y Detalles de la maleta
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
