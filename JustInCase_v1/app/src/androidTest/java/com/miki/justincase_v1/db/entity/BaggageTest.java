package com.miki.justincase_v1.db.entity;

import com.miki.justincase_v1.db.entity.Baggage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaggageTest {

    static Baggage baggage;

    @Before
    public void setUp() {
        //test without connection & DB
        baggage = new Baggage(0,1, "nombreItem");
    }

    @Test
    public void getBaggageFKItemID(){
        assertEquals(baggage.getFKitemID(), 0);
    }

    @Test
    public void getBaggageFKHandLuggageID(){
        assertEquals(baggage.getFKHandLuggageID(), 1);
    }

    @Test
    public void getBaggageCount() {
        assertEquals(baggage.getBaggageCount(),1);
        baggage.setCount(2);
        assertEquals(baggage.getBaggageCount(),2);
    }

    @Test
    public void isCheck() {
        assertFalse(baggage.isCheck());
        baggage.setCheck(true);
        assertTrue(baggage.isCheck());


    }
}