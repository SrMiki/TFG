package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adaptador_CheckList extends RecyclerView.Adapter<Adaptador_CheckList.BaggageContentViewHolder> implements View.OnClickListener {

    AppDatabase db;

    Binding_Entity_focusEntity bindingBaggageFocusBaggage;
    Activity activity;

    private ArrayList<Baggage> dataset;
    private View.OnClickListener listener;

    public Adaptador_CheckList(Context context, ArrayList<Baggage> contenidoDeEstaMaleta) {
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

        int id = dataset.get(position).getFKitemID();
        Item i = db.itemDAO().getItem(id);
        holder.baggageContentName.setText(i.getItemName());

        int baggageContentID = dataset.get(position).baggageID;
        Baggage baggage = (Baggage) db.baggageDAO().getBaggageContent(baggageContentID);
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
