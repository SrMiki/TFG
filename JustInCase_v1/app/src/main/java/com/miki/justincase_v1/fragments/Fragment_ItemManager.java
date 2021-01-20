package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;

public class Fragment_ItemManager extends Fragment {

    Button bntShowItems, btnShowCategories;

    Activity activity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Fragment_ShowCategories fragment_showCategories;
    Fragment_ShowItems fragment_showItems;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_manager, container, false);

        fragment_showCategories = new Fragment_ShowCategories();
        fragment_showItems = new Fragment_ShowItems();

        //items default
        if (savedInstanceState != null ) {
           // savedInstanceState.getSerializable("showCategory");
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_itemManager_FrameContent, fragment_showCategories).commit();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_itemManager_FrameContent, fragment_showItems).commit();
        }

        btnShowCategories = view.findViewById(R.id.fragment_itemManager_btn_categories);

        btnShowCategories.setOnClickListener(v -> {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_itemManager_FrameContent, new Fragment_ShowCategories());
            fragmentTransaction.commit();
        });

        bntShowItems = view.findViewById(R.id.fragment_itemManager_btn_items);
        bntShowItems.setOnClickListener(v -> {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_itemManager_FrameContent, new Fragment_ShowItems());
            fragmentTransaction.commit();
        });

        return view;
    }
}
