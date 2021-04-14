package com.miki.justincase_v1.fragments.Trip;

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

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_String;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_CountryList extends BaseFragment {

    Adapter_String adapter;
    ArrayList<String> dataset;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country_list, container, false);

        setHasOptionsMenu(true);

        String[] stringArray = view.getResources().getStringArray(R.array.countries);
        dataset = new ArrayList<>(Arrays.asList(stringArray));

        recyclerView = view.findViewById(R.id.country_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_String(dataset);
        recyclerView.setAdapter(adapter);

        adapter.setListener(v -> {
            Bundle bundle = getArguments();
            if (bundle != null) {
                Trip trip = (Trip) bundle.getSerializable("trip");

                String s = dataset.get(recyclerView.getChildAdapterPosition(v));
                trip.setDestination(s);
                bundle.putSerializable("trip", trip );
                getNav().navigate(R.id.action_fragment_CountryList_to_fragment_Edit_Trip, bundle);
            } else {

                Bundle obundle = new Bundle();
                obundle.putSerializable("destination", dataset.get(recyclerView.getChildAdapterPosition(v)));
                getNav().navigate(R.id.action_fragment_CountryList_to_fragment_CreateTrip, obundle);
            }
        });

        return view;

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
        super.onCreateOptionsMenu(menu, inflater);
    }
}
