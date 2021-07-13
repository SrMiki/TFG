package com.miki.justincase_v1.fragments.Show;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.Swipers.Item_RecyclerItemTouchHelper;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Template;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowTemplateElements extends BaseFragment
        implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Item adapter;
    RecyclerView recyclerView;
    ArrayList<Item> dataset;

    FloatingActionButton floatingButton;
    Bundle bundle;
    Item focusItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

        setHasOptionsMenu(true);

        bundle = getArguments();
        if (bundle != null) {

            Template template = (Template) bundle.getSerializable("template");
            dataset = Presenter.selectItemFromThisTemplate(template, getContext());

            if (!dataset.isEmpty()) {
                LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
                linearLayout.setVisibility(View.VISIBLE);
            }

            TextView textView = view.findViewById(R.id.showEntity_title);
            String title = getString(R.string.toolbar_template) + ": " + template.getTemplateName();
            textView.setText(title);
            textView.setVisibility(View.VISIBLE);

            floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
            floatingButton.setOnClickListener(v -> getNav().navigate(R.id.fragment_Add_Item_To_Template, bundle));

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Item(dataset, getActivity());
            recyclerView.setAdapter(adapter);

            ItemTouchHelper.SimpleCallback simpleCallback = new Item_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

            adapter.setListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                focusItem = dataset.get(position);
                editItemDialog();
                return true;
            });

        }
        return view;
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Item.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            String name = dataset.get(deletedIndex).getItemName();
            Item deletedItem = dataset.get(deletedIndex);

            adapter.remove(viewHolder.getAdapterPosition());

//            restoreDeletedElement(viewHolder, name, deletedItem, deletedIndex);
            //Note: if the item it's deleted and then restore, only restore in item. You must
            //yo add again in Baggages and Categorys.
            Presenter.removeItemFromThisTemplate(deletedItem, getContext());
            getNav().navigate(R.id.fragment_ShowTemplateElements, bundle);

        }
    }

    private void editItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(getString(R.string.dialog_title_editItem));


        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setText(focusItem.getItemName());

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_confirm), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String itemName = editText.getText().toString();
            boolean update = Presenter.updateItem(focusItem, itemName, focusItem.getItemPhotoURI(), getContext());
            if (update) {
                makeToast(getContext(), getString(R.string.toast_updated_item));
                getNav().navigate(R.id.fragment_ShowTemplateElements, bundle);
            } else {
                makeToast(getContext(), getString(R.string.toast_warning_item));
            }
        });
    }

}
