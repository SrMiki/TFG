package com.miki.justincase_v1.fragments.Trip;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.adapters.Adapter_trip;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowTrips extends BaseFragment {

    Adapter_trip adapter;
    ArrayList<Trip> dataset;
    Binding_Entity_focusEntity binding;
    RecyclerView recyclerView;

    Activity activity;

    FloatingActionButton floatingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_entity, container, false);

        setHasOptionsMenu(true);
        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);

        floatingButton.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_CreateTrip);
        });
        dataset = Presented.getAllTrips(view);

        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_trip(getContext(), dataset);
        recyclerView.setAdapter(adapter);

        adapter.setListener(v -> {
            binding.sendTrip(dataset.get(recyclerView.getChildAdapterPosition(v)));
        });

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            binding = (Binding_Entity_focusEntity) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
//        super.onCreateOptionsMenu(menu, inflater);
    }
}
