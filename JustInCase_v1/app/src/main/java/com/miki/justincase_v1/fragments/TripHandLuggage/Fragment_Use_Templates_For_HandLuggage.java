package com.miki.justincase_v1.fragments.TripHandLuggage;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Template;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Template;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Fragment_Use_Templates_For_HandLuggage extends BaseFragment {

    Adapter_Template adapter;
    ArrayList<Template> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;
    Template focusTemplate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_category, container, false);

        recyclerView = view.findViewById(R.id.fragment_Add_Item_To_Category_recyclerview);
        floatingButton = view.findViewById(R.id.fragment_Add_Item_To_Category_finish);
        floatingButton.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        if (bundle != null) {

            dataset = Presenter.selectAllTemplates(getContext());

            if (!dataset.isEmpty()) {
//                LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
//                linearLayout.setVisibility(View.VISIBLE);
//                setHasOptionsMenu(true);
            } else {

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new Adapter_Template(dataset, getActivity());
                recyclerView.setAdapter(adapter);

                adapter.setOnClickListener(v -> {
                    int position = recyclerView.getChildAdapterPosition(v);
                    focusTemplate = dataset.get(position);
                    ArrayList<Item> arrayList = Presenter.selectItemFromThisTemplate(focusTemplate, getContext());

                    Presenter.addSuggestedItemList(arrayList, getContext());

                    HandLuggage handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");

                    Presenter.createBaggageByItems(arrayList, handLuggage, getContext());

                    getNav().navigate(R.id.action_fragment_Use_Templates_For_HandLuggage_to_fragment_ShowBaggage, bundle);
                });
            }

        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) search.getActionView();

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