package com.miki.justincase_v1.db.entity;

import com.miki.justincase_v1.db.entity.CategoryContent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryContentTest {

    static CategoryContent categoryContent;

    @Before
    public void setUp()  {
        categoryContent = new CategoryContent(0, 1, "itemName");
    }

    @Test
    public void getCategoryCount() {
        assertEquals(categoryContent.getCategoryCount(), "0");
    }

    @Test
    public void increaseThisItem() {
        categoryContent.setCategorySize(0);
        categoryContent.increaseThisItem();
        assertEquals(categoryContent.getCategoryCount(),"1");
    }

    @Test
    public void decreaseThisItem() {
        categoryContent.setCategorySize(1);
        categoryContent.decreaseThisItem();
        assertEquals(categoryContent.getCategoryCount(),"0");

        categoryContent.decreaseThisItem();
        assertEquals(categoryContent.getCategoryCount(),"0");
    }
}