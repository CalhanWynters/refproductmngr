package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionVOTest {

    @Test
    void constructor_shouldThrowException_whenVersionIsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new VersionVO(-1));
        assertEquals("Version cannot be negative", ex.getMessage());
    }

    @Test
    void constructor_shouldCreateObject_whenVersionIsZero() {
        VersionVO version = new VersionVO(0);
        assertNotNull(version);
        assertEquals(0, version.version());
    }

    @Test
    void constructor_shouldCreateObject_whenVersionIsPositive() {
        VersionVO version = new VersionVO(1);
        assertNotNull(version);
        assertEquals(1, version.version());
    }

    @Test
    void constructor_shouldCreateObject_whenVersionIsLargeValue() {
        VersionVO version = new VersionVO(1000);
        assertNotNull(version);
        assertEquals(1000, version.version());
    }
}
