package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Objects;
import java.util.UUID;

// NOTE: These record definitions are necessary placeholders for the tests to compile.
// In a real application, these would be separate, immutable value object classes/records.

/** A placeholder for the actual NameVO, assuming it wraps a non-null string. */
record NameVO(String name) {
    public NameVO {
        Objects.requireNonNull(name, "Name cannot be null");
    }
}

/** A placeholder for the actual LabelVO, assuming it wraps a non-null string. */
record LabelVO(String label) {
    public LabelVO {
        Objects.requireNonNull(label, "Label cannot be null");
    }
}

/** A placeholder for the actual DescriptionVO, assuming it wraps a string. It can be null. */
record DescriptionVO(String description) {}

/** A placeholder for the actual FeatureIdVO, assuming it wraps a UUID. */
record FeatureIdVO(UUID id) {
    public FeatureIdVO {
        Objects.requireNonNull(id, "ID cannot be null");
    }
    // Helper constructor to easily create VOs using strings for testing convenience
    FeatureIdVO(String uuidString) {
        this(UUID.fromString(uuidString));
    }
}


class FeatureBasicEntityTest {

    private FeatureIdVO id;
    private NameVO nameVO;
    private DescriptionVO description;
    private LabelVO labelVO;

    /**
     * Set up common, valid test objects before each test run.
     */
    @BeforeEach
    void setUp() {
        // Use unique UUIDs for ID generation for a more realistic test setup
        id = new FeatureIdVO(UUID.randomUUID());
        nameVO = new NameVO("Color");
        description = new DescriptionVO("A feature representing color.");
        labelVO = new LabelVO("Blue");
    }

    /**
     * Test successful creation and verification of all getters.
     */
    @Test
    void testFeatureBasicEntityCreation() {
        // Act
        FeatureBasicEntity feature = new FeatureBasicEntity(id, nameVO, description, labelVO);

        // Assert
        assertNotNull(feature, "Feature should not be null after construction");
        assertSame(id, feature.getId(), "ID should be the same instance");
        assertSame(nameVO, feature.getNameVO(), "NameVO should be the same instance");
        assertSame(description, feature.getDescription(), "DescriptionVO should be the same instance");
        assertSame(labelVO, feature.getLabelVO(), "LabelVO should be the same instance");
    }

    /**
     * Test that an entity can be created successfully even if the description is null.
     */
    @Test
    void testFeatureCreationWithNullDescription() {
        // Act
        FeatureBasicEntity feature = new FeatureBasicEntity(id, nameVO, null, labelVO);

        // Assert
        assertNotNull(feature);
        assertNull(feature.getDescription());
    }

    /**
     * Test that two entities with the same ID are considered equal,
     * regardless of their other field values.
     */
    @Test
    void testFeatureBasicEntityEqualityById() {
        // Arrange
        UUID commonId = UUID.randomUUID();
        // feature1 uses the standard setup VOs
        FeatureBasicEntity feature1 = new FeatureBasicEntity(new FeatureIdVO(commonId), nameVO, description, labelVO);

        // feature2 uses different name/label VOs, but the *same* ID value
        FeatureBasicEntity feature2 = new FeatureBasicEntity(
                new FeatureIdVO(commonId),
                new NameVO("Different Name"),
                new DescriptionVO("Different Desc"),
                new LabelVO("Different Label")
        );

        // Assert
        // The equals() method in FeatureAbstractClass relies ONLY on the 'id' field
        assertEquals(feature1, feature2, "Features with the same ID should be equal");
        assertEquals(feature1.hashCode(), feature2.hashCode(), "Hash codes should match for equal objects");
    }

    /**
     * Test constructor constraints (non-null checks).
     */
    @Test
    void testConstructorRequiresNonNullValues() {
        // ID must not be null
        assertThrows(NullPointerException.class,
                () -> new FeatureBasicEntity(null, nameVO, description, labelVO),
                "ID cannot be null");

        // NameVO must not be null
        assertThrows(NullPointerException.class,
                () -> new FeatureBasicEntity(id, null, description, labelVO),
                "NameVO cannot be null");

        // LabelVO must not be null
        assertThrows(NullPointerException.class,
                () -> new FeatureBasicEntity(id, nameVO, description, null),
                "LabelVO cannot be null");

        // Note: description can be null, tested elsewhere.
    }

    /**
     * Test that two entities with different IDs are not equal.
     */
    @Test
    void testFeatureBasicEntityInequality() {
        // Arrange
        FeatureBasicEntity feature1 = new FeatureBasicEntity(id, nameVO, description, labelVO);

        // Create a second feature with a unique, different ID
        FeatureIdVO differentId = new FeatureIdVO(UUID.randomUUID());
        FeatureBasicEntity feature2 = new FeatureBasicEntity(differentId, new NameVO("Shape"), new DescriptionVO("A feature representing shape."), new LabelVO("Square"));

        // Assert
        assertNotEquals(feature1, feature2, "Features with different IDs should not be equal");
        assertNotEquals(feature1.hashCode(), feature2.hashCode(), "Hash codes should not match for unequal objects (usually)");
    }
}
