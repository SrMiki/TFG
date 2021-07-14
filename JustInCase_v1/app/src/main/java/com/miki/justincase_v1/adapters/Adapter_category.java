package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.CategoryContent_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.AdapterViewHolder>
        implements View.OnClickListener, Filterable {

    private View.OnClickListener clickListener;

    public final Activity activity;

    ArrayList<Category> dataset;
    List<Category> referencesDataset; //for search

    private boolean isSelected = false;
    private int cardSelected = -1;

    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

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

    public Adapter_Category(ArrayList<Category> dataset, Activity activity) {
        this.dataset = dataset;
        this.activity = activity;

        referencesDataset = new ArrayList<>(dataset);
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

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    @NonNull
    @Override
    public Adapter_Category.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_category, parent, false);
        view.setOnClickListener(this);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Category category = dataset.get(position);
        setOptionsButtons(holder, category);

        holder.elementNameTV.setText(category.getCategoryName());

        holder.nestedRecyclerview_LinearLayout.setVisibility(View.GONE);
        holder.optionLinearLayout.setVisibility(View.GONE);
        holder.arrowLayout.setVisibility(View.VISIBLE);

        updateSelectedState(holder, position);

        setChildRecyclerView(holder, category);
    }

    private void setChildRecyclerView(AdapterViewHolder holder, Category category) {
        ArrayList<Item> childDataset;

        holder.childRecyclerview = holder.itemView.findViewById(R.id.card_view_category_nestedRecyclerView);

        ItemTouchHelper.SimpleCallback simpleCallback = new CategoryContent_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, holder);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(holder.childRecyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.itemView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        childDataset = Presenter.selectItemFromThisCategory(category, holder.itemView.getContext());

        linearLayoutManager.setInitialPrefetchItemCount(childDataset.size());

        holder.adapter_categoryContent = new Adapter_CategoryContent(childDataset, category, activity);

        holder.childRecyclerview.setLayoutManager(linearLayoutManager);
        holder.childRecyclerview.setAdapter(holder.adapter_categoryContent);
        holder.childRecyclerview.setRecycledViewPool(recycledViewPool);
    }

    /**
     * Update the focusCategory's layout
     */
    private void updateSelectedState(AdapterViewHolder holder, int position) {
        if (cardSelected == position) {
            if (!holder.selected) {
                holder.selected = true;
                holder.nestedRecyclerview_LinearLayout.setVisibility(View.VISIBLE);
                holder.optionLinearLayout.setVisibility(View.VISIBLE);
                holder.arrowLayout.setVisibility(View.GONE);
            } else {
                holder.selected = false;
                isSelected = false;
                holder.arrowLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.selected = false;
        }
    }

    private void setOptionsButtons(AdapterViewHolder holder, Category category) {
        NavController navController = Navigation.findNavController(activity, R.id.fragment);

        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        holder.editCategory.setOnClickListener(v -> editCategoryDialog(v.getContext(), category, navController));

        holder.deleteCategory.setOnClickListener(v -> deleteCategoryDialog(category, navController, v));

        holder.addItemToCategory.setOnClickListener(v -> {
            bundle.putSerializable("categoryPosition", holder.getAdapterPosition());
            Navigation.findNavController(activity, R.id.fragment).navigate(R.id.fragment_Add_Item_To_Category, bundle);
        });
    }

    private void deleteCategoryDialog(Category category, NavController navController, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(v.getContext().getString(R.string.dialog_title_deleteCategory));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(v.getResources().getString(R.string.dialog_warning_deleteCategory));

        builder.setView(view);

        builder.setNegativeButton(v.getResources().getString(R.string.dialog_button_no), ((dialog, which) ->
                navController.navigate(R.id.fragment_ShowCategories)));
        builder.setPositiveButton(v.getResources().getString(R.string.dialog_button_yes), ((dialog, which) -> {
            Presenter.deleteCategory(category, v.getContext());
            Bundle bundle = new Bundle();
            bundle.putSerializable("notification", "categoryDeleted");
            navController.navigate(R.id.fragment_ShowCategories, bundle);
        }));
        builder.show();
    }

    private void editCategoryDialog(Context context, Category category, NavController navController) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(context.getString(R.string.dialog_title_editCategory));

        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setText(category.getCategoryName());

        builder.setView(view);

        builder.setNegativeButton(context.getString(R.string.dialog_button_cancel), ((DialogInterface dialog, int which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(context.getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String categoryName = editText.getText().toString();
            if (categoryName.isEmpty()) {
                makeToast(context, context.getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.updateCategory(category, categoryName.trim().toLowerCase(), context)) {
                    makeToast(context, context.getString(R.string.toast_warning_category));
                } else {
                    closeKeyBoard(view);
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "categoryUpdated");
                    navController.navigate(R.id.fragment_ShowCategories, bundle);
                }
            }
        });
    }

    private void makeToast(Context context, String text) {
        Toast toast =
                Toast.makeText(context,
                        text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void closeKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focus = activity.getCurrentFocus();
        if (focus == null) {
            focus = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public void remove(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder
            implements CategoryContent_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

        public boolean selected;
        public LinearLayout nestedRecyclerview_LinearLayout, optionLinearLayout, arrowLayout;
        public RecyclerView childRecyclerview;
        TextView elementNameTV;

        public LinearLayout layout;
        Adapter_CategoryContent adapter_categoryContent;

        Button editCategory, deleteCategory, addItemToCategory;

        public AdapterViewHolder(View view) {
            super(view);
            elementNameTV = view.findViewById(R.id.card_view_category_name);
            layout = view.findViewById(R.id.card_view_category_layout);

            editCategory = view.findViewById(R.id.card_view_category_editButton);
            deleteCategory = view.findViewById(R.id.card_view_category_deleteButton);
            addItemToCategory = view.findViewById(R.id.card_view_category_addItemButton);

            optionLinearLayout = view.findViewById(R.id.card_view_category_options_layout);
            arrowLayout = view.findViewById(R.id.card_view_category_arrow_layout);

            nestedRecyclerview_LinearLayout = view.findViewById(R.id.card_view_category_nestedRecyclerView_layout);
            childRecyclerview = view.findViewById(R.id.card_view_category_nestedRecyclerView);
        }

        @Override
        public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            adapter_categoryContent.onSwipe(viewHolder, direction, position);
            adapter_categoryContent.notifyItemRemoved(position);
        }
    }
}