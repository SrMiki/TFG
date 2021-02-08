package com.miki.justincase_v1;

import android.content.Context;
import android.view.View;

import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;

//ModelViewPresented pattern
//Model >> AppDatabase with ROOM & DAO
//Presented
//View >> all fragments. Fragments just inflates layouts.
public class Presented {

    private static AppDatabase db;

    public static ArrayList<Trip> getAllTrips(View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Trip>) db.tripDao().getAllEntity();
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

        trip.setTrip(destination, travelDate, returnDate, travelTransport, returnTransport);
        db.tripDao().update(trip);
    }

    public static Suitcase getSuitcase(HandLuggage handLuggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return db.suitcaseDAO().getSuitcase(handLuggage.getFKsuitcaseID());
    }

    public static void updateHandLuggage(HandLuggage handLuggage, String nombreMaleta, String color, String weight, String dimns, View view) {
        db = AppDatabase.getInstance(view.getContext());
        Suitcase suitcase = getSuitcase(handLuggage, view);
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
//        if (Suitcase.check(newSuitcase)) {
        db = AppDatabase.getInstance(view.getContext());
        db.suitcaseDAO().insert(newSuitcase);
//        }

    }

    public static void createItem(String nombreItem, View view) {
        if (!nombreItem.isEmpty()) {
            Item item = new Item(nombreItem);
            db = AppDatabase.getInstance(view.getContext());
            db.itemDAO().addItem(item);
        }


    }

    public static void createCategory(String nameCategory, View view) {
        db = AppDatabase.getInstance(view.getContext());
        Category category = new Category(nameCategory);
        db.categoryDAO().insert(category);

    }

    public static void deleteItem(Item item, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.itemDAO().delete(item);
    }

    public static ArrayList<HandLuggage> getHandLuggage(Trip trip, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<HandLuggage>) db.handLuggageDAO().getTheHandLuggageOfThisTrip(trip.tripID);
    }

    public static void deleteTrip(Trip trip, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.tripDao().delete(trip);
    }

    public static void deleteSuitcase(Suitcase suitcase, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.suitcaseDAO().delete(suitcase);
    }

    public static ArrayList<Item> getAllItemsFromThisCategory(Category category, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Item>) db.itemDAO().getAllItemsFromThisCategory(category.categoryID);
    }

    public static void deleteCategory(Category category, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.categoryDAO().delete(category);
    }

    public static ArrayList<Baggage> getBaggage(HandLuggage handLuggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Baggage>) db.baggageDAO().getItemsFromThisBaggage(handLuggage.handLuggageID);
    }

    public static Trip getTrip(HandLuggage handLuggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return db.tripDao().getTrip(handLuggage.getFKtripID());
    }

    public static void deleteBaggage(HandLuggage handLuggage, View view) {
        db = AppDatabase.getInstance(view.getContext());
        db.handLuggageDAO().delete(handLuggage);
    }

    public static ArrayList<Item> getAllItemsThatItNotInThisCategory(View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Item>) db.itemDAO().getAllItemsThatItNotInThisCategory();
    }

    public static ArrayList<Item> getAllItemsThatItNotInThisBaggage(HandLuggage thisBagagge, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Item>) db.itemDAO().getAllItemsThatItNotInThisBaggage(thisBagagge.handLuggageID);
    }

    public static void addBaggage(ArrayList<Baggage> baggageArrayList, View view) {
        db = AppDatabase.getInstance(view.getContext());

        for (Baggage baggage : baggageArrayList) {
            db.baggageDAO().addANewBaggage(baggage);
        }
    }

    public static ArrayList<Suitcase> getAllSuitcaseThatItNotInThisTrip(Trip trip, View view) {
        db = AppDatabase.getInstance(view.getContext());
        return (ArrayList<Suitcase>) db.suitcaseDAO().getAllSuitcaseThatItNotInThisTrip(trip.tripID);

    }

    public static void addANewBaggageForThisTrip(Suitcase suitcase, Trip trip, View view) {
        HandLuggage newHandLuggage = new HandLuggage(trip.tripID, suitcase.suitcaseID);

        db = AppDatabase.getInstance(view.getContext());
        db.handLuggageDAO().addANewHandLuggageForThisTrip(newHandLuggage);

    }

    public static void addItemsToThisCateogory(ArrayList<CategoryContent> categoryContents, Category thisCategory, View view) {
        db = AppDatabase.getInstance(view.getContext());

        for (CategoryContent categoryContent : categoryContents) {
            db.categoryContentDAO().insert(categoryContent);
        }
    }

    public static void removeFromThisCategory(Item deletedItem, View itemView) {
        db = AppDatabase.getInstance(itemView.getContext());
        CategoryContent categoryContentByItemID = db.categoryContentDAO().getCategoryContentByItemID(deletedItem.itemID);
        db.categoryContentDAO().delete(categoryContentByItemID);
    }

    public static void initDB(Context applicationContext) {
        db = AppDatabase.getInstance(applicationContext);
        ArrayList<Item> items = initItems();
        db.itemDAO().addListItem(items);

        ArrayList<Category> categories = initCategory();
        db.categoryDAO().insertAll(categories);

        ArrayList<Suitcase> suitcases = initSuitcase();
        db.suitcaseDAO().insertAll(suitcases);


    }

    private static ArrayList<Suitcase> initSuitcase() {
        ArrayList<Suitcase> suitcases = new ArrayList<>();
        suitcases.add(new Suitcase("maleta", "roja", "55", "30x30x20"));
        suitcases.add(new Suitcase("maletín", "negro", "20", "20x30x20"));
        suitcases.add(new Suitcase("maleta grande", "azul", "80", "40x40x50"));
        suitcases.add(new Suitcase("Amarilla", "", "", ""));
        return suitcases;
    }

    private static ArrayList<Category> initCategory() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("ropa"));
        categories.add(new Category("higiene"));
        categories.add(new Category("electrónica"));
        categories.add(new Category("personal"));
        return categories;
    }

    private static ArrayList<Item> initItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("camisa"));
        items.add(new Item("camiseta"));
        items.add(new Item("camisóm"));
        items.add(new Item("corbata"));
        items.add(new Item("calcetines"));
        items.add(new Item("gafas"));
        items.add(new Item("cartera"));
        items.add(new Item("llaves"));
        items.add(new Item("bufanda"));
        items.add(new Item("billete"));
        items.add(new Item("pantalón"));
        items.add(new Item("vaqueros"));
        items.add(new Item("vestido rojo"));
        return items;

    }

    public static ArrayList<Category> getAllCategoriesAItem(ArrayList<Baggage> itemsFromThisBaggage, View itemView) {
        db = AppDatabase.getInstance(itemView.getContext());
        ArrayList<Integer> integers = new ArrayList<>();


        ArrayList<Category> categories = new ArrayList<>();
        for (Baggage baggage : itemsFromThisBaggage) {
            int fKitemID = baggage.FKitemID;
            Item item = db.itemDAO().getItem(fKitemID);
            CategoryContent categoryContentwithItemID = db.categoryContentDAO().getCategoryContentByItemID(item.itemID);
            if (categoryContentwithItemID != null) { // no todo item tiene una categoria
                int fKcategoryID = categoryContentwithItemID.FKcategoryID;
                if (!integers.contains(fKcategoryID)) {
                    integers.add(fKcategoryID);
                    Category category = db.categoryDAO().getCategory(fKcategoryID);
                    categories.add(category);
                }
            }
        }
        return categories;
    }
}