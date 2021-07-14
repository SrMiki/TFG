package com.miki.justincase_v1.fragments.Show;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.Item_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.Adapter_Item;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;

public class Fragment_ShowItems extends BaseFragment
        implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION};
    private static final int REQUEST_CODE_PERMISSIONS = 1;
    boolean permissions;

    Adapter_Item adapter;
    ArrayList<Item> dataset;
    RecyclerView recyclerView;

    ImageView item_photo;

    FloatingActionButton floatingButton;
    String itemPhotoUri = "";

    String itemName = "";

    String operation;
    private SharedPreferences sp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

        sp = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        permissions = sp.getBoolean("permissions", false);
        if (sp.getInt("noAskMore", 2) > 0) {
            if (allPermissionsGranted()) {
                setPermissions();
            } else {
                ActivityCompat.requestPermissions(
                        getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                SharedPreferences.Editor editor = sp.edit();

                int noAskMore = sp.getInt("noAskMore", 2);
                editor.putInt("noAskMore", noAskMore-1);
                editor.apply();
            }
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("itemOperation") != null) {
                operate(bundle);
            }
            if (bundle.getSerializable("notification") != null) {
                showNotification(bundle);
            }
        }

        dataset = Presenter.selectAllItems(getContext());
        if (!dataset.isEmpty()) {
            LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
            linearLayout.setVisibility(View.VISIBLE);

            setHasOptionsMenu(true);

            recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
            ItemTouchHelper.SimpleCallback simpleCallback = new Item_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter_Item(dataset, getActivity());
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                Item item = dataset.get(recyclerView.getChildAdapterPosition(v));
                itemPhotoUri = item.getItemPhotoURI();
                editItemDialog(item);
                return true;
            });
        }


        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
        floatingButton.setOnClickListener(v -> createNewItemDialog());
        return view;
    }

    private void operate(Bundle bundle) {
        operation = (String) bundle.getSerializable("itemOperation");
        itemName = (String) bundle.getSerializable("itemName");
        itemPhotoUri = (String) bundle.getSerializable("itemPhotoUri");
        switch (operation) {
            case "create":
                createNewItemDialog();
                break;
            case "edit":
                Item item = Presenter.getItemByItemName(getContext(), itemName);
                editItemDialog(item);
                break;
        }
    }

    private void showNotification(Bundle bundle) {
        String notification = (String) bundle.getSerializable("notification");

        switch (notification) {
            case "itemCreated":
                makeToast(getContext(), getString(R.string.toast_created_item));
                break;
            case "itemUpdated":
                makeToast(getContext(), getString(R.string.toast_updated_item));
                break;
        }

    }

    private void createNewItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);
        builder.setView(view);

        item_photo = view.findViewById(R.id.itemPhoto);
        if (permissions) {
            if (!itemPhotoUri.isEmpty()) {
                item_photo.setVisibility(View.VISIBLE);
                item_photo.setImageURI(Uri.parse(itemPhotoUri));
            }
        } else {
            item_photo.setVisibility(View.GONE);
        }

        TextView dialogTitle = view.findViewById(R.id.dialog_title_itemTextview);
        dialogTitle.setText(getString(R.string.dialog_title_newItem));

        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        if (itemName.isEmpty()) {
            editText.setHint(getString(R.string.hint_itemName));
        } else {
            editText.setText(itemName);
        }

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.createItem(itemName, itemPhotoUri, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_item));
                } else {
                    closeKeyBoard(view);
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "itemCreated");
                    getNav().navigate(R.id.fragment_ShowItems, bundle);
                }
            }
        });

        TextView addPhoto = view.findViewById(R.id.itemAlertdialog_addPhoto);
        if (permissions) {
            addPhoto.setOnClickListener(v -> {
                dialog.dismiss();
                itemName = editText.getText().toString();
                AddPhotoDialog(editText.getText().toString(), "create");
            });
        } else {
            addPhoto.setVisibility(View.GONE);
        }
    }


    private void editItemDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_itemTextview);
        dialogTitle.setText(getString(R.string.dialog_title_editItem));

        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        editText.setText(item.getItemName());

        item_photo = view.findViewById(R.id.itemPhoto);
        if (permissions) {
            if (!itemPhotoUri.isEmpty()) {
                item_photo.setVisibility(View.VISIBLE);
                item_photo.setImageURI(Uri.parse(itemPhotoUri));
            }
        } else {
            item_photo.setVisibility(View.GONE);
        }


        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((DialogInterface dialog, int which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(getString(R.string.dialog_button_confirm), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.updateItem(item, itemName.trim().toLowerCase(), itemPhotoUri, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_item));
                } else {
                    closeKeyBoard(view);
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "itemUpdated");
                    getNav().navigate(R.id.fragment_ShowItems, bundle);
                }
            }
        });

        TextView addPhoto = view.findViewById(R.id.itemAlertdialog_addPhoto);
        if (permissions) {
            addPhoto.setText(R.string.text_changephoto);
            addPhoto.setOnClickListener(v -> {
                dialog.dismiss();
                itemName = editText.getText().toString();
                AddPhotoDialog(itemName, "edit");
            });
        } else {
            addPhoto.setVisibility(View.GONE);
        }
    }

    private void AddPhotoDialog(String itemName, String operation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_photo, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_photo);
        dialogTitle.setText(getString(R.string.dialog_title_photo));

        ImageView photo = view.findViewById(R.id.take_photo);
        ImageView galery = view.findViewById(R.id.galery);

        builder.setView(view);
        AlertDialog show = builder.show();

        Bundle bundle = new Bundle();
        bundle.putSerializable("itemName", itemName);
        bundle.putSerializable("itemOperation", operation);
        this.operation = operation;
        this.itemName = itemName;

        galery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                mGetContent.launch("image/*");
                startActivityForResult(intent.createChooser(intent, "selec"), 5);
                show.dismiss();
            }
        });

        photo.setOnClickListener(v -> {
            show.dismiss();
            getNav().navigate(R.id.cameraX, bundle);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (requestCode == 5) {
                Uri saveUri = data.getData();
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemName", itemName);
                bundle.putSerializable("itemOperation", operation);
                bundle.putSerializable("itemPhotoUri", saveUri.toString());
                getNav().navigate(R.id.fragment_ShowItems, bundle);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean allPermissionsGranted() {
        for (String permission :
                REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setPermissions();
            } else {
                Toast.makeText(getContext(),
                        getString(R.string.toast_warning_cameraPermissions),
                        Toast.LENGTH_SHORT).show();
                getNav().navigate(R.id.fragment_ShowItems);
                showExplanation();
            }
        } else {
            getNav().navigate(R.id.fragment_ShowItems);
        }
    }

    private void setPermissions() {
        sp = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("permissions", true);
        editor.putInt("noAskMore", 0);
        editor.apply();
        getNav().navigate(R.id.fragment_ShowItems);
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(getString(R.string.dialog_title_warning));

        TextView textView = view.findViewById(R.id.itemAlertdialog_editText);
        textView.setText(R.string.requestPermission);

        builder.setView(view);

        builder.setPositiveButton(getString(R.string.dialog_button_ok), (dialog, which) -> {
        });
        builder.show();
    }


    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Item.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            String name = dataset.get(deletedIndex).getItemName();
            Item deletedItem = dataset.get(deletedIndex);

            adapter.remove(viewHolder.getAdapterPosition());

            restoreDeletedElement(viewHolder, name, deletedItem, deletedIndex);
            //Note: if the item it's deleted and then restore, only restore in item. You must
            //yo add again in Baggages and Categorys.
            Presenter.deleteItem(deletedItem, getContext());
            getNav().navigate(R.id.fragment_ShowItems);

        }
    }

    public void restoreDeletedElement(RecyclerView.ViewHolder viewHolder, String name, Item
            deletedItem, int deletedIndex) {
        String deleted = getString(R.string.toast_deleted);
        String restore = getString(R.string.snackbar_restore);
        Snackbar snackbar = Snackbar.make(((Adapter_Item.AdapterViewHolder) viewHolder).layout,
                name + " " + deleted,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(restore, v -> {
            adapter.restoreItem(deletedItem, deletedIndex);
            Presenter.createItem(deletedItem.getItemName(), getContext());
            getNav().navigate(R.id.fragment_ShowItems);
        });

        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();
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
        searchView.setOnCloseListener(() -> false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}