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
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.HandLuggage;

import java.util.ArrayList;

public class Adapter_paraStartTrip extends RecyclerView.Adapter<Adapter_paraStartTrip.AdapterViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<HandLuggage> dataset;

    public Adapter_paraStartTrip(ArrayList<HandLuggage> dataset) {
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
    public Adapter_paraStartTrip.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_simplesuitcase, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_paraStartTrip.AdapterViewHolder holder, int position) {
        HandLuggage handLuggage = dataset.get(position);

        holder.elementNameTV.setText(handLuggage.handLuggageName);

        ArrayList<Baggage> baggageOfThisHandLuggage = Presented.getBaggageOfThisHandLuggage(handLuggage, holder.itemView.getContext());
        int checked = 0;
        for (Baggage baggage : baggageOfThisHandLuggage) {
            if (baggage.isCheck()) {
                checked++;
            }
        }
        String s = checked + " / " + handLuggage.getHandLuggageSize();
        holder.count.setText(s);

        handLuggage.setHandLuggageCompleted(checked == handLuggage.getHandLuggageSize());
        Presented.updateHandLuggage(handLuggage, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

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
