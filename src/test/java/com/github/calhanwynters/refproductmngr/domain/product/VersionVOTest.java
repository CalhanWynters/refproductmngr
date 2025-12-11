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

    // --- New Tests for nextVersion() ---

    @Test
    void nextVersion_shouldIncrementVersionByOne() {
        VersionVO initialVersion = new VersionVO(5);
        VersionVO nextVersion = initialVersion.nextVersion();
        assertEquals(6, nextVersion.version());
    }

    @Test
    void nextVersion_shouldMaintainImmutability() {
        VersionVO initialVersion = new VersionVO(5);
        // Call nextVersion(), but verify the original object remains unchanged
        VersionVO nextVersion = initialVersion.nextVersion();

        assertEquals(5, initialVersion.version(), "Original VersionVO object should remain immutable");
        assertEquals(6, nextVersion.version(), "New VersionVO object should have the incremented value");
        assertNotSame(initialVersion, nextVersion, "nextVersion() should return a new instance, not modify the original");
    }

    @Test
    void nextVersion_startingFromZero() {
        VersionVO initialVersion = new VersionVO(0);
        VersionVO nextVersion = initialVersion.nextVersion();
        assertEquals(1, nextVersion.version());
    }

    @Test
    void nextVersion_multipleCalls() {
        VersionVO initial = new VersionVO(0);
        VersionVO v1 = initial.nextVersion();
        VersionVO v2 = v1.nextVersion();
        VersionVO v3 = v2.nextVersion();

        assertEquals(0, initial.version());
        assertEquals(1, v1.version());
        assertEquals(2, v2.version());
        assertEquals(3, v3.version());
    }
}
