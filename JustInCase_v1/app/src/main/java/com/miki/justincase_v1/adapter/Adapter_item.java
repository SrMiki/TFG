package com.miki.justincase_v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adapter_item extends RecyclerView.Adapter<Adapter_item.ItemViewHolder> implements View.OnClickListener {

    private final ArrayList<Item> dataset;
    private View.OnClickListener listener;

    public Adapter_item(Context context, ArrayList<Item> dataset) {
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
    public Adapter_item.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_item, parent, false);
        v.setOnClickListener(this);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_item.ItemViewHolder holder, int position) {
        String itemName = dataset.get(position).getItemName();
        holder.itemName_TextView.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName_TextView;

        public ItemViewHolder(@NonNull View v) {
            super(v);
            itemName_TextView = v.findViewById(R.id.recyclerview_Item_itemName);
        }
    }
}
