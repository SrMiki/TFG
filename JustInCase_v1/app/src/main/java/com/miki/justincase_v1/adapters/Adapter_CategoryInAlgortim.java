package com.miki.justincase_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class Adapter_CategoryInAlgortim extends RecyclerView.Adapter<Adapter_CategoryInAlgortim.AdapterViewHolder> implements View.OnClickListener {

    private View.OnClickListener clickListener;

    ArrayList<Category> dataset;

    public Adapter_CategoryInAlgortim(ArrayList<Category> dataset) {
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
    public Adapter_CategoryInAlgortim.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_categoryinalgoritm, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Category category = dataset.get(position);
        holder.elementNameTV.setText(category.getCategoryName());

//        holder.toggleButtonAlgoritm.setChecked(holder.toggleButtonAlgoritm.isChecked());

    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        ImageView toggleButtonAlgoritm;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.categoryName_algortim);
            toggleButtonAlgoritm = view.findViewById(R.id.toggleButtonAlgoritm);
        }

    }
}
