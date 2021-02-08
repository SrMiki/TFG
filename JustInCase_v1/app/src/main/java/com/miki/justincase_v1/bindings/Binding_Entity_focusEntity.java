package com.miki.justincase_v1.bindings;

import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;

public interface Binding_Entity_focusEntity {
    public void sendHandLuggage(HandLuggage handLuggage);
    public void sendTrip(Trip trip);
}
