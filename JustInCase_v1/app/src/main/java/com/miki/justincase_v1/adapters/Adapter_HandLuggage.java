package com.miki.justincase_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Suitcase;

import java.util.ArrayList;


public class Adapter_HandLuggage extends RecyclerView.Adapter<Adapter_HandLuggage.AdapterViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<HandLuggage> dataset;

    public Adapter_HandLuggage(ArrayList<HandLuggage> dataset) {
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
    public Adapter_HandLuggage.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_suitcase, parent, false);
        view.setOnClickListener(this);
        AdapterViewHolder vh = new AdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_HandLuggage.AdapterViewHolder holder, int position) {

        HandLuggage handLuggage = dataset.get(position);
        Suitcase suitcase = Presented.getSuitcase(handLuggage, holder.itemView);
        holder.elementNameTV.setText(suitcase.getSuitcaseName());
        holder.colorTV.setText(suitcase.getSuitcaseColor());
        holder.WeigthTV.setText(suitcase.getSuitcaseWeight());
        holder.DimnsTV.setText(suitcase.getSuitcaseDims());
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV, colorTV, WeigthTV, DimnsTV;
        public LinearLayout relativeLayout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.card_view_suitcase_suitcaseName);
            colorTV = view.findViewById(R.id.card_view_suitcase_suitcaseColor);
            WeigthTV = view.findViewById(R.id.card_view_suitcase_suitcaseWeight);
            DimnsTV = view.findViewById(R.id.card_view_suitcase_suitcaseDimns);
            relativeLayout = view.findViewById(R.id.card_view_suitcase_layoutToDeleted);
        }
    }
}
