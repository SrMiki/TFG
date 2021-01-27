package com.miki.justincase_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Category;

import java.text.BreakIterator;
import java.util.ArrayList;

public class Adapter_category extends RecyclerView.Adapter<Adapter_category.CategoryViewHolder> implements View.OnClickListener {

    AppDatabase db;

    private final ArrayList<Category> dataset;
    private View.OnClickListener listener;

    public Adapter_category(Context context, ArrayList<Category> dataset) {
        this.dataset = dataset;
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

    @NonNull
    @Override
    public Adapter_category.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_category, parent, false);
        view.setOnClickListener(this);
        CategoryViewHolder vh = new CategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_category.CategoryViewHolder holder, int position) {
        db = AppDatabase.getInstance(holder.categoryNameTV.getContext());

        String categoryName = dataset.get(position).getCategoryName();
        holder.categoryNameTV.setText(categoryName);

        int categoryID = dataset.get(position).categoryID;
        int size = db.categoryContentDAO().getAllItemsFromThisCategory(categoryID).size();
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameTV;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTV = itemView.findViewById(R.id.recyclerview_category_categoryName);
        }
    }
}
