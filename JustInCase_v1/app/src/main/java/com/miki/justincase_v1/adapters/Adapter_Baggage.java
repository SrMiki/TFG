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
import com.miki.justincase_v1.Swipers.Baggage_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adapter_Baggage extends RecyclerView.Adapter<Adapter_Baggage.AdapterViewHolder> implements View.OnClickListener, Baggage_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private final HandLuggage handLuggage;
    AppDatabase db;

    private ArrayList<Baggage> dataset;
    private View.OnClickListener listener;

    public Adapter_Baggage(ArrayList<Baggage> baggages, HandLuggage handLuggage) {
        this.dataset = baggages;
        this.handLuggage = handLuggage;
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
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_count, parent, false);
        v.setOnClickListener(this);
        return new AdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Baggage baggage = dataset.get(position);

        holder.baggageContentName.setText(baggage.baggageName);

        holder.baggageContentCount.setText(String.valueOf(baggage.getBaggageCount()));
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void removeItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Baggage.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            String name = dataset.get(deletedIndex).baggageName;
            Baggage deletedItem = dataset.get(deletedIndex);


//            restoreDeletedElement(viewHolder, name, deletedItem, deletedIndex);
            //Note: if the item it's deleted and then restore, only restore in item. You must
            //yo add again in Baggages and Categorys.
            Presented.removeBaggageFromThisHandLuggage(handLuggage, deletedItem, viewHolder.itemView.getContext());
            this.removeItem(viewHolder.getAdapterPosition());
//            getNav().navigate(R.id.fragment_ShowItems);

        }
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView baggageContentName, baggageContentCount;

        public LinearLayout layout;
        public AdapterViewHolder(@NonNull View v) {
            super(v);
            baggageContentName = itemView.findViewById(R.id.recyclerview_elementCount_title);
            baggageContentCount = itemView.findViewById(R.id.recyclerview_elementCount_size);
            layout = itemView.findViewById(R.id.card_view_count_layout);
        }
    }


}
