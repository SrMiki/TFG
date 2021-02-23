package com.miki.justincase_v1.fragments.Show;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_HandLuggage;
import com.miki.justincase_v1.adapters.Adapter_Trip;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowFinishedTrip extends BaseFragment {

    Adapter_Trip adapter;
    ArrayList<Trip> dataset;
    ArrayList<HandLuggage> handLuggageDataset;
    RecyclerView recyclerView, childRecyclerview;

    Adapter_HandLuggage adapter_handLuggage;
//    LinearLayout progressTripLayout, finishedTripsLayout;

    Button button;

    TextView textView;
    Trip focusTrip;
    private boolean showOptionMenu = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_trip, container, false);
        setHasOptionsMenu(true);

        textView = view.findViewById(R.id.showTrip_trips);
        textView.setOnClickListener(v -> {
            getNav().navigate(R.id.fragment_ShowTrips);
        });

        button = view.findViewById(R.id.fragment_show_trip_button);
        button.setVisibility(View.GONE);

        dataset = Presented.selectAllTripsFinished(getContext());
        if (dataset.size() != 0) {
            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Trip(getActivity(), dataset);
            adapter.activity = getActivity();
            recyclerView.setAdapter(adapter);


            adapter.setListener(v -> {
////                setButton(view);
                int position = recyclerView.getChildAdapterPosition(v);
                focusTrip = dataset.get(position);
                Presented.deleteTrip(focusTrip, getContext());
                getNav().navigate(R.id.fragment_ShowFinishedTrip);

//                adapter.setCardSelected(position);
//                getActivity().invalidateOptionsMenu();
//                showOptionMenu = !showOptionMenu;
//                adapter.setSelectedState(showOptionMenu);
            });
        } else {

        }


//        setButton(view);

//        adapter.setListener(v -> {
//            setButton(view);
//            int position = recyclerView.getChildAdapterPosition(v);
//            focusTrip = dataset.get(position);
//
//            isSeletec(position);
//        });
        return view;

    }


//    private void isSeletec(int position) {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("trip", focusTrip);
//        if (!adapter.isSelected()) {
//
//            button.setText(getString(R.string.text_doCheckIn));
//            button.setBackgroundColor(getContext().getResources().getColor(R.color.secundary_400));
//            button.setOnClickListener(vs -> {
//                if (Presented.getHandLuggage(focusTrip, getContext()).size() != 0) {
//                    getNav().navigate(R.id.fragment_CheckIn, bundle);
//                } else {
//                    AlertDialog.Builder builder = makeNewAlertDialog(getString(R.string.text_warning));
//                    TextView textView = new TextView(getContext());
//                    textView.setText(getString(R.string.warning_startTrip));
//                    builder.setView(textView);
//                    builder.setPositiveButton(getString(R.string.text_confirm), ((dialog, which) -> {
//                        getNav().navigate(R.id.fragment_CheckIn, bundle);
//                    }));
//                    builder.show();
//                }
//
//            });
//            a(position);
//        } else if (adapter.getCardSelected() == position) {
//            a(-1);
//
//        } else {
//            adapter.setCardSelected(position);
//        }
//    }

//    private void setButton(View view) {
//        button = view.findViewById(R.id.fragment_show_trip_button);
//        button.setText(getString(R.string.text_newTrip));
//        button.setBackgroundColor(getContext().getResources().getColor(R.color.primary_700));
//        button.setOnClickListener(vs -> getNav().navigate(R.id.fragment_CreateTrip));
//    }

    private void a(int position) {
        adapter.setCardSelected(position);
        getActivity().invalidateOptionsMenu();
        showOptionMenu = !showOptionMenu;
        adapter.setSelectedState(showOptionMenu);

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

//    @Override
//    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        MenuItem menuItem;
//        menuItem = menu.findItem(R.id.action_addItem);
//        menuItem.setVisible(showOptionMenu);
//
//        menuItem = menu.findItem(R.id.action_edit);
//        menuItem.setVisible(showOptionMenu);
//
//        menuItem = menu.findItem(R.id.action_delete);
//        menuItem.setVisible(showOptionMenu);
//
////        super.onPrepareOptionsMenu(menu);
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Bundle obundle = new Bundle();
//        obundle.putSerializable("Trip", focusTrip);
//        switch (item.getItemId()) {
//            case R.id.action_delete:
//                deleteThisTrip();
//                return true;
//            case R.id.action_edit:
//                adapter.setSelectedState(false);
//                getActivity().invalidateOptionsMenu();
//                showOptionMenu = !showOptionMenu;
//
//                return true;
//            case R.id.action_addItem:
//                adapter.setSelectedState(false);
//                getActivity().invalidateOptionsMenu();
//                showOptionMenu = !showOptionMenu;
//
//                return true;
//            default:
//                return true;
////                return super.onOptionsItemSelected(item);
//        }
//
//    }

}
