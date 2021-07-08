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

public class Fragment_ShowItems extends BaseFragment implements Item_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Adapter_Item adapter;
    ArrayList<Item> dataset;
    RecyclerView recyclerView;

    ImageView item_photo;

    FloatingActionButton floatingButton;
    private String itemPhotoUri;

    private ImageCapture itemPhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_addbutton, container, false);

        floatingButton = view.findViewById(R.id.fragment_show_entity_btn_add);
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        dataset = Presenter.selectAllItems(getContext());
        if (!dataset.isEmpty()) {
            LinearLayout linearLayout = view.findViewById(R.id.showEntity_swipeLayout);
            linearLayout.setVisibility(View.VISIBLE);
        }

        setHasOptionsMenu(true);

        floatingButton.setOnClickListener(v -> createNewItemDialog());

        recyclerView = view.findViewById(R.id.fragment_show_entity_recyclerview);
        ItemTouchHelper.SimpleCallback simpleCallback = new Item_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Item(dataset, getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setListener((View v) -> {
            Item item = dataset.get(recyclerView.getChildAdapterPosition(v));
            editItemDialog(item);
            return true;
        });


        return view;
    }

    private void createNewItemDialog() {
        itemPhotoUri = "";

        // --  Dialog -- //
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getString(R.string.dialog_title_newItem));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);
        builder.setView(view);

        item_photo = view.findViewById(R.id.itemPhoto);
        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        editText.setHint(getString(R.string.hint_itemName));

        TextView addPhoto = view.findViewById(R.id.itemAlertdialog_addPhoto);

        // --  Dialog -- //


        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_add), (dialog, which) -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_emptyName));
            } else {
                if (Presenter.createItem(itemName, itemPhotoUri, getContext())) {
                    makeToast(getContext(), getString(R.string.toast_itemCreated));
                    dialog.dismiss();
                    getNav().navigate(R.id.fragment_ShowItems);
                } else {
                    makeToast(getContext(), getString(R.string.toast_error_createItem));
                }
            }
        });

        AlertDialog show = builder.show();

        //ItemPhoto!
        addPhoto.setOnClickListener(v -> {
            show.dismiss();
            AddPhotoDialog();
        });

    }

    private void editItemDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.text_editItem));

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_item, null);

        EditText editText = view.findViewById(R.id.itemAlertdialog_editText);
        editText.setText(item.getItemName());

        ImageView itemPhoto = view.findViewById(R.id.itemPhoto);
        if (!item.getItemPhotoURI().isEmpty()) {
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

        addPhoto.setOnClickListener(v -> AddPhotoDialog());

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_cancel), ((DialogInterface dialog, int which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_edit), ((dialog, which) -> {
            String itemName = editText.getText().toString();
            if (itemName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_emptyName));
                dialog.dismiss();
            } else {
                boolean updateItem = Presenter.updateItem(item, itemName.trim().toLowerCase(), getContext());
                if (updateItem) {
                    makeToast(getContext(), getString(R.string.toast_itemUpdated));
                    getNav().navigate(R.id.fragment_ShowItems);
                } else {
                    makeToast(getContext(), getString(R.string.toast_warning_updateItem));
                }
            }
        }));
        builder.show();
    }


    private void AddPhotoDialog() {
        // --  Dialog -- //
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.text_item));

        builder.setTitle(R.string.addPhotoDialogTitle);

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_photo, null);
        builder.setView(view);

        ImageView photo = view.findViewById(R.id.take_photo);
        ImageView galery = view.findViewById(R.id.galery);

        builder.setNegativeButton(getString(R.string.dialog_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.dialog_add), ((dialog, which) -> dialog.dismiss()));
        AlertDialog show = builder.show();
        // --  Dialog -- //

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
            getNav().navigate(R.id.cameraX);
        });
    }


    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_Item.AdapterViewHolder) {

            int deletedIndex = viewHolder.getAdapterPosition();
            String name = dataset.get(deletedIndex).getItemName();
            Item deletedItem = dataset.get(deletedIndex);

            adapter.removeItem(viewHolder.getAdapterPosition());

            restoreDeletedElement(viewHolder, name, deletedItem, deletedIndex);
            //Note: if the item it's deleted and then restore, only restore in item. You must
            //yo add again in Baggages and Categorys.
            Presenter.deleteItem(deletedItem, getContext());
            getNav().navigate(R.id.fragment_ShowItems);

        }
    }

    public void restoreDeletedElement(RecyclerView.ViewHolder viewHolder, String name, Item deletedItem, int deletedIndex) {
        String deleted = getString(R.string.toast_hasBeenDeleted);
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