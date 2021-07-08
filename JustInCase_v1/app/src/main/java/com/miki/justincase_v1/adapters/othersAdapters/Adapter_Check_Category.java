package com.miki.justincase_v1.adapters.othersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;

import java.util.ArrayList;

public class Adapter_Check_Category extends RecyclerView.Adapter<Adapter_Check_Category.AdapterViewHolder> implements View.OnClickListener {

    AppDatabase db;

    private ArrayList<Category> dataset;

    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    Category focusCategory;
    private ArrayList<Baggage> itemList;
    private Adapter_Check_Items adapter_item;

    private View.OnClickListener listener;
    private HandLuggage handLuggage;

    public Adapter_Check_Category(ArrayList<Category> categories, HandLuggage handLuggage) {
        this.dataset = categories;
        this.handLuggage = handLuggage;
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
                .inflate(R.layout.card_view_category_checklist, parent, false);
        v.setOnClickListener(this);
        return new AdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        db = AppDatabase.getInstance(holder.context);

        focusCategory = dataset.get(position);

        holder.name.setText(focusCategory.getCategoryName());

//        holder.checkBox.setChecked(baggage.isCheck());

//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            baggage.setCheck(holder.checkBox.isChecked());
//            Presented.updateBaggage(baggage, holder.context);
//        });

        /**
         *  items recyclerview
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.name.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        itemList = Presenter.selectBaggageFromThisCategoryInThisHandLuggage(focusCategory, this.handLuggage, holder.itemView.getContext());
        linearLayoutManager.setInitialPrefetchItemCount(itemList.size());

        holder.childRecyclerview.setLayoutManager(linearLayoutManager);
        adapter_item = new Adapter_Check_Items(itemList);
        holder.childRecyclerview.setAdapter(adapter_item);
        holder.childRecyclerview.setRecycledViewPool(recycledViewPool);

//        holder.checkBox.setChecked(allitemChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setAllItemCheked(holder.checkBox.isChecked());
            adapter_item.notifyDataSetChanged();
        });
    }

    private void setAllItemCheked(boolean state) {
        for (Baggage baggage : itemList) {
            baggage.setCheck(state);
        }
    }

    private boolean allitemChecked() {
        for (Baggage baggage : itemList) {
            if (!baggage.isCheck()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        CheckBox checkBox;
        Context context;
        RecyclerView childRecyclerview;

        public AdapterViewHolder(@NonNull View v) {
            super(v);

            context = itemView.getContext();
            name = itemView.findViewById(R.id.categoryChecklistCardView_name);
            checkBox = itemView.findViewById(R.id.recyclerview_categoryCheckList_checkBox);
            childRecyclerview = itemView.findViewById(R.id.recyclerview_categoryCheckList);
//            childRecyclerview_LinearLayout = view.findViewById(R.id.categoryCardView_itemLinearLayout);

        }
    }


}
