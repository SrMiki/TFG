package com.miki.justincase_v1.fragments.Trip;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.Swipers.String_RecyclerItemTouchHelper;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_StringList;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_CreateTrip extends BaseFragment implements String_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    EditText travelDate, returnDate, transport;
    TextView destination, size;

    LinearLayout membersLayout;
    TextView swipeTextView;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch dateSwitch;
    NumberPicker numberPicker;

    FloatingActionButton btn_finish;
    Button btn_country;

    private Trip trip;

    RecyclerView recyclerView;
    ArrayList<String> dataset;
    Adapter_StringList adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_trip, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.fragment_createTrip_returnDateLayout);

        //members Layout
        membersLayout = view.findViewById(R.id.membersLayout);

        destination = view.findViewById(R.id.fragment_createTrip_destination);
        destination.setOnClickListener(v -> tripDestinationDialog());

        travelDate = view.findViewById(R.id.activity_createTrip_travelDate);
        returnDate = view.findViewById(R.id.fragment_createTrip_tripReturnDate);
        transport = view.findViewById(R.id.fragment_createTrip_transport);

        btn_finish = view.findViewById(R.id.fragment_createTrip_btn_finish);


        //SWITCH BUTTON
        dateSwitch = view.findViewById(R.id.fragment_createTrip_switch);
        dateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayout.setVisibility(View.GONE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                travelDate.setText("");
            }
            returnDate.setText("");
        });

        travelDate.setOnClickListener(v -> {
            showDatePickerDialog(travelDate);
            returnDate.setText("");
        });

        returnDate.setOnClickListener(v -> {
            //cannot used until insert TravelDate
            if (!dateSwitch.isChecked())
                if (!travelDate.getText().toString().isEmpty()) {
                    showDatePickerDialog(travelDate, returnDate);
                }
        });


        dataset = new ArrayList<>();
        recyclerView = view.findViewById(R.id.fragment_createTrip_recyclerview);

        size = view.findViewById(R.id.fragment_createTrip_tripSize);
        size.setOnClickListener(this::changeMemberNumber);

        trip = new Trip();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("trip") != null) { //edit trip!
                trip = (Trip) bundle.getSerializable("trip");

                destination.setText(trip.destination);
                travelDate.setText(trip.travelDate);
                transport.setText(trip.travelTransport);

                if (!trip.getReturnDate().isEmpty()) {
                    returnDate.setText(trip.getReturnDate());
                }
                size.setText(String.valueOf(trip.getMemberSize()));
                dataset.addAll(Arrays.asList(trip.getMembers().split(", ")));
            }
        }

        changeMembersForm();

        btn_country = view.findViewById(R.id.fragment_createTrip_countryBtn);
        btn_country.setOnClickListener(v -> {
            saveTrip();
            Bundle temp = new Bundle();
            temp.putSerializable("trip", trip);
            if (bundle.getSerializable("operation") != null) {
                temp.putSerializable("operation", "create");
            }
            getNav().navigate(R.id.fragment_CountryList, temp);
        });


        btn_finish.setOnClickListener(v -> {
            if (bundle.getSerializable("operation") != null) { //edit trip!
                saveTrip();
                if (!Presenter.createTrip(trip, getContext())) {
                    makeToast(getContext(), getString(R.string.dialog_warning_createTrip));
                } else {
                    ask_newHandLuggageDialog();
                }
            } else { //new trip
                saveTrip();
                Presenter.updateTrip(trip, getContext());
                getNav().navigate(R.id.action_fragment_CreateTrip_to_fragment_ShowTrips);
            }
        });
        return view;
    }

    private void saveTrip() {
        String tripDestination = destination.getText().toString();
        String tripTravelDate = travelDate.getText().toString();
        String tripReturnDate = returnDate.getText().toString();

        String travelTransport = transport.getText().toString();
        String returnTransport = "";

        String members;
        int memberSize = Integer.parseInt(size.getText().toString());
        if (memberSize == 1) {
            members = "";
        } else {
            members = datasetToString();
        }
        trip.setTrip(tripDestination, tripTravelDate, tripReturnDate, travelTransport, returnTransport, members, memberSize);
    }

    private String datasetToString() {
        String s = "";
        for (String member : dataset) {
            s += member + ", ";
        }
        if (s.isEmpty()) {
            return "";
        } else {
            return s.substring(0, s.length() - 2);
        }
    }

    private void tripDestinationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(getString(R.string.dialog_title_editTripDestination));

        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setText(destination.getText());

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((DialogInterface dialog, int which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(getString(R.string.dialog_button_confirm), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String string = editText.getText().toString();
            if (string.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                destination.setText(string);
                closeKeyBoard(view);
                dialog.dismiss();
            }
        });
    }

    private void ask_newHandLuggageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_textview, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_textview);
        dialogTitle.setText(getString(R.string.dialog_title_addHandLuggageDialog));

        TextView textView = view.findViewById(R.id.dialog_message_textview);
        textView.setText(getString(R.string.dialog_ask_addHandluggage));

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_no), ((dialog, which) -> {
            dialog.dismiss();
            getNav().navigate(R.id.action_fragment_CreateTrip_to_fragment_ShowTrips);
        }));
        builder.setPositiveButton(getString(R.string.dialog_button_yes), ((dialog, which) -> {
            Bundle bundle = new Bundle();
            // We can't use the new Trip cause we need the trip ID
            // The new Trip ID is the last register on the DataBase (size()-1 of all trips)
            ArrayList<Trip> allTrips = Presenter.getAllTrips(getContext());
            Trip trip = allTrips.get(allTrips.size() - 1);
            bundle.putSerializable("trip", trip);
            getNav().navigate(R.id.action_fragment_CreateTrip_to_fragment_Add_HandLuggage, bundle);
        }));
        builder.show();
    }

    private void changeMemberNumber(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_count, null);
        builder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.alertdialog_title_count);
        dialogTitle.setText(getString(R.string.dialog_text_itemCount));

        numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);
        numberPicker.setValue(Integer.parseInt(size.getText().toString()));

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((dialog, which) -> dialog.dismiss()));

        builder.setPositiveButton(getString(R.string.text_edit), ((dialog, which) -> {
            size.setText(String.valueOf(numberPicker.getValue()));
            dialog.dismiss();
            changeMembersForm();
        }));
        builder.show();
    }

    private void changeMembersForm() {
        int length = Integer.parseInt(size.getText().toString());
        if (length > 1) {
            recyclerView.setVisibility(View.VISIBLE);

            for (int i = dataset.size(); i < length; i++) {
                dataset.add(getString(R.string.text_member) + " " + (i + 1));
            }

            //1st member
            //When you change the size member, the 1st is blank
            if (dataset.get(0) != null && dataset.get(0).isEmpty()) {
                dataset.set(0, getString(R.string.text_member) + " " + (1));
            }

            adapter = new Adapter_StringList(dataset);
            adapter.setArrow(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

            if(dataset != null){
                membersLayout.setVisibility(View.VISIBLE);
            }


            ItemTouchHelper.SimpleCallback simpleCallback = new String_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

            adapter.setListener(v -> {
                int adapterPosition = recyclerView.getChildAdapterPosition(v);
                String memberName = dataset.get(adapterPosition);
                changeMemberName(adapterPosition, memberName);
            });
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void changeMemberName(int adapterPosition, String memberName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_edittext, null);

        TextView dialogTitle = view.findViewById(R.id.dialog_title_edittext);
        dialogTitle.setText(getString(R.string.dialog_title_editTripMember));

        EditText editText = view.findViewById(R.id.dialog_edittext_input);
        editText.setText(memberName);

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.dialog_button_cancel), ((DialogInterface dialog, int which) -> dialog.dismiss()));

        //still need for older versions of Android
        builder.setPositiveButton(getString(R.string.dialog_button_add), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newMemberName = editText.getText().toString();
            if (newMemberName.isEmpty()) {
                makeToast(getContext(), getString(R.string.toast_warning_emptyName));
            } else {
                dataset.set(adapterPosition, newMemberName);
                adapter.notifyItemChanged(adapterPosition);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_StringList.AdapterViewHolder) {
            adapter.remove(viewHolder.getAdapterPosition());
            size.setText(String.valueOf(adapter.getItemCount()));
        }
    }
}