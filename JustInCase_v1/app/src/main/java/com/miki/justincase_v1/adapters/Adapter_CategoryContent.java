package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adapter_CategoryContent extends RecyclerView.Adapter<Adapter_CategoryContent.CategoryContentViewHolder> implements View.OnClickListener {

    AppDatabase db;

    private ArrayList<CategoryContent> dataset;
    private View.OnClickListener listener;
    private Binding_Entity_focusEntity bindingCategoryFocusCategory;
    private Activity activity;

    public Adapter_CategoryContent(Context context, ArrayList<CategoryContent> dataset) {
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
    public Adapter_CategoryContent.CategoryContentViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_categorycontent, parent, false);
        v.setOnClickListener(this);
        CategoryContentViewHolder vh = new CategoryContentViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_CategoryContent.CategoryContentViewHolder
                                         holder, int position) {
        db = AppDatabase.getInstance(holder.categoryContentName.getContext());

        int id = dataset.get(position).getFKitemID();
        Item i = db.itemDAO().getItem(id);
        holder.categoryContentName.setText(i.getItemName());

        int categoryContentID = dataset.get(position).categoryContentID;
        CategoryContent categoryContent = db.categoryContentDAO().getCategoryContent(categoryContentID);

        holder.categoryContentCount.setText(categoryContent.getCategoryCount());

        holder.categoryContentButtonAdd.setOnClickListener(v -> {
            categoryContent.increaseThisItem();
            db.categoryContentDAO().updateCategoryContent(categoryContent);

            this.activity = (Activity) holder.context;
            bindingCategoryFocusCategory = (Binding_Entity_focusEntity) this.activity;
            bindingCategoryFocusCategory.sendCategory(db.categoryDAO().getCategory(categoryContent.FKcategoryID));
        });

    }


    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class CategoryContentViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryContentName, categoryContentCount;
        public Button categoryContentButtonAdd;

        Context context;

        public CategoryContentViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            categoryContentName = itemView.findViewById(R.id.recyclerview_categoryContent_itemName);
            categoryContentCount = itemView.findViewById(R.id.recyclerview_categoryContent_itemCount);
            categoryContentButtonAdd = itemView.findViewById(R.id.recyclerview_categoryContent_btn_add);
        }
    }
}
