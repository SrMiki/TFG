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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Template_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.Adapter_Template;
import com.miki.justincase_v1.db.entity.Template;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowTemplates extends BaseFragment implements Template_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Template adapter;
    ArrayList<Template> dataset;
    RecyclerView recyclerView;

    FloatingActionButton floatingButton;
    Template focusTemplate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

        dataset = Presenter.selectAllTemplates(getContext());

        if (!dataset.isEmpty()) {
            LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
            linearLayout.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);
        }

        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Template(dataset, getActivity());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new Template_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);

        floatingButton.setOnClickListener(v -> newTemplateDialog());

        adapter.setOnClickListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            focusTemplate = dataset.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("template", focusTemplate);
            getNav().navigate(R.id.action_fragment_ShowTemplates_to_fragment_ShowTemplateElements, bundle);
        });

        adapter.setOnLongClickListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            focusTemplate = dataset.get(position);
            editTemplateDialog();
            return true;
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            showNotification(bundle);
        }
        return view;
    }

    private void showNotification(Bundle bundle) {
        String notification = (String) bundle.getSerializable("notification");
        if (notification != null) {
            switch (notification) {
                case "templateCreated":
                    makeToast(getContext(), getString(R.string.toast_created_template));
                    break;
                case "templateUpdated":
                    makeToast(getContext(), getString(R.string.toast_updated_template));
                    break;
                case "templateDeleted":
                    makeToast(getContext(), getString(R.string.toast_deleted_template));
                    break;
            }
        }
    }

    private void editTemplateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(getString(R.string.dialog_title_editTemplate));

        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setText(focusTemplate.getTemplateName());

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = editText.getText().toString();
            if (name.isEmpty()) {
                makeToast(v.getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.updateTemplate(focusTemplate, name, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_template));
                } else {
                    closeKeyBoard(view);
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "templateUpdated");
                    getNav().navigate(R.id.fragment_ShowTemplates, bundle);
                }
            }
        });
    }

    private void newTemplateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(getString(R.string.dialog_title_newTemplate));


        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setHint(getString(R.string.fragment_createTemplate_hint));
        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_create), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (editText.getText().toString().isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                String name = editText.getText().toString();
                if (!Presenter.createTemplate(name, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_template));
                } else {
                    closeKeyBoard(view);
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "templateCreated");
                    getNav().navigate(R.id.fragment_ShowTemplates, bundle);
                    addItemsDialog();
                }
            }
        });
    }

    private void addItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(R.string.dialog_ask_addItemsToTemplate);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(R.string.dialog_title_newTemplate);


        builder.setView(view);

        builder.setNegativeButton(R.string.dialog_button_notNow, ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(R.string.dialog_button_yes, ((dialog, which) -> {
            ArrayList<Template> allTemplate = Presenter.getAllTemplate(getContext());
            Template template = allTemplate.get(allTemplate.size() - 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable("template", template);
            getNav().navigate(R.id.fragment_Add_Item_To_Template, bundle);
        }));
        builder.show();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) search.getActionView();

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Template.AdapterViewHolder) {
            warningTemplateDialog(viewHolder);
        }
    }


    private void warningTemplateDialog(RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(R.string.dialog_title_warning);

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(R.string.dialog_warning_deleteTemplate);

        builder.setView(view);

        builder.setNegativeButton(R.string.dialog_button_cancel, ((dialog, which) -> getNav().navigate(R.id.fragment_ShowTemplates)));

        builder.setPositiveButton(R.string.dialog_button_yes, ((dialog, which) -> deleteTemplate(viewHolder)));
        builder.show();
    }

    private void deleteTemplate(RecyclerView.ViewHolder viewHolder) {
        int deletedIndex = viewHolder.getAdapterPosition();
        focusTemplate = dataset.get(deletedIndex);
        adapter.remove(viewHolder.getAdapterPosition());
        Presenter.deleteTemplate(focusTemplate, getContext());
        Bundle bundle = new Bundle();
        bundle.putSerializable("notification", "templateDeleted");
        getNav().navigate(R.id.fragment_ShowTemplates,bundle);
    }
}
