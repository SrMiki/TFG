package com.miki.justincase_v1.bindings;

import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;

public interface Binding_Entity_focusEntity {
    public void sendBaggage(Baggage baggage);
    public void sendCategory(Category category);
    public void sendItem(Item item);
    public void sendSuitcase(Suitcase suitcase);
    public void sendTrip(Trip trip);
}
