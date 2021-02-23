package com.miki.justincase_v1.db;

import android.content.Context;

import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;

public class initDB {

    private static AppDatabase db;

    public static void initDB(Context applicationContext) {
        db = AppDatabase.getInstance(applicationContext);
        ArrayList<Category> categories = initCategory();
        db.categoryDAO().insertAll(categories);

        ArrayList<Item> items = initItems(categories);
        db.itemDAO().insertAll(items);

        ArrayList<Suitcase> suitcases = initSuitcase();
        db.suitcaseDAO().insertAll(suitcases);

        ArrayList<Item> itemList = (ArrayList<Item>) db.itemDAO().getAll();
        ArrayList<Category> categoryList = (ArrayList<Category>) db.categoryDAO().getAll();
        initCategoryContent(itemList, categoryList);

        ArrayList<Trip> trips = initTrips();
        db.tripDao().insertAll(trips);

    }

    private static void initCategoryContent(ArrayList<Item> items, ArrayList<Category> categories) {

//        insertCategory(items, categories, 0, 0);
//        insertCategory(items, categories, 0, 1);
//        insertCategory(items, categories, 0, 2);

    }

    private static void insertCategory(ArrayList<Item> items, ArrayList<Category> categories, int categoryPOS, int itemPOS) {
        Item item = items.get(itemPOS);
        Category category = categories.get(categoryPOS);
        CategoryContent categoryContent = new CategoryContent(item.itemID, category.categoryID, item.getItemName());
        db.categoryContentDAO().insert(categoryContent);
    }

    private static ArrayList<Trip> initTrips() {
        ArrayList<Trip> trips = new ArrayList<>();

//        trips.add(new Trip("Barcelona", "10 / 03 / 2021", "10 / 04 / 2021", "", ""));
        trips.add(new Trip("Madrid", "01 / 05 / 2021", "", "", ""));

        return trips;
    }


    private static ArrayList<Suitcase> initSuitcase() {
        ArrayList<Suitcase> suitcases = new ArrayList<>();
        suitcases.add(new Suitcase("maleta", "roja", 55, 30, 30, 20));
//        suitcases.add(new Suitcase("maletín", "negro", 20, 20,30,20));
//        suitcases.add(new Suitcase("maleta grande", "azul", 80, 40,40,50));
        return suitcases;
    }

    private static ArrayList<Category> initCategory() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("ropa"));
//        categories.add(new Category("higiene"));
        return categories;
    }

    private static ArrayList<Item> initItems(ArrayList<Category> categories) {
        ArrayList<Item> items = new ArrayList<>();

        items.add(new Item("billete"));
        items.add(new Item("bufanda"));

        items.add(new Item("camisa"));
        items.add(new Item("cartera"));
        items.add(new Item("corbata"));
        items.add(new Item("cerillas"));
        items.add(new Item("caramelos"));

        items.add(new Item("deportivas"));
        items.add(new Item("dados"));

        items.add(new Item("guantes"));

        items.add(new Item("llaves"));

        items.add(new Item("ordenador"));

        items.add(new Item("pantalon"));
        items.add(new Item("peine"));
        items.add(new Item("portatil"));
        items.add(new Item("pasaporte"));

        items.add(new Item("vaqueros"));
        items.add(new Item("zapatos"));


        return items;
    }
}
