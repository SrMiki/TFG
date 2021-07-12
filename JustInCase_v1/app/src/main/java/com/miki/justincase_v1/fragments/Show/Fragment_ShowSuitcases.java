package com.miki.justincase_v1.fragments.Show;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Item_RecyclerItemTouchHelper;
import com.miki.justincase_v1.Swipers.Suitcase_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.Adapter_Suitcase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowSuitcases extends BaseFragment
        implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Suitcase adapter;
    ArrayList<Suitcase> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;
    ItemTouchHelper.SimpleCallback simpleCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

        dataset = Presenter.selectAllSuitcase(getContext());

        if (!dataset.isEmpty()) {
            LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
            linearLayout.setVisibility(View.VISIBLE);

            setHasOptionsMenu(true);

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Suitcase(dataset);
            recyclerView.setAdapter(adapter);

            simpleCallback = new Suitcase_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

//            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), 1);
//            recyclerView.addItemDecoration(dividerItemDecoration);

            adapter.setListener(v -> {
                Suitcase suitcase = dataset.get(recyclerView.getChildAdapterPosition(v));
                editSuitcaseDialog(suitcase);
            });
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            showNotification(bundle);
        }

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
        floatingButton.setOnClickListener(v -> createNewSuitcaseDialog());

        return view;
    }

    private void showNotification(Bundle bundle) {
        String notification = (String) bundle.getSerializable("notification");
        if (notification != null) {
            switch (notification) {
                case "suitcaseCreated":
                    makeToast(getContext(), getString(R.string.toast_created_suitcase));
                    break;
                case "suitcaseUpdated":
                    makeToast(getContext(), getString(R.string.toast_updated_suitcase));
                    break;
            }
        }
    }

    private void createNewSuitcaseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_suitcase, null);

        builder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_suitcase);
        dialogTitle.setText(getString(R.string.dialog_title_newSuitcase));

        EditText nameET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText colorET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);

        EditText weigthET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);

        EditText heigthET = view.findViewById(R.id.card_view_suitcase_heigth);
        EditText widthET = view.findViewById(R.id.card_view_suitcase_width);
        EditText depthET = view.findViewById(R.id.card_view_suitcase_depth);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_add), ((dialog, which) -> {
        }));

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String name = nameET.getText().toString();
            String color = colorET.getText().toString();
            double weigth = Double.parseDouble(weigthET.getText().toString());

            double heigth = Double.parseDouble(heigthET.getText().toString());
            double width = Double.parseDouble(widthET.getText().toString());
            double depth = Double.parseDouble(depthET.getText().toString());

            if (name.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                Suitcase suitcase = new Suitcase(name, color, weigth, heigth, width, depth);
                if (!Presenter.createSuitcase(suitcase, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_suitcase));
                } else {
                    dialog.dismiss();
                    closeKeyBoard(view);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "suitcaseCreated");
                    getNav().navigate(R.id.fragment_ShowSuitcases, bundle);
                }
            }
        });
    }


    private void editSuitcaseDialog(Suitcase suitcase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_suitcase, null);
        builder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_suitcase);
        dialogTitle.setText(getString(R.string.dialog_title_editSuitcase));

        EditText nameET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        EditText colorET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);

        EditText weigthET = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);

        EditText heigthET = view.findViewById(R.id.card_view_suitcase_heigth);
        EditText widthET = view.findViewById(R.id.card_view_suitcase_width);
        EditText depthET = view.findViewById(R.id.card_view_suitcase_depth);

        nameET.setText(suitcase.getName());
        colorET.setText(suitcase.getColor());

        weigthET.setText(String.valueOf(suitcase.getWeight()));

        heigthET.setText(String.valueOf(suitcase.getHeigth()));
        widthET.setText(String.valueOf(suitcase.getWidth()));
        depthET.setText(String.valueOf(suitcase.getDepth()));

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));
        builder.setPositiveButton(getString(R.string.dialog_button_confirm), ((dialog, which) -> {
        }));

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String name = nameET.getText().toString();
            String color = colorET.getText().toString();
            double weigth = Double.parseDouble(weigthET.getText().toString());

            double heigth = Double.parseDouble(heigthET.getText().toString());
            double width = Double.parseDouble(widthET.getText().toString());
            double depth = Double.parseDouble(depthET.getText().toString());
            suitcase.setSuticase(name, color, weigth, heigth, width, depth);
            if (name.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.updateSuitcase(suitcase, getContext())) {
                    makeToast(v.getContext(), getString(R.string.toast_warning_suitcase));
                } else {
                    dialog.dismiss();
                    closeKeyBoard(view);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "suitcaseUpdated");
                    getNav().navigate(R.id.fragment_ShowSuitcases, bundle);
                }
            }
        });
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Suitcase.AdapterViewHolder) {
            int deletedIndex = viewHolder.getAdapterPosition();
            Suitcase suitcase = dataset.get(deletedIndex);
            deleteSuitcaseDialog(viewHolder, suitcase);
        }

    }

    private void deleteSuitcaseDialog(RecyclerView.ViewHolder viewHolder, Suitcase suitcase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);
        builder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(getString(R.string.dialog_title_editSuitcase));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_warning_deleteSuitcase));

        builder.setNegativeButton(getString(R.string.dialog_button_no), ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.fragment_ShowSuitcases);
        }));
        builder.setPositiveButton(getString(R.string.dialog_button_yes), ((dialog, which) -> {
            adapter.removeItem(viewHolder.getAdapterPosition());
            Presenter.deleteSuitcase(suitcase, getContext());
            getNav().navigate(R.id.fragment_ShowSuitcases);
        }));
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
