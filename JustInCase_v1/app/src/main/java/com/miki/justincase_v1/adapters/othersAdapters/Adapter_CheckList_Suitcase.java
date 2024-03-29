package com.miki.justincase_v1.adapters.othersAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.HandLuggage;

import java.util.ArrayList;

public class Adapter_CheckList_Suitcase extends RecyclerView.Adapter<Adapter_CheckList_Suitcase.AdapterViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<HandLuggage> dataset;

    public Adapter_CheckList_Suitcase(ArrayList<HandLuggage> dataset) {
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
    public Adapter_CheckList_Suitcase.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_simplesuitcase, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_CheckList_Suitcase.AdapterViewHolder holder, int position) {
        HandLuggage handLuggage = dataset.get(position);
        if (handLuggage.getHandLuggageSize() > 0 ) {
            
            holder.elementNameTV.setText(handLuggage.handLuggageName);

            ArrayList<Baggage> baggageOfThisHandLuggage = Presenter.getBaggageOfThisHandLuggage(handLuggage, holder.itemView.getContext());
            int checked = 0;
            for (Baggage baggage : baggageOfThisHandLuggage) {
                if (baggage.isCheck()) {
                    checked++;
                }
            }
            String s = checked + " / " + handLuggage.getHandLuggageSize();
            holder.count.setText(s);

            handLuggage.setHandLuggageCompleted(checked == handLuggage.getHandLuggageSize());
            Presenter.updateHandLuggage(handLuggage, holder.itemView.getContext());
        } else {

            holder.elementNameTV.setText(handLuggage.handLuggageName);
            holder.count.setText(R.string.text_emptysuitcase);
            handLuggage.setHandLuggageCompleted(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV, count;
        public LinearLayout layout;


        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.card_view_simplesuitcase_name);
            count = view.findViewById(R.id.card_view_simplesuitcase_count);
            layout = view.findViewById(R.id.card_view_simplesuitcase_layout);
        }
    }
}
