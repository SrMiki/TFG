package com.miki.justincase_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adapter_ItemSeleccionados extends RecyclerView.Adapter<Adapter_ItemSeleccionados.AdapterViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<Item> dataset;
    int focusPosition;
    boolean state;

    public Adapter_ItemSeleccionados(ArrayList<Item> dataset) {
        this.dataset = dataset;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter_ItemSeleccionados.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_entity, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_ItemSeleccionados.AdapterViewHolder holder, int position) {
        Item item = dataset.get(position);
        holder.elementNameTV.setText(item.getItemName());
        Context context = holder.itemView.getContext();

        if (item.isSelectedState()) {
            holder.color = context.getResources().getColor(R.color.item_selected);
        } else {
            holder.color = context.getResources().getColor(R.color.design_default_color_on_primary);
        }
        holder.layout.setBackgroundColor(holder.color);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void setFocusItem(int position) {
        this.focusPosition = position;
    }

    public void setSelectedState(boolean b) {
        this.state = b;
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        public LinearLayout layout;
        public boolean isSelected;
        int color;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.simpleCardView_name);
            layout = view.findViewById(R.id.simpleCardView_layout);
            color = view.getResources().getColor(R.color.design_default_color_on_primary);
            layout.setBackgroundColor(color);
            isSelected = false;
        }

        public boolean isSelected(){
            return isSelected;
        }

        public void setSeletecState(Boolean isSelected){
            this.isSelected = isSelected;
        }
    }
}
