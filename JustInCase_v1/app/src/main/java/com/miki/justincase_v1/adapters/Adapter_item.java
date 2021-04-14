package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.AdapterViewHolder> implements View.OnLongClickListener, Filterable {

    private View.OnLongClickListener listener;
    List<Item> dataset;

    List<Item> referencesDataset; //for search
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

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
                .inflate(R.layout.card_view_item, parent, false);
        view.setOnLongClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Item.AdapterViewHolder holder, int position) {
        Item item = dataset.get(position);
        holder.elementNameTV.setText(item.getItemName());

        String itemPhotoURI = item.getItemPhotoURI();
        if (!itemPhotoURI.isEmpty() && activity!=null) {

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.parse(itemPhotoURI));
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 50 /*Ancho*/, 50 /*Alto*/, false /* filter*/);
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytearrayoutputstream);

                holder.itemViewPhoto.setVisibility(View.VISIBLE);
                holder.itemViewPhoto.setImageBitmap(resizedBitmap);
                holder.elementCategoryTV.setText(itemPhotoURI);
                holder.elementCategoryTV.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Category categoryOfThisItem = Presented.getCategoryOfThisItem(item, holder.itemView.getContext());
//        if (categoryOfThisItem != null) {
//            holder.elementCategoryTV.setText(categoryOfThisItem.categoryName);
//        }

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

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV, elementCategoryTV;
        public LinearLayout layout;
        ImageView itemViewPhoto;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.itemCardView_name);
            elementCategoryTV = view.findViewById(R.id.itemCardView_Category);
            layout = view.findViewById(R.id.itemCardView_layout);
            itemViewPhoto = view.findViewById(R.id.itemViewPhoto);
        }

    }
}
