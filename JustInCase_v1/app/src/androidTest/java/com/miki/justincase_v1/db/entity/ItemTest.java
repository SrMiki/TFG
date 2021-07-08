package com.miki.justincase_v1.db.entity;

import com.miki.justincase_v1.db.entity.Item;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    static Item item1;
    static Item item2;

    @Before
    public void setUp() {
        item1 = new Item("item1", "null");
        item2 = new Item("item2", "null");
    }

    @Test
    public void getItemName() {
        assertEquals(item1.getItemName(), "item1");
        assertEquals(item2.getItemName(), "item2");
    }

    @Test
    public void testEquals() {
        assertNotSame(item1, item2);
    }

    @Test
    public void isSelectedState() {
        assertFalse(item1.isSelectedState());
        item1.setSelectedState(true);
        assertTrue(item1.isSelectedState());
    }

    @Test
    public void getItemCount() {
        assertEquals(item1.getItemCount(), 1);
        item1.setCount(2);
        assertEquals(item1.getItemCount(), 2);
    }
}