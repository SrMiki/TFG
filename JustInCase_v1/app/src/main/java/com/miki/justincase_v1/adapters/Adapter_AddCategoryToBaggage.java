package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.content.Context;
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

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_AddCategoryToBaggage extends RecyclerView.Adapter<Adapter_AddCategoryToBaggage.AdapterViewHolder> implements View.OnClickListener, View.OnLongClickListener, Filterable {

    HandLuggage handluggage;

    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();


    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    public ArrayList<Item> itemArrayList;

    ArrayList<Category> dataset;
    List<Category> referencesDataset; //for search
    private boolean selectedState = false;

    public boolean isSelectedState() {
        return selectedState;
    }

    public Adapter_AddCategoryToBaggage(ArrayList<Category> dataset, Activity activity, HandLuggage handLuggage) {
        this.dataset = dataset;
        referencesDataset = new ArrayList<>(dataset);
        this.handluggage = handLuggage;
        itemArrayList = new ArrayList<>();
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
    public Adapter_AddCategoryToBaggage.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_category, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Category category = dataset.get(position);

        holder.elementNameTV.setText(category.getCategoryName());

        Context context = holder.itemView.getContext();

//        if (selectedState) {
//            ArrayList<Item> selectItemFromThisCategory = Presented.selectItemFromThisCategory(category, holder.itemView.getContext());
//            for (Item item : selectItemFromThisCategory) {
//                if(!itemArrayList.contains(item)){
//                    itemArrayList.add(item);
//                }
//            }
//            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.item_selected));
//        } else {
////            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_on_primary));
//        }
        setChildRecyclerview(holder, category);
    }

    private void setChildRecyclerview(@NonNull Adapter_AddCategoryToBaggage.AdapterViewHolder holder, Category focusCategory) {
        ArrayList<Item> childDataset;

        holder.childRecyclerview = holder.itemView.findViewById(R.id.categoryCardView_nestedRecyclerView);

        childDataset = Presented.selectItemFromThisCategoryButNotInThisBaggage(handluggage, focusCategory, holder.itemView.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.elementNameTV.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        linearLayoutManager.setInitialPrefetchItemCount(childDataset.size());
        holder.childRecyclerview.setLayoutManager(linearLayoutManager);
        holder.adapter_item = new Adapter_ItemSeleccionados(childDataset);
        holder.childRecyclerview.setAdapter(holder.adapter_item);
        holder.childRecyclerview.setRecycledViewPool(recycledViewPool);

        if(selectedState){
            holder.adapter_item.setSelectedState(true);
        }

        holder.adapter_item.setListener(v -> {
            int adapterPosition = holder.childRecyclerview.getChildAdapterPosition(v);
            Item item = childDataset.get(adapterPosition);

            if (!itemArrayList.contains(item)) {
                itemArrayList.add(item);
                holder.adapter_item.setSelectedState(true);
            } else {
                itemArrayList.remove(item);
                holder.adapter_item.setSelectedState(false);
            }
            holder.adapter_item.notifyItemChanged(adapterPosition);
        });


    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void setSelectedState(boolean selectedState) {
        this.selectedState = selectedState;
    }

    public boolean getSelectedState() {
        return selectedState;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        public LinearLayout layout;
        RecyclerView childRecyclerview;

        Adapter_ItemSeleccionados adapter_item;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.categoryCardView_name);
            layout = view.findViewById(R.id.categoryCardView_layout);
            childRecyclerview = view.findViewById(R.id.categoryCardView_nestedRecyclerView);
        }
    }
}
