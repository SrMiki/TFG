package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
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

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.AdapterViewHolder>
        implements View.OnLongClickListener, Filterable {

    private final boolean permissions;
    private View.OnLongClickListener listener;

    List<Item> dataset;
    List<Bitmap> photoDataset;

    List<Item> referencesDataset; //for search
    private final Activity activity;

    public Adapter_Item(List<Item> dataset, Activity activity) {
        this.dataset = dataset;
        this.activity = activity;

        referencesDataset = new ArrayList<>(dataset);
        photoDataset = new ArrayList<>();


        SharedPreferences sp = activity.getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        permissions = sp.getBoolean("permissions", false);

//        if (permissions) {
//            initPhotoDataset(dataset);
//        }
    }

    /**
     * Load the photo of all item localy
     * to reduce memory access
     */
    private void initPhotoDataset(List<Item> dataset) {
        for (Item item : dataset) {
            if (item.getItemPhotoURI() != null && !item.getItemPhotoURI().isEmpty()) {
                try {
                    if (Build.VERSION.SDK_INT < 28) { //pre-Android 9
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.parse(item.getItemPhotoURI()));
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 25, 25, true);
                        photoDataset.add(resizedBitmap);
                    } else { // post-Android 9
                        ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), Uri.parse(item.getItemPhotoURI()));
                        //"This method may take several seconds to complete, so it should only be called from a worker thread - API Android"
                        Bitmap bitmap;
                        bitmap = ImageDecoder.decodeBitmap(source, (decoder, info, src) -> decoder.setTargetSampleSize(4));
                        photoDataset.add(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_photo, null);
                photoDataset.add(bitmap);
            }
        }
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

    public void remove(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Item e, int position) {
        dataset.add(position, e);
        notifyItemInserted(position);
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView elementNameTV, elementCategoryTV;
        ImageView itemViewPhoto;
        public LinearLayout layout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.card_view_item_itemName);
            elementCategoryTV = view.findViewById(R.id.card_view_item_ItemCategory);
            layout = view.findViewById(R.id.card_view_item_layout);
            itemViewPhoto = view.findViewById(R.id.card_view_item_itemPhoto);
        }
    }


}