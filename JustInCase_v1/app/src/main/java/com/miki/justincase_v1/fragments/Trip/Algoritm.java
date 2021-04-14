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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.adapters.Adapter_CategoryInAlgortim;
import com.miki.justincase_v1.adapters.Adapter_Item;
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

public class Algoritm extends BaseFragment {

    public TextView tripName_TextView, tripDate_TextView;
    public LinearLayout layout;

    Trip trip;
    HandLuggage handLuggage;
    Suitcase suitcase;

    ArrayList<Item> suggestedItem;
    ArrayList<Category> selectedCategory;

    int travelDay, travelMonth, travelYear, returnDay, returnMonth, returnYear;

    ToggleButton toggleButton_snow, toggleButton_beach, toggleButton_work, toggleButton_sport;
    private RecyclerView recyclerView;
    private Adapter_CategoryInAlgortim adapter;
    private ArrayList<Category> dataset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_algoritm, container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedCategory = new ArrayList<>();

            toggleButton_beach = view.findViewById(R.id.toggle_beach);
            toggleButton_snow = view.findViewById(R.id.toggle_snow);
            toggleButton_work = view.findViewById(R.id.toggle_work);
            toggleButton_sport = view.findViewById(R.id.toggle_sport);


            handLuggage = (HandLuggage) bundle.getSerializable("handLuggage");

            suitcase = Presented.getSuitcase(handLuggage, getContext());

            trip = (Trip) bundle.getSerializable("trip");
            setTrip(view);

            TextView datysTV = view.findViewById(R.id.algoritm_days);
            int days = getTotalDays(trip);
            datysTV.setText(days + "");

            FloatingActionButton btn = view.findViewById(R.id.fragment_algoritm_btn);
            btn.setOnClickListener(v -> {
                //PROCESO DE ASIGNACION DE ITEMS
                addSuggestedBaggage(v);
                getNav().navigate(R.id.action_algoritm_to_fragment_ShowBaggageByItem, bundle);
            });


            dataset = Presented.getAllCategories(getContext());
            recyclerView = view.findViewById(R.id.myCategory_recyclerview);
            //la clave >> gridLayout para hacerlo por columnas!
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

            adapter = new Adapter_CategoryInAlgortim(dataset);
            recyclerView.setAdapter(adapter);
            adapter.setOnClickListener(v -> {
                int position = recyclerView.getChildAdapterPosition(v);
                Category category = dataset.get(position);
                if (!selectedCategory.contains(category)) {
                    selectedCategory.add(category);
                } else {
                    selectedCategory.remove(category);
                }
                adapter.notifyItemChanged(position);
            });


        }
        return view;
    }

    private void addSuggestedBaggage(View v) {
        suggestedItem = new ArrayList<>();
        // 1) limpiamos de baggage la maleta
        Presented.clearHandLuggage(handLuggage, getContext());

        // 2) Configuramos la lista

        // 2) Extraemos la lista de items sugeridos
        String[] stringArray;
        stringArray = v.getResources().getStringArray(R.array.suggested_Items);
        extractXMLlist(stringArray);

        if (toggleButton_sport.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.sports_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_snow.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.snow_Items);
            extractXMLlist(stringArray);
        }

        if (toggleButton_beach.isChecked()) {
            stringArray = v.getResources().getStringArray(R.array.beach_Items);
            extractXMLlist(stringArray);
        }

        // 3) agregamos a la BD la lista
        //en caso de que existan, se sobreescribe para obtener su ID
        Presented.addSuggestedItemList(suggestedItem, getContext());

        if (!selectedCategory.isEmpty()) {
            for (Category category :
                    selectedCategory) {
                suggestedItem.addAll(Presented.selectItemFromThisCategory(category, getContext()));
            }
        }


        //quitar duplicados en caso de que algun item de la lista xml ya exista y esté en una de las categorias
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            suggestedItem = (ArrayList<Item>) suggestedItem.stream().distinct().collect(Collectors.toList());
        }
        // 4) Se agrega la lista de items a la maleta
        Presented.createBaggageByItems(suggestedItem, handLuggage, getContext());

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
            setReturnDate(returnDate);
            LocalDate LocalReturnDate = LocalDate.of(returnYear, returnMonth, returnDay);

            return (int) DAYS.between(LocalTravelDate, LocalReturnDate);

        } else {
            return 0;
        }
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
        tripDate_TextView = view.findViewById(R.id.fragment_focusTrip_tripDate);

        String destination = trip.getDestination();
        String travelDate = trip.getTravelDate();
        String returnDate = trip.getReturnDate();

        if (!returnDate.isEmpty()) {
            travelDate += " - " + returnDate;
        }
        tripName_TextView.setText(destination);
        tripDate_TextView.setText(travelDate);
    }

}
