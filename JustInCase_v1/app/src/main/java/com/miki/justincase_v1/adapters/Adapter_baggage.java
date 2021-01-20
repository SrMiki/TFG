package com.miki.justincase_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Suitcase;

import java.util.ArrayList;

public class Adapter_baggage extends RecyclerView.Adapter<Adapter_baggage.BaggageViewHolder> implements View.OnClickListener {

    AppDatabase db;

    private ArrayList<Baggage> dataset;
    private View.OnClickListener listener;

    public Adapter_baggage(Context context, ArrayList<Baggage> maletasDeEsteViaje) {
        this.dataset =  maletasDeEsteViaje;
    }

    public void setListener(View.OnClickListener listener) { this.listener = listener; }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public Adapter_baggage.BaggageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_baggage, parent, false);
        v.setOnClickListener(this);
        BaggageViewHolder vh = new BaggageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_baggage.BaggageViewHolder holder, int position) {

        db = AppDatabase.getInstance(holder.baggageName.getContext());
        int id = dataset.get(position).FKsuitcaseID;
        Suitcase suitcase = db.suitcaseDAO().getSuitcase(id);
        holder.setFKsuitcaseID(id);
        holder.baggageName.setText(suitcase.getSuitcaseName());
    }


    @Override
    public int getItemCount() { return dataset == null ? 0 : dataset.size(); }

    public class BaggageViewHolder extends RecyclerView.ViewHolder {

        public TextView baggageName;
        private int FKsuitcaseID;

        public BaggageViewHolder(@NonNull View v) {
            super(v);
            baggageName = v.findViewById(R.id.recyclerview_baggage_baggageName);
        }

        public void setFKsuitcaseID(int FKsuitcaseID) {
            this.FKsuitcaseID = FKsuitcaseID;
        }


        public int getFKsuitcaseID() {
            return FKsuitcaseID;
        }
    }
}
