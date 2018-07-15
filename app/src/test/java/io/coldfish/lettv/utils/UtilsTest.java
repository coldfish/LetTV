package io.coldfish.lettv.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UtilsTest {

    @Test
    public void getYearFromAirDate_CorrectDate() {
        assertEquals(Utils.getYearFromAirDate("1977-10-08"), "1977");
    }

    @Test
    public void getYearFromAirDate_CorrectNullDate() {
        assertEquals(Utils.getYearFromAirDate(null), "");
    }

    @Test
    public void getYearFromAirDate_CorrectEmptyDate() {
        assertEquals(Utils.getYearFromAirDate(""), "");
    }

    @Test
    public void getYearFromAirDate_CorrectInvalidDateEquals() {
        assertEquals(Utils.getYearFromAirDate("197-10-08"), "");
    }

    @Test
    public void getYearFromAirDate_CorrectInvalidDateNotEquals() {
        assertNotEquals(Utils.getYearFromAirDate("197-10-08"), "197-");
    }

}