package com.miki.justincase_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_String extends RecyclerView.Adapter<Adapter_String.AdapterViewHolder> implements View.OnClickListener, Filterable {

    private View.OnClickListener listener;
    List<String> dataset;
    ArrayList<String> referencesDataset;

    public Adapter_String(ArrayList<String> dataset) {
        this.dataset = dataset;
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
            List<String> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(referencesDataset);
            } else {
                for (String e : referencesDataset) {
                    if (e.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            dataset.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter_String.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_entity, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_String.AdapterViewHolder holder, int position) {
        String country = dataset.get(position);
        holder.elementNameTV.setText(country);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        public LinearLayout layout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.entityCardView_name);
            layout = view.findViewById(R.id.entityCardView_layout);
        }
    }
}
