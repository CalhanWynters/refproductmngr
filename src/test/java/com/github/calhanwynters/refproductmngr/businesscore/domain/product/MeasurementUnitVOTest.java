package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.MeasurementUnitVO;
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
    public void testMeasurementUnitWithBlankValue() {
        // Renamed and updated to test the handling of blank space input
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MeasurementUnitVO("   "));
        assertEquals("Measurement unit must not be empty.", exception.getMessage());
    }

    @Test
    public void testMeasurementUnitWithEmptyValue() {
        // New test case for a truly empty string
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MeasurementUnitVO(""));
        assertEquals("Measurement unit must not be empty.", exception.getMessage());
    }


    @Test
    public void testMeasurementUnitWithExcessiveLengthThrowsException() {
        String longUnit = "A".repeat(21); // Create a string that exceeds the max length (20)
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
        // Updated assertion for Java record toString() behavior
        assertTrue(unit.toString().contains("Mile"));
        assertTrue(unit.toString().contains("MeasurementUnitVO[unit=Mile]"));
    }
}
