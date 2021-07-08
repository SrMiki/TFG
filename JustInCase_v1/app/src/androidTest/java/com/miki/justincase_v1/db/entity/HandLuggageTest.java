package com.miki.justincase_v1.db.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HandLuggageTest {

    static HandLuggage handLuggage;

    @Before
    public void setUp()  {
        handLuggage = new HandLuggage(0, 0, "suitcaseName", );
    }

    @Test
    public void isHandLuggageCompleted() {
        assertFalse(handLuggage.isHandLuggageCompleted());
        handLuggage.setHandLuggageCompleted(true);
        assertTrue(handLuggage.isHandLuggageCompleted());
    }

    @Test
    public void increaseSize() {
        handLuggage.setHandLuggageSize(0);
        assertEquals(handLuggage.getHandLuggageSize(), 0);
        handLuggage.increaseSize();
        assertEquals(handLuggage.getHandLuggageSize(), 1);

    }

    @Test
    public void decreaseSize() {
        handLuggage.setHandLuggageSize(1);
        assertEquals(handLuggage.getHandLuggageSize(), 1);
        handLuggage.decreaseSize();
        assertEquals(handLuggage.getHandLuggageSize(), 0);

        handLuggage.decreaseSize();
        assertEquals(handLuggage.getHandLuggageSize(), 0);
    }

    @Test
    public void getHandLuggageSize() {
        handLuggage.setHandLuggageSize(0);
        assertEquals(handLuggage.getHandLuggageSize(), 0);
    }

    @Test
    public void getHandLuggageName() {
        assertEquals(handLuggage.getHandLuggageName(), "suitcaseName");
    }
}