package com.miki.justincase_v1.db.entity;

import com.miki.justincase_v1.db.entity.Category;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTest {

    static Category category1;
    static Category category2;

    @Before
    public void setUp()  {
        category1 = new Category("category1");
        category2 = new Category("category2");
    }

    @Test
    public void getCategoryName() {
        assertEquals(category1.getCategoryName(), "category1");
    }

    @Test
    public void testEquals() {
        assertNotSame(category1, category2);
    }
}