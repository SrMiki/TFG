package com.miki.justincase_v1.adapters.othersAdapters;

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

public class Adapter_StringList extends RecyclerView.Adapter<Adapter_StringList.AdapterViewHolder> implements View.OnClickListener, Filterable {

    private View.OnClickListener listener;
    List<String> dataset;
    ArrayList<String> referencesDataset;
    private boolean arrow = false;

    public Adapter_StringList(ArrayList<String> dataset) {
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
    public Adapter_StringList.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_entity, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_StringList.AdapterViewHolder holder, int position) {
        String string = dataset.get(position);
        holder.elementNameTV.setText(string);
        if (arrow) {
            holder.deletedArrow.setVisibility(View.VISIBLE);
        } else{ // just in case : )
            holder.deletedArrow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void remove(int adapterPosition) {
        dataset.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public void setArrow(boolean b) {
        arrow = b;
    }


    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        public LinearLayout layout, deletedArrow;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.card_view_entity_name);
            layout = view.findViewById(R.id.card_view_entity_layout);
            deletedArrow = view.findViewById(R.id.card_view_entity_deletedArrow);
        }
    }
}
