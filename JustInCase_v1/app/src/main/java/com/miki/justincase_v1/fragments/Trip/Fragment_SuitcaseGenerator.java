package com.miki.justincase_v1.fragments.Trip;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presenter;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.othersAdapters.Adapter_Categories_in_SuitcaseGenerator;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.BaseFragment;

import java.time.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class Fragment_SuitcaseGenerator extends BaseFragment {

    public TextView tripName_TextView, tripTravelDate_TextView, tripReturnDate_TextView;
    public LinearLayout layout;

    TextView recyclerTitle;

    Trip trip;
    HandLuggage handLuggage;
    Suitcase suitcase;

    ArrayList<Item> suggestedItem;
    private ArrayList<Category> dataset, selectedCategory;

    int travelDay, travelMonth, travelYear, returnDay, returnMonth, returnYear;

    ToggleButton toggleButton_snow, toggleButton_beach, toggleButton_hiking, toggleButton_sport,
            toggleButton_sailing, toggleButton_pool, toggleButton_photografy, toggleButton_camping,
            toggleButton_personalCare, toggleButton_pet, toggleButton_baby;

    private RecyclerView recyclerView;
    private Adapter_Categories_in_SuitcaseGenerator adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suitcase_generator, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {

            toggleButton_snow = view.findViewById(R.id.toggle_snow);
            toggleButton_beach = view.findViewById(R.id.toggle_beach);
            toggleButton_hiking = view.findViewById(R.id.toggle_hiking);
            toggleButton_sport = view.findViewById(R.id.toggle_sport);

            toggleButton_sailing = view.findViewById(R.id.toggle_sailing);
            toggleButton_pool = view.findViewById(R.id.toggle_pool);
            toggleButton_photografy = view.findViewById(R.id.toggle_photografy);
            toggleButton_camping = view.findViewById(R.id.toggle_camping);

            toggleButton_personalCare = view.findViewById(R.id.toggle_personalCare);

            toggleButton_pet = view.findViewById(R.id.toggle_pets);
            toggleButton_baby = view.findViewById(R.id.toggle_baby);


            handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");

            suitcase = Presenter.getSuitcase(handLuggage, getContext());

            trip = (Trip) bundle.getSerializable("trip");
            setTrip(view);

            TextView datysTV = view.findViewById(R.id.algoritm_days);
            int days = getTotalDays(trip);
            datysTV.setText(days + "");


            recyclerTitle = view.findViewById(R.id.recyclerview_categorySuitcaseGenerator_title);
            recyclerView = view.findViewById(R.id.myCategory_recyclerview);

            dataset = Presenter.getAllCategories(getContext());
            if (!dataset.isEmpty()) {
                Presenter.removeCategorySelectedState(dataset, getContext());
                recyclerTitle.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            selectedCategory = new ArrayList<>();

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            adapter = new Adapter_Categories_in_SuitcaseGenerator(dataset);
            recyclerView.setAdapter(adapter);

            adapter.setListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                Category category = dataset.get(position);
                if (!contains(selectedCategory, category)) {
                    selectedCategory.add(category);
                    category.setSelectedState(true);
                } else {
                    category.setSelectedState(false);
                    selectedCategory.remove(category);
                }
                adapter.notifyItemChanged(position);
            });

            FloatingActionButton btn = view.findViewById(R.id.fragment_algoritm_btn);
            btn.setOnClickListener(v -> {
                addSuggestedBaggage(v);
                getNav().navigate(R.id.action_algoritm_to_fragment_ShowBaggageByItem, bundle);
            });
        }
        return view;
    }

    private boolean contains(ArrayList<Category> selectedCategory, Category compareCategory) {
        for (Category category : selectedCategory) {
            if (category.getCategoryName().equals(compareCategory.getCategoryName())) {
                return true;
            }
        }
        return false;
    }


    private void addSuggestedBaggage(View v) {
        suggestedItem = new ArrayList<>();
        // 1) limpiamos de baggage la maleta
        Presenter.clearHandLuggage(handLuggage, getContext());

        // 2) Configuramos la lista

        // 2) Extraemos la lista de items sugeridos
        String[] stringArray;
        stringArray = v.getResources().getStringArray(R.array.suggested_Items);
        extractXMLlist(stringArray);

        if (toggleButton_snow.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.snow_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_beach.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.beach_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_hiking.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.hiking_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_sport.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.sports_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_sailing.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.sailing_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_pool.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.pool_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_photografy.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.photografy_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_camping.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.camping_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_personalCare.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.personal_care);
            extractXMLlist(stringArray);
        }

        if (toggleButton_pet.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.pet_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_baby.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.baby_Items);
            extractXMLlist(stringArray);
        }

        // 3) agregamos a la BD la lista
        //en caso de que existan, se sobreescribe para obtener su ID
        Presenter.addSuggestedItemList(suggestedItem, getContext());

        if (!selectedCategory.isEmpty()) {
            for (Category category :
                    selectedCategory) {
                ArrayList<Item> itemArrayList = Presenter.selectItemFromThisCategory(category, getContext());
                suggestedItem.addAll(itemArrayList);
            }
        }

        //quitar duplicados en caso de que algun item de la lista xml ya exista y esté en una de las categorias
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            suggestedItem = (ArrayList<Item>) suggestedItem.stream().distinct().collect(Collectors.toList());
        } else {

        }
        // 4) Se agrega la lista de items a la maleta
        Presenter.createBaggageByItems(suggestedItem, handLuggage, getContext());

    }

    private void extractXMLlist(String[] stringArray) {
        for (String s : stringArray) {
            Item aux = new Item(s, "");
            suggestedItem.add(aux);
        }
    }

    private int getTotalDays(Trip trip) {
        String travelDate = trip.getTravelDate();
        setTravelDate(travelDate);

        LocalDate LocalTravelDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTravelDate = LocalDate.of(travelYear, travelMonth, travelDay);


            String returnDate = trip.getReturnDate();
            if (!returnDate.isEmpty()) {
                setReturnDate(returnDate);
                LocalDate LocalReturnDate = LocalDate.of(returnYear, returnMonth, returnDay);
                return (int) DAYS.between(LocalTravelDate, LocalReturnDate);
            }
        }
        return 0;
    }

    private void setReturnDate(String returnDate) {
        if (!returnDate.isEmpty()) {
            String[] split = returnDate.split(" / ");
            returnDay = Integer.parseInt(split[0]);
            returnMonth = Integer.parseInt(split[1]);
            returnYear = Integer.parseInt(split[2]);
        } else { // si no pone viaje de vuelta ?? Supongamos planing de 1 semana
            returnDay = travelDay + 7;
            returnYear = travelYear;
            returnMonth = travelMonth;
        }
    }

    private void setTravelDate(String travelDate) {
        String[] split = travelDate.split(" / ");
        travelDay = Integer.parseInt(split[0]);
        travelMonth = Integer.parseInt(split[1]);
        travelYear = Integer.parseInt(split[2]);
    }


    private void setTrip(View view) {
        tripName_TextView = view.findViewById(R.id.fragment_focusTrip_tripDestino);
        tripTravelDate_TextView = view.findViewById(R.id.fragment_focusTrip_tripDate);
        tripReturnDate_TextView = view.findViewById(R.id.fragment_focusTrip_returnDate);

        String destination = trip.getDestination();
        String travelDate = trip.getTravelDate();
        String returnDate = trip.getReturnDate();

        tripName_TextView.setText(destination);
        tripTravelDate_TextView.setText(travelDate);
        if (returnDate.isEmpty()) {
            tripReturnDate_TextView.setVisibility(View.GONE);
        } else {
            tripReturnDate_TextView.setText(returnDate);

        }
    }

}
