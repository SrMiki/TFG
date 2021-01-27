package com.miki.justincase_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class Adapter_categoryItemsDesplegados extends RecyclerView.Adapter<Adapter_categoryItemsDesplegados.CategoryViewHolder> implements View.OnClickListener {

    AppDatabase db;

    private final ArrayList<Category> dataset;
    private View.OnClickListener listener;
    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

    public Adapter_categoryItemsDesplegados(Context context, ArrayList<Category> dataset) {
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
    public Adapter_categoryItemsDesplegados.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_category_itemsdesplegados, parent, false);
        view.setOnClickListener(this);
        CategoryViewHolder vh = new CategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_categoryItemsDesplegados.CategoryViewHolder holder, int position) {
        db = AppDatabase.getInstance(holder.categoryNameTV.getContext());


        String categoryName = dataset.get(position).getCategoryName();
        holder.categoryNameTV.setText(categoryName);

        int categoryID = dataset.get(position).categoryID;
        int size = db.categoryContentDAO().getAllItemsFromThisCategory(categoryID).size();


        holder.linearLayout.setVisibility(holder.itemView.GONE);
        holder.toogleButton.setOnClickListener(v -> {
            if (holder.linearLayout.getVisibility() == holder.itemView.GONE) {
                holder.linearLayout.setVisibility(holder.itemView.VISIBLE);
                holder.toogleButton.setText("^");
            } else {
                holder.linearLayout.setVisibility(holder.itemView.GONE);
                holder.toogleButton.setText(">");
            }
        });


        ArrayList<CategoryContent> listaDeItemsQueTieneEstaCategoria = (ArrayList<CategoryContent>) db.categoryContentDAO().getAllItemsFromThisCategory(categoryID);
        ArrayList<Item> listaDeItems = new ArrayList<>();
        for (CategoryContent categoryContent : listaDeItemsQueTieneEstaCategoria) {
            Item item = db.itemDAO().getItem(categoryContent.FKitemID);
            listaDeItems.add(item);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.categoryNameTV.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        linearLayoutManager.setInitialPrefetchItemCount(size);

        Adapter_item adapterItem = new Adapter_item(holder.categoryNameTV.getContext(), listaDeItems);

        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(adapterItem);
        holder.recyclerView.setRecycledViewPool(recycledViewPool);
        adapterItem.setListener(v->{

        });
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameTV;

        RecyclerView recyclerView;
        Button toogleButton;
        LinearLayout linearLayout;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTV = itemView.findViewById(R.id.recyclerview_categoryItemsDesplegados_categoryName);
            recyclerView = itemView.findViewById(R.id.recyclerview_categoryItemsDesplegados_ItemsRecyclerview);
            toogleButton = itemView.findViewById(R.id.recyclerview_categoryItemsDesplegados_togglebnt);
            linearLayout = itemView.findViewById(R.id.recyclerview_category_LayoutItemsDesplegados_ItemsRecyclerview);
        }
    }
}
