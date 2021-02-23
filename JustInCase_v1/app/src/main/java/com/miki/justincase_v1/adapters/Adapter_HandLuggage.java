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
    HandLuggage focusHandLuggage;
    int selected = -1;

    public void setSelected(int selected){
        this.selected = selected;
        notifyDataSetChanged();
    }

    public HandLuggage getFocusHandLuggage(){
        return this.focusHandLuggage;
    }

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
                .inflate(R.layout.card_view_handluggage, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_HandLuggage.AdapterViewHolder holder, int position) {

        HandLuggage handLuggage = dataset.get(position);
        Suitcase suitcase = Presented.getSuitcase(handLuggage, holder.itemView.getContext());
        holder.elementNameTV.setText(suitcase.getName());

        if(position==selected){
            focusHandLuggage= handLuggage;
        }
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView elementNameTV;
        public LinearLayout layout;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.handluggage_cardviewName);
            layout = view.findViewById(R.id.handluggage_cardviewLayout);
        }
    }
}
