package com.miki.justincase_v1.db.entity;

import com.miki.justincase_v1.db.entity.Suitcase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SuitcaseTest {

    static Suitcase suitcase1;
    static Suitcase suitcase2;

    @Before
    public void setUp() {
        suitcase1 = new Suitcase("suitcase1", "none", 0, 0, 0, 0);
        suitcase2 = new Suitcase("suitcase2", "none", 0, 0, 0, 0);

    }

    @Test
    public void isSelectedState() {
        assertFalse(suitcase1.isSelectedState());
    }

    @Test
    public void getName() {
        assertEquals(suitcase1.getName(), "suitcase1");
    }

    @Test
    public void getColor() {
        assertEquals(suitcase1.getColor(), "none");
    }

    @Test
    public void getWeight() {
        assertEquals( suitcase1.getWeight(), 0, 0);
    }

    @Test
    public void getHeigth() {
        assertEquals( suitcase1.getHeigth(), 0, 0);
    }

    @Test
    public void getWidth() {
        assertEquals( suitcase1.getWidth(), 0, 0);
    }

    @Test
    public void getDepth() {
        assertEquals( suitcase1.getDepth(), 0, 0);
    }

    @Test
    public void testEquals() {
        assertNotSame(suitcase1, suitcase2);
    }
}