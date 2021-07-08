package com.miki.justincase_v1.adapters.othersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adapter_Check_Items extends RecyclerView.Adapter<Adapter_Check_Items.BaggageContentViewHolder> implements View.OnClickListener {

    AppDatabase db;

    private ArrayList<Baggage> dataset;
    private View.OnClickListener listener;

    public Adapter_Check_Items(ArrayList<Baggage> contenidoDeEstaMaleta) {
        this.dataset = contenidoDeEstaMaleta;
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
    public BaggageContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_checklist, parent, false);
        v.setOnClickListener(this);
        return new BaggageContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaggageContentViewHolder holder, int position) {
        db = AppDatabase.getInstance(holder.context);

        Baggage baggage = dataset.get(position);

        Item i = db.itemDAO().getItem(baggage.getFKitemID());
        holder.baggageContentName.setText(i.getItemName());

        holder.checkBox.setChecked(baggage.isCheck());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            baggage.setCheck(holder.checkBox.isChecked());
            Presenter.updateBaggage(baggage, holder.context);
        });



    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class BaggageContentViewHolder extends RecyclerView.ViewHolder {
        public TextView baggageContentName;
        CheckBox checkBox;
        Context context;


        public BaggageContentViewHolder(@NonNull View v) {
            super(v);

            context = itemView.getContext();
            baggageContentName = itemView.findViewById(R.id.recyclerview_checkList_title);
            checkBox = itemView.findViewById(R.id.recyclerview_checkList_checkBox);

        }
    }


}
