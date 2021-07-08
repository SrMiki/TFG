package com.miki.justincase_v1;

import android.content.Context;

import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.CategoryContent;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Template;
import com.miki.justincase_v1.db.entity.TemplateElement;
import com.miki.justincase_v1.db.entity.Trip;

import java.util.ArrayList;
import java.util.List;

//ModelViewPresented pattern
//Model >> AppDatabase with ROOM & DAO
//Presented >> Comunicated View with Model
//View >> all fragments
public class Presenter {

    private static AppDatabase db;

    // TODO --- GET --- //

    public static ArrayList<Trip> getAllTrips(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Trip>) db.tripDao().getAll();
    }

    public static ArrayList<Category> getAllCategories(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Category>) db.categoryDAO().getAll();
    }

    public static ArrayList<Baggage> getBaggageOfThisHandLuggage(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Baggage>) db.baggageDAO().selectBaggageOfThisHandLuggage(handLuggage.handLuggageID);
    }

    public static Trip getTrip(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        return db.tripDao().getTrip(handLuggage.getFKtripID());
    }

    public static Suitcase getSuitcase(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        return db.suitcaseDAO().getSuitcase(handLuggage.getFKsuitcaseID());
    }

    public static ArrayList<HandLuggage> getHandLuggage(Trip trip, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<HandLuggage>) db.handLuggageDAO().getTheHandLuggageOfThisTrip(trip.tripID);
    }

    // TODO --- SELECT --- //

    public static ArrayList<Trip> selectAllTripsInProgress(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Trip>) db.tripDao().selectCheckOutTrip();
    }

    public static ArrayList<Trip> selectCheckInBACKtrip(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Trip>) db.tripDao().selectCheckInBACKtrip();
    }

    public static ArrayList<Trip> selectAllTripsFinished(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Trip>) db.tripDao().selectFinishedTrip();
    }

    public static ArrayList<Item> selectItemWhitNoCategory(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Item>) db.itemDAO().selectItemWhitNoCategory();
    }

    public static ArrayList<Item> selectItemFromThisCategory(Category category, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Item>) db.itemDAO().selectItemFromThisCategory(category.categoryID);
    }

    public static ArrayList<Trip> selectAllTripsNP(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Trip>) db.tripDao().selectPlanningTrip();
    }

    public static ArrayList<Suitcase> selectAllSuitcase(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Suitcase>) db.suitcaseDAO().selectAll();
    }

    public static ArrayList<Item> selectAllItems(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Item>) db.itemDAO().selectAll();
    }

    public static ArrayList<Category> selectAllCategories(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Category>) db.categoryDAO().getAllOrdened();
    }

    public static ArrayList<Category> selectCategoriesNOTFromThisBaggage(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Category>) db.categoryDAO().selectCategoriesNOTFromThisBaggage(handLuggage.getHandLuggageID());
    }


    public static ArrayList<Item> selectItemNOTFromThisBaggage(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Item>) db.itemDAO().selectItemNOTFromThisBaggage(handLuggage.handLuggageID);
    }


    public static ArrayList<Suitcase> selectSuitcaseNOTFromThisTrip(Trip trip, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Suitcase>) db.suitcaseDAO().selectSuitcaseNOTFromThisTrip(trip.tripID);

    }

    public static ArrayList<Template> selectAllTemplates(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Template>) db.templateDAO().selectAll();
    }

    public static ArrayList<Item> selectItemFromThisTemplate(Template template, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Item>) db.templateElementDAO().selectItemFromThisTemplate(template.getTemplateID());

    }

    public static ArrayList<Item> selectItemNOTintThisTemplate(Template template, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Item>) db.templateElementDAO().selectItemNOTinThisTemplate(template.getTemplateID());

    }


    //  TODO --- CREATE --- //

    public static boolean createTrip(Trip newTrip, Context context) {
        if (newTrip.getDestination().isEmpty() || newTrip.getTravelDate().isEmpty()) {
            return false;
        }
        db = AppDatabase.getInstance(context);
        db.tripDao().insert(newTrip);
        return true;
    }

    public static boolean createSuitcase(Suitcase suitcase, Context context) {
        db = AppDatabase.getInstance(context);
        List<Suitcase> suitcases = db.suitcaseDAO().getAll();
        if (!suitcases.contains(suitcase)) {
            db.suitcaseDAO().insert(suitcase);
            return true;
        }
        return false;

    }

    public static boolean createItem(String nombreItem, Context context) {
        if (!nombreItem.isEmpty()) {
            nombreItem = nombreItem.toLowerCase().trim();
            db = AppDatabase.getInstance(context);

//            List<Item> itemList = db.itemDAO().getAll();
//            for (Item item : itemList) {
//                if (item.getItemName().equals(nombreItem)) {
//                    return false;
//                }
//            }

            if (db.itemDAO().getItemByItemName(nombreItem) == null) {
                Item newItem = new Item(nombreItem, "");
                db.itemDAO().insert(newItem);
                return true;
            }
        }
        return false;

    }

    public static boolean createItem(String itemName, String itemPhotoUri, Context context) {
        if (!itemName.isEmpty()) {
            itemName = itemName.toLowerCase().trim();
            db = AppDatabase.getInstance(context);

            if (db.itemDAO().getItemByItemName(itemName) == null) {
                Item newItem = new Item(itemName, itemPhotoUri);
                db.itemDAO().insert(newItem);
                return true;
            }
        }
        return false;

    }

    public static boolean createCategory(String name, Context context) {
        db = AppDatabase.getInstance(context);
        if (name.isEmpty()) {
            return false;
        }

        name = name.toLowerCase().trim();
        List<Category> dbList = db.categoryDAO().getAll();
        for (Category category : dbList) {
            if (category.getCategoryName().equals(name)) {
                return false;
            }
        }

        Category newEntity = new Category(name);
        db.categoryDAO().insert(newEntity);
        return true;
    }

    public static boolean createBaggageByItems(ArrayList<Item> itemArrayList, HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        ArrayList<Baggage> baggageArrayList = new ArrayList<>();
        for (Item item : itemArrayList) {
            Baggage baggage = new Baggage(item.itemID, handLuggage.handLuggageID, item.getItemName());
            baggageArrayList.add(baggage);
            handLuggage.increaseSize();
        }
        db.handLuggageDAO().update(handLuggage);
        db.baggageDAO().insertAll(baggageArrayList);
        return true;
    }

