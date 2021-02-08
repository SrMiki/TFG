package com.miki.justincase_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Suitcase extends RecyclerView.Adapter<Adapter_Suitcase.AdapterViewHolder> implements View.OnClickListener, Filterable {

    private View.OnClickListener listener;
    List<Suitcase> dataset;
    List<Suitcase> referencesDataset; //for search

    public Adapter_Suitcase(List<Suitcase> dataset) {
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
            List<Suitcase> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(referencesDataset);
            } else {
                for (Suitcase e : referencesDataset) {
                    if (e.getSuitcaseName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            dataset.addAll((Collection<? extends Suitcase>) results.values);
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
    public Adapter_Suitcase.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_suitcase, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Suitcase.AdapterViewHolder holder, int position) {

        Suitcase suitcase = dataset.get(position);
        holder.elementNameTV.setText(suitcase.getSuitcaseName());
        holder.colorTV.setText(suitcase.getSuitcaseColor());
        holder.WeigthTV.setText(suitcase.getSuitcaseWeight());
        holder.DimnsTV.setText(suitcase.getSuitcaseDims());
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }


    public void removeItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Suitcase e, int position) {
        dataset.add(position, e);
        notifyItemInserted(position);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV, colorTV, WeigthTV, DimnsTV;
        public LinearLayout relativeLayout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.card_view_suitcase_suitcaseName);
            colorTV = view.findViewById(R.id.card_view_suitcase_suitcaseColor);
            WeigthTV = view.findViewById(R.id.card_view_suitcase_suitcaseWeight);
            DimnsTV = view.findViewById(R.id.card_view_suitcase_suitcaseDimns);
            relativeLayout = view.findViewById(R.id.card_view_suitcase_layoutToDeleted);
        }
    }
}
