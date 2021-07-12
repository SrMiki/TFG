package com.miki.justincase_v1.fragments.Show;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.camera.core.ImageCapture;
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

import java.io.IOException;
import java.util.ArrayList;

public class Fragment_ShowItems extends BaseFragment
        implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Item adapter;
    ArrayList<Item> dataset;
    RecyclerView recyclerView;

    ImageView item_photo;

    FloatingActionButton floatingButton;
    private String itemPhotoUri = "";

    private ImageCapture itemPhoto;

    String itemName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

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
                editItemDialog(item);
                return true;
            });
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            showNotification(bundle);
        }

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
        floatingButton.setOnClickListener(v -> createNewItemDialog());
        return view;
    }

    private void showNotification(Bundle bundle) {
        String notification = (String) bundle.getSerializable("notification");
        if (notification != null) {
            switch (notification) {
                case "itemCreate":
                    makeToast(getContext(), getString(R.string.toast_created_item));
                    break;
                case "itemUpdated":
                    makeToast(getContext(), getString(R.string.toast_updated_item));
                    break;
            }
        }
    }

    private void createNewItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);
        builder.setView(view);

        item_photo = view.findViewById(R.id.itemPhoto);
        TextView dialogTitle = view.findViewById(R.id.dialog_title_itemTextview);
        dialogTitle.setText(getString(R.string.dialog_title_newItem));

        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        editText.setHint(getString(R.string.hint_itemName));

        TextView addPhoto = view.findViewById(R.id.itemAlertdialog_addPhoto);

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.createItem(itemName, itemPhotoUri, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_item));
                } else {
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "itemCreated");
                    getNav().navigate(R.id.fragment_ShowItems, bundle);
                }
            }
        });

        //ItemPhoto!
        addPhoto.setOnClickListener(v -> {
            dialog.dismiss();
            itemName = editText.getText().toString();
            AddPhotoDialog(editText.getText().toString(), true);
        });
    }

    private void editItemDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_itemTextview);
        dialogTitle.setText(getString(R.string.dialog_title_editItem));

        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        editText.setText(item.getItemName());

        ImageView itemPhoto = view.findViewById(R.id.itemPhoto);
        if (item.getItemPhotoURI() != null && !item.getItemPhotoURI().isEmpty()) {
            Bitmap bitmap;
            try {
                if (Build.VERSION.SDK_INT < 28) { //Android 9
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(item.getItemPhotoURI()));
                    int alto = 25;
                    int ancho = 25;
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, ancho, alto, true);
                    itemPhoto.setImageBitmap(resizedBitmap);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getActivity().getContentResolver(), Uri.parse(item.getItemPhotoURI()));
                    bitmap = ImageDecoder.decodeBitmap(source);
                    itemPhoto.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            itemPhoto.setImageResource(R.drawable.ic_photo);
        }

        TextView addPhoto = view.findViewById(R.id.itemAlertdialog_addPhoto);
        addPhoto.setText(R.string.text_changephoto);

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((DialogInterface dialog, int which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.updateItem(item, itemName.trim().toLowerCase(), getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_item));
                } else {
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "itemUpdated");
                    getNav().navigate(R.id.fragment_ShowItems, bundle);
                }
            }
        });

        addPhoto.setOnClickListener(v -> {
            dialog.dismiss();
            itemName = editText.getText().toString();
            AddPhotoDialog(itemName, false);
        });
    }

    /**
     * @param itemName
     * @param operation >> false == edit, true == create
     *                  that's cause camaraX need to know
     */
    private void AddPhotoDialog(String itemName, boolean operation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_photo, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_photo);
        dialogTitle.setText(getString(R.string.dialog_title_photo));

        ImageView photo = view.findViewById(R.id.take_photo);
        ImageView galery = view.findViewById(R.id.galery);

        builder.setView(view);
        AlertDialog show = builder.show();

        galery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                mGetContent.launch("image/*");
                startActivityForResult(intent.createChooser(intent, "selec"), 10);
                show.dismiss();
            }
        });

        photo.setOnClickListener(v -> {
            show.dismiss();
            Bundle bundle = new Bundle();
            bundle.putSerializable("itemName", itemName);
            bundle.putSerializable("itemOperation", operation);
            getNav().navigate(R.id.cameraX, bundle);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (requestCode == 10) {
                Uri itemUri = data.getData();

//                if (operation.equals("itemCreated")) {
//                    createItemDialog(itemUri);
//                } else if (operation.equals("itemUpdated")) {
//                    editItemDialog(itemUri);
//                } else {
//
//                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createItemDialog(Uri itemUri) {
        String itemPhotoUri = itemUri.toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_itemTextview);
        dialogTitle.setText(getString(R.string.dialog_title_newItem));

        item_photo = view.findViewById(R.id.itemPhoto);
        item_photo.setVisibility(View.VISIBLE);
        item_photo.setImageURI(itemUri);

        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        editText.setText(itemName);

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });


        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.createItem(itemName, itemPhotoUri, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_item));
                } else {
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "itemCreated");
                    getNav().navigate(R.id.fragment_ShowItems, bundle);
                }
            }
        });
    }

    private void editItemDialog(Uri itemUri) {
        String itemPhotoUri = itemUri.toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_itemTextview);
        dialogTitle.setText(getString(R.string.dialog_title_editItem));

        item_photo = view.findViewById(R.id.itemPhoto);
        item_photo.setVisibility(View.VISIBLE);
        item_photo.setImageURI(itemUri);

        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        editText.setText(itemName);

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) v -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                if (!Presenter.createItem(itemName, itemPhotoUri, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_warning_item));
                } else {
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", "itemCreated");
                    getNav().navigate(R.id.fragment_ShowItems, bundle);
                }
            }
        });
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

    public void restoreDeletedElement(RecyclerView.ViewHolder viewHolder, String name, Item deletedItem, int deletedIndex) {
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