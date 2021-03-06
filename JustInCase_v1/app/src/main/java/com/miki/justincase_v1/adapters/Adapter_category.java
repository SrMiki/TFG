package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.AdapterViewHolder> implements View.OnClickListener, View.OnLongClickListener, Filterable {

    private final Activity activity;
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    ArrayList<Category> dataset;
    List<Category> referencesDataset; //for search

    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

    public Adapter_Category(ArrayList<Category> dataset, Activity activity) {
        this.dataset = dataset;
        this.activity = activity;
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
            List<Category> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(referencesDataset);
            } else {
                for (Category e : referencesDataset) {
                    if (e.getCategoryName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            dataset.addAll((Collection<? extends Category>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClickListener != null) {
            longClickListener.onLongClick(v);
            return true;
        }
        return false;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    @NonNull
    @Override
    public Adapter_Category.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_category, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Category category = dataset.get(position);
        holder.elementNameTV.setText(category.getCategoryName());

        setChildRecyclerView(holder, category);
    }

    private void setChildRecyclerView(AdapterViewHolder holder, Category category) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.itemView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        //encapsulate!
        ArrayList<Item> childDataset;
        Adapter_Item adapter_item;
        //encapsulate!

        childDataset = Presenter.selectItemFromThisCategory(category, holder.itemView.getContext());

        linearLayoutManager.setInitialPrefetchItemCount(childDataset.size());

        adapter_item = new Adapter_Item(childDataset, activity);

        holder.childRecyclerview.setLayoutManager(linearLayoutManager);
        holder.childRecyclerview.setAdapter(adapter_item);
        holder.childRecyclerview.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        RecyclerView childRecyclerview;
        TextView elementNameTV;
        public LinearLayout layout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.categoryCardView_name);
            layout = view.findViewById(R.id.categoryCardView_itemLinearLayout);
            childRecyclerview = view.findViewById(R.id.categoryCardView_nestedRecyclerView);
        }
    }
}