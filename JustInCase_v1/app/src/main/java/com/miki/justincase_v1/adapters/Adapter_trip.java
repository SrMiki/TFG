package com.miki.justincase_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;

public class Adapter_trip extends RecyclerView.Adapter<Adapter_trip.MyViewHolder> implements View.OnClickListener {

    private ArrayList<Trip> dataset;
    private View.OnClickListener listener;

    public Adapter_trip(Context context, ArrayList<Trip> myDataset) {
        dataset = myDataset;
    }

    // new "View"
    @Override
    public Adapter_trip.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_trip, parent, false);

        v.setOnClickListener(this);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Get the element from the dataset
        String destination = dataset.get(position).getDestination();
        String travelDate = dataset.get(position).getTravelDate();
        String returnDate = dataset.get(position).getReturnDate();

        if(!returnDate.isEmpty()){
            travelDate += " - " + returnDate;
        }
        holder.tripName_TextView.setText(destination);
        holder.tripDescription_TextView.setText(travelDate);
    }

    // List size, defult 0
    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tripName_TextView, tripDescription_TextView;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tripName_TextView = v.findViewById(R.id.recyclerview_Trip_tripDestino);
            tripDescription_TextView = v.findViewById(R.id.recyclerview_Trip_tripDate);
        }
    }
}

