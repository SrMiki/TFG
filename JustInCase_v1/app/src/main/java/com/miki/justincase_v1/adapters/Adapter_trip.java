package com.miki.justincase_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_trip extends RecyclerView.Adapter<Adapter_trip.AdapterViewHolder> implements View.OnClickListener, Filterable {

    private List<Trip> referencesDataset;
    private ArrayList<Trip> dataset;
    View.OnClickListener listener;

    public Adapter_trip(Context contex, ArrayList<Trip> myDataset) {
        dataset = myDataset;
        referencesDataset = new ArrayList<>(dataset);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Trip> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(referencesDataset);
            } else {
                for (Trip e : referencesDataset) {
                    if (searchByDestination(constraint, e) || searchByDate(constraint, e)) {
                        filteredList.add(e);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //run on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataset.clear();
            dataset.addAll((Collection<? extends Trip>) results.values);
            notifyDataSetChanged();
        }
    };

    private boolean searchByDate(CharSequence constraint, Trip e) {
        return e.getTravelDate().toLowerCase().startsWith(constraint.toString().toLowerCase());
    }

    private boolean searchByDestination(CharSequence constraint, Trip e) {
        return e.getDestination().toLowerCase().startsWith(constraint.toString().toLowerCase());
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_trip, parent, false);
        v.setOnClickListener(this);
        AdapterViewHolder vh = new AdapterViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        // Get the element from the dataset
        String destination = dataset.get(position).getDestination();
        String travelDate = dataset.get(position).getTravelDate();
        String returnDate = dataset.get(position).getReturnDate();

        if(!returnDate.isEmpty()){
            travelDate += " - " + returnDate;
        }
        holder.tripName_TextView.setText(destination);
        holder.tripDescription_TextView.setText(travelDate);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }


    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView tripName_TextView, tripDescription_TextView;
        public RelativeLayout relativeLayout;
        public AdapterViewHolder(@NonNull View view) {
            super(view);
            tripName_TextView = view.findViewById(R.id.recyclerview_Trip_tripDestino);
            tripDescription_TextView = view.findViewById(R.id.recyclerview_Trip_tripDate);
            relativeLayout = view.findViewById(R.id.tripCardView_layoutToDeleted);
        }
    }
}

