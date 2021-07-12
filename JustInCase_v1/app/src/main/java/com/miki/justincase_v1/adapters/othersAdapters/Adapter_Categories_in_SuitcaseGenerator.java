package com.miki.justincase_v1.adapters.othersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;

import java.util.ArrayList;

public class Adapter_Categories_in_SuitcaseGenerator extends RecyclerView.Adapter<Adapter_Categories_in_SuitcaseGenerator.AdapterViewHolder> implements View.OnClickListener {

    private View.OnClickListener clickListener;

    ArrayList<Category> dataset;

    public Adapter_Categories_in_SuitcaseGenerator(ArrayList<Category> dataset) {
        this.dataset = dataset;
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    @NonNull
    @Override
    public Adapter_Categories_in_SuitcaseGenerator.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_category_in_suitcase_generator, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Category category = dataset.get(position);
        Context context = holder.toggleButton_Category.getContext();

        holder.toggleButton_Category.setBackgroundColor(context.getResources().getColor(R.color.quantum_grey50));
        holder.toggleButton_Category.setText(category.getCategoryName()); // init name

        holder.toggleButton_Category.setTextOn(category.getCategoryName());
        holder.toggleButton_Category.setTextOff(category.getCategoryName());
        
        if (holder.toggleButton_Category.isChecked()) {
            holder.toggleButton_Category.setBackgroundColor(context.getResources().getColor(R.color.item_selected));
        } else {
            holder.toggleButton_Category.setBackgroundColor(context.getResources().getColor(R.color.quantum_grey50));
        }
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {


        ToggleButton toggleButton_Category;

        public AdapterViewHolder(View view) {
            super(view);

            toggleButton_Category = view.findViewById(R.id.toggleButton_Category);
        }

    }
}
