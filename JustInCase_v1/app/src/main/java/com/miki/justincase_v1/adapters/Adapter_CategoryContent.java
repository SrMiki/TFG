package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Baggage_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adapter_CategoryContent extends RecyclerView.Adapter<Adapter_CategoryContent.AdapterViewHolder> implements View.OnClickListener, Baggage_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private final Category category;

    private final ArrayList<Item> dataset;
    private View.OnClickListener listener;
    Activity activity;

    private final boolean permissions;

    public Adapter_CategoryContent(ArrayList<Item> database, Category category, Activity activity) {
        this.dataset = database;
        this.category = category;

        SharedPreferences sp = activity.getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        permissions = sp.getBoolean("permissions", false);

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
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item, parent, false);
        v.setOnClickListener(this);
        return new AdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Item item = dataset.get(position);
        holder.itemName.setText(item.getItemName());
        if (permissions) {
//            holder.itemViewPhoto.setImageBitmap(photoDataset.get(position));
            holder.itemViewPhoto.setImageURI(Uri.parse(item.getItemPhotoURI()));
        } else {
            holder.itemViewPhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void removeItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_CategoryContent.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            Item deletedItem = dataset.get(deletedIndex);

            Presenter.removeItemFromThisCategory(deletedItem, viewHolder.itemView.getContext());
            this.removeItem(viewHolder.getAdapterPosition());

        }
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public LinearLayout layout;
        ImageView itemViewPhoto;

        public AdapterViewHolder(@NonNull View v) {
            super(v);
            itemName = v.findViewById(R.id.card_view_item_itemName);
            layout = v.findViewById(R.id.card_view_item_layout);
            itemViewPhoto = v.findViewById(R.id.card_view_item_itemPhoto);
        }
    }
}