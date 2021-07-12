package com.miki.justincase_v1.fragments.Show;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Trip;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class MainFragment extends BaseFragment {

    ArrayList<Trip> dataset;
    RecyclerView recyclerView;
    Adapter_Trip adapter;

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        textView = view.findViewById(R.id.mainFragmnet_text);

        dataset = Presenter.selectAllTripsInProgress(getContext());

        recyclerView = view.findViewById(R.id.recyclerview_progressTrip);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        textView.setText(R.string.text_progressTrip);
        adapter = new Adapter_Trip(getActivity(), dataset);
        recyclerView.setAdapter(adapter);
        adapter.setListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            Trip trip = dataset.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("trip", trip);
            getNav().navigate(R.id.fragment_CheckIn, bundle);
        });

        return view;
    }
}
