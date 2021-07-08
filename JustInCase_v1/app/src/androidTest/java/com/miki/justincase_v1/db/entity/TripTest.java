package com.miki.justincase_v1.db.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TripTest {

    static Trip trip;

    @Before
    public void setUp()  {
        trip = new Trip("trip", "0", "1", "none", "none", );
    }

    @Test
    public void getDestination() {
        assertEquals(trip.getDestination(), "trip");
    }

    @Test
    public void getTravelDate() {
        assertEquals(trip.getTravelDate(), "0");
    }

    @Test
    public void getReturnDate() {
        assertEquals(trip.getDestination(), "trip");
    }

    @Test
    public void isTravelling() {
        assertEquals(trip.isTravelling(),0 );
        trip.setTravelling(1);
        assertEquals(trip.isTravelling(),1 );
    }
}