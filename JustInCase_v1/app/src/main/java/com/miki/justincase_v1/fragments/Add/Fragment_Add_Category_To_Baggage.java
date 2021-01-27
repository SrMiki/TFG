package com.miki.justincase_v1.fragments.Add;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_category;
import com.miki.justincase_v1.adapters.Adapter_categoryItemsDesplegados;
import com.miki.justincase_v1.adapters.Adapter_item;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_Add_Category_To_Baggage extends BaseFragment {

    Adapter_categoryItemsDesplegados adapterCategory;
    RecyclerView categoryRecyclerView;
    ArrayList<Category> categoryArrayList;

    Button btn_items, btn_toggleCategory;

    Activity activity;
    Binding_Entity_focusEntity bindingEntityFocusEntity;

    ArrayList<BaggageContent> baggageContentArrayList;
    FloatingActionButton floatingActionButton;

    Baggage thisBagagge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_baggagecontent_bycateogry, container, false);


        Bundle bundle = getArguments();

        baggageContentArrayList = new ArrayList<>();

        if (bundle != null) {
            btn_items = view.findViewById(R.id.fragment_itemManager_btn_items);
            btn_items.setOnClickListener(v -> {
                getNav().navigate(R.id.fragment_Add_item_to_Baggage, bundle);
            });
            thisBagagge = (Baggage) bundle.getSerializable("ThisBaggage");

            //TODO fragment_add_baggagecontent_bycateogry
            floatingActionButton = view.findViewById(R.id.fragment_Add_Item_To_BaggageByCategory_btn_finish);
            floatingActionButton.setOnClickListener(v -> {
                Presented.addThosesItemsForThisBaggage(baggageContentArrayList, view);
                bindingEntityFocusEntity.sendBaggage(thisBagagge);
            });

            categoryArrayList = Presented.getAllCategories(view);

            categoryRecyclerView = view.findViewById(R.id.fragment_addBaggageContentByCategory_recyclerview);
            loadRecyclerView();
        }
        return view;
    }

    private void loadRecyclerView() {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCategory = new Adapter_categoryItemsDesplegados(getContext(), categoryArrayList);
        categoryRecyclerView.setAdapter(adapterCategory);

        adapterCategory.setListener(view -> {
            Category category = categoryArrayList.get(categoryRecyclerView.getChildAdapterPosition(view));
            ArrayList<CategoryContent> allItemsFromThisCategory = Presented.getAllItemsFromThisCategory(category, view);
            for (CategoryContent categoryContent : allItemsFromThisCategory) {
                BaggageContent baggageContent = new BaggageContent(categoryContent.FKitemID, thisBagagge.baggageID, categoryContent.categoryContentName);
                if (!baggageContentArrayList.contains(baggageContent)) {
                    baggageContentArrayList.add(baggageContent);
                }
            }
        });
    }


    //Comunicacion entre el fragment Maleta y Detalles de la maleta
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            bindingEntityFocusEntity = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

