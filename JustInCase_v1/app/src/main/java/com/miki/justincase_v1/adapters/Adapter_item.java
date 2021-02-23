package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.AdapterViewHolder> implements View.OnLongClickListener, Filterable {

    private final Activity activity;
    private View.OnLongClickListener listener;
    List<Item> dataset;

    List<Item> referencesDataset; //for search

    public Adapter_Item(List<Item> dataset, Activity activity) {
        this.dataset = dataset;
        referencesDataset = new ArrayList<>(dataset);
        this.activity = activity;
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
        Item item = dataset.get(position);
        holder.elementNameTV.setText(item.getItemName());

//        Category category = Presented.getCategoryOfThisItem(item, holder.itemView.getContext());
//        if (category != null) {
//            holder.elementCategoryTV.setText(category.getCategoryName());
//            holder.elementCategoryTV.setVisibility(View.VISIBLE);
////            holder.elementCategoryTV.setOnClickListener( v -> {
////                changeCategoryDialog(holder, category, position);
////            });
//        }
    }

    private void changeCategoryDialog(AdapterViewHolder vh, Category category, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(vh.itemView.getContext());
        builder.setTitle(vh.itemView.getResources().getString(R.string.text_Item));

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView textView = view.findViewById(R.id.alertdialog_textView);
        textView.setText(vh.itemView.getResources().getString(R.string.text_ask_changeCategory));
        builder.setView(view);

        builder.setNegativeButton(vh.itemView.getResources().getString(R.string.text_no), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(vh.itemView.getResources().getString(R.string.text_yes), ((dialog, which) -> {
            Presented.deleteItemOfThisCategory(category, vh.itemView.getContext());
            notifyItemChanged(position);
        }));
        builder.show();
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

        TextView elementNameTV, elementCategoryTV;
        public LinearLayout layout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.simpleCardView_name);
            elementCategoryTV = view.findViewById(R.id.simpleCardView_category);
            layout = view.findViewById(R.id.simpleCardView_layout);
        }

    }
}
