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
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.AdapterViewHolder> implements View.OnLongClickListener, Filterable {

    private View.OnLongClickListener listener;
    List<Item> dataset;

    List<Item> referencesDataset; //for search
    public Adapter_Item(List<Item> dataset) {
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
            List<Item> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(referencesDataset);
            } else {
                for (Item e : referencesDataset) {
                    if (e.getItemName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            dataset.addAll((Collection<? extends Item>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            listener.onLongClick(v);
        }
        return true;
    }

    public void setListener(View.OnLongClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter_Item.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_entity, parent, false);
        view.setOnLongClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Item.AdapterViewHolder holder, int position) {
        String elementName =  dataset.get(position).getItemName();
        holder.elementNameTV.setText(elementName);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }


    public void removeItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Item e, int position) {
        dataset.add(position, e);
        notifyItemInserted(position);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        public LinearLayout layout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.simpleCardView_name);
            layout = view.findViewById(R.id.simpleCardView_layout);
        }

    }
}
