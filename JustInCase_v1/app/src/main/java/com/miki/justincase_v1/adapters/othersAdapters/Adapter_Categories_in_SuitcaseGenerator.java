package com.miki.justincase_v1.adapters.othersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;

import java.util.ArrayList;

public class Adapter_Categories_in_SuitcaseGenerator extends RecyclerView.Adapter<Adapter_Categories_in_SuitcaseGenerator.AdapterViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;

    ArrayList<Category> dataset;

    public Adapter_Categories_in_SuitcaseGenerator(ArrayList<Category> dataset) {
        this.dataset = dataset;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setListener( View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter_Categories_in_SuitcaseGenerator.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_category_suitcasegenerator, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Category category = dataset.get(position);
        Context context = holder.itemView.getContext();

        holder.category.setBackgroundColor(context.getResources().getColor(R.color.grey));
        holder.category.setText(category.getCategoryName()); // init name

        if (category.isSelectedState()) {
            holder.category.setBackgroundColor(context.getResources().getColor(R.color.category_selected));
        } else {
            holder.category.setBackgroundColor(context.getResources().getColor(R.color.grey));
        }
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public AdapterViewHolder(View view) {
            super(view);
            category = view.findViewById(R.id.card_view_category_suitcasegenerator_name);
        }
    }
}
