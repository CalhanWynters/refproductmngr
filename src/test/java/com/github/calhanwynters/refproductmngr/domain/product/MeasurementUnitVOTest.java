package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementUnitVOTest {

    @Test
    public void testValidMeasurementUnit() {
        MeasurementUnitVO unit = new MeasurementUnitVO("Kilogram");
        assertNotNull(unit);
        assertEquals("Kilogram", unit.getUnit());
    }

    @Test
    public void testMeasurementUnitWithNullValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new MeasurementUnitVO(null), "Measurement unit must not be null or empty.");
    }

    @Test
    public void testMeasurementUnitWithEmptyValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new MeasurementUnitVO(" "), "Measurement unit must not be null or empty.");
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
