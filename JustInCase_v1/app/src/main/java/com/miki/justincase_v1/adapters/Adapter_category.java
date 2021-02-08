package com.miki.justincase_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.AdapterViewHolder> implements View.OnClickListener, Filterable, Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private View.OnClickListener listener;
    Adapter_Item adapter_item;
    ArrayList<Category> dataset;
    ArrayList<Item> itemList;
    List<Category> referencesDataset; //for search
    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    private boolean isSelected = false;
    private boolean noMore = false;

    public void setCardSelected(int cardSelected) {
        this.cardSelected = cardSelected;
        notifyDataSetChanged();
    }

    private int cardSelected = -1;

    public Adapter_Category(ArrayList<Category> dataset) {
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
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void selected(boolean select) {
        isSelected = select;
    }

    public boolean isNoMre() {
        return noMore;
    }

    @NonNull
    @Override
    public Adapter_Category.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_category, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Category.AdapterViewHolder holder, int position) {
        String elementName = dataset.get(position).getCategoryName();
        holder.elementNameTV.setText(elementName);

        Context mContext = holder.itemView.getContext();
        if (cardSelected == position){
            if(!holder.cardSelected) {
                holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.selected2));
                holder.cardSelected = true;
            } else { // LAST ITEM!
                holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.design_default_color_on_primary));
                holder.cardSelected = false;
                noMore = true;
            }
        } else {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.design_default_color_on_primary));
            holder.cardSelected = false;
        }

        /**
         *  items recyclerview
         */
        setArrowActions(holder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.elementNameTV.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        Category category = dataset.get(position);
        itemList = Presented.getAllItemsFromThisCategory(category, holder.itemView);
        linearLayoutManager.setInitialPrefetchItemCount(itemList.size());

        holder.nestedRecyclerView = holder.itemView.findViewById(R.id.categoryCardView_nestedRecyclerView);
        ItemTouchHelper.SimpleCallback simpleCallback = new Item_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(holder.nestedRecyclerView);

        holder.nestedRecyclerView.setLayoutManager(linearLayoutManager);
        adapter_item = new Adapter_Item(itemList);
        holder.nestedRecyclerView.setAdapter(adapter_item);
        holder.nestedRecyclerView.setRecycledViewPool(recycledViewPool);

    }

    private void setArrowActions(@NonNull AdapterViewHolder holder) {
        holder.nestedRecyclerview_LinearLayout.setVisibility(View.GONE);
        holder.arrow.setOnClickListener(v -> {
            if (holder.nestedRecyclerview_LinearLayout.getVisibility() == View.GONE) {
                holder.nestedRecyclerview_LinearLayout.setVisibility(View.VISIBLE);
                holder.arrow.setImageResource(R.drawable.ic_down_arrow);
            } else {
                holder.nestedRecyclerview_LinearLayout.setVisibility(View.GONE);
                holder.arrow.setImageResource(R.drawable.ic_right_arrow);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void removeItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Category e, int position) {
        dataset.add(position, e);
        notifyItemInserted(position);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Item.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            Item deletedItem = itemList.get(deletedIndex);

            adapter_item.removeItem(viewHolder.getAdapterPosition());

            Presented.removeFromThisCategory(deletedItem, viewHolder.itemView);
            removeItem(position);
        }
    }



    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        public LinearLayout linearLayout;
        ImageView arrow;
        LinearLayout nestedRecyclerview_LinearLayout;
        RecyclerView nestedRecyclerView;
        private boolean cardSelected;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.categoryCardView_name);
            linearLayout = view.findViewById(R.id.categoryCardView_layout);
            arrow = view.findViewById(R.id.categoryCardView_Arrow);
            nestedRecyclerview_LinearLayout = view.findViewById(R.id.categoryCardView_itemLinearLayout);
            nestedRecyclerView = view.findViewById(R.id.categoryCardView_nestedRecyclerView);
        }
    }
}
