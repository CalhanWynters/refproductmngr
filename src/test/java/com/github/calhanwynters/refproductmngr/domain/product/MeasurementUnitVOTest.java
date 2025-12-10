package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementUnitVOTest {

    @Test
    public void testValidMeasurementUnit() {
        MeasurementUnitVO unit = new MeasurementUnitVO("Kilogram");
        assertNotNull(unit);
        assertEquals("Kilogram", unit.unit()); // Use .unit() to access value
    }

    @Test
    public void testMeasurementUnitWithNullValue() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new MeasurementUnitVO(null));
        assertEquals("Measurement unit must not be null.", exception.getMessage());
    }

    @Test
    public void testMeasurementUnitWithEmptyValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MeasurementUnitVO(" "), "Measurement unit must not be empty.");
        assertEquals("Measurement unit must not be empty.", exception.getMessage());
    }

    @Test
    public void testMeasurementUnitWithExcessiveLengthThrowsException() {
        String longUnit = "A".repeat(21); // Create a string that exceeds the max length
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MeasurementUnitVO(longUnit));
        assertEquals("Measurement unit cannot exceed 20 characters.", exception.getMessage());
    }

    @Test
    public void testMeasurementUnitWithForbiddenCharactersThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MeasurementUnitVO("Invalid#Unit"));
        assertEquals("Measurement unit contains forbidden characters. Only letters, numbers, '.', '%', and '°' are allowed.", exception.getMessage());
    }

    @Test
    public void testEqualsMethod() {
        MeasurementUnitVO unit1 = new MeasurementUnitVO("Liter");
        MeasurementUnitVO unit2 = new MeasurementUnitVO("Liter");
        MeasurementUnitVO unit3 = new MeasurementUnitVO("Gram");

        assertEquals(unit1, unit2);
        assertNotEquals(unit1, unit3);
    }

    @Test
    public void testToStringMethod() {
        MeasurementUnitVO unit = new MeasurementUnitVO("Mile");
        assertEquals("Mile", unit.toString());
    }
}
