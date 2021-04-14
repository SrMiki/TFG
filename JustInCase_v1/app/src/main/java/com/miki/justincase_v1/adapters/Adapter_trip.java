package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Trip extends RecyclerView.Adapter<Adapter_Trip.AdapterViewHolder> implements View.OnClickListener, Filterable {

    View.OnClickListener listener;

    private List<Trip> referencesDataset;
    private ArrayList<Trip> dataset;

    private boolean isSelected = false;
    private int cardSelected = -1;

    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

    public Activity activity;

    public int getCardSelected() {
        return cardSelected;
    }

    public void setCardSelected(int cardSelected) {
        this.cardSelected = cardSelected;
        notifyDataSetChanged();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelectedState(boolean newState) {
        isSelected = newState;
    }

    public Adapter_Trip(Activity activity, ArrayList<Trip> myDataset) {
        dataset = myDataset;
        referencesDataset = new ArrayList<>(dataset);
        this.activity = activity;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Trip> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(referencesDataset);
            } else {
                for (Trip e : referencesDataset) {
                    if (searchByDestination(constraint, e) || searchByDate(constraint, e)) {
                        filteredList.add(e);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //run on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataset.clear();
            dataset.addAll((Collection<? extends Trip>) results.values);
            notifyDataSetChanged();
        }
    };

    private boolean searchByDate(CharSequence constraint, Trip e) {
        return e.getTravelDate().toLowerCase().startsWith(constraint.toString().toLowerCase());
    }

    private boolean searchByDestination(CharSequence constraint, Trip e) {
        return e.getDestination().toLowerCase().startsWith(constraint.toString().toLowerCase());
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_trip, parent, false);
        v.setOnClickListener(this);
        return new AdapterViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        // Get the element from the dataset
        Trip trip = dataset.get(position);
        setOptionsButtons(holder, trip);

        String destination = trip.getDestination();
        String travelDate = trip.getTravelDate();
        String returnDate = trip.getReturnDate();

        if (!returnDate.isEmpty()) {
            travelDate += " - " + returnDate;
        }
        holder.tripName_TextView.setText(destination);
        holder.tripDate_TextView.setText(travelDate);

        Context context = holder.itemView.getContext();
        holder.nestedRecyclerview_LinearLayout.setVisibility(View.GONE);
        holder.optionLinearLayout.setVisibility(View.GONE);

        isSelected(holder, position, context);

        setChildRecyclerView(holder, trip);
    }

    private void setOptionsButtons(AdapterViewHolder holder, Trip trip) {
        NavController navController = Navigation.findNavController(activity, R.id.fragment);

        Bundle obundle = new Bundle();
        obundle.putSerializable("trip", trip);
        holder.editTrip.setOnClickListener(v -> {
            navController.navigate(R.id.fragment_Edit_Trip, obundle);
        });

        holder.deleteTrip.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(v.getResources().getString(R.string.warning_title));

            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.alertdialog_textview, null);

            TextView textView = view.findViewById(R.id.alertdialog_textView);
            textView.setText(v.getResources().getString(R.string.warning_deleteTrip));
            builder.setView(view);

            builder.setNegativeButton(v.getResources().getString(R.string.text_no), ((dialog, which) -> dialog.dismiss()));
            builder.setPositiveButton(v.getResources().getString(R.string.text_yes), ((dialog, which) -> {
                Presented.deleteTrip(trip, holder.itemView.getContext());
                navController.navigate(R.id.fragment_ShowTrips);
            }));
            builder.show();
        });

        holder.addHandluggage.setOnClickListener(v -> {
            navController.navigate(R.id.fragment_Add_HandLuggage, obundle);
        });
    }

    private void setChildRecyclerView(AdapterViewHolder holder, Trip trip) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.itemView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        ArrayList<HandLuggage> childDataset;
        Adapter_HandLuggage adapter_handLuggage;

        childDataset = Presented.getHandLuggage(trip, holder.itemView.getContext());

        linearLayoutManager.setInitialPrefetchItemCount(childDataset.size());

        adapter_handLuggage = new Adapter_HandLuggage(childDataset);

        holder.childRecyclerview.setLayoutManager(linearLayoutManager);
        holder.childRecyclerview.setAdapter(adapter_handLuggage);
        holder.childRecyclerview.setRecycledViewPool(recycledViewPool);

        adapter_handLuggage.setListener(v -> {
            int adapterPosition = holder.childRecyclerview.getChildAdapterPosition(v);
            HandLuggage focusHandLuggage = childDataset.get(adapterPosition);

            NavController navController = Navigation.findNavController(activity, R.id.fragment);

            Bundle obundle = new Bundle();
            obundle.putSerializable("handLuggage", focusHandLuggage);
            SharedPreferences sp;
            sp = v.getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            boolean showCategoires = sp.getBoolean("showCategories", false);
            if (showCategoires) {
                navController.navigate(R.id.fragment_ShowBaggageByCategory, obundle);
            } else {
                navController.navigate(R.id.fragment_ShowBaggageByItem, obundle);
            }
        });

    }


    private void isSelected(AdapterViewHolder holder, int position, Context context) {
        if (cardSelected == position) {
            if (!holder.selected) {
//                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.item_selected));
                holder.selected = true;
                holder.nestedRecyclerview_LinearLayout.setVisibility(View.VISIBLE);
                holder.optionLinearLayout.setVisibility(View.VISIBLE);
            } else { // LAST ITEM!
//                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_on_primary));
                holder.selected = false;
                isSelected = false;
            }
        } else {
//            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_on_primary));
            holder.selected = false;
        }
    }

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

    public void removeItem(int adapterPosition) {
        dataset.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        RecyclerView childRecyclerview;
        public TextView tripName_TextView, tripDate_TextView;
        public LinearLayout layout;

        LinearLayout nestedRecyclerview_LinearLayout;

        LinearLayout optionLinearLayout;
        Button button, editTrip, deleteTrip, addHandluggage;

        private boolean selected;

        public AdapterViewHolder(@NonNull View view) {
            super(view);
            tripName_TextView = view.findViewById(R.id.recyclerview_Trip_tripDestino);
            tripDate_TextView = view.findViewById(R.id.recyclerview_Trip_tripDate);
            layout = view.findViewById(R.id.tripCardView_layoutToDeleted);

            childRecyclerview = view.findViewById(R.id.cardviewtrip_nestedRecyclerView);
            nestedRecyclerview_LinearLayout = view.findViewById(R.id.cardviewtrip_suitcasesLinearLayout);

            optionLinearLayout = view.findViewById(R.id.options_layout);
            editTrip = view.findViewById(R.id.showTrip_button_editTrip);
            deleteTrip = view.findViewById(R.id.showTrip_button_deleteTrip);
            addHandluggage = view.findViewById(R.id.showTrip_button_addHandlugage);

        }


    }
}

