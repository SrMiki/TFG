package com.miki.justincase_v1;

import android.view.View;

import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.DatePickerFragment;

import java.util.ArrayList;

//ModelViewPresented pattern
//Model >> AppDatabase with ROOM & DAO
//Presented
//View >> all fragments. Fragments just inflates layouts.
public class Presented {

    private static AppDatabase db;

    public static ArrayList<Trip> getAllTrips(View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Trip>) db.tripDao().getAllTrips();
    }

    public static ArrayList<Suitcase> getAllSuitcase(View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Suitcase>) db.suitcaseDAO().getAll();
    }

    public static ArrayList<Item> getAllItems(View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Item>) db.itemDAO().getAll();
    }

    public static ArrayList<Category> getAllCategories(View view) {
        return (ArrayList<Category>) db.categoryDAO().getAll();
    }

    public static void updateTrip(Trip trip, String destination, String travelDate, String returnDate, String travelTransport, String returnTransport, View view) {
        db = AppDatabase.getInstance(view.getContext());
        //Todo check values


        trip.setTrip(destination, travelDate, returnDate, travelTransport, returnTransport);
        db.tripDao().update(trip);
    }

    public static Suitcase getSuitcaseAsociatedWithThisBaggage(Baggage baggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return db.suitcaseDAO().getSuitcase(baggage.getFKsuitcaseID());
    }

    public static void updateBaggage(Baggage baggage, String nombreMaleta, String color, String weight, String dimns, View view) {
        db = AppDatabase.getInstance(view.getContext());
        Suitcase suitcase = getSuitcaseAsociatedWithThisBaggage(baggage, view);
        suitcase.setSuticase(nombreMaleta, color, weight, dimns);
        db.suitcaseDAO().update(suitcase);
    }

    public static void updateCategory(Category category, String nameCategory, View view) {
        db = AppDatabase.getInstance(view.getContext());
        category.setCategory(nameCategory);
        db.categoryDAO().update(category);

    }

    public static void updateItem(Item item, String nombreItem, View view) {
        db = AppDatabase.getInstance(view.getContext());
        item.setItem(nombreItem);
        db.itemDAO().updateItem(item);

    }

    public static void updateSuitcase(Suitcase suitcase, String nombreMaleta, String color, String weight, String dimns, View view) {
        suitcase.setSuticase(nombreMaleta, color, weight, dimns);
        db = AppDatabase.getInstance(view.getContext());
        db.suitcaseDAO().update(suitcase);

    }

    public static void createTrip(String destination, String travelDate, String returnDate, String travelTransport, String returnTransport, View view) {
        Trip trip = new Trip(destination, travelDate, returnDate, travelTransport, returnTransport);
        db = AppDatabase.getInstance(view.getContext());
        db.tripDao().addViaje(trip);

    }

    public static void createSuitcase(String suticaseName, String color, String weight, String dimns, View view) {
        Suitcase newSuitcase = new Suitcase(suticaseName, color, weight, dimns);
        db = AppDatabase.getInstance(view.getContext());
        db.suitcaseDAO().addANewSuitcase(newSuitcase);

    }

    public static void createItem(String nombreItem, View view) {
        Item item = new Item(nombreItem);
        db = AppDatabase.getInstance(view.getContext());
        db.itemDAO().addItem(item);


    }

    public static void createCategory(String nameCategory, View view) {
        db = AppDatabase.getInstance(view.getContext());
        Category category = new Category(nameCategory);
        db.categoryDAO().addANewCategory(category);

    }

    public static void deleteItem(Item item, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.itemDAO().delete(item);
    }

    public static ArrayList<Baggage> getTheBaggageOfThisTrip(Trip trip, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Baggage>) db.baggageDAO().getTheBaggageOfThisTrip(trip.tripID);
    }

    public static void deleteTrip(Trip trip, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.tripDao().delete(trip);
    }

    public static void deleteSuitcase(Suitcase suitcase, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.suitcaseDAO().delete(suitcase);
    }

    public static ArrayList<CategoryContent> getAllItemsFromThisCategory(Category category, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<CategoryContent>) db.categoryContentDAO().getAllItemsFromThisCategory(category.categoryID);
    }

    public static void deleteCategory(Category category, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.categoryDAO().delete(category);
    }

    public static ArrayList<BaggageContent> getItemsFromThisBaggage(Baggage baggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<BaggageContent>) db.baggageContentDAO().getItemsFromThisBaggage(baggage.baggageID);
    }

    public static Trip getTheTripAsociatedWithThisBaggage(Baggage baggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return db.tripDao().getTrip(baggage.getFKtripID());
    }

    public static void deleteBaggage(Baggage baggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.baggageDAO().deleteBaggage(baggage);
    }

    public static ArrayList<Item> getAllItemsThatItNotInThisCategory(Category thisCategory, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Item>) db.itemDAO().getAllItemsThatItNotInThisCategory(thisCategory.categoryID);
    }

    public static void addANewItemForThisCategory(Item item, Category category, View view) {
        CategoryContent categoryContent = new CategoryContent(item.itemID, category.categoryID, item.itemName);
        db = AppDatabase.getInstance(view.getContext());
        db.categoryContentDAO().addANewItemForThisCategory(categoryContent);
    }

    public static ArrayList<Item> getAllItemsThatItNotInThisBaggage(Baggage thisBagagge, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Item>) db.itemDAO().getAllItemsThatItNotInThisBaggage(thisBagagge.baggageID);
    }

    public static void addThosesItemsForThisBaggage(ArrayList<BaggageContent> baggageContentArrayList, View view) {
        db = AppDatabase.getInstance(view.getContext());

        for (BaggageContent baggageContent : baggageContentArrayList) {
            db.baggageContentDAO().addANewItemForThisSuitcase(baggageContent);
        }
    }

    public static ArrayList<Suitcase> getAllSuitcaseThatItNotInThisTrip(Trip trip, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Suitcase>) db.suitcaseDAO().getAllSuitcaseThatItNotInThisTrip(trip.tripID);

    }

    public static void addANewBaggageForThisTrip(Suitcase suitcase, Trip trip, View view) {
        Baggage newBaggage = new Baggage(trip.tripID, suitcase.suitcaseID);

        db = AppDatabase.getInstance(view.getContext());
        db.baggageDAO().addANewBaggageForThisTrip(newBaggage);

    }

    public static void addThesesItemsForThisCategory(ArrayList<CategoryContent> categoryContents, Category thisCategory, View view) {
        db = AppDatabase.getInstance(view.getContext());

        for (CategoryContent categoryContent : categoryContents) {
            db.categoryContentDAO().addANewItemForThisCategory(categoryContent);
        }
    }
}
