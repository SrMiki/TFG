package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Baggage_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_CategoryBaggage extends RecyclerView.Adapter<Adapter_CategoryBaggage.AdapterViewHolder> implements View.OnClickListener, View.OnLongClickListener, Filterable {

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

    public Adapter_CategoryBaggage(ArrayList<Category> dataset, Activity activity, HandLuggage handLuggage) {
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
    public Adapter_CategoryBaggage.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        Context context = holder.itemView.getContext();

//        if (selectedState) {
//            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.item_selected));
//        } else {
//            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_on_primary));
//        }

//        holder.nestedRecyclerview_LinearLayout.setVisibility(View.GONE);
//        holder.options_layout.setVisibility(View.GONE);
//        holder.arrow.setImageResource(R.drawable.ic_right_arrow);
//        Context context = holder.itemView.getContext();
//        if (cardSelected == position) {
//            if (!holder.selected) {
//                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.item_selected));
//                holder.selected = true;
//                holder.nestedRecyclerview_LinearLayout.setVisibility(View.VISIBLE);
//                holder.arrow.setImageResource(R.drawable.ic_down_arrow);
//                holder.options_layout.setVisibility(View.VISIBLE);
//            } else { // LAST ITEM!
//                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_on_primary));
//                holder.selected = false;
//                isSelected = false;
//            }
//        } else {
//            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_on_primary));
//            holder.selected = false;
//        }

        /**
         *  items recyclerview
         */

        setChildRecyclerview(holder, focusCategory);
    }

    private void setChildRecyclerview(@NonNull AdapterViewHolder holder, Category focusCategory) {
        ArrayList<Baggage> childDataset;


        holder.childRecyclerview = holder.itemView.findViewById(R.id.categoryCardView_nestedRecyclerView);
        ItemTouchHelper.SimpleCallback simpleCallback = new Baggage_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, holder);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(holder.childRecyclerview);

        childDataset = Presented.selectBaggageFromThisCategoryInThisHandLuggage(focusCategory, handluggage, holder.itemView.getContext());
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

        builder.setTitle(v.getResources().getString(R.string.text_itemCount));

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_count, null);
        builder.setView(view);

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);
        numberPicker.setValue(focusBaggage.getBaggageCount());

        builder.setNegativeButton(v.getResources().getString(R.string.text_cancel), ((dialog, which) -> dialog.dismiss()));

        NavController navController = Navigation.findNavController(activity, R.id.fragment);
        Bundle bundle = new Bundle();
        bundle.putSerializable("handluggage", handluggage);
        builder.setPositiveButton(v.getResources().getString(R.string.text_edit), ((dialog, which) -> {
            Presented.updateBaggage(focusBaggage, numberPicker.getValue(), v.getContext());
//            makeToast(v.getContext(), getString(R.string.text_haveBeenUpdated));
            navController.navigate(R.id.fragment_ShowBaggageByCategory, bundle);
        }));
        builder.show();
    }

   /* private void setOptionsButton(AdapterViewHolder holder, Category category) {

        navController = Navigation.findNavController(activity, R.id.fragment);

        Bundle obundle = new Bundle();
        obundle.putSerializable("category", category);
        holder.editTrip.setOnClickListener(v -> editCategoryDialog(v));

        holder.deleteTrip.setOnClickListener(v -> deletedCategoryDialog(v));

        holder.addHandluggage.setOnClickListener(v -> {
            navController.navigate(R.id.fragment_Add_Item_To_Category, obundle);
        });

    }

    private void deletedCategoryDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setCancelable(true);
        builder.setNegativeButton(v.getResources().getString(R.string.text_no), ((dialog, which) -> dialog.dismiss()));

        builder.setTitle(v.getResources().getString(R.string.text_delete) + " " + v.getResources().getString(R.string.text_category));
        TextView textView = new TextView(v.getContext());
        textView.setText(v.getResources().getString(R.string.warning_deleteCategory));
        builder.setView(textView);

        builder.setPositiveButton(v.getResources().getString(R.string.text_yes), ((dialog, which) -> {
            Presented.deleteCategory(focusCategory, v.getContext());
            navController.navigate(R.id.fragment_ShowCategories);
        }));
        builder.show();
    }

    private void editCategoryDialog(View v) {
        String title = v.getResources().getString(R.string.text_edit) + " " + v.getResources().getString(R.string.text_category);
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setNegativeButton(v.getResources().getString(R.string.text_cancel), ((dialog, which) -> dialog.dismiss()));
        builder.setTitle(title);
        builder.setCancelable(true);

        EditText editText = new EditText(v.getContext());
        editText.setText(focusCategory.getCategoryName());
        builder.setView(editText);

        builder.setPositiveButton(v.getResources().getString(R.string.text_edit), ((dialog, which) -> {
            String itemName = editText.getText().toString();
            Presented.updateCategory(focusCategory, itemName, v.getContext());
//            makeToast(v.getContext(), getString(R.string.text_haveBeenUpdated);

            navController.navigate(R.id.fragment_ShowCategories);
        }));
        builder.show();
    }*/

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void setSelectedState(boolean selectedState) {
        this.selectedState = selectedState;
    }

    public void removeCategory(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder implements Baggage_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

        TextView elementNameTV;
        public LinearLayout linearLayout;
        //        ImageView arrow;
        LinearLayout childRecyclerview_LinearLayout;
        RecyclerView childRecyclerview;
        private boolean cardSelected;

        Adapter_Baggage adapter_baggage;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.categoryCardView_name);
            linearLayout = view.findViewById(R.id.categoryCardView_layout);
//            arrow = view.findViewById(R.id.categoryCardView_Arrow);
            childRecyclerview_LinearLayout = view.findViewById(R.id.categoryCardView_itemLinearLayout);
            childRecyclerview = view.findViewById(R.id.categoryCardView_nestedRecyclerView);
        }

        @Override
        public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            adapter_baggage.onSwipe(viewHolder, direction, position);
            NavController navController = Navigation.findNavController(activity, R.id.fragment);
            Bundle bundle = new Bundle();
            bundle.putSerializable("handluggage", handluggage);
            navController.navigate(R.id.fragment_ShowBaggageByCategory, bundle);
        }
    }
}
