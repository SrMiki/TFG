package com.miki.justincase_v1.fragments.Show;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.fragments.BaseFragment;

public class Fragment_ItemManager extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_manager, container, false);

        getNav().navigate(R.id.fragment_ShowItems);

        return view;
    }
}