//    public static boolean createBaggageByCategory(ArrayList<Category> categoryArrayList, HandLuggage handLuggage, Context context) {
//        db = AppDatabase.getInstance(context);
//        ArrayList<Item> itemArrayList = new ArrayList<>();
//        for (Category category : categoryArrayList) {
//            List<CategoryContent> allItemsFromThisCategory = db.categoryContentDAO().getAllItemsFromThisCategory(category.categoryID);
//            for (CategoryContent categoryContent : allItemsFromThisCategory) {
//                itemArrayList.add(db.itemDAO().getItem(categoryContent.getFKitemID()));
//            }
//        }
//        return createBaggageByItems(itemArrayList, handLuggage, context);
//    }

    public static boolean createTemplate(String name, Context context) {
        db = AppDatabase.getInstance(context);
        if (name.isEmpty()) {
            return false;
        }

        name = name.toLowerCase().trim();
        List<Template> dbList = db.templateDAO().getAll();
        for (Template template : dbList) {
            if (template.getTemplateName().equals(name)) {
                return false;
            }
        }

        Template newEntity = new Template(name);
        db.templateDAO().insert(newEntity);
        return true;

    }

    // TODO --- UPDATE ---

    public static void updateTrip(Trip trip, Context context) {
        db = AppDatabase.getInstance(context);
        db.tripDao().update(trip);
    }

    public static boolean updateCategory(Category category, String nameCategory, Context context) {
        db = AppDatabase.getInstance(context);
        category.setCategory(nameCategory);
        db.categoryDAO().update(category);
        return true;
    }

    public static boolean updateItem(Item updateItem, String nombreItem, Context context) {
        db = AppDatabase.getInstance(context);
        List<Item> itemList = db.itemDAO().getAll();
        for (Item item : itemList) {
            if (item.getItemName().equals(nombreItem)) {
                return false;
            }
        }
        updateItem.setItem(nombreItem);
        db.itemDAO().update(updateItem);
        return true;
    }

    public static void updateItem(Item item, int count, Context context) {
        db = AppDatabase.getInstance(context);
        item.setCount(count);
        db.itemDAO().update(item);
    }


    public static boolean updateSuitcase(Suitcase suitcase, Context context) {
        if (suitcase.getName().isEmpty()) {
            return false;
        }
        db = AppDatabase.getInstance(context);
        db.suitcaseDAO().update(suitcase);
        return true;
    }

    public static void updateBaggage(Baggage baggage, Context context) {
        db = AppDatabase.getInstance(context);
        db.baggageDAO().update(baggage);
    }

    public static void updateHandLuggage(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        db.handLuggageDAO().update(handLuggage);
    }

    public static void updateBaggage(Baggage baggage, int count, Context context) {
        db = AppDatabase.getInstance(context);
        baggage.setCount(count);
        db.baggageDAO().update(baggage);
    }

    public static boolean updateTemplate(Template template, String templateName, Context context) {
        db = AppDatabase.getInstance(context);
        template.setTemplateName(templateName);
        db.templateDAO().update(template);
        return true;
    }



    // TODO --- DELETE ---

    public static void deleteItem(Item item, Context context) {
        db = AppDatabase.getInstance(context);

        CategoryContent categoryContentByItemID = db.categoryContentDAO().getCategoryContentByItemID(item.getItemID());
        if (categoryContentByItemID != null) {
            db.categoryContentDAO().delete(categoryContentByItemID);
        }

        List<Baggage> baggageByItem = db.baggageDAO().getBaggageByItem(item.getItemID());
        for (Baggage baggage : baggageByItem) {
            if (baggage != null) {
                HandLuggage handLuggage = db.handLuggageDAO().getHandLuggage(baggage.FKHandLuggageID);
                handLuggage.decreaseSize();
                db.handLuggageDAO().update(handLuggage);
                db.baggageDAO().delete(baggage);
            }
        }

        db.itemDAO().delete(item);
    }

    public static void deleteTrip(Trip trip, Context context) {
        db = AppDatabase.getInstance(context);
        db.tripDao().delete(trip);
    }

    public static void deleteSuitcase(Suitcase suitcase, Context context) {
        db = AppDatabase.getInstance(context);
        db.suitcaseDAO().delete(suitcase);
    }


    public static void deleteCategory(Category category, Context context) {
        db = AppDatabase.getInstance(context);
        List<CategoryContent> allItemsFromThisCategory = db.categoryContentDAO().getAllItemsFromThisCategory(category.categoryID);
        db.categoryContentDAO().deleteAll(allItemsFromThisCategory);
        db.categoryDAO().delete(category);
    }

    public static void deleteHandluggage(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        List<Baggage> baggages = db.baggageDAO().selectBaggageOfThisHandLuggage(handLuggage.getHandLuggageID());
        db.baggageDAO().deleteAll(baggages);
        db.handLuggageDAO().delete(handLuggage);
    }

    public static void deleteItemOfThisCategory(Category category, Context context) {
        db = AppDatabase.getInstance(context);

        List<Item> itemFromThisCategory = db.itemDAO().getItemFromThisCategory(category.categoryID);
//        List<CategoryContent> allItemsFromThisCategory = db.categoryContentDAO().getAllItemsFromThisCategory(category.categoryID);
//        List<Item> itemList = new ArrayList<>();
//        for (CategoryContent categoryContent: allItemsFromThisCategory) {
//            itemList.add(db.itemDAO().getItem(categoryContent.getFKitemID()));
//        }
        db.itemDAO().deleteAll(itemFromThisCategory);

    }

    public static void deleteTemplate(Template template, Context context) {
        db = AppDatabase.getInstance(context);
        List<TemplateElement> allItemsFromThisTemplate = db.templateElementDAO().getAllItemsFromThisTemplate(template.templateID);
        db.templateElementDAO().deleteAll(allItemsFromThisTemplate);
        db.templateDAO().delete(template);
    }



    public static void removeItemFromThisTemplate(Item deletedItem, Context context) {
        db = AppDatabase.getInstance(context);
        TemplateElement templateElement = db.templateElementDAO().getTemplateElementByItemID(deletedItem.itemID);
        db.templateElementDAO().delete(templateElement);
    }



    // TODO --


    public static void addSuitcaseToThisTrip(Suitcase suitcase, Trip trip, Context context) {
        db = AppDatabase.getInstance(context);
        HandLuggage newHandLuggage = new HandLuggage(trip.tripID, suitcase.suitcaseID, suitcase.getName(), "");
        db.handLuggageDAO().insert(newHandLuggage);
    }

    public static void addSomeSuitcaseToThisTrip(ArrayList<Suitcase> suitcaseArrayList, Trip trip, Context context) {
        db = AppDatabase.getInstance(context);
        for (Suitcase suitcase : suitcaseArrayList) {
            HandLuggage newHandLuggage = new HandLuggage(trip.tripID, suitcase.suitcaseID, suitcase.getName(), "");
            db.handLuggageDAO().insert(newHandLuggage);
        }
    }

    public static void addItemToThisCategory(ArrayList<Item> itemArrayList, Category category, Context context) {
        db = AppDatabase.getInstance(context);

        for (Item item : itemArrayList) {
            CategoryContent categoryContent = new CategoryContent(item.itemID, category.categoryID, category.categoryName);
            db.categoryContentDAO().insert(categoryContent);
        }
    }

    public static void removeItemFromThisCategory(Item deletedItem, Context context) {
        db = AppDatabase.getInstance(context);
        CategoryContent categoryContentByItemID = db.categoryContentDAO().getCategoryContentByItemID(deletedItem.itemID);
        db.categoryContentDAO().delete(categoryContentByItemID);
    }


    public static ArrayList<Category> selectAllCategoriesOfThisHandLuggage(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);

        return (ArrayList<Category>) db.baggageDAO().selectCategoriesOfThisHandLuggage(handLuggage.getHandLuggageID());

