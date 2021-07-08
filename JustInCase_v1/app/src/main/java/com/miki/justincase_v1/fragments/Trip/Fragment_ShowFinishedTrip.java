package com.miki.justincase_v1.fragments.Trip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Trip_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.Adapter_Trip;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowFinishedTrip extends BaseFragment implements Trip_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Trip adapter;
    ArrayList<Trip> dataset;
    RecyclerView recyclerView;

    Button button;

    TextView finishedTripTv, planingTripTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_trip, container, false);
        setHasOptionsMenu(true);

        finishedTripTv = view.findViewById(R.id.showTrip_finishedTrips);
        finishedTripTv.setTextColor(view.getResources().getColor(R.color.item_selected));

        planingTripTV = view.findViewById(R.id.showTrip_trips);
        planingTripTV.setOnClickListener(v -> {
            getNav().navigate(R.id.action_fragment_ShowFinishedTrip_to_fragment_ShowTrips);
        });

        button = view.findViewById(R.id.fragment_show_trip_button);
        button.setVisibility(View.GONE);

        dataset = Presenter.selectAllTripsFinished(getContext());
        if (dataset.size() != 0) {
            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Trip(getActivity(), dataset);
            adapter.activity = getActivity();
            recyclerView.setAdapter(adapter);

            ItemTouchHelper.SimpleCallback simpleCallback = new Trip_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        }
        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

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

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Trip.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            Trip deletedItem = dataset.get(deletedIndex);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Presenter.deleteTrip(deletedItem, getContext());
            getNav().navigate(R.id.fragment_ShowFinishedTrip);
        }
    }
}
