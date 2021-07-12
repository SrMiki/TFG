package com.miki.justincase_v1.adapters.othersAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Baggage_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.Adapter_Baggage;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Categories_in_Baggage extends RecyclerView.Adapter<Adapter_Categories_in_Baggage.AdapterViewHolder>
        implements View.OnClickListener, View.OnLongClickListener, Filterable {

    HandLuggage handluggage;

    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

    private final Activity activity;

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    Category focusCategory;
    ArrayList<Category> dataset;
    List<Category> referencesDataset; //for search
    boolean selectedState = false;

    private NumberPicker numberPicker;

    public boolean isSelectedState() {
        return selectedState;
    }

    public Adapter_Categories_in_Baggage(ArrayList<Category> dataset, Activity activity, HandLuggage handLuggage) {
        this.dataset = dataset;
        referencesDataset = new ArrayList<>(dataset);
        this.handluggage = handLuggage;
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
            List<Category> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(referencesDataset);
            } else {
                for (Category e : referencesDataset) {
                    if (e.getCategoryName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            dataset.addAll((Collection<? extends Category>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClickListener != null) {
            longClickListener.onLongClick(v);
            return true;
        }
        return false;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    @NonNull
    @Override
    public Adapter_Categories_in_Baggage.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_category, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        focusCategory = dataset.get(position);
        holder.elementNameTV.setText(focusCategory.getCategoryName());

        setChildRecyclerview(holder, focusCategory);
    }

    private void setChildRecyclerview(@NonNull AdapterViewHolder holder, Category focusCategory) {
        ArrayList<Baggage> childDataset; //encapsulate!

        holder.childRecyclerview = holder.itemView.findViewById(R.id.card_view_category_nestedRecyclerView);

        ItemTouchHelper.SimpleCallback simpleCallback = new Baggage_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, holder);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(holder.childRecyclerview);

        childDataset = Presenter.selectBaggageFromThisCategoryInThisHandLuggage(focusCategory, handluggage, holder.itemView.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.elementNameTV.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        linearLayoutManager.setInitialPrefetchItemCount(childDataset.size());
        holder.childRecyclerview.setLayoutManager(linearLayoutManager);
        holder.adapter_baggage = new Adapter_Baggage(childDataset, handluggage);
        holder.childRecyclerview.setAdapter(holder.adapter_baggage);
        holder.childRecyclerview.setRecycledViewPool(recycledViewPool);

        holder.adapter_baggage.setListener(v -> {
            int adapterPosition = holder.childRecyclerview.getChildAdapterPosition(v);
            Baggage focusBaggage = childDataset.get(adapterPosition);
            changeItemCount(v, focusBaggage, holder.adapter_baggage, adapterPosition);
        });
    }


    private void changeItemCount(View v, Baggage focusBaggage, Adapter_Baggage adapter_baggage, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_count, null);

        TextView dialogTitle = view.findViewById(R.id.alertdialog_title_count);
        dialogTitle.setText(R.string.dialog_text_itemCount);


        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);
        numberPicker.setValue(focusBaggage.getBaggageCount());

        builder.setView(view);

        builder.setNegativeButton(v.getResources().getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        NavController navController = Navigation.findNavController(activity, R.id.fragment);
        Bundle bundle = new Bundle();
        bundle.putSerializable("handluggage", handluggage);
        builder.setPositiveButton(v.getResources().getString(R.string.text_edit), ((dialog, which) -> {
            Presenter.updateBaggage(focusBaggage, numberPicker.getValue(), v.getContext());
//            makeToast(v.getContext(), getString(R.string.text_haveBeenUpdated));
            navController.navigate(R.id.fragment_ShowBaggage, bundle);
        }));
        builder.show();
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void setSelectedState(boolean selectedState) {
        this.selectedState = selectedState;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder
            implements Baggage_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

        TextView elementNameTV;
        //        ImageView arrow;
        LinearLayout childRecyclerview_LinearLayout;
        RecyclerView childRecyclerview;
        private boolean cardSelected;

        public LinearLayout linearLayout;
        Adapter_Baggage adapter_baggage;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.card_view_category_name);
            linearLayout = view.findViewById(R.id.card_view_category_layout);
//            arrow = view.findViewById(R.id.categoryCardView_Arrow);
            childRecyclerview_LinearLayout = view.findViewById(R.id.card_view_category_nestedRecyclerView_layout);
            childRecyclerview = view.findViewById(R.id.card_view_category_nestedRecyclerView);
        }

        @Override
        public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            adapter_baggage.onSwipe(viewHolder, direction, position);
            NavController navController = Navigation.findNavController(activity, R.id.fragment);
            Bundle bundle = new Bundle();
            bundle.putSerializable("handluggage", handluggage);
            navController.navigate(R.id.fragment_ShowBaggage, bundle);
        }
    }
}