//        ArrayList<Category> categoryList = new ArrayList<>();
//
//        List<Item> itemList = db.itemDAO().selectItemFromThisBaggage(handLuggage.handLuggageID);
//        for (Item item : itemList) {
//            CategoryContent categoryContentByItemID = db.categoryContentDAO().getCategoryContentByItemID(item.getItemID());
//            if (categoryContentByItemID != null) { // (TODO) no todos los items tienen categorias!!
//                Category category = db.categoryDAO().getCategory(categoryContentByItemID.getFKcategoryID());
//                if(!categoryList.contains(category)){
//                    categoryList.add(category);
//                }
//            }
//        }
//        return categoryList;
    }

    public static void checkBaggage(HandLuggage handLuggage, ArrayList<Baggage> baggageOfThisHandLuggage, Context context) {
        int checked = 0;
        for (Baggage baggage : baggageOfThisHandLuggage) {
            if (baggage.isCheck()) {
                checked++;
            }
        }
        handLuggage.setHandLuggageCompleted(checked == handLuggage.getHandLuggageSize());
        updateHandLuggage(handLuggage, context);
    }

    public static void checkAllBaggage(ArrayList<Baggage> dataset, Boolean state, Context context) {
        db = AppDatabase.getInstance(context);
        for (Baggage baggage : dataset) {
            baggage.setCheck(state);
            db.baggageDAO().update(baggage);
        }
    }


    public static void clearCheckHandLuggage(Trip trip, Context context) {
        db = AppDatabase.getInstance(context);
        ArrayList<HandLuggage> handLuggageArrayList = getHandLuggage(trip, context);
        for (HandLuggage handLuggage : handLuggageArrayList) {
            handLuggage.setHandLuggageCompleted(false);
            ArrayList<Baggage> baggageOfThisHandLuggage = getBaggageOfThisHandLuggage(handLuggage, context);
            for (Baggage baggage : baggageOfThisHandLuggage) {
                baggage.setCheck(false);
                db.baggageDAO().update(baggage);
            }
            db.handLuggageDAO().update(handLuggage);
        }
    }


    public static ArrayList<Baggage> selectBaggageFromThisCategoryInThisHandLuggage(Category category, HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Baggage>) db.baggageDAO().theMethod(category.categoryID, handLuggage.handLuggageID);
    }


    public static void removeBaggageFromThisHandLuggage(HandLuggage handluggage, Baggage baggage, Context context) {
        db = AppDatabase.getInstance(context);

        handluggage.decreaseSize();
        db.handLuggageDAO().update(handluggage);

        db.baggageDAO().delete(baggage);
    }

    public static ArrayList<Item> selectItemFromThisCategoryButNotInThisBaggage(HandLuggage handluggage, Category focusCategory, Context context) {
        db = AppDatabase.getInstance(context);

//        return (ArrayList<Item>) db.baggageDAO().selectItemFromThisCategoryButNotInThisBaggage(focusCategory.categoryID, handluggage.getHandLuggageID());

        List<Item> content = db.baggageDAO().selectItemFromThisCategory(focusCategory.categoryID);
        List<Item> baggage = db.baggageDAO().selectItemFromThisHandLuggage(handluggage.getHandLuggageID());

        if (baggage.isEmpty()) {
            return (ArrayList<Item>) content;
        }

        ArrayList<Item> result = new ArrayList<>();
        for (Item item : content) {
            if (!baggage.contains(item)) {
                result.add(item);
            }
        }

        return result;
    }

    public static void deleteItemFromTrips(Category category, Context context) {
        db = AppDatabase.getInstance(context);

        List<CategoryContent> allItemsFromThisCategory = db.categoryContentDAO().getAllItemsFromThisCategory(category.categoryID);

        for (CategoryContent categoryContent : allItemsFromThisCategory) {
            List<Baggage> baggageByItem = db.baggageDAO().getBaggageByItem(categoryContent.getFKitemID());
            for (Baggage baggage : baggageByItem) {
                if (baggage != null) {
                    HandLuggage handLuggage = db.handLuggageDAO().getHandLuggage(baggage.FKHandLuggageID);
                    handLuggage.decreaseSize();
                    db.handLuggageDAO().update(handLuggage);
                    db.baggageDAO().delete(baggage);
                }
            }
        }
    }

    public static ArrayList<Suitcase> getAllSuitcase(Context context) {
        db = AppDatabase.getInstance(context);
        return (ArrayList<Suitcase>) db.suitcaseDAO().getAll();
    }


    public static Category getCategoryOfThisItem(Item item, Context context) {
        db = AppDatabase.getInstance(context);
        return db.categoryContentDAO().getCategoryByItem(item.getItemID());
    }

    public static void updateItemState(Item item, Context context) {
        db = AppDatabase.getInstance(context);

        db.itemDAO().update(item);
    }

    public static void removeItemSelectedState(ArrayList<Item> arrayList, Context context) {
        db = AppDatabase.getInstance(context);

        for (Item item : arrayList) {
            item.setSelectedState(false);
        }

        db.itemDAO().updateListOfItem(arrayList);


    }

    public static void updateSuitcaseState(Suitcase suitcase, Context context) {
        db = AppDatabase.getInstance(context);

        db.suitcaseDAO().update(suitcase);
    }

    public static void removeSuitcaseSelectedState(ArrayList<Suitcase> arrayList, Context context) {
        db = AppDatabase.getInstance(context);

        for (Suitcase suitcase : arrayList) {
            suitcase.setSelectedState(false);
        }

        db.suitcaseDAO().updateListOfSuitcase(arrayList);

    }


    public static void saveTrip(Trip trip, Context context) {
        db = AppDatabase.getInstance(context);

//        db.handLuggageDAO().getTheHandLuggageOfThisTrip(trip);


    }

    public static boolean updateCategoryContent(Item item, String newCategory, Context context) {
        db = AppDatabase.getInstance(context);

        Category category = db.categoryDAO().getCategoryByName(newCategory);
        if (category != null) {
            CategoryContent categoryContent = db.categoryContentDAO().getCategoryContentByItemID(item.getItemID());
            categoryContent.setFKcategoryID(category.categoryID);
            if (newCategory.isEmpty()) {
                db.categoryContentDAO().delete(categoryContent);
            } else {
                db.categoryContentDAO().update(categoryContent);
            }
            return true;
        } else {
            return false;
        }
    }

    public static void addSuggestedItemList(ArrayList<Item> suggestedItem, Context context) {
        db = AppDatabase.getInstance(context);
        //agregamos (si no están) los items sugeridos a la BD (manejo de ID, persistencia)
        for (int i = 0; i < suggestedItem.size(); i++) {
            Item item = suggestedItem.get(i);
            Presenter.createItem(item.getItemName(), context);
            item = db.itemDAO().getItemByItemName(item.getItemName()); //Sobreescribimos el Item para que pille la ID
            suggestedItem.set(i, item);
        }
    }

    public static void clearHandLuggage(HandLuggage handLuggage, Context context) {
        db = AppDatabase.getInstance(context);
        List<Baggage> baggages = db.baggageDAO().selectBaggageOfThisHandLuggage(handLuggage.getHandLuggageID());
        for (Baggage baggage :
                baggages) {
            db.baggageDAO().delete(baggage);
        }
    }



    public static void addItemToThisTemplate(ArrayList<Item> arrayList, Template template, Context context) {
        db = AppDatabase.getInstance(context);

        for (Item item : arrayList) {
            TemplateElement templateElement = new TemplateElement(item.getItemID(), template.getTemplateID(), item.getItemName());
            db.templateElementDAO().insert(templateElement);
        }
    }
}
